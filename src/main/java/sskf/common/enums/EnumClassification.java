package sskf.common.enums;

public enum EnumClassification {
    HOUSEHOLD("家庭用", 0),
    BUSINESS("業務用", 1);

    public final String value;
    public final Integer code;

    EnumClassification(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public static String getValueByCode(Byte code) {
        for(EnumClassification e : values()) {
            if(e.code == (int)code) return e.value;
        }
        return null;
    }

    public static Byte getCodeByValue(String value) {
        for(EnumClassification e : values()) {
            if(e.value.equals(value)) return e.code.byteValue();
        }
        return null;
    }
}
