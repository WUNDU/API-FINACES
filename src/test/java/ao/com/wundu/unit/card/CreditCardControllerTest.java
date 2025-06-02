//package ao.com.wundu.unit.card;
//
//import ao.com.wundu.application.dtos.card.CreditCardCreateDTO;
//import ao.com.wundu.application.dtos.card.CreditCardResponseDTO;
//import ao.com.wundu.application.usercases.card.AssociateCreditCardUseCase;
//import ao.com.wundu.application.usercases.card.ListCreditCardsUseCase;
//import ao.com.wundu.infrastructure.security.JwtUtil;
//import ao.com.wundu.presentation.controllers.CreditCardController;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(CreditCardController.class)
//public class CreditCardControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AssociateCreditCardUseCase associateCreditCardUseCase;
//
//    @MockBean
//    private ListCreditCardsUseCase listCreditCardsUseCase;
//
//    @MockBean
//    private JwtUtil jwtUtil;
//
//    @Test
//    void shouldAddCreditCardSuccessfully() throws Exception {
//        String userId = "b28906bf-561b-4b61-9bd4-fd7c23eec23f";
//        CreditCardCreateDTO createDTO = new CreditCardCreateDTO(
//                "4532015112830366",
//                "Test Bank",
//                new BigDecimal("1000.00"),
//                LocalDate.now().plusMonths(1)
//        );
//        CreditCardResponseDTO responseDTO = new CreditCardResponseDTO(
//                "card-id",
//                "**** **** **** 0366",
//                "Test Bank",
//                new BigDecimal("1000.00"),
//                "06/25",
//                userId
//        );
//
//        when(associateCreditCardUseCase.execute(eq(userId), any(CreditCardCreateDTO.class)))
//                .thenReturn(responseDTO);
//
//        mockMvc.perform(post("/api/credit-cards/user/{userId}", userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"cardNumber\":\"4532015112830366\",\"bankName\":\"Test Bank\",\"creditLimit\":1000.00,\"expirationDate\":\"2025-06-13\"}"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value("card-id"))
//                .andExpect(jsonPath("$.maskedCardNumber").value("**** **** **** 0366"))
//                .andExpect(jsonPath("$.bankName").value("Test Bank"))
//                .andExpect(jsonPath("$.creditLimit").value(1000.00))
//                .andExpect(jsonPath("$.expirationDate").value("06/25"))
//                .andExpect(jsonPath("$.userId").value(userId));
//    }
//}
