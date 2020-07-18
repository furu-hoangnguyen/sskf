package sskf.common.enums;

public enum EnumPromotionExpenses {

    REBATE("リベート", 0),
    MANNEQUIN_FEE("マネキン代", 1),
    FLYER_FEE("チラシ代", 2),
    END_SPONSORSHIP("エンド協賛", 3),
    POP_FEE("POP代", 4),
    EXHIBITION_FEE("展示会費用", 5),
    CENTER_FEE("センターフィー", 6),
    OTHERS("その他", 7);

    public final String value;
    public final Integer code;

    EnumPromotionExpenses(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public static String getValueByCode(Byte code) {
        for(EnumPromotionExpenses e : values()) {
            if(e.code == (int)code) return e.value;
        }
        return null;
    }

    public static Byte getCodeByValue(String value) {
        for(EnumPromotionExpenses e : values()) {
            if(e.value.equals(value)) return e.code.byteValue();
        }
        return null;
    }
}
