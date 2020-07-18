package sskf.common.enums;

public enum EnumTypeCommission {

    INTERNAL("内口銭", 0),
    EXTERNAL("外口銭", 1);

    public final String value;
    public final Integer code;

    EnumTypeCommission(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public static String getValueByCode(Byte code) {
        for(EnumTypeCommission e : values()) {
            if(e.code == (int)code) return e.value;
        }
        return null;
    }

    public static Byte getCodeByValue(String value) {
        for(EnumTypeCommission e : values()) {
            if(e.value.equals(value)) return e.code.byteValue();
        }
        return null;
    }
}
