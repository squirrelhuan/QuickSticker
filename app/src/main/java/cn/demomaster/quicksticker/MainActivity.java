package cn.demomaster.quicksticker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import cn.demomaster.quicksticker_annotations.BindEditView;
import cn.demomaster.quicksticker_annotations.BindView;
import cn.demomaster.quicksticker_api.launcher.QuickStickerBinder;
import cn.demomaster.quicksticker_lib.EditViewUtil;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_context)
    public TextView tv_context;

    @BindEditView(R.id.et_username)
    public EditText et_username;
    @BindEditView(R.id.et_phone)
    public EditText et_phone;
    @BindEditView(R.id.et_password)
    public EditText et_password;

    //EditViewUtil editViewUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuickStickerBinder.getInstance().inject(this);
        tv_context.setText("tv_context");
        //et_phone.setText(parse());
/*
        editViewUtil = new EditViewUtil(new EditViewUtil.OnEditViewChangeListener() {
            @Override
            public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
                editText.setError("error");
                switch (editText.getId()){
                    case R.id.et_username:
                        break;
                    case R.id.et_phone:
                        break;
                    case R.id.et_password:
                        break;
                }
            }

            @Override
            public void afterTextChanged(EditText editText, Editable s) {

            }
        });

        editViewUtil.bind(et_username);
        editViewUtil.bind(et_phone);
        editViewUtil.bind(et_password);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QuickStickerBinder.getInstance().inject(this);

    }


    /**
     * 解析运行时注解
     */
    private String parse() {
        // 创建一个线程安全的字符串拼接对象
        StringBuffer sb = new StringBuffer();
        // 获取类字节码
        Class cls = TestRuntimeAnnotation.class;
        // 获取构造方法
        Constructor[] constructors = cls.getConstructors();
        // 获取指定类型的注解
        // 获取ClassInfo注解
        sb.append("Class注解：").append("\n");
        RuntimeAnnotation.ClassInfo classInfo =
                (RuntimeAnnotation.ClassInfo) cls.getAnnotation(RuntimeAnnotation.ClassInfo.class);
        if (null != classInfo) {
            // 拼接访问修饰符和类名
            sb.append(Modifier.toString(cls.getModifiers())).append(" ")
                    .append(cls.getSimpleName()).append("\n");
            sb.append("注解值：").append(classInfo.value()).append("\n\n");
        }

        // 获取FieldInfo注解
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            RuntimeAnnotation.FieldInfo fieldInfo = field.getAnnotation(RuntimeAnnotation.FieldInfo.class);
            if (null != fieldInfo) {
                // 拼接访问修饰符，类型名，字段名
                sb.append(Modifier.toString(field.getModifiers())).append(" ")
                        .append(field.getType().getSimpleName()).append(" ")
                        .append(field.getName()).append("\n");
                sb.append("注解值：").append(Arrays.toString(fieldInfo.value())).append("\n\n");
            }
        }

        // 获取MethodInfo注解
        sb.append("Method注解：").append("\n");
        Method[] methods = cls.getDeclaredMethods();
        for (Method method: methods) {
            RuntimeAnnotation.MethodInfo methodInfo = method.getAnnotation(RuntimeAnnotation.MethodInfo.class);
            if (null != methodInfo) {
                // 拼接访问修饰符、返回值类型、方法名
                sb.append(Modifier.toString(method.getModifiers())).append(" ")
                        .append(method.getReturnType().getSimpleName()).append(" ")
                        .append(method.getName()).append("\n");
                // 注解值
                sb.append("注解值：").append("\n");
                sb.append("name: ").append(methodInfo.name()).append("\n");
                sb.append("data: ").append(methodInfo.data()).append("\n");
                sb.append("age: ").append(methodInfo.age()).append("\n");
            }
        }
        return sb.toString();
    }
}
