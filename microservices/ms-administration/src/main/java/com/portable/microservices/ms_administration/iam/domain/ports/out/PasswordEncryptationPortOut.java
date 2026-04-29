package com.portable.microservices.ms_administration.iam.domain.ports.out;

public interface PasswordEncryptationPortOut {
    String encrypt(String plainPassword);
    boolean matches(String plainPassword, String encryptedPassword);
}
