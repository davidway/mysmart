package org.smart4j.chapter1.util;

import org.apache.commons.lang3.StringUtils;

public class CastUtil {
    public static String castString(Object obj){
        return CastUtil.castString(obj,"");
    }

    private static String castString(Object obj, String defaultValue) {
        return obj!=null?String.valueOf(obj):defaultValue;
    }
    public static double castDouble(Object obj){
        return CastUtil.castDouble(obj,0);
    }

    private static double castDouble(Object obj, int defaultValue) {
        double doubleValue = defaultValue;
        if ( obj!=null){
            String strValue = castString(obj);
            if (StringUtils.isNotEmpty(strValue)){
                try{
                    doubleValue = Double.parseDouble(strValue);
                }catch(NumberFormatException e){
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }


    public static double castLong(Object obj){
        return CastUtil.castLong(obj,0);
    }

    private static Long castLong(Object obj, int defaultValue) {
        long longValue = defaultValue;
        if ( obj!=null){
            String strValue = castString(obj);
            if (StringUtils.isNotEmpty(strValue)){
                try{
                    longValue = Long.parseLong(strValue);
                }catch(NumberFormatException e){
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    public static int castInt(Object obj){
        return CastUtil.castInt(obj,0);
    }

    private static Integer castInt(Object obj, int defaultValue) {
        int intValue = defaultValue;
        if ( obj!=null){
            String strValue = castString(obj);
            if (StringUtils.isNotEmpty(strValue)){
                try{
                    intValue = Integer.parseInt(strValue);
                }catch(NumberFormatException e){
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }


    public static boolean castBoolean(Object obj){
        return CastUtil.castBoolean(obj,false);
    }

    private static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if ( obj!=null){
            String strValue = castString(obj);
            if (StringUtils.isNotEmpty(strValue)){
                try{
                    booleanValue = Boolean.parseBoolean(castString(obj));
                }catch(NumberFormatException e){
                    booleanValue = defaultValue;
                }
            }
        }
        return booleanValue;
    }
}

