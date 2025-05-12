package ao.com.wundu.domain.enums;

import java.util.Arrays;

public enum NotificationPreference {

    EMAIL("email"),
    SMS("sms"),
    PUSH("push");

    private final String value;

    NotificationPreference(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static NotificationPreference fromValue(String value) {
        if (value == null) {
            return EMAIL; // Valor padrão
        }
        return Arrays.stream(values())
                .filter(pref -> pref.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid NotificationPreference value: " + value));
    }
}

