package framework.telegram.ui.selectedroundcardview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import framework.telegram.ui.R;


/**
 * Author:   yichongmiao
 * CreateAt: 20/01/2018.
 */

public class DIYRoundedCardView extends LinearLayout {


    private static final int[] COLOR_BACKGROUND_ATTR = {android.R.attr.colorBackground};
    private static final CardViewImpl IMPL;

    static {
        if (Build.VERSION.SDK_INT >= 21) {
            IMPL = new DIYCardViewApi21Impl();
        } else if (Build.VERSION.SDK_INT >= 17) {
            IMPL = new CardViewJellybeanMr1();
        } else {
            IMPL = new CardViewBaseImpl();
        }

        IMPL.initStatic();
    }

    private boolean mCompatPadding;

    private boolean mPreventCornerOverlap;

    /**
     * CardView requires to have a particular minimum size to draw shadows before API 21. If
     * developer also sets min width/height, they might be overridden.
     * <p>
     * CardView works around this issue by recording user given parameters and using an internal
     * method to set them.
     */
//    int mUserSetMinWidth, mUserSetMinHeight;

    final Rect mContentPadding = new Rect();

    final Rect mShadowBounds = new Rect();

    public DIYRoundedCardView(@NonNull Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public DIYRoundedCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public DIYRoundedCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }



    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        // NO OP
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        // NO OP
    }

    /**
     * Returns whether CardView will add inner padding on platforms Lollipop and after.
     *
     * @return <code>true</code> if CardView adds inner padding on platforms Lollipop and after to
     * have same dimensions with platforms before Lollipop.
     */
    public boolean getUseCompatPadding() {
        return mCompatPadding;
    }

    /**
     * CardView adds additional padding to draw shadows on platforms before Lollipop.
     * <p>
     * This may cause Cards to have different sizes between Lollipop and before Lollipop. If you
     * need to align CardView with other Views, you may need api version specific dimension
     * resources to account for the changes.
     * As an alternative, you can set this flag to <code>true</code> and CardView will add the same
     * padding values on platforms Lollipop and after.
     * <p>
     * Since setting this flag to true adds unnecessary gaps in the UI, default value is
     * <code>false</code>.
     *
     * @param useCompatPadding <code>true></code> if CardView should add padding for the shadows on
     *                         platforms Lollipop and above.
     * @attr ref android.support.v7.cardview.R.styleable#CardView_cardUseCompatPadding
     */
    public void setUseCompatPadding(boolean useCompatPadding) {
        if (mCompatPadding != useCompatPadding) {
            mCompatPadding = useCompatPadding;
            IMPL.onCompatPaddingChanged(mCardViewDelegate);
        }
    }

    /**
     * Sets the padding between the Card's edges and the children of CardView.
     * <p>
     * Depending on platform version or {@link #getUseCompatPadding()} settings, CardView may
     * update these values before calling {@link View#setPadding(int, int, int, int)}.
     *
     * @param left   The left padding in pixels
     * @param top    The top padding in pixels
     * @param right  The right padding in pixels
     * @param bottom The bottom padding in pixels
     * @attr ref android.support.v7.cardview.R.styleable#CardView_contentPadding
     * @attr ref android.support.v7.cardview.R.styleable#CardView_contentPaddingLeft
     * @attr ref android.support.v7.cardview.R.styleable#CardView_contentPaddingTop
     * @attr ref android.support.v7.cardview.R.styleable#CardView_contentPaddingRight
     * @attr ref android.support.v7.cardview.R.styleable#CardView_contentPaddingBottom
     */
    public void setContentPadding(int left, int top, int right, int bottom) {
        mContentPadding.set(left, top, right, bottom);
        IMPL.updatePadding(mCardViewDelegate);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!(IMPL instanceof DIYCardViewApi21Impl)) {
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            switch (widthMode) {
                case MeasureSpec.EXACTLY:
                case MeasureSpec.AT_MOST:
                    final int minWidth = (int) Math.ceil(IMPL.getMinWidth(mCardViewDelegate));
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(minWidth,
                            MeasureSpec.getSize(widthMeasureSpec)), widthMode);
                    break;
                case MeasureSpec.UNSPECIFIED:
                    // Do nothing
                    break;
            }

            final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            switch (heightMode) {
                case MeasureSpec.EXACTLY:
                case MeasureSpec.AT_MOST:
                    final int minHeight = (int) Math.ceil(IMPL.getMinHeight(mCardViewDelegate));
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(minHeight,
                            MeasureSpec.getSize(heightMeasureSpec)), heightMode);
                    break;
                case MeasureSpec.UNSPECIFIED:
                    // Do nothing
                    break;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DIYRoundedCardView, defStyleAttr,
                R.style.CardView);

        ColorStateList backgroundColor;
        if (a.hasValue(R.styleable.DIYRoundedCardView_cardBackgroundColor)) {
            backgroundColor = a.getColorStateList(R.styleable.DIYRoundedCardView_cardBackgroundColor);
        } else {
            // There isn't one set, so we'll compute one based on the theme
            final TypedArray aa = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
            final int themeColorBackground = aa.getColor(0, 0);
            aa.recycle();

            // If the theme colorBackground is light, use our own light color, otherwise dark
            final float[] hsv = new float[3];
            Color.colorToHSV(themeColorBackground, hsv);
            backgroundColor = ColorStateList.valueOf(hsv[2] > 0.5f
                    ? getResources().getColor(R.color.cardview_light_background)
                    : getResources().getColor(R.color.cardview_dark_background));
        }
        float radius = a.getDimension(R.styleable.DIYRoundedCardView_cardCornerRadius, 0);
        float elevation = a.getDimension(R.styleable.DIYRoundedCardView_cardElevation, 0);
        float maxElevation = a.getDimension(R.styleable.DIYRoundedCardView_cardMaxElevation, 0);
        mCompatPadding = a.getBoolean(R.styleable.DIYRoundedCardView_cardUseCompatPadding, false);
        mPreventCornerOverlap = a.getBoolean(R.styleable.DIYRoundedCardView_cardPreventCornerOverlap, true);
        int defaultPadding = a.getDimensionPixelSize(R.styleable.DIYRoundedCardView_contentPadding, 0);
        mContentPadding.left = a.getDimensionPixelSize(R.styleable.DIYRoundedCardView_contentPaddingLeft,
                defaultPadding);
        mContentPadding.top = a.getDimensionPixelSize(R.styleable.DIYRoundedCardView_contentPaddingTop,
                defaultPadding);
        mContentPadding.right = a.getDimensionPixelSize(R.styleable.DIYRoundedCardView_contentPaddingRight,
                defaultPadding);
        mContentPadding.bottom = a.getDimensionPixelSize(R.styleable.DIYRoundedCardView_contentPaddingBottom,
                defaultPadding);
        if (elevation > maxElevation) {
            maxElevation = elevation;
        }
