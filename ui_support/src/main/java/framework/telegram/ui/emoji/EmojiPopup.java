package framework.telegram.ui.emoji;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.autofill.AutofillManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import framework.telegram.ui.emoji.base.Emoji;
import framework.telegram.ui.emoji.listeners.OnEmojiBackspaceClickListener;
import framework.telegram.ui.emoji.listeners.OnEmojiClickListener;
import framework.telegram.ui.emoji.listeners.OnEmojiLongClickListener;
import framework.telegram.ui.emoji.listeners.OnPopupDismissListener;
import framework.telegram.ui.emoji.listeners.OnPopupShownListener;
import framework.telegram.ui.emoji.listeners.OnSoftKeyboardCloseListener;
import framework.telegram.ui.emoji.listeners.OnSoftKeyboardOpenListener;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

public final class EmojiPopup implements EmojiResultReceiver.Receiver {
    static final String TAG = "EmojiPopup";

    static final int MIN_KEYBOARD_HEIGHT = 50;

    final View rootView;
    final Activity context;

    @NonNull
    final RecentEmoji recentEmoji;
    @NonNull
    final VariantEmoji variantEmoji;
    @NonNull
    final EmojiVariantPopup variantPopup;

    final PopupWindow popupWindow;
    final EditText editText;

    boolean isPendingOpen;
    boolean isKeyboardOpen;

    @Nullable
    OnPopupShownListener onPopupShownListener;
    @Nullable
    OnSoftKeyboardCloseListener onSoftKeyboardCloseListener;
    @Nullable
    OnSoftKeyboardOpenListener onSoftKeyboardOpenListener;

    @Nullable
    OnEmojiBackspaceClickListener onEmojiBackspaceClickListener;
    @Nullable
    OnEmojiClickListener onEmojiClickListener;
    @Nullable
    OnPopupDismissListener onPopupDismissListener;

    int originalImeOptions = -1;

    final EmojiResultReceiver emojiResultReceiver = new EmojiResultReceiver(new Handler(Looper.getMainLooper()));

    final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = () -> updateKeyboardState();

