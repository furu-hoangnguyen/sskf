package sskf.common.enums;

public enum EnumTypeUse {
    EXHIBITION_ATTENDANCE("展示会出席", 0),
    ATTENDING_MEETING("会合出席", 1),
    SPONSORSHIP("協賛(販促リベートを除く)", 2),
    PURCHASE_OF_GOODS("物品購入", 3),
    SALES_UNPAID("販売未収", 4),
    OTHER("その他", 5);

    public final String value;
    public final Integer code;

    EnumTypeUse(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public static String getValueByCode(Byte code) {
        for(EnumTypeUse e : values()) {
            if(e.code == (int)code) return e.value;
        }
        return null;
    }

    public static Byte getCodeByValue(String value) {
        for(EnumTypeUse e : values()) {
            if(e.value.equals(value)) return e.code.byteValue();
        }
        return null;
    }
}
