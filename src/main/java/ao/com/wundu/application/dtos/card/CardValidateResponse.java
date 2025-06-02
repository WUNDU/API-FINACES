package ao.com.wundu.application.dtos.card;

public record CardValidateResponse(
        Long cardId,
        boolean approved
) {
}
