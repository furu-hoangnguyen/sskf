package sskf.common.enums;

public enum EnumRequestType {

    ACCOUNT_RECEIVABLES("account-receivables", "販売未収金"),
    EXHIBITION_PROMOTIONS("exhibition-promotions", "展示会・協賛"),
    MANNEQUIN_PROMOTIONS("mannequin-promotions", "マネキン");

    private String value;
    private String label;

    EnumRequestType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getValueByLabel(String label) {
        for (EnumRequestType enumRequestType : values()) {
            if (enumRequestType.label.equals(label)) {
                return enumRequestType.value;
            }
        }

        return null;
    }
}
