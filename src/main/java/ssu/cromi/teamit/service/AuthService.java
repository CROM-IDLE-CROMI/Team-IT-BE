package ssu.cromi.teamit.service;

import ssu.cromi.teamit.dto.auth.JwtResponse;
import ssu.cromi.teamit.dto.auth.LoginRequest;

public interface AuthService {
    JwtResponse authenticateAndCreateToken(LoginRequest request);
}
