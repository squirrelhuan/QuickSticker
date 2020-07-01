package cn.demomaster.quicksticker_api.launcher;


import cn.demomaster.quicksticker_api.template.BindInterface;

import static cn.demomaster.quicksticker_annotations.Constant.BindClassSuffix;

public class QuickStickerBinder {
    private static volatile QuickStickerBinder instance = null;

    public QuickStickerBinder() {
    }

    public static QuickStickerBinder getInstance() {
        if(instance == null) {
            synchronized (QuickStickerBinder.class) {
                if (instance == null) {
                    instance = new QuickStickerBinder();
                }
            }
        }
        return instance;
    }

    public void inject(Object target) {
        String className = target.getClass().getCanonicalName();
        String helperName = className + BindClassSuffix;
        try {
            BindInterface helper = (BindInterface) (Class.forName(helperName).getConstructor().newInstance());

            helper.bind(target);
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unBind(Object target) {
        String className = target.getClass().getCanonicalName();
        String helperName = className + BindClassSuffix;
        try {
            BindInterface helper = (BindInterface) (Class.forName(helperName).getConstructor().newInstance());
            helper.unbind(target);
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }
}
