package com.github.kestivvi.qna_rest_api.domain.user;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.github.kestivvi.qna_rest_api.auth.dto.RegisterDto;
import com.github.kestivvi.qna_rest_api.validation.exceptions.NotAdminException;
import com.github.kestivvi.qna_rest_api.validation.exceptions.NotAuthorizedException;

import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository repository;
    private final ModelMapper mapper;
    private final AppUserValidator validator;
    private final PasswordEncoder passwordEncoder;

    public AppUserDto convertToDto(AppUser p) {
        return mapper.map(p, AppUserDto.class);
    }

    public AppUser convertToEntity(AppUserDto dto) {
        return mapper.map(dto, AppUser.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return validator.validateExists(username);
    }

    public AppUser findByEmail(String email) {
        return validator.validateExists(email);
    }

    public AppUserDto getUserById(long id) {
        return convertToDto(validator.validateExists(id));
    }

    public List<AppUserDto> getAllUsers(Optional<List<String>> authorities) {
        List<AppUser> users = repository.findAll().stream().toList();

        if (authorities.isPresent()) {
            ArrayList<Role> roles = new ArrayList<>();
            for (String authority : authorities.get()) {
                roles.add(validator.validateRoleExists(authority));
            }
            users = users.stream()
                    .filter(u -> u.getRoles().containsAll(roles))
                    .toList();
        }

        return users.stream()
                .map(this::convertToDto)
                .toList();
    }

    public AppUser register(RegisterDto request) {
        validator.validateInput(request);
        validator.validateNotDuplicate(request.getEmail());
        AppUser user = AppUser.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .authorities(Collections.singleton(Role.ROLE_USER))
                .build();
        return repository.save(user);
    }

    public AppUserDto update(long id, AppUserDto request) {
        if (!hasRights(id))
            throw new NotAuthorizedException("AppUser", Long.toString(id), SecurityContextHolder.getContext().getAuthentication().getName());
        AppUser user = validator.validateExists(id);
        validator.validateNotDuplicate(request.getEmail());
        if (user.getRoles().contains(Role.ROLE_ADMIN))
            user.setAuthorities(validator.validateRoles(request.getAuthorities()));
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setAuthorities(request.getAuthorities().stream().map(Role::valueOf).collect(Collectors.toSet()));
        validator.validateInput(user);
        repository.save(user);
        return convertToDto(user);
    }

    public void delete(long id) {
        if (!hasRights(id))
            throw new NotAuthorizedException("AppUser", Long.toString(id), SecurityContextHolder.getContext().getAuthentication().getName());
        AppUser user = validator.validateExists(id);
        repository.delete(user);
    }

    public boolean hasRights(long id) {
        try {
            AppUser auth = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            AppUser user = findByEmail(auth.getUsername());
            return user.getRoles().contains(Role.ROLE_ADMIN) || user.getId() == id;
        } catch (ClassCastException e) {
            throw new NotAuthorizedException("AppUser", Long.toString(id), "not logged in");
        }
    }

    public boolean isAdmin() {
        try {
            AppUser auth = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            AppUser user = findByEmail(auth.getUsername());
            return user.getRoles().contains(Role.ROLE_ADMIN);
        } catch (ClassCastException e) {
            throw new NotAuthorizedException("AppUser", "", "not logged in");
        }
    }

    public AppUserDto getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        AppUser user = findByEmail(authentication.getName());
        return convertToDto(user);
    }

    public void addRoleToUser(long id, String role) {
        AppUser user = validator.validateExists(id);
        Role roleToAdd = validator.validateRoleExists(role);
        if (!isAdmin())
            throw new NotAdminException();
        validator.userAlreadyHasRole(user, roleToAdd);
        user.getRoles().add(roleToAdd);
        repository.save(user);
    }

    public void removeRoleFromUser(long id, String role) {
        AppUser user = validator.validateExists(id);
        Role roleToRemove = validator.validateRoleExists(role);
        if (!isAdmin())
            throw new NotAdminException();
        validator.userAlreadyHasNotRole(user, roleToRemove);
        user.getRoles().remove(roleToRemove);
        repository.save(user);
    }
}
