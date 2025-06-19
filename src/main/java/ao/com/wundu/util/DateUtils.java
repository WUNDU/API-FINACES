package ao.com.wundu.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter MM_YY_FORMATTER = DateTimeFormatter.ofPattern("MM/yy");
    private static final DateTimeFormatter DD_MM_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");

    /**
     * Converte data no formato MM/yy para LocalDate
     * Assume o último dia do mês
     */
    public static LocalDate convertMMyyToLocalDate(String mmyy) {
        if (mmyy == null || mmyy.isEmpty()) {
            throw new IllegalArgumentException("Data de expiração não pode ser vazia");
        }

        try {
            String[] parts = mmyy.split("/");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Formato de data inválido. Use MM/yy");
            }

            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]); // Assume século 21

            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Mês inválido: " + month);
            }

            // Retorna o último dia do mês
            return LocalDate.of(year, month, 1).withDayOfMonth(
                    LocalDate.of(year, month, 1).lengthOfMonth()
            );

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use MM/yy", e);
        }
    }

    /**
     * Converte LocalDate para formato MM/yy
     */
    public static String convertLocalDateToMMyy(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(MM_YY_FORMATTER);
    }

    /**
     * Converte data no formato dd/MM para MM/yy do ano atual
     */
    public static String convertDDMMToMMYY(String ddmm) {
        if (ddmm == null || ddmm.isEmpty()) {
            return null;
        }

        try {
            String[] parts = ddmm.split("/");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Formato de data inválido. Use dd/MM");
            }

            String month = parts[1];
            String currentYear = String.valueOf(LocalDate.now().getYear()).substring(2);

            return month + "/" + currentYear;

        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de data inválido. Use dd/MM", e);
        }
    }

    /**
     * Valida se a data de expiração está no futuro
     */
    public static boolean isExpirationDateValid(LocalDate expirationDate) {
        if (expirationDate == null) {
            return false;
        }
        return expirationDate.isAfter(LocalDate.now());
    }
}
