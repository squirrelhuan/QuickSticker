package cn.demomaster.quicksticker_compiler;


import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

import cn.demomaster.quicksticker_annotations.BindEditView;
import cn.demomaster.quicksticker_annotations.BindInterface;
import cn.demomaster.quicksticker_annotations.BindView;

import static cn.demomaster.quicksticker_annotations.Constant.BindClassSuffix;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"cn.demomaster.quicksticker_annotations.BindView", "cn.demomaster.quicksticker_annotations.BindEditView"})
public class BindViewProcessor extends AbstractProcessor {
    private Filer mFilerUtils;
    private Types mTypesUtils;
    private Elements mElementsUtils;

    private Map<TypeElement, Set<ViewInfo>> mToBindMap = new HashMap<>();
    //editview 校验处理
    private Map<TypeElement, Set<ViewInfo>> mToBindEditMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFilerUtils = processingEnv.getFiler();
        mTypesUtils = processingEnv.getTypeUtils();
        mElementsUtils = processingEnv.getElementUtils();
    }

//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        HashSet<String> supportTypes = new LinkedHashSet<>();
//        supportTypes.add(BindView.class.getCanonicalName());
//        return supportTypes;
//    }
//
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.latestSupported();

    String printStr = "";
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        println("start process");
        if (set != null && set.size() != 0) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindView.class);
            categories(elements, BindView.class);

            Set<? extends Element> elements_editView = roundEnv.getElementsAnnotatedWith(BindEditView.class);
            categories(elements_editView, BindEditView.class);

            for (TypeElement typeElement : mToBindMap.keySet()) {
                String code = generateCode(typeElement);
                String helperClassName = typeElement.getQualifiedName() + BindClassSuffix;

                try {
                    JavaFileObject jfo = mFilerUtils.createSourceFile(helperClassName, typeElement);
                    Writer writer = jfo.openWriter();
                    writer.write(code);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (TypeElement typeElement : mToBindEditMap.keySet()) {
                String code = generateCode(typeElement);
                String helperClassName = typeElement.getQualifiedName() + BindClassSuffix;

                try {
                    JavaFileObject jfo = mFilerUtils.createSourceFile(helperClassName, typeElement);
                    Writer writer = jfo.openWriter();
                    writer.write(code);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return true;
        }
        return false;
    }

    public void println(String msg) {
        printStr = printStr + msg + "\n";
    }

    private void categories(Set<? extends Element> elements, Class clazz) {
        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement enclosingElement = (TypeElement) variableElement.getEnclosingElement();
            println("categories=" + elements);

            if (clazz == BindView.class) {
                Set<ViewInfo> views = mToBindMap.get(enclosingElement);
                if (views == null) {
                    views = new HashSet<>();
                    mToBindMap.put(enclosingElement, views);
                }
                Object bindAnnotation = variableElement.getAnnotation(clazz);
                int id = ((BindView) bindAnnotation).value();
                views.add(new ViewInfo(variableElement.getSimpleName().toString(), id));
                println(clazz.getSimpleName() + " enclosingElement=" + enclosingElement + ",views=" + views);
            } else if (clazz == BindEditView.class) {
                Set<ViewInfo> editViews = mToBindEditMap.get(enclosingElement);
                if (editViews == null) {
                    editViews = new HashSet<>();
                    mToBindEditMap.put(enclosingElement, editViews);
                }
                Object bindAnnotation_eidtView = variableElement.getAnnotation(clazz);
                int id2 = ((BindEditView) bindAnnotation_eidtView).value();
                editViews.add(new ViewInfo(variableElement.getSimpleName().toString(), id2));
                println(clazz.getSimpleName() + " enclosingElement=" + enclosingElement + ",views=" + editViews);
            }
        }
    }

    /**
     * 生成代碼
     *
     * @param typeElement
     * @return
     */
    private String generateCode(TypeElement typeElement) {
        String rawClassName = typeElement.getSimpleName().toString();
        String packageName = ((PackageElement) mElementsUtils.getPackageOf(typeElement)).getQualifiedName().toString();
        String helperClassName = rawClassName + BindClassSuffix;

        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(packageName).append(";\n");
        builder.append("import " + BindInterface.class.getName() + ";\n");
        builder.append("\n" +
                "import android.text.Editable;\n" +
                "import android.text.TextWatcher;\n" +
                "import android.widget.Toast;\n" +
                "import cn.demomaster.quicksticker_lib.EditViewUtil;\n" +
                "import cn.demomaster.quicksticker_lib.BindViewUtil;\n" +
                "\n");

        //builder.append("/*" + printStr + "\n*/\n");

        builder.append("public class ").append(helperClassName).append(" implements ").append("BindInterface");
        builder.append(" {\n");


        builder.append("\t");
        builder.append("EditViewUtil  editViewUtil = EditViewUtil.getInstance();\n");

        builder.append("\t@Override\n");
        builder.append("\tpublic void bind(" + "Object" + " target) {\n");

        builder.append("\t\t");
        builder.append(rawClassName + " substitute = " + "(" + rawClassName + ")" + "target;\n");

        if (mToBindMap != null && typeElement != null && mToBindMap.get(typeElement) != null) {
            for (ViewInfo viewInfo : mToBindMap.get(typeElement)) {
                builder.append("\t\t");
                builder.append("substitute." + viewInfo.viewName + " = BindViewUtil.initViewById(substitute," + viewInfo.id + ");\n");
            }
        }

        if (mToBindEditMap != null && typeElement != null && mToBindEditMap.get(typeElement) != null) {
            for (ViewInfo viewInfo : mToBindEditMap.get(typeElement)) {
                /*builder.append("\t\t");
                builder.append("substitute." + viewInfo.viewName).append(" = ");
                builder.append("substitute.findViewById(" + viewInfo.id + ");\n");
                builder.append("\t\t");*/

                builder.append("\t\t");
                builder.append("substitute." + viewInfo.viewName + " = BindViewUtil.initViewById(substitute," + viewInfo.id + ");\n");
                //builder.append(EidtViewHelper.class.getSimpleName()+".bindEditView("+builder+",substitute." + viewInfo.viewName+");\n");
                //builder.append(EidtViewHelper.bindEditView("substitute." + viewInfo.viewName));
                //builder.append("editViewUtil.bind(substitute." + viewInfo.viewName + ");\n");
            }
        }

        builder.append("\t}\n");


        builder.append("//适用于fragment\n");
        builder.append("\t@Override\n");
        builder.append("\tpublic void bind(" + "Object  target,Object view) {\n");
        builder.append("\t\t");
        builder.append(rawClassName + " substitute = " + "(" + rawClassName + ")" + "target;\n");
        if (mToBindMap != null && typeElement != null && mToBindMap.get(typeElement) != null) {
            for (ViewInfo viewInfo : mToBindMap.get(typeElement)) {
                builder.append("\t\t");
                builder.append("substitute." + viewInfo.viewName + " = BindViewUtil.initViewById(view," + viewInfo.id + ");\n");
            }
        }
        if (mToBindEditMap != null && typeElement != null && mToBindEditMap.get(typeElement) != null) {
            for (ViewInfo viewInfo : mToBindEditMap.get(typeElement)) {
                builder.append("\t\t");
                builder.append("substitute." + viewInfo.viewName + " = BindViewUtil.initViewById(view," + viewInfo.id + ");\n");
                //builder.append(EidtViewHelper.class.getSimpleName()+".bindEditView("+builder+",substitute." + viewInfo.viewName+");\n");
                //builder.append(EidtViewHelper.bindEditView("substitute." + viewInfo.viewName));
                //builder.append("editViewUtil.bind(substitute." + viewInfo.viewName + ");\n");
            }
        }

        builder.append("\t}\n");


        builder.append("\t@Override\n");
        builder.append("\tpublic void unbind(" + "Object" + " target) {\n");

        builder.append("\t\t");
        builder.append("editViewUtil.unBind(target);\n");
        builder.append("\t}\n");

        builder.append('\n');
        builder.append("}\n");

        return builder.toString();
    }

    class ViewInfo {
        String viewName;
        int id;

        public ViewInfo(String viewName, int id) {
            this.viewName = viewName;
            this.id = id;
        }
    }
}


