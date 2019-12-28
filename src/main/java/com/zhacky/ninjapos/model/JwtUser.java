package com.zhacky.ninjapos.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.Date;

public class JwtUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final Integer pin;
    private final Boolean enabled;
    private final Boolean locked;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Date lastPasswordResetDate;

    /**
     * @param id User ID
     * @param username Username of User
     * @param email (Login) Email
     * @param password Password
     * @param pin Six (6) digit PIN Code
     * @param enabled Boolean if enabled
     * @param locked Boolean if locked
     * @param authorities List of authorizations
     * @param lastPasswordResetDate Date of last password reset done
     */
    public JwtUser(Long id, String username, String email, String password, Integer pin, Boolean enabled, Boolean locked, Collection<? extends GrantedAuthority> authorities, Date lastPasswordResetDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.pin = pin;
        this.enabled = enabled;
        this.locked = locked;
        this.authorities = authorities;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Integer getPin() {
        return pin;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
}
