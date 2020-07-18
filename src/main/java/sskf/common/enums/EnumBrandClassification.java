package sskf.common.enums;

public enum EnumBrandClassification {
    NB("NB", 0),
    PB("PB", 1),
    OEM("OEM", 2),
    Other("その他", 3);

    public final String value;
    public final Integer code;

    EnumBrandClassification(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public static String getValueByCode(Byte code) {
        for(EnumBrandClassification e : values()) {
            if(e.code == (int)code) return e.value;
        }
        return null;
    }

    public static Byte getCodeByValue(String value) {
        for(EnumBrandClassification e : values()) {
            if(e.value.equals(value)) return e.code.byteValue();
        }
        return null;
    }
}
