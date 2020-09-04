package cn.demomaster.quicksticker_annotations;


//import static cn.demomaster.quicksticker_annotations.Constant.BindClassSuffix;

public class QuickStickerBinder {
    private static volatile QuickStickerBinder instance = null;
    static String BindClassSuffix ="$$BindClass";
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

    /**
     * 绑定
     * @param target
     */
    public void bind(Object target) {
        String className = target.getClass().getCanonicalName();
        String helperName = className + BindClassSuffix;
        try {
            BindInterface helper = (BindInterface) (Class.forName(helperName).getConstructor().newInstance());
            helper.bind(target);
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * fragment 绑定view
     * @param target
     * @param mView
     */
    public void bind(Object target, Object mView) {
        String className = target.getClass().getCanonicalName();
        String helperName = className + BindClassSuffix;
        try {
            BindInterface helper = (BindInterface) (Class.forName(helperName).getConstructor().newInstance());

            helper.bind(target,mView);
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
