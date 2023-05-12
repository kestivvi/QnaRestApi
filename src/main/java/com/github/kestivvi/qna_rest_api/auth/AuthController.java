package com.github.kestivvi.qna_rest_api.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import com.github.kestivvi.qna_rest_api.auth.dto.LoginDto;
import com.github.kestivvi.qna_rest_api.auth.dto.RegisterDto;
import com.github.kestivvi.qna_rest_api.auth.dto.TokenDto;
import com.github.kestivvi.qna_rest_api.validation.ErrorBody;

@RestController
@RequestMapping("/${app.prefix}/${app.version}/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<TokenDto> register(@RequestBody RegisterDto request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authenticate(@RequestBody LoginDto request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/denied")
    public ResponseEntity<ErrorBody> denied() {
        throw new AccessDeniedException("Access denied, don't have privileges to this resource");
    }

}