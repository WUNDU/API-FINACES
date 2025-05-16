package ao.com.wundu.presentation.controllers;

import ao.com.wundu.application.dtos.notification.NotificationResponseDTO;
import ao.com.wundu.application.usercases.notification.MarkNotificationAsReadUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;

    public NotificationController(MarkNotificationAsReadUseCase markNotificationAsReadUseCase) {
        this.markNotificationAsReadUseCase = markNotificationAsReadUseCase;
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark a notification as read")
    @ApiResponse(responseCode = "200", description = "Notification marked as read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(@PathVariable String id) {
        logger.info("Marcando notificação como lida: {}", id);
        NotificationResponseDTO response = markNotificationAsReadUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}
