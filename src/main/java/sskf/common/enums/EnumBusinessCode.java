package sskf.common.enums;

public enum EnumBusinessCode {

    SEASONING_BUSINESS("調味料事業", "1000"),
    BEVERAGE_BUSINESS("商品", "6000"),
    AGRIBUSINESS("原料", "8000"),
    OTHER("その他", "9999");

    public final String value;
    public final String code;

    EnumBusinessCode(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public static String getValueByCode(String code) {
        for(EnumBusinessCode e : values()) {
            if(e.code.equals(code)) return e.value;
        }
        return null;
    }

    public static String getCodeByValue(String value) {
        for(EnumBusinessCode e : values()) {
            if(e.value.equals(value)) return e.code;
        }
        return null;
    }
}
