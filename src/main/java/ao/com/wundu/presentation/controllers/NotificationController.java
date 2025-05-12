package ao.com.wundu.presentation.controllers;

import ao.com.wundu.application.dtos.NotificationCreateDTO;
import ao.com.wundu.application.dtos.NotificationResponseDTO;
import ao.com.wundu.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@RequestBody NotificationCreateDTO create) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(create));
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> findAllNotifications() {
        return ResponseEntity.ok(notificationService.findAllNotifications());
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}
