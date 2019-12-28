package com.zhacky.ninjapos.controller;

import com.zhacky.ninjapos.data.request.JwtAuthByPinRequest;
import com.zhacky.ninjapos.data.response.JwtAuthenticationResponse;
import com.zhacky.ninjapos.data.request.JwtAuthenticationRequest;
import com.zhacky.ninjapos.exception.AuthenticationException;
import com.zhacky.ninjapos.model.JwtUser;
import com.zhacky.ninjapos.utility.JwtTokenUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
@Api(
        value = "Security",
        description = "Rest endpoints for user authentication and authorization",
        consumes = "application/json",
        produces = "application/json"
)
public class AuthController {

    @Value("Authorization")
    private String tokenHeader;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        authenticate(username, password);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @PostMapping(value = "/auth/pin")
    public ResponseEntity<?> createAuthenticationTokenByPin(@RequestBody JwtAuthByPinRequest authByPinRequest) {
        String username = authByPinRequest.getUsername();
        Integer pin = authByPinRequest.getPin();
        final JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        Integer userPin = user.getPin();
        if (userPin.equals(pin)) {
            final String token = jwtTokenUtil.generateToken(user);
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect PIN");
        }
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User account is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
}
