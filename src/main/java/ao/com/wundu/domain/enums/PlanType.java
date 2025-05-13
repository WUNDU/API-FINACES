package ao.com.wundu.domain.enums;

import java.util.Arrays;

public enum PlanType {

    FREE("FREE"),
    PREMIUM("PREMIUM");

    private final String value;

    PlanType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PlanType fromValue(String value) {
        if (value == null) {
            return FREE; // Valor padrão
        }
        return Arrays.stream(values())
                .filter(plan -> plan.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid PlanType value: " + value));
    }
}
