package com.api.muinda_kubika.model.Usuarios;

import com.api.muinda_kubika.Defaults.DefaultModel;
import com.api.muinda_kubika.model.Roles_permissions.RolesModel;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "USUARIOS")
@Getter
@Setter
public class DefaultUserModel extends DefaultModel implements UserDetails {

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String numeroDeTelefone;

    private LocalDate dataDeNascimento;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "USERS_ROLES",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Set<RolesModel> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // Permissões
        authorities.addAll(
            roles
                .stream()
                .filter(role -> Boolean.TRUE.equals(role.getIsActive()))
                .flatMap(role -> role.getPermissions().stream())
                .filter(perm -> Boolean.TRUE.equals(perm.getIsActive()))
                .map(perm -> new SimpleGrantedAuthority(perm.getDescricao()))
                .collect(Collectors.toSet())
        );

        // Roles
        authorities.addAll(
            roles
                .stream()
                .filter(role -> Boolean.TRUE.equals(role.getIsActive()))
                .map(role -> new SimpleGrantedAuthority(role.getDescricao()))
                .collect(Collectors.toSet())
        );

        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
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
        return isActive;
    }
}