//        mUserSetMinWidth = a.getDimensionPixelSize(R.styleable.CardView_android_minWidth, 0);
//        mUserSetMinHeight = a.getDimensionPixelSize(R.styleable.CardView_android_minHeight, 0);


        //Get XML optConnerCardView

        boolean leftTop = a.getBoolean(R.styleable.DIYRoundedCardView_DIYLeftTopCorner, true);
        boolean rightBottom = a.getBoolean(R.styleable.DIYRoundedCardView_DIYRightBottomCorner, true);
        boolean leftBottom = a.getBoolean(R.styleable.DIYRoundedCardView_DIYLeftBottomCorner, true);
        boolean rightTop = a.getBoolean(R.styleable.DIYRoundedCardView_DIYRightTopCorner, true);


        boolean[]roundedCorner={leftTop,rightTop,rightBottom,leftBottom};

        a.recycle();

        IMPL.initialize(mCardViewDelegate, context, backgroundColor, radius,
                elevation, maxElevation,roundedCorner);
    }

//    @Override
//    public void setMinimumWidth(int minWidth) {
//        mUserSetMinWidth = minWidth;
//        super.setMinimumWidth(minWidth);
//    }
//
//    @Override
//    public void setMinimumHeight(int minHeight) {
//        mUserSetMinHeight = minHeight;
//        super.setMinimumHeight(minHeight);
//    }

    /**
     * Updates the background color of the CardView
     *
     * @param color The new color to set for the card background
     * @attr ref R.styleable#CardView_cardBackgroundColor
     */
    public void setCardBackgroundColor(@ColorInt int color) {
        IMPL.setBackgroundColor(mCardViewDelegate, ColorStateList.valueOf(color));
    }

    /**
     * Updates the background ColorStateList of the CardView
     *
     * @param color The new ColorStateList to set for the card background
     * @attr ref R.styleable#CardView_cardBackgroundColor
     */
    public void setCardBackgroundColor(@Nullable ColorStateList color) {
        IMPL.setBackgroundColor(mCardViewDelegate, color);
    }

    /**
     * Returns the background color state list of the CardView.
     *
     * @return The background color state list of the CardView.
     */
    public ColorStateList getCardBackgroundColor() {
        return IMPL.getBackgroundColor(mCardViewDelegate);
    }

    /**
     * Returns the inner padding after the Card's left edge
     *
     * @return the inner padding after the Card's left edge
     */
    public int getContentPaddingLeft() {
        return mContentPadding.left;
    }

    /**
     * Returns the inner padding before the Card's right edge
     *
     * @return the inner padding before the Card's right edge
     */
    public int getContentPaddingRight() {
        return mContentPadding.right;
    }

    /**
     * Returns the inner padding after the Card's top edge
     *
     * @return the inner padding after the Card's top edge
     */
    public int getContentPaddingTop() {
        return mContentPadding.top;
    }

    /**
     * Returns the inner padding before the Card's bottom edge
     *
     * @return the inner padding before the Card's bottom edge
     */
    public int getContentPaddingBottom() {
        return mContentPadding.bottom;
    }

    /**
     * Updates the corner radius of the CardView.
     *
     * @param radius The radius in pixels of the corners of the rectangle shape
     * @attr ref R.styleable#CardView_cardCornerRadius
     * @see #setRadius(float)
     */
    public void setRadius(float radius) {
        IMPL.setRadius(mCardViewDelegate, radius);
    }

    /**
     * Returns the corner radius of the CardView.
     *
     * @return Corner radius of the CardView
     * @see #getRadius()
     */
    public float getRadius() {
        return IMPL.getRadius(mCardViewDelegate);
    }

    /**
     * Updates the backward compatible elevation of the CardView.
     *
     * @param elevation The backward compatible elevation in pixels.
     * @attr ref R.styleable#CardView_cardElevation
     * @see #getCardElevation()
     * @see #setMaxCardElevation(float)
     */
    public void setCardElevation(float elevation) {
        IMPL.setElevation(mCardViewDelegate, elevation);
    }

    /**
     * Returns the backward compatible elevation of the CardView.
     *
     * @return Elevation of the CardView
     * @see #setCardElevation(float)
     * @see #getMaxCardElevation()
     */
    public float getCardElevation() {
        return IMPL.getElevation(mCardViewDelegate);
    }

    /**
     * Updates the backward compatible maximum elevation of the CardView.
     * <p>
     * Calling this method has no effect if device OS version is Lollipop or newer and
     * {@link #getUseCompatPadding()} is <code>false</code>.
     *
     * @param maxElevation The backward compatible maximum elevation in pixels.
     * @attr ref R.styleable#CardView_cardMaxElevation
     * @see #setCardElevation(float)
     * @see #getMaxCardElevation()
     */
    public void setMaxCardElevation(float maxElevation) {
        IMPL.setMaxElevation(mCardViewDelegate, maxElevation);
    }

    /**
     * Returns the backward compatible maximum elevation of the CardView.
     *
     * @return Maximum elevation of the CardView
     * @see #setMaxCardElevation(float)
     * @see #getCardElevation()
     */
    public float getMaxCardElevation() {
        return IMPL.getMaxElevation(mCardViewDelegate);
    }

    /**
     * Returns whether CardView should add extra padding to content to avoid overlaps with rounded
     * corners on pre-Lollipop platforms.
     *
     * @return True if CardView prevents overlaps with rounded corners on platforms before Lollipop.
     * Default value is <code>true</code>.
     */
    public boolean getPreventCornerOverlap() {
        return mPreventCornerOverlap;
    }

    /**
     * On pre-Lollipop platforms, CardView does not clip the bounds of the Card for the rounded
     * corners. Instead, it adds padding to content so that it won't overlap with the rounded
     * corners. You can disable this behavior by setting this field to <code>false</code>.
     * <p>
     * Setting this value on Lollipop and above does not have any effect unless you have enabled
     * compatibility padding.
     *
     * @param preventCornerOverlap Whether CardView should add extra padding to content to avoid
     *                             overlaps with the CardView corners.
     * @attr ref R.styleable#CardView_cardPreventCornerOverlap
     * @see #setUseCompatPadding(boolean)
     */
    public void setPreventCornerOverlap(boolean preventCornerOverlap) {
        if (preventCornerOverlap != mPreventCornerOverlap) {
            mPreventCornerOverlap = preventCornerOverlap;
            IMPL.onPreventCornerOverlapChanged(mCardViewDelegate);
        }
    }

    private final CardViewDelegate mCardViewDelegate = new CardViewDelegate() {
        private Drawable mCardBackground;

        @Override
        public void setCardBackground(Drawable drawable) {
            mCardBackground = drawable;
            setBackgroundDrawable(drawable);
        }

        @Override
        public boolean getUseCompatPadding() {
            return DIYRoundedCardView.this.getUseCompatPadding();
        }

        @Override
        public boolean getPreventCornerOverlap() {
            return DIYRoundedCardView.this.getPreventCornerOverlap();
        }

        @Override
        public void setShadowPadding(int left, int top, int right, int bottom) {
            mShadowBounds.set(left, top, right, bottom);
            DIYRoundedCardView.super.setPadding(left + mContentPadding.left, top + mContentPadding.top,
                    right + mContentPadding.right, bottom + mContentPadding.bottom);
        }

        @Override
        public void setMinWidthHeightInternal(int width, int height) {

        }

//        @Override
//        public void setMinWidthHeightInternal(int width, int height) {
//            if (width > mUserSetMinWidth) {
//                DIYRoundedCardView.super.setMinimumWidth(width);
//            }
//            if (height > mUserSetMinHeight) {
//                DIYRoundedCardView.super.setMinimumHeight(height);
//            }
//        }

        @Override
        public Drawable getCardBackground() {
            return mCardBackground;
        }

        @Override
        public View getCardView() {
            return DIYRoundedCardView.this;
        }


//            private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OptRoundCardView, defStyleAttr,
//                R.style.OptRoundCardView_DayNight);
//        ColorStateList backgroundColor;
//
//        if (a.hasValue(R.styleable.OptRoundCardView_optRoundCardBackgroundColor)) {
//            // use xml color
//            backgroundColor = a.getColorStateList(R.styleable.OptRoundCardView_optRoundCardBackgroundColor);
//        } else {
//            // There isn't one set, so we'll compute one based on the theme
//            final TypedArray aa = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
//            final int themeColorBackground = aa.getColor(0, 0);
//            aa.recycle();
//
//            // If the theme colorBackground is light, use our own light color, otherwise dark
//            final float[] hsv = new float[3];
//            Color.colorToHSV(themeColorBackground, hsv);
//            backgroundColor = ColorStateList.valueOf(hsv[2] > 0.5f
//                    ? getResources().getColor(R.color.opt_round_card_view_light_background)
//                    : getResources().getColor(R.color.opt_round_card_view_dark_background));
//        }
//
//
//        float radius = a.getDimension(R.styleable.OptRoundCardView_optRoundCardCornerRadius, 0);
//        float elevation = a.getDimension(R.styleable.OptRoundCardView_optRoundCardElevation, 0);
//        float maxElevation = a.getDimension(R.styleable.OptRoundCardView_optRoundCardMaxElevation, 0);
//        mCompatPadding = a.getBoolean(R.styleable.OptRoundCardView_optRoundCardUseCompatPadding, false);
//        mPreventCornerOverlap = a.getBoolean(R.styleable.OptRoundCardView_optRoundCardPreventCornerOverlap, true);
//        int defaultPadding = a.getDimensionPixelSize(R.styleable.OptRoundCardView_optRoundContentPadding, 0);
//        mContentPadding.left = a.getDimensionPixelSize(R.styleable.OptRoundCardView_optRoundContentPaddingLeft,
//                defaultPadding);
//        mContentPadding.top = a.getDimensionPixelSize(R.styleable.OptRoundCardView_optRoundContentPaddingTop,
//                defaultPadding);
//        mContentPadding.right = a.getDimensionPixelSize(R.styleable.OptRoundCardView_optRoundContentPaddingRight,
//                defaultPadding);
//        mContentPadding.bottom = a.getDimensionPixelSize(R.styleable.OptRoundCardView_optRoundContentPaddingBottom,
//                defaultPadding);
//        if (elevation > maxElevation) {
//            maxElevation = elevation;
//        }
//
//        int cornerFlag = 0;
//        boolean flag = a.getBoolean(R.styleable.OptRoundCardView_optRoundCardLeftTopCorner, true);
//        cornerFlag += flag ? OptRoundRectDrawable.FLAG_LEFT_TOP_CORNER : 0;
//
//        flag = a.getBoolean(R.styleable.OptRoundCardView_optRoundCardRightTopCorner, true);
//        cornerFlag += flag ? OptRoundRectDrawable.FLAG_RIGHT_TOP_CORNER : 0;
//
//        flag = a.getBoolean(R.styleable.OptRoundCardView_optRoundCardLeftBottomCorner, true);
//        cornerFlag += flag ? OptRoundRectDrawable.FLAG_LEFT_BOTTOM_CORNER : 0;
//
//        flag = a.getBoolean(R.styleable.OptRoundCardView_optRoundCardRightBottomCorner, true);
//        cornerFlag += flag ? OptRoundRectDrawable.FLAG_RIGHT_BOTTOM_CORNER : 0;
//
//        int edgesFlag = 0;
//        flag = a.getBoolean(R.styleable.OptRoundCardView_optRoundCardLeftEdges, true);
//        edgesFlag += flag ? OptRoundRectDrawableWithShadow.FLAG_LEFT_EDGES : 0;
//
//        flag = a.getBoolean(R.styleable.OptRoundCardView_optRoundCardTopEdges, true);
//        edgesFlag += flag ? OptRoundRectDrawableWithShadow.FLAG_TOP_EDGES : 0;
//
//        flag = a.getBoolean(R.styleable.OptRoundCardView_optRoundCardRightEdges, true);
//        edgesFlag += flag ? OptRoundRectDrawableWithShadow.FLAG_RIGHT_EDGES : 0;
//
//        flag = a.getBoolean(R.styleable.OptRoundCardView_optRoundCardBottomEdges, true);
//        edgesFlag += flag ? OptRoundRectDrawableWithShadow.FLAG_BOTTOM_EDGES : 0;
//
//        a.recycle();
//
//        IMPL.initialize(this, context, backgroundColor.getDefaultColor(), radius, elevation, maxElevation, cornerFlag, edgesFlag);
//    }
    };
}
