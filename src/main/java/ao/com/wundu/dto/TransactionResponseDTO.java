package ao.com.wundu.dto;

public record TransactionResponseDTO(String id, Double amount, String description, String type, String accountId, String dateTime) {}
