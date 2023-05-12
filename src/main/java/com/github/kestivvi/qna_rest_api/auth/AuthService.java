package com.github.kestivvi.qna_rest_api.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.github.kestivvi.qna_rest_api.auth.dto.LoginDto;
import com.github.kestivvi.qna_rest_api.auth.dto.RegisterDto;
import com.github.kestivvi.qna_rest_api.auth.dto.TokenDto;
import com.github.kestivvi.qna_rest_api.auth.dto.TokenService;
import com.github.kestivvi.qna_rest_api.domain.user.AppUser;
import com.github.kestivvi.qna_rest_api.domain.user.AppUserService;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppUserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public TokenDto register(RegisterDto request) {
        String jwtToken = tokenService.generateToken(userService.register(request));
        return TokenDto.builder()
                .token(jwtToken)
                .build();
    }

    public TokenDto authenticate(LoginDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        AppUser user = userService.findByEmail(request.getEmail());
        String jwtToken = tokenService.generateToken(user);
        return TokenDto.builder()
                .token(jwtToken)
                .build();
    }
}