package com.github.kestivvi.qna_rest_api.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Set<String> authorities;

}
