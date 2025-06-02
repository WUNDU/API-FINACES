//package ao.com.wundu.unit.card;
//
//import ao.com.wundu.application.dtos.card.CreditCardCreateDTO;
//import ao.com.wundu.application.dtos.card.CreditCardResponseDTO;
//import ao.com.wundu.application.mappers.CreditCardMapper;
//import ao.com.wundu.domain.entities.CreditCard;
//import ao.com.wundu.domain.entities.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class CreditCardMapperTest {
//
//    private CreditCardMapper creditCardMapper;
//
//    @BeforeEach
//    void setUp() {
//        creditCardMapper = new CreditCardMapper();
//    }
//
//    @Test
//    void shouldMapCreateDTOToEntity() {
//        CreditCardCreateDTO dto = new CreditCardCreateDTO(
//                "4532015112830366",
//                "Test Bank",
//                new BigDecimal("1000.00"),
//                LocalDate.of(2025, 12, 1)
//        );
//
//        CreditCard card = creditCardMapper.toEntity(dto);
//
//        assertEquals(dto.cardNumber(), card.getCardNumber());
//        assertEquals(dto.bankName(), card.getBankName());
//        assertEquals(dto.creditLimit(), card.getCreditLimit());
//        assertEquals(dto.expirationDate(), card.getExpirationDate());
//        assertNull(card.getUser());
//    }
//
//    @Test
//    void shouldMapEntityToResponseDTO() {
//        User user = new User();
//        user.setId(UUID.randomUUID().toString());
//
//        CreditCard card = new CreditCard();
//        card.setId(UUID.randomUUID().toString());
//        card.setCardNumber("4532015112830366");
//        card.setBankName("Test Bank");
//        card.setCreditLimit(new BigDecimal("1000.00"));
//        card.setExpirationDate(LocalDate.of(2025, 12, 1));
//        card.setUser(user);
//
//        CreditCardResponseDTO response = creditCardMapper.toResponseDTO(card);
//
//        assertEquals(card.getId(), response.id());
//        assertEquals("**** **** **** 0366", response.maskedCardNumber());
//        assertEquals(card.getBankName(), response.bankName());
//        assertEquals(card.getCreditLimit(), response.creditLimit());
//        assertEquals("12/25", response.expirationDate());
//        assertEquals(user.getId(), response.userId());
//    }
//}
