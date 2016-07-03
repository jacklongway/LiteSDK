package com.longway.framework.util;

import android.content.res.Resources;

/**
 * Created by longway on 15/8/18.
 */
public class ResourceUtils {
    private ResourceUtils(){

    }

    public static String getString(Resources resources,String name,String type,String packagename){
        try {
            return resources.getString(resources.getIdentifier(name, type, packagename));
        }catch (Resources.NotFoundException ex){
            ex.printStackTrace();
        }
        return null;
    }

}
