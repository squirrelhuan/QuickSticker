package cn.demomaster.quicksticker_lib;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

public class BindViewUtil {

    /**
     *
     * @param view parent
     * @param id 要获取的viewid
     * @param c 目标类型
     * @param <T>
     * @return
     */
   public static <T>T findViewById(Object view,int id,Class<T> c){
       if(view instanceof View){
           return (T)((View)view).findViewById(id);
       }else  if(view instanceof Activity){
           return (T)((Activity)view).findViewById(id);
       }
       return null;
   }
    public static <T>T findViewById(Object view,int id){
       T targetView = null;
        if(view instanceof View){
            targetView = (T)((View)view).findViewById(id);
        }else  if(view instanceof Activity){
            targetView = (T)((Activity)view).findViewById(id);
        }
        return targetView;
    }

    public static <T>T initViewById(Object view,int id){
        T targetView = findViewById(view,id);
        if(targetView instanceof EditText) {
            EditViewUtil editViewUtil = EditViewUtil.getInstance();
            editViewUtil.bind(targetView);
        }
        return targetView;
    }

}
