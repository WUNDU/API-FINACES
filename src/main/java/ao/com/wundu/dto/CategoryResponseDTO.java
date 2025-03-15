package ao.com.wundu.dto;

import java.util.List;

public record CategoryResponseDTO(String idCategory, String nameCategory, String icon, List<String> transactionIds) {
}
