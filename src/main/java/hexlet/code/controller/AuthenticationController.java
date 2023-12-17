package hexlet.code.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import hexlet.code.utils.JWTUtils;
import hexlet.code.dto.AuthRequest;

@RestController
@RequiredArgsConstructor
public final class AuthenticationController {

    private final JWTUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/api/login")
    public ResponseEntity<String> create(@RequestBody AuthRequest authRequest) {
        try {
            var authentication = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword());

            authenticationManager.authenticate(authentication);

            var token = jwtUtils.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
