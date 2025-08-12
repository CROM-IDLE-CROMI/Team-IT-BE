package ssu.cromi.teamit.service;

import ssu.cromi.teamit.DTO.auth.JwtResponse;
import ssu.cromi.teamit.DTO.auth.LoginRequest;

public interface AuthService {
    JwtResponse authenticateAndCreateToken(LoginRequest request);
}
