package cn.ximoon.plugin;

public class StringUtil {

    public static String checkEmpty(String content){
        if (null == content){
            return "";
        }
        return content;
    }
}
