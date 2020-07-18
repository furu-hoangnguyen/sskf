package sskf.common.enums;

public enum EnumPaymentMethod {

    CASH_PAYMENT("現金支払", 0),
    CHECK_BRING("小切手(持参)", 1),
    CHECK_MAIL("小切手(郵送)", 2),
    BANK_TRANSFER(":銀行振込", 3),
    ACCOUNTS_RECEIVABLE_OFFSET("売掛金相殺", 4),
    OTHER("その他", 5);

    public final String value;
    public final Integer code;

    EnumPaymentMethod(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public static String getValueByCode(Byte code) {
        for(EnumPaymentMethod e : values()) {
            if(e.code == (int)code) return e.value;
        }
        return null;
    }

    public static Byte getCodeByValue(String value) {
        for(EnumPaymentMethod e : values()) {
            if(e.value.equals(value)) return e.code.byteValue();
        }
        return null;
    }
}
