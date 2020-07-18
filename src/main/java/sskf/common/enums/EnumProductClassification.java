package sskf.common.enums;

public enum EnumProductClassification {
    FINISHED_PRODUCT("製品", "10"),
    PRODUCT("商品", "11"),
    RAW_MATERIAL("原料", "12"),
    PACKAGING("包材", "13"),
    SEMI_FINISHED_PRODUCT("半製品", "14"),
    SET_PRODUCT("セット品", "15"),
    IMPORTED_GOODS("輸入品", "19"),
    FARE("運賃", "20"),
    CORRECTION("訂正", "21"),
    CONSUMPTION_TAX("消費税", "22"),
    DISCOUNT("値引", "23"),
    PRICE_INCREASE("値増", "24"),
    SALES_PROMOTION_EXPENSES("販促費", "25"),
    LOGISTICS_COST("物流費", "26");

    public final String value;
    public final String code;

    EnumProductClassification(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public static String getValueByCode(String code) {
        for(EnumProductClassification e : values()) {
            if(e.code.equals(code)) return e.value;
        }
        return null;
    }

    public static String getCodeByValue(String value) {
        for(EnumProductClassification e : values()) {
            if(e.value.equals(value)) return e.code;
        }
        return null;
    }
}
