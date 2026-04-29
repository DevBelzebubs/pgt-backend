package com.portable.microservices.ms_administration.iam.infrastructure.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.portable.microservices.ms_administration.iam.domain.ports.out.PasswordEncryptationPortOut;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BcryptPasswordEncoderAdapter implements PasswordEncryptationPortOut {
    
    private final PasswordEncoder passwordEncoder;

    @Override
    public String encrypt(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    @Override
    public boolean matches(String plainPassword, String encryptedPassword) {
        return passwordEncoder.matches(plainPassword, encryptedPassword);
    }

    
}
