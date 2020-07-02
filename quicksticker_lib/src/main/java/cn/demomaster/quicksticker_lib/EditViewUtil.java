package cn.demomaster.quicksticker_lib;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.NO_ID;

public class EditViewUtil {

    private static EditViewUtil instance;

    public static EditViewUtil getInstance() {
        if(instance==null){
            instance = new EditViewUtil();
        }
        return instance;
    }

    private EditViewUtil(){
        mOnEditViewChangeListener = mOnEditViewChangeListener = new OnEditViewChangeListener() {
            @Override
            public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
                editText.setError("error");
            }

            @Override
            public void afterTextChanged(EditText editText, Editable s) {

            }
        };
    }

    Map<Integer, MyTextWatcher> viewMap;
    Map<Integer,OnEditViewChangeListener> listenerMap = new HashMap<>();
    public void addTextWarcherListener(Context context, OnEditViewChangeListener onEditViewChangeListener){
        listenerMap.put(context.hashCode(),onEditViewChangeListener);
    }
    public void bind(EditText view) {
        if (viewMap == null) {
            viewMap = new HashMap<>();
        }
        if(view!=null&&view.getId()!=NO_ID&&!viewMap.containsKey(view.getId())) {
            MyTextWatcher myTextWatcher = new MyTextWatcher(view) {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    int hashCode =  editText.getContext().hashCode();
                    if(listenerMap.containsKey(hashCode)){
                        OnEditViewChangeListener listener = listenerMap.get(hashCode);
                        listener.beforeTextChanged(editText,s,start,count,after);
                    }else {
                        mOnEditViewChangeListener.beforeTextChanged(editText, s, start, count, after);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int hashCode =  editText.getContext().hashCode();
                    if(listenerMap.containsKey(hashCode)){
                        OnEditViewChangeListener listener = listenerMap.get(hashCode);
                        listener.onTextChanged(editText,s,start,before,count);
                    }else {
                        mOnEditViewChangeListener.onTextChanged(editText,s,start,before,count);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int hashCode =  editText.getContext().hashCode();
                    if(listenerMap.containsKey(hashCode)){
                        OnEditViewChangeListener listener = listenerMap.get(hashCode);
                        listener.afterTextChanged(editText,s);
                    }else {
                        mOnEditViewChangeListener.afterTextChanged(editText,s);
                    }
                }
            };
            view.addTextChangedListener(myTextWatcher);
            viewMap.put(view.getId(), myTextWatcher);
        }
    }

    OnEditViewChangeListener mOnEditViewChangeListener;

    public void unBind(Object target) {
        if(viewMap!=null){
            //移除
            int hashCode = target.hashCode();
            removeEditView(viewMap,hashCode);
            removeListener(hashCode);
            viewMap.clear();
            viewMap = null;
        }
    }

    private void removeListener(int hashCode) {
        listenerMap.remove(hashCode);
    }

    private void removeEditView(Map<Integer, MyTextWatcher> map, int hashCode) {
        for(Map.Entry entry:map.entrySet()){
            if(((EditText)entry.getValue()).getContext().hashCode() == hashCode){
                viewMap.remove(entry.getKey());
                removeEditView(viewMap,hashCode);
            }
        }
    }

    public abstract class MyTextWatcher implements TextWatcher {
        public EditText editText;
        public MyTextWatcher(EditText editText) {
            this.editText = editText;
        }
    }

    public static interface OnEditViewChangeListener{

        public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after);

        public void onTextChanged(EditText editText,CharSequence s, int start, int before, int count);

        public void afterTextChanged(EditText editText,Editable s);
    }
}
