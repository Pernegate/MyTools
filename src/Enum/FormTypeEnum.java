package Enum;

/**
 * @author xu
 * @date 2019-3-18
 */
public enum FormTypeEnum {

    /**
     * 上报填单七种类型，和RPT_ITEM_DICT.FORM_TYPE字段保持一致
     */
    SF("1", "身份识别"),

    ZS("2", "自杀自伤"),

    DD("3", "跌倒/坠床"),

    GY("4", "给药错误"),

    BG("5", "非计划拔管"),

    YL("6", "院内压力性损伤"),

    OTHER("7", "其他不良事件");

    private String code;

    private String description;

    FormTypeEnum() {
    }

    FormTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescriptionByCode(String code) {
        for (FormTypeEnum typeEnum : values()) {
            if (code.equals(typeEnum.getCode())) {
                return typeEnum.getDescription();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

