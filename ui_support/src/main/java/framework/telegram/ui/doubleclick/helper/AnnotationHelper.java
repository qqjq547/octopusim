package framework.telegram.ui.doubleclick.helper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.telegram.ui.doubleclick.click.annotation.AAddDoubleClick;
import framework.telegram.ui.doubleclick.click.annotation.ACancelActivity;
import framework.telegram.ui.doubleclick.click.annotation.AClickListener;

/**
 * @Description: 解析注解
 * @Author: liys
 * @CreateDate: 2019/8/27 14:29
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/8/27 14:29
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AnnotationHelper {

    /**
     * 获取ACancelActivity集合
     *
     * @param annotationClass
     */
    public static List<Class> getACancelActivity(Class annotationClass) {
        List<Class> classList = new ArrayList<>();
        Method[] methods = annotationClass.getDeclaredMethods(); //解析方法上的注解
        for (Method method : methods) {
            boolean methodHasAnno = method.isAnnotationPresent(ACancelActivity.class);
            if (methodHasAnno) {
                ACancelActivity methodAnno = method.getAnnotation(ACancelActivity.class); //得到注解
                Class[] clazzs = methodAnno.activitys();
                for (int i = 0; i < clazzs.length; i++) {
                    classList.add(clazzs[i]);
                }
                return classList;
            }
        }
        return classList;
    }


    /**
     * 获取AAddDoubleClick集合
     *
     * @param
     */
    public static Map<Class, Map<Integer, Long>> getAddDoubleClick(Class annotationClass) {
        Map<Class, Map<Integer, Long>> addViewMap = new HashMap<>();
        Method[] methods = annotationClass.getDeclaredMethods(); //解析方法上的注解
        for (Method method : methods) {
            boolean methodHasAnno = method.isAnnotationPresent(AAddDoubleClick.class);
            if (methodHasAnno) {
                AAddDoubleClick methodAnno = method.getAnnotation(AAddDoubleClick.class); //得到注解
                Class clazz = methodAnno.activity();
                int[] ids = methodAnno.addIds();
                long[] times = methodAnno.times();
                Map<Integer, Long> idsMap = new HashMap<>();
                for (int i = 0; i < ids.length; i++) {
                    if (i < times.length) {
                        idsMap.put(ids[i], times[i]);
                    } else {
                        idsMap.put(ids[i], 0L);
                    }
                }
                addViewMap.put(clazz, idsMap);
            }
        }
        return addViewMap;
    }


    /**
     * 获取AClickListener集合
     *
     * @param annotationClass
     * @return
     */
    public static Map<Class, Map<Integer, Class>> getClickListener(Class annotationClass) {
        Map<Class, Map<Integer, Class>> viewListenerMap = new HashMap<>();
        Method[] methods = annotationClass.getDeclaredMethods(); //解析方法上的注解
        for (Method method : methods) {
            boolean methodHasAnno = method.isAnnotationPresent(AClickListener.class);
            if (methodHasAnno) {
                AClickListener methodAnno = method.getAnnotation(AClickListener.class); //得到注解
                Class activityClazz = methodAnno.activity();
                Class lisennerClazz = methodAnno.lisenner();
                int[] ids = methodAnno.ids();
                Map<Integer, Class> idsMap = new HashMap<>();
                for (int i = 0; i < ids.length; i++) {
                    idsMap.put(ids[i], lisennerClazz);
                }
                viewListenerMap.put(activityClazz, idsMap);
            }
        }
        return viewListenerMap;
    }

}
