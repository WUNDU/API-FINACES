package ao.com.wundu.enums;

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
        for ( NotificationPreference pref : NotificationPreference.values() ) {
            if ( pref.getValue().equalsIgnoreCase(value) ) {
                return pref;
            }
        }

        throw new IllegalArgumentException("Invalid notification preference: " + value);
    }
}
