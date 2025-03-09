package ao.com.wundu.controller;

import ao.com.wundu.dto.UserCreateDTO;
import ao.com.wundu.dto.UserListDTO;
import ao.com.wundu.dto.UserUpdateDTO;
import ao.com.wundu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserListDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserListDTO> createUser(
            @Parameter(description = "User data for creation", required = true)
            @Valid @RequestBody UserCreateDTO create) {

        UserListDTO user = userService.createUser(create);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Updates user details by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserListDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserListDTO> updateUser(
            @Parameter(description = "User ID", required = true, example = "b28906bf-561b-4b61-9bd4-fd7c23eec23f")
            @PathVariable String id,

            @Parameter(description = "Updated user data", required = true)
            @Valid @RequestBody UserUpdateDTO update) {

        UserListDTO user = userService.updateUser(id, update);

        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrives a user by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retriever successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserListDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserListDTO> findUserById(
            @Parameter(description = "User ID", required = true, example = "b28906bf-561b-4b61-9bd4-fd7c23eec23f")
            @PathVariable String id) {

        UserListDTO user = userService.findUserById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

    @GetMapping
    @Operation(summary = "List All users", description = "Retrives a list of all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retriever successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserListDTO.class)))
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<UserListDTO>> findAllUsers() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findAllUsers());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "List All users", description = "Retrives a list of all users")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserListDTO.class)))
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
