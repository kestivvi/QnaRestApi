package com.github.kestivvi.qna_rest_api.domain.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import com.github.kestivvi.qna_rest_api.auth.dto.RegisterDto;
import com.github.kestivvi.qna_rest_api.validation.CustomValidator;
import com.github.kestivvi.qna_rest_api.validation.ValidationService;
import com.github.kestivvi.qna_rest_api.validation.exceptions.ArgumentNotValidException;
import com.github.kestivvi.qna_rest_api.validation.exceptions.DuplicateException;
import com.github.kestivvi.qna_rest_api.validation.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AppUserValidator extends CustomValidator<AppUser> implements ValidationService<AppUser> {

    private final AppUserRepository repository;

    public AppUserValidator(Validator validator, AppUserRepository repository) {
        super(validator);
        this.repository = repository;
    }

    public void validateInput(RegisterDto registerDto) {
        Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
        if (!violations.isEmpty()) {
            Map<String, String> messages = new HashMap<>();
            for (ConstraintViolation<RegisterDto> violation : violations) {
                messages.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new ArgumentNotValidException("Not valid data provided", messages);
        }
    }

    @Override
    public AppUser validateExists(long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(className, id));
    }

    public AppUser validateExists(String username) {
        return repository.findByEmail(username).orElseThrow(() -> new NotFoundException(className, username));
    }

    @Override
    public void validateNotDuplicate(AppUser appUser) {
        repository.findByEmail(appUser.getUsername()).ifPresent(a -> {
            throw new DuplicateException(className, "username", a.getUsername());
        });
    }

    public void validateNotDuplicate(String username) {
        repository.findByEmail(username).ifPresent(a -> {
            throw new DuplicateException(className, "username", a.getUsername());
        });
    }

    public Role validateRoleExists(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new NotFoundException("Role", role);
    }

    public Set<Role> validateRoles(Set<String> authorities) {
        Set<Role> roles = new HashSet<>();
        for (String authority : authorities) {
            roles.add(validateRoleExists(authority));
        }
        return roles;
    }

    public void userAlreadyHasRole(AppUser user, Role roleToAdd) {
        if (user.getRoles().contains(roleToAdd)) {
            throw new DuplicateException("AppUser", "role", roleToAdd.name());
        }
    }

    public void userAlreadyHasNotRole(AppUser user, Role roleToRemove) {
        if (!user.getRoles().contains(roleToRemove)) {
            throw new NotFoundException("AppUser " + user.getUsername() + " hasn't got role", roleToRemove.name());
        }
    }
}
