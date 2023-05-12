package com.github.kestivvi.qna_rest_api.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(("/${app.prefix}/${app.version}/users"))
@Tag(name = "User", description = "User management endpoints")
public class AppUserController {

    private final AppUserService service;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all users", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = AppUserDto.class)))}),
            @ApiResponse(responseCode = "204", description = "Users not found", content = @Content)
    })
    public ResponseEntity<List<AppUserDto>> getAllAuthorities(@RequestParam Optional<List<String>> authorities) {
        List<AppUserDto> users = service.getAllUsers(authorities);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get user by id", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user",
                    content = {@Content(schema = @Schema(implementation = AppUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<AppUserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping("/me")
    @Operation(summary = "Get logged user", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user",
                    content = {@Content(schema = @Schema(implementation = AppUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<AppUserDto> getLoggedUser() {
        return ResponseEntity.ok(service.getCurrentUser());
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update user", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = {@Content(schema = @Schema(implementation = AppUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<AppUserDto> updateUser(@PathVariable Long id, @RequestBody AppUserDto userDto) {
        return ResponseEntity.ok(service.update(id, userDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted",
                    content = {@Content(schema = @Schema(implementation = AppUserDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/roles")
    @Operation(summary = "add role to user", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role added",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<Void> addRoleToUser(@PathVariable Long id, @RequestParam String role) {
        service.addRoleToUser(id, role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/roles")
    @Operation(summary = "remove role from user", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role removed",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable Long id, @RequestParam String role) {
        service.removeRoleFromUser(id, role);
        return ResponseEntity.noContent().build();
    }
}
