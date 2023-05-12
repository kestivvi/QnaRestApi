package com.github.kestivvi.qna_rest_api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.github.kestivvi.qna_rest_api.validation.passwordValidator.ValidPassword;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "Field must not be blank")
    @Size(min = 1, max = 50, message = "Field must be between {min} and {max} characters")
    private String firstname;
    @NotBlank(message = "Field must not be blank")
    @Size(min = 1, max = 50, message = "Field must be between {min} and {max} characters")
    private String lastname;
    @NotBlank(message = "Field must not be blank")
    @Email(message = "Email should be valid")
    private String email;
    @ValidPassword
    private String password;

}