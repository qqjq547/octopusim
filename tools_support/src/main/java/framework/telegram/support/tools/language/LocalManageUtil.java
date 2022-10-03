package framework.telegram.support.tools.language;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

import framework.telegram.support.system.storage.sp.SharePreferencesStorage;

public class LocalManageUtil {

    public static final int FOLLOW_SYSTEM = 0;
    public static final int SIMPLIFIED_CHINESE = 1;
    public static final int TRADITIONAL_CHINESE = 2;
    public static final int ENGLISH = 3;
    public static final int VI = 4;
    public static final int THAI = 5;

    private static int mSelectLanguage = -1;

    /**
     * 判断当前选择的语言
     *
     * @return
     */
    public static int getCurLanguaue() {
        switch (getSetLanguageLocale().getLanguage()) {
            case "zh":
                if (getSetLanguageLocale().getCountry().equals("CN")) {
                    return SIMPLIFIED_CHINESE;
                } else {
                    return TRADITIONAL_CHINESE;
                }
            case "th":
                return THAI;
            case "vi":
                return VI;
            default:
                return ENGLISH;
        }
    }

    public static int getSelectLanguage() {
        if (mSelectLanguage == -1) {
            mSelectLanguage = SharePreferencesStorage.createStorageInstance(LanguagePref.class).getLanguage(0);
        }
        return mSelectLanguage;
    }

    /**
     * 获取选择的语言设置
     *
     * @return
     */
    public static Locale getSetLanguageLocale() {
        switch (getSelectLanguage()) {
            case FOLLOW_SYSTEM:
                return getSystemLocale();
            case SIMPLIFIED_CHINESE:
                return Locale.CHINA;
            case TRADITIONAL_CHINESE:
                return Locale.TAIWAN;
            case THAI:
                return new Locale("th");
            case VI:
                return new Locale("vi");
            default:
                return Locale.ENGLISH;
        }
    }


    public static void saveSelectLanguage(Context context, int select) {
        mSelectLanguage = select;
        SharePreferencesStorage.createStorageInstance(LanguagePref.class).putLanguage(select);
        MultiLanguage.setApplicationLanguage(context);
    }


    private static Locale systemCurrentLocal = Locale.ENGLISH;

    /**
     * 获取系统的locale
     *
     * @return Locale对象
     */
    public static Locale getSystemLocale() {
        return systemCurrentLocal;
    }

    public static void saveSystemCurrentLanguage(Context context) {
        systemCurrentLocal = MultiLanguage.getSystemLocal(context);
    }

    /**
     * 保存系统语言
     *
     * @param context
     * @param newConfig
     */
    public static void saveSystemCurrentLanguage(Context context, Configuration newConfig) {
        systemCurrentLocal = MultiLanguage.getSystemLocal(newConfig);
    }
}
