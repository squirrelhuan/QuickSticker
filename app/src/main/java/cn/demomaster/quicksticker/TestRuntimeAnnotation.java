package cn.demomaster.quicksticker;

/**
 * @author mazaiting
 * @date 2018/1/16
 */
@RuntimeAnnotation.ClassInfo("TestRuntimeAnnotation class")
public class TestRuntimeAnnotation {
    @RuntimeAnnotation.FieldInfo(value = {1, 2})
    public String fieldnfo = "FieldInfo";
    @RuntimeAnnotation.FieldInfo(value = {10086})
    public int i = 100;
    @RuntimeAnnotation.MethodInfo(name = "mazaiting", data = "big")
    public static String getMethodInfo() {
        return TestRuntimeAnnotation.class.getSimpleName();
    }
}