package com.portable.microservices.ms_administration.iam.domain.ports.in;

public interface LoginPortIn {
    String execute(String username, String plainPassword);
}
