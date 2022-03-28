package com.mnb.security.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class AccountDetails implements UserDetails {

    private String id;
    private String username;
    private String password;
    private boolean blocked;
    private Set<? extends GrantedAuthority> authorities;

    public AccountDetails(String id, String username,
                          String password, boolean blocked,
                          Set<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.blocked = blocked;
        this.authorities = authorities;
    }

    public static AccountDetails build(Account account) {
        final Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(account.getRoles().stream()
                .flatMap(role -> role.getPrivileges().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet()));
        authorities.addAll(account.getPrivileges().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet()));
        return new AccountDetails(
                account.getId(),
                account.getUsername(),
                account.getPassword(),
                account.isBlocked(),
                authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !blocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final AccountDetails user = (AccountDetails) o;
        return Objects.equals(id, user.id);
    }
}
