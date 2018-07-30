package ir.codetower.moshaver;

import java.util.HashMap;

/**
 * Created by Mr-R00t on 3/7/2018.
 */

public class Param {
    private static HashMap<String,String> params;
    public static void put(String key,String val){
        params.put(key,val);
    }
    public static HashMap<String,String> get(){
        return params;
    }
    public static void clear(){
        params=new HashMap<>();
    }
}
