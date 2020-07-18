package sskf.common.enums;

public enum EnumRequestStatus {

    CREATE("create", "作成中"),
    CONFIRM_CREATE("confirm-create", "確認待ち"),
    APPLY("apply", "申請待ち"),
    APPROVE("approve", "承認待ち"),
    REJECT("reject", "却下"),
    CONFIRM_SETTLEMENT("confirm-settlement", "決済確認待ち"),
    PAYMENT_DONE("payment-done", "支払済");

    private String value;
    private String label;

    EnumRequestStatus(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getValueByLabel(String label) {
        for (EnumRequestStatus enumRequestStatus : values()) {
            if (enumRequestStatus.label.equals(label)) {
                return enumRequestStatus.value;
            }
        }

        return null;
    }

}
