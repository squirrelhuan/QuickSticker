package cn.demomaster.quicksticker_lib;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.NO_ID;

public class EditViewUtil {

    public EditViewUtil(){
        mOnEditViewChangeListener = new OnEditViewChangeListener() {
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

    public void setTextWarcherListener(OnEditViewChangeListener onEditViewChangeListener){
        mOnEditViewChangeListener = onEditViewChangeListener;
    }
    public void bind(EditText view) {
        if (viewMap == null) {
            viewMap = new HashMap<>();
        }
        if(view!=null&&view.getId()!=NO_ID&&!viewMap.containsKey(view.getId())) {
            MyTextWatcher myTextWatcher = new MyTextWatcher(view) {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(mOnEditViewChangeListener!=null)
                    mOnEditViewChangeListener.beforeTextChanged(editText,s,start,count,after);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(mOnEditViewChangeListener!=null)
                    mOnEditViewChangeListener.onTextChanged(editText,s,start,before,count);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(mOnEditViewChangeListener!=null)
                    mOnEditViewChangeListener.afterTextChanged(editText,s);
                }
            };
            view.addTextChangedListener(myTextWatcher);
            viewMap.put(view.getId(), myTextWatcher);
        }
    }
    OnEditViewChangeListener mOnEditViewChangeListener;
    Map<Integer, MyTextWatcher> viewMap;

    public void unBind() {
        if(viewMap!=null){
            viewMap.clear();
            viewMap = null;
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
