package ao.com.wundu.application.dtos.card;

public record CardValidateResponse(
        Long cardId,
        String bankName,
        boolean approved
) {
}
