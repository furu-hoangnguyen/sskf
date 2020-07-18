package sskf.common.enums;

public enum EnumItem {
    CLASSIC("定番", 0),
    SPECIAL_SALE("特売", 1),
    EDLP("EDLP", 2),
    MONTHLY_SERVICE("月間奉仕", 3),
    HALF_PRICE_INTRODUCTION("半値導入", 4),
    DISPOSAL_SALE("処分販売", 5),
    REMOTE_ISLAND("離島", 6),
    OTHER("その他", 7);

    public final String value;
    public final Integer code;

    EnumItem(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public static String getValueByCode(Byte code) {
        for(EnumItem e : values()) {
            if(e.code == (int)code) return e.value;
        }
        return null;
    }

    public static Byte getCodeByValue(String value) {
        for(EnumItem e : values()) {
            if(e.value.equals(value)) return e.code.byteValue();
        }
        return null;
    }

}
