package com.lw.iocsample;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.lw.iocsample.event.DynamicHandler;
import com.lw.iocsample.event.EventBase;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by lw on 2016/11/11.
 */

public class ViewInjectUtils {
    private static final String METHOD_SET_CONTENTVIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

    /**
     * 注入布局
     *
     * @param activity
     */
    private static void injectContentView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        //获取注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int contentViewId = contentView.value();
            try {
                Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW, int.class);
                method.setAccessible(true);
                method.invoke(activity, contentViewId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注入View
     *
     * @param activity
     * @throws Exception
     */
    private static void injectViews(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fileds = clazz.getDeclaredFields();
        for (Field field : fileds) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int viewId = viewInject.value();
                if (viewId != -1) {
                    Log.e("Tag", "viewId:" + viewId);
                    Method method = null;
                    try {
//                        method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID);// error
                        method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
                        Object resView = method.invoke(activity, viewId);
                        Log.e("resView", "resView:" + resView);

                        field.setAccessible(true);
                        field.set(activity, resView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 注入所有的事件
     *
     * @param activity
     */
    public static void injectEvents(Activity activity) {
        //获取class
        Class<? extends Activity> clazz = activity.getClass();
        //获取Activity上面的所有方法
        Method[] methods = clazz.getMethods();
        //遍历
        for (Method method : methods) {
            //获取每个方法上面的注解,因为注解可能不只是一个-----@OnClick注解
            Annotation[] annotations = method.getAnnotations();
            //遍历方法上面的注解
            for (Annotation annotation : annotations) {

                // 获取注解类型的Class
                Class<? extends Annotation> annotationType = annotation.annotationType();
                //拿到注解上的注解-----@EventBase
                EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                if (eventBase != null) {
                    String listenerSetter = eventBase.listenerSetter();
                    Class<?> listenerType = eventBase.listenerType();
                    String methodName = eventBase.methodName();
                    //拿到OnClick的value()
                    try {
                        Method declaredMethod = annotationType.getDeclaredMethod("value");
                        //取出所有的value值,通过执行value()方法获取viewId数组
                        /**
                         * 警告： 最后一个参数使用了不准确的变量类型的 varargs 方法的非 varargs 调用；
                         [javac] 对于 varargs 调用，应使用 Java.lang.Object
                         [javac] 对于非 varargs 调用，应使用 java.lang.Object[]，这样也可以抑制此警告

                         解决办法：
                                 Method method  =  cls.getMethod( " hashCode " ,  new  Class[ 0 ]);  //  编译通过
                                 Method method  =  cls.getMethod( " hashCode " ,  null );  //  编译失败

                                 allMethod[i].invoke(dbInstance,  new  Object[]{});  //  编译通过
                                 allMethod[i].invoke(dbInstance,  null );  //  编译失败
                         */
                        int[] viewIds = (int[]) declaredMethod.invoke(annotation, new  Object[]{});
                        //通过InvocationHanldler设置代理
                        DynamicHandler handler = new DynamicHandler(activity);
                        handler.addMethod(methodName, method);
                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, handler);
                        //遍历所有的View，设置事件
                        for (int viewId : viewIds) {
                            View view = activity.findViewById(viewId);
                            Method setEventListener = view.getClass().getMethod(listenerSetter, listenerType);
                            //执行监听器方法
                            setEventListener.invoke(view, listener);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }
    }


    public static void inject(Activity activity) {
        injectContentView(activity);
        injectViews(activity);
        injectEvents(activity);
    }


}
