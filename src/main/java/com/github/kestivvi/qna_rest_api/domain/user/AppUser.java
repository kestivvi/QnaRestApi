package com.github.kestivvi.qna_rest_api.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", initialValue = 101)
    private Long id;

    @NotBlank(message = "Field must not be blank")
    @Size(min = 1, max = 50, message = "Field must be between {min} and {max} characters")
    private String firstname;
    @NotBlank(message = "Field must not be blank")
    @Size(min = 1, max = 50, message = "Field must be between {min} and {max} characters")
    private String lastname;
    @NotBlank(message = "Field must not be blank")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Password is mandatory")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = EAGER)
    @Enumerated(EnumType.ORDINAL)
    @CollectionTable(name = "USER_X_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "id"))
    @Column(name = "ROLE")
    private Set<Role> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(e -> new SimpleGrantedAuthority(e.name())).toList();
    }

    public Set<Role> getRoles() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