    EmojiPopup(@NonNull final View rootView, @NonNull final EditText editText,
               @Nullable final RecentEmoji recent, @Nullable final VariantEmoji variant,
               @ColorInt final int backgroundColor, @ColorInt final int iconColor, @ColorInt final int dividerColor,
               @StyleRes final int animationStyle, @Nullable final ViewPager.PageTransformer pageTransformer) {
        this.context = Utils.asActivity(rootView.getContext());
        this.rootView = rootView.getRootView();
        this.editText = editText;
        this.recentEmoji = recent != null ? recent : new RecentEmojiManager(context);
        this.variantEmoji = variant != null ? variant : new VariantEmojiManager(context);

        popupWindow = new PopupWindow(context);

        final OnEmojiLongClickListener longClickListener = new OnEmojiLongClickListener() {
            @Override
            public void onEmojiLongClick(@NonNull final EmojiImageView view, @NonNull final Emoji emoji) {
                variantPopup.show(view, emoji);
            }
        };

        final OnEmojiClickListener clickListener = new OnEmojiClickListener() {
            @Override
            public void onEmojiClick(@NonNull final EmojiImageView imageView, @NonNull final Emoji emoji) {
                Utils.input(editText, emoji);

                recentEmoji.addEmoji(emoji);
                variantEmoji.addVariant(emoji);
                imageView.updateEmoji(emoji);

                if (onEmojiClickListener != null) {
                    onEmojiClickListener.onEmojiClick(imageView, emoji);
                }

                variantPopup.dismiss();
            }
        };

        variantPopup = new EmojiVariantPopup(this.rootView, clickListener);

        final EmojiView emojiView = new EmojiView(context, clickListener, longClickListener, recentEmoji, variantEmoji, backgroundColor, iconColor, dividerColor, pageTransformer);
        emojiView.setOnEmojiBackspaceClickListener(v -> {
            Utils.backspace(editText);

            if (onEmojiBackspaceClickListener != null) {
                onEmojiBackspaceClickListener.onEmojiBackspaceClick(v);
            }
        });

        popupWindow.setContentView(emojiView);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null)); // To avoid borders and overdraw.
        popupWindow.setOnDismissListener(() -> {
            if (onPopupDismissListener != null) {
                onPopupDismissListener.onPopupDismiss();
            }
        });

        if (animationStyle != 0) {
            popupWindow.setAnimationStyle(animationStyle);
        }

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    void updateKeyboardState() {
        final int keyboardHeight = Utils.getInputMethodHeight(context, rootView);

        if (keyboardHeight > Utils.dpToPx(context, MIN_KEYBOARD_HEIGHT)) {
            updateKeyboardStateOpened(keyboardHeight);
        } else {
            updateKeyboardStateClosed();
        }
    }

    private void updateKeyboardStateOpened(final int keyboardHeight) {
        if (popupWindow.getHeight() != keyboardHeight) {
            popupWindow.setHeight(keyboardHeight);
        }

        final Rect rect = Utils.windowVisibleDisplayFrame(context);

        final int properWidth = Utils.getOrientation(context) == Configuration.ORIENTATION_PORTRAIT ? rect.right : Utils.getScreenWidth(context);
        if (popupWindow.getWidth() != properWidth) {
            popupWindow.setWidth(properWidth);
        }

        if (!isKeyboardOpen) {
            isKeyboardOpen = true;
            if (onSoftKeyboardOpenListener != null) {
                onSoftKeyboardOpenListener.onKeyboardOpen(keyboardHeight);
            }
        }

        if (isPendingOpen) {
            showAtBottom();
        }
    }

    private void updateKeyboardStateClosed() {
        isKeyboardOpen = false;

        if (onSoftKeyboardCloseListener != null) {
            onSoftKeyboardCloseListener.onKeyboardClose();
        }

        if (isShowing()) {
            dismiss();
        }
    }

    public void toggle() {
        if (!popupWindow.isShowing()) {
            if (Utils.shouldOverrideRegularCondition(context, editText) && originalImeOptions == -1) {
                originalImeOptions = editText.getImeOptions();
            }
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            showAtBottomPending();
        } else {
            dismiss();
        }
    }

    private void showAtBottomPending() {
        isPendingOpen = true;
        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (Utils.shouldOverrideRegularCondition(context, editText)) {
            editText.setImeOptions(editText.getImeOptions() | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
            if (inputMethodManager != null) {
                inputMethodManager.restartInput(editText);
            }
        }

        if (inputMethodManager != null) {
            emojiResultReceiver.setReceiver(this);
            inputMethodManager.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN, emojiResultReceiver);
        }
    }

    public boolean isShowing() {
        return popupWindow.isShowing();
    }

    public void dismiss() {
        popupWindow.dismiss();
        variantPopup.dismiss();
        recentEmoji.persist();
        variantEmoji.persist();

        emojiResultReceiver.setReceiver(null);

        if (originalImeOptions != -1) {
            editText.setImeOptions(originalImeOptions);
            final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputMethodManager != null) {
                inputMethodManager.restartInput(editText);
            }

            if (SDK_INT >= O) {
                final AutofillManager autofillManager = context.getSystemService(AutofillManager.class);
                if (autofillManager != null) {
                    autofillManager.cancel();
                }
            }
        }
    }

    void showAtBottom() {
        isPendingOpen = false;
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);

        if (onPopupShownListener != null) {
            onPopupShownListener.onPopupShown();
        }
    }

    @Override
    public void onReceiveResult(final int resultCode, final Bundle data) {
        if (resultCode == 0 || resultCode == 1) {
            showAtBottom();
        }
    }

    public static final class Builder {
        @NonNull
        private final View rootView;
        @StyleRes
        private int keyboardAnimationStyle;
        @ColorInt
        private int backgroundColor;
        @ColorInt
        private int iconColor;
        @ColorInt
        private int dividerColor;
        @Nullable
        private ViewPager.PageTransformer pageTransformer;
        @Nullable
        private OnPopupShownListener onPopupShownListener;
        @Nullable
        private OnSoftKeyboardCloseListener onSoftKeyboardCloseListener;
        @Nullable
        private OnSoftKeyboardOpenListener onSoftKeyboardOpenListener;
        @Nullable
        private OnEmojiBackspaceClickListener onEmojiBackspaceClickListener;
        @Nullable
        private OnEmojiClickListener onEmojiClickListener;
        @Nullable
        private OnPopupDismissListener onPopupDismissListener;
        @Nullable
        private RecentEmoji recentEmoji;
        @Nullable
        private VariantEmoji variantEmoji;

        private Builder(final View rootView) {
            this.rootView = Utils.checkNotNull(rootView, "The root View can't be null");
        }

        /**
         * @param rootView The root View of your layout.xml which will be used for calculating the height
         *                 of the keyboard.
         * @return builder For building the {@link EmojiPopup}.
         */
        @CheckResult
        public static Builder fromRootView(final View rootView) {
            return new Builder(rootView);
        }

        @CheckResult
        public Builder setOnSoftKeyboardCloseListener(@Nullable final OnSoftKeyboardCloseListener listener) {
            onSoftKeyboardCloseListener = listener;
            return this;
        }

        @CheckResult
        public Builder setOnEmojiClickListener(@Nullable final OnEmojiClickListener listener) {
            onEmojiClickListener = listener;
            return this;
        }

        @CheckResult
        public Builder setOnSoftKeyboardOpenListener(@Nullable final OnSoftKeyboardOpenListener listener) {
            onSoftKeyboardOpenListener = listener;
            return this;
        }

        @CheckResult
        public Builder setOnPopupShownListener(@Nullable final OnPopupShownListener listener) {
            onPopupShownListener = listener;
            return this;
        }

        @CheckResult
        public Builder setOnPopupDismissListener(@Nullable final OnPopupDismissListener listener) {
            onPopupDismissListener = listener;
            return this;
        }

        @CheckResult
        public Builder setOnEmojiBackspaceClickListener(@Nullable final OnEmojiBackspaceClickListener listener) {
            onEmojiBackspaceClickListener = listener;
            return this;
        }

        /**
         * Allows you to pass your own implementation of recent emojis. If not provided the default one
         * {@link RecentEmojiManager} will be used.
         *
         * @since 0.2.0
         */
        @CheckResult
        public Builder setRecentEmoji(@Nullable final RecentEmoji recent) {
            recentEmoji = recent;
            return this;
        }

        /**
         * Allows you to pass your own implementation of variant emojis. If not provided the default one
         * {@link VariantEmojiManager} will be used.
         *
         * @since 0.5.0
         */
        @CheckResult
        public Builder setVariantEmoji(@Nullable final VariantEmoji variant) {
            variantEmoji = variant;
            return this;
        }

        @CheckResult
        public Builder setBackgroundColor(@ColorInt final int color) {
            backgroundColor = color;
            return this;
        }

        @CheckResult
        public Builder setIconColor(@ColorInt final int color) {
            iconColor = color;
            return this;
        }

        @CheckResult
        public Builder setDividerColor(@ColorInt final int color) {
            dividerColor = color;
            return this;
        }

        @CheckResult
        public Builder setKeyboardAnimationStyle(@StyleRes final int animation) {
            keyboardAnimationStyle = animation;
            return this;
        }

        @CheckResult
        public Builder setPageTransformer(@Nullable final ViewPager.PageTransformer transformer) {
            pageTransformer = transformer;
            return this;
        }

        @CheckResult
        public EmojiPopup build(@NonNull final EditText editText) {
            EmojiManager.getInstance().verifyInstalled();
            Utils.checkNotNull(editText, "EditText can't be null");

            final EmojiPopup emojiPopup = new EmojiPopup(rootView, editText, recentEmoji, variantEmoji, backgroundColor,
                    iconColor, dividerColor, keyboardAnimationStyle, pageTransformer);
            emojiPopup.onSoftKeyboardCloseListener = onSoftKeyboardCloseListener;
            emojiPopup.onEmojiClickListener = onEmojiClickListener;
            emojiPopup.onSoftKeyboardOpenListener = onSoftKeyboardOpenListener;
            emojiPopup.onPopupShownListener = onPopupShownListener;
            emojiPopup.onPopupDismissListener = onPopupDismissListener;
            emojiPopup.onEmojiBackspaceClickListener = onEmojiBackspaceClickListener;
            return emojiPopup;
        }
    }
}
