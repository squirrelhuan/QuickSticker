package cn.demomaster.quicksticker_annotations;

public interface BindInterface {
    void bind(Object target);
    void bind(Object target,Object view);
    void unbind(Object target);
}
