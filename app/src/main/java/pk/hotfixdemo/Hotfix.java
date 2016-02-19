package pk.hotfixdemo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * @author zijiao
 * @version 2016/2/19
 * @Mark
 */
public class Hotfix {

    private static final String TAG = Hotfix.class.getSimpleName();
    private static final String DEX_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "hotfixTest"
            + File.separator + "hotfix.apk";

    public static void loadDexIfNeed(Context context) {
        try {
            // get classLoader
            ClassLoader cl = context.getClassLoader();
            if (cl instanceof PathClassLoader) {
                PathClassLoader classLoader = (PathClassLoader) cl;

                //get pathList
                Class baseCls = BaseDexClassLoader.class;
                Field pathListField = baseCls.getDeclaredField("pathList");
                pathListField.setAccessible(true);
                Object pathList = pathListField.get(classLoader);
                L(pathList);

                //get dexElements
                Class dexCls = pathList.getClass();
                Field dexElementsField = dexCls.getDeclaredField("dexElements");
                dexElementsField.setAccessible(true);
                Object dexElements = dexElementsField.get(pathList);
                L(dexElements);

                //access newElement
                DexClassLoader newDexClassLoader = new DexClassLoader(DEX_PATH, context.getDir("dex", 0).getAbsolutePath(), null, classLoader);
                Object hotfixElements = getElements(newDexClassLoader);
                int fixElementsLength = Array.getLength(hotfixElements);

                //create newElements
                Class elementCls = dexElements.getClass().getComponentType();
                int length = Array.getLength(dexElements);
                Object newElements = Array.newInstance(elementCls, length + fixElementsLength);
                for (int i = 0; i < length + fixElementsLength; i++) {
                    //注意此处hotfix部分插到队首
                    if (i < fixElementsLength) {
                        Array.set(newElements, i, Array.get(hotfixElements, i));
                    } else {
                        Array.set(newElements, i, Array.get(dexElements, i - fixElementsLength));
                    }
                    //插入队尾测试hotfix无效
//                    if (i < length) {
//                        Array.set(newElements, i, Array.get(dexElements, i));
//                    } else {
//                        Array.set(newElements, i, Array.get(hotfixElements, i - length));
//                    }
                }

                //set dexElements to newElements
                dexElementsField.set(pathList, newElements);
                L(pathList);


            }
        } catch (Exception e) {
            e.printStackTrace();
            L("Error --> " + e.getMessage());
        }
    }

    private static Object getElements(BaseDexClassLoader classLoader) {
        Object dexElements = null;
        try {
            //get pathList
            Class baseCls = BaseDexClassLoader.class;
            Field pathListField = baseCls.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            Object pathList = pathListField.get(classLoader);

            //get dexElements
            dexElements = getField(pathList, "dexElements");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dexElements;
    }

    private static Object getField(Object obj, String name) {
        Object result = null;
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final void L(Object s) {
        Log.i(TAG, String.valueOf(s));
    }

}
