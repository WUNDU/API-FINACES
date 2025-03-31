package ao.com.wundu.enums;

public enum PlanType {

    FREE("free"),
    PREMIUM("premium");

    private final String value;

    PlanType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
