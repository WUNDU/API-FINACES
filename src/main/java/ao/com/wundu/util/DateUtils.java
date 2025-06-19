package ao.com.wundu.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    private static final DateTimeFormatter MM_YY_FORMATTER = DateTimeFormatter.ofPattern("MM/yy");
    private static final DateTimeFormatter DD_MM_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");


    /**
     * Converte uma string no formato MM/yy para LocalDate
     * A data será o último dia do mês especificado
     * @param mmyy String no formato MM/yy (exemplo: "02/27")
     * @return LocalDate representando o último dia do mês
     * @throws IllegalArgumentException se o formato for inválido
     */
    public static LocalDate convertMMyyToLocalDate(String mmyy) {
        if (mmyy == null || mmyy.isBlank()) {
            throw new IllegalArgumentException("Data de expiração não pode ser nula ou vazia");
        }

        try {
            // Parse MM/yy para YearMonth
            YearMonth yearMonth = YearMonth.parse(mmyy, MM_YY_FORMATTER);

            // Ajustar para século 21 se o ano for menor que 30 (assumindo anos 2000-2099)
            int year = yearMonth.getYear();
            if (year < 2000) {
                year += 2000;
                yearMonth = YearMonth.of(year, yearMonth.getMonth());
            }

            // Retornar o último dia do mês
            return yearMonth.atEndOfMonth();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use MM/yy (exemplo: 02/27)", e);
        }
    }

    /**
     * Converte LocalDate para string no formato MM/yy
     * @param date LocalDate a ser convertida
     * @return String no formato MM/yy
     */
    public static String convertLocalDateToMMyy(LocalDate date) {
        if (date == null) {
            return null;
        }

        // Ajustar ano para formato de 2 dígitos
        int year = date.getYear() % 100;
        return String.format("%02d/%02d", date.getMonthValue(), year);
    }

    /**
     * Valida se a data de expiração é válida (não está no passado)
     * @param expirationDate Data de expiração
     * @return true se a data for válida, false caso contrário
     */
    public static boolean isExpirationDateValid(LocalDate expirationDate) {
        if (expirationDate == null) {
            return false;
        }

        // A data deve ser pelo menos o mês atual
        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(now);
        YearMonth expirationMonth = YearMonth.from(expirationDate);

        return !expirationMonth.isBefore(currentMonth);
    }

    /**
     * Formata LocalDate para dd/MM
     * @param date Data a ser formatada
     * @return String no formato dd/MM
     */
    public static String formatToDDMM(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DD_MM_FORMATTER);
    }

