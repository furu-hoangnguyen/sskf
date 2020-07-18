package sskf.common.enums;

public enum  EnumTypeInput {

    PRODUCT_CODE("品目コード", 0),
    PRODUCT_CATEGORY("商品カテゴリ", 1),
    PRODUCT_SUBCATEGORY("商品サブカテゴリ", 2),
    PRODUCT_SERIES("商品シリーズ", 3);

    public final String value;
    public final Integer code;

    EnumTypeInput(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public static String getValueByCode(Byte code) {
        for(EnumTypeInput e : values()) {
            if(e.code == (int)code) return e.value;
        }
        return null;
    }

    public static Byte getCodeByValue(String value) {
        for(EnumTypeInput e : values()) {
            if(e.value.equals(value)) return e.code.byteValue();
        }
        return null;
    }

    public static EnumTypeInput value(String value) {
        for(EnumTypeInput e : values()) {
            if(e.value.equals(value)) return e;
        }
        return null;

    }
}
