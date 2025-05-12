package ao.com.wundu.application.dtos;

public record TransactionCreateDTO(
        Double amount,
        String description,
        String type) {
}