//    /**
//     * Converte data no formato MM/yy para LocalDate
//     * Assume o último dia do mês
//     */
//    public static LocalDate convertMMyyToLocalDate(String mmyy) {
//        if (mmyy == null || mmyy.trim().isEmpty()) {
//            throw new IllegalArgumentException("Data de expiração não pode ser vazia");
//        }
//
//        try {
//            String[] parts = mmyy.trim().split("/");
//            if (parts.length != 2) {
//                throw new IllegalArgumentException("Formato de data inválido. Use MM/yy (exemplo: 02/27)");
//            }
//
//            int month = Integer.parseInt(parts[0]);
//            int year = Integer.parseInt(parts[1]);
//
//            // Validar mês
//            if (month < 1 || month > 12) {
//                throw new IllegalArgumentException("Mês inválido: " + month + ". Deve estar entre 01 e 12");
//            }
//
//            // Validar ano (assumir que anos 00-30 são 2000-2030, e 31-99 são 1931-1999)
//            if (year < 0 || year > 99) {
//                throw new IllegalArgumentException("Ano inválido: " + year + ". Use formato yy (exemplo: 27 para 2027)");
//            }
//
//            // Converter ano de 2 dígitos para 4 dígitos
//            int fullYear;
//            if (year <= 30) {
//                fullYear = 2000 + year;
//            } else {
//                fullYear = 1900 + year;
//            }
//
//            // Retorna o último dia do mês
//            LocalDate firstDayOfMonth = LocalDate.of(fullYear, month, 1);
//            return firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
//
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("Formato de data inválido. Use números para MM/yy (exemplo: 02/27)", e);
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Erro ao processar data de expiração: " + mmyy, e);
//        }
//    }
//
//    /**
//     * Converte LocalDate para formato MM/yy
//     */
//    public static String convertLocalDateToMMyy(LocalDate date) {
//        if (date == null) {
//            return null;
//        }
//        return date.format(MM_YY_FORMATTER);
//    }
//
//    /**
//     * Converte data no formato dd/MM para MM/yy do ano atual
//     */
//    public static String convertDDMMToMMYY(String ddmm) {
//        if (ddmm == null || ddmm.trim().isEmpty()) {
//            return null;
//        }
//
//        try {
//            String[] parts = ddmm.trim().split("/");
//            if (parts.length != 2) {
//                throw new IllegalArgumentException("Formato de data inválido. Use dd/MM");
//            }
//
//            int day = Integer.parseInt(parts[0]);
//            int month = Integer.parseInt(parts[1]);
//
//            if (day < 1 || day > 31) {
//                throw new IllegalArgumentException("Dia inválido: " + day);
//            }
//
//            if (month < 1 || month > 12) {
//                throw new IllegalArgumentException("Mês inválido: " + month);
//            }
//
//            String monthFormatted = String.format("%02d", month);
//            String currentYear = String.valueOf(LocalDate.now().getYear()).substring(2);
//
//            return monthFormatted + "/" + currentYear;
//
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("Formato de data inválido. Use números para dd/MM", e);
//        }
//    }
//
//    /**
//     * Valida se a data de expiração está no futuro
//     * Considera válida se a data de expiração é posterior ao mês atual
//     */
//    public static boolean isExpirationDateValid(LocalDate expirationDate) {
//        if (expirationDate == null) {
//            return false;
//        }
//
//        LocalDate now = LocalDate.now();
//        // Para cartões de crédito, consideramos o último dia do mês atual
//        LocalDate endOfCurrentMonth = now.withDayOfMonth(now.lengthOfMonth());
//
//        return expirationDate.isAfter(endOfCurrentMonth);
//    }
//
//    /**
//     * Valida se a data de expiração está no futuro com margem mínima
//     * @param expirationDate Data de expiração
//     * @param minMonthsInFuture Número mínimo de meses no futuro
//     */
//    public static boolean isExpirationDateValid(LocalDate expirationDate, int minMonthsInFuture) {
//        if (expirationDate == null) {
//            return false;
//        }
//
//        LocalDate minimumDate = LocalDate.now().plusMonths(minMonthsInFuture);
//        return expirationDate.isAfter(minimumDate);
//    }
//
//    /**
//     * Formata uma data para exibição no formato brasileiro
//     */
//    public static String formatToBrazilianDate(LocalDate date) {
//        if (date == null) {
//            return null;
//        }
//        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//    }
//
//    /**
//     * Valida se a string está no formato MM/yy correto
//     */
//    public static boolean isValidMMyyFormat(String dateString) {
//        if (dateString == null || dateString.trim().isEmpty()) {
//            return false;
//        }
//
//        return dateString.matches("^(0[1-9]|1[0-2])/\\d{2}$");
//    }

//    /**
//     * Converte data no formato MM/yy para LocalDate
//     * Assume o último dia do mês
//     */
//    public static LocalDate convertMMyyToLocalDate(String mmyy) {
//        if (mmyy == null || mmyy.isEmpty()) {
//            throw new IllegalArgumentException("Data de expiração não pode ser vazia");
//        }
//
//        try {
//            String[] parts = mmyy.split("/");
//            if (parts.length != 2) {
//                throw new IllegalArgumentException("Formato de data inválido. Use MM/yy");
//            }
//
//            int month = Integer.parseInt(parts[0]);
//            int year = 2000 + Integer.parseInt(parts[1]); // Assume século 21
//
//            if (month < 1 || month > 12) {
//                throw new IllegalArgumentException("Mês inválido: " + month);
//            }
//
//            // Retorna o último dia do mês
//            return LocalDate.of(year, month, 1).withDayOfMonth(
//                    LocalDate.of(year, month, 1).lengthOfMonth()
//            );
//
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("Formato de data inválido. Use MM/yy", e);
//        }
//    }
//
//    /**
//     * Converte LocalDate para formato MM/yy
//     */
//    public static String convertLocalDateToMMyy(LocalDate date) {
//        if (date == null) {
//            return null;
//        }
//        return date.format(MM_YY_FORMATTER);
//    }
//
//    /**
//     * Converte data no formato dd/MM para MM/yy do ano atual
//     */
//    public static String convertDDMMToMMYY(String ddmm) {
//        if (ddmm == null || ddmm.isEmpty()) {
//            return null;
//        }
//
//        try {
//            String[] parts = ddmm.split("/");
//            if (parts.length != 2) {
//                throw new IllegalArgumentException("Formato de data inválido. Use dd/MM");
//            }
//
//            String month = parts[1];
//            String currentYear = String.valueOf(LocalDate.now().getYear()).substring(2);
//
//            return month + "/" + currentYear;
//
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Formato de data inválido. Use dd/MM", e);
//        }
//    }
//
//    /**
//     * Valida se a data de expiração está no futuro
//     */
//    public static boolean isExpirationDateValid(LocalDate expirationDate) {
//        if (expirationDate == null) {
//            return false;
//        }
//        return expirationDate.isAfter(LocalDate.now());
//    }
}
