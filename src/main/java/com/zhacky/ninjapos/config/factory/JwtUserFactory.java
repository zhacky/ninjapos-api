package com.zhacky.ninjapos.config.factory;

import com.zhacky.ninjapos.model.Authority;
import com.zhacky.ninjapos.model.JwtUser;
import com.zhacky.ninjapos.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUserFactory {

    private JwtUserFactory() {

    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getPin(),
                user.getEnabled(), user.getLocked(), mapToGrantedAuthorities(user.getAuthorities()),
                user.getLastPasswordResetDate()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getName().name())
        ).collect(Collectors.toList());
    }
}
