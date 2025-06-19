package ao.com.wundu.application.mappers;

import ao.com.wundu.application.dtos.card.CreditCardCreateDTO;
import ao.com.wundu.application.dtos.card.CreditCardResponseDTO;
import ao.com.wundu.domain.entities.CreditCard;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class CreditCardMapper {

    private final ModelMapper modelMapper;

    public CreditCardMapper() {
        this.modelMapper = new ModelMapper();

        // Configuração do mapeamento CreditCardCreateDTO -> CreditCard
        Converter<CreditCardCreateDTO, CreditCard> createDTOToEntityConverter = ctx -> {
            CreditCardCreateDTO src = ctx.getSource();
            if (src == null) {
                throw new IllegalArgumentException("CreditCardCreateDTO cannot be null");
            }
            CreditCard card = new CreditCard();
            card.setCardNumber(src.cardNumber() != null ? src.cardNumber() : "");
            card.setBankName(src.bankName() != null ? src.bankName() : "");
            card.setExpirationDate(src.expirationDate());
            return card;
        };
        modelMapper.createTypeMap(CreditCardCreateDTO.class, CreditCard.class)
                .setConverter(createDTOToEntityConverter);

        // Configuração do mapeamento CreditCard -> CreditCardResponseDTO
        Converter<CreditCard, CreditCardResponseDTO> cardToResponseDTOConverter = ctx -> {
            CreditCard src = ctx.getSource();
            if (src == null) {
                throw new IllegalArgumentException("CreditCard cannot be null");
            }
            return new CreditCardResponseDTO(
                    src.getId(),
                    src.getMaskedCardNumber() != null ? src.getMaskedCardNumber() : "**** **** **** ****",
                    src.getBankName(),
                    src.getCardHolderName(),
                    src.getExpirationDate() != null ? src.getFormattedExpirationDate() : null,
                    src.getUser() != null ? src.getUser().getId() : null
            );
        };
        modelMapper.createTypeMap(CreditCard.class, CreditCardResponseDTO.class)
                .setConverter(cardToResponseDTOConverter);
    }

    public CreditCard toEntity(CreditCardCreateDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CreditCardCreateDTO cannot be null");
        }
        return modelMapper.map(dto, CreditCard.class);
    }

    public CreditCardResponseDTO toResponseDTO(CreditCard card) {
        if (card == null) {
            throw new IllegalArgumentException("CreditCard cannot be null");
        }
        return modelMapper.map(card, CreditCardResponseDTO.class);
    }
}
