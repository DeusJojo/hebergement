package com.afpa.hebergement.security.service;

import com.afpa.hebergement.model.entity.AppUser;
import com.afpa.hebergement.model.entity.Role;
import com.afpa.hebergement.util.StringUtil;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Data
public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idAfpaCenter;
    private Integer id;
    private String fullName;
    private String password;
    private String email;
    private String beneficiaryNumber;
    private GrantedAuthority authority;
    StringBuilder builder;

    public UserDetailsImpl(Integer id,
                           String fullName,
                           String password,
                           String email,
                           String beneficiaryNumber,
                           Integer idAfpaCenter,
                           GrantedAuthority authority){
        this.id = id;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.beneficiaryNumber = beneficiaryNumber;
        this.idAfpaCenter = idAfpaCenter;
        this.authority = authority;

    }

    public static UserDetailsImpl build(AppUser user) {
        StringBuilder builder = new StringBuilder();
        Role role = user.getIdRole();
        GrantedAuthority authorities = new SimpleGrantedAuthority(role.getWordingRole().toString());
        return new UserDetailsImpl(
                user.getId(),
                builder.append(user.getFirstname()).append(" ").append(user.getName()).toString(),
                user.getPassword(),
                user.getEmail(),
                user.getBeneficiaryNumber(),
                user.getIdAfpaCenter().getId(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return fullName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}