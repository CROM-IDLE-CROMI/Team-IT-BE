package ssu.cromi.teamit.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssu.cromi.teamit.DTO.auth.JwtResponse;
import ssu.cromi.teamit.DTO.auth.LoginRequest;
import ssu.cromi.teamit.domain.RefreshToken;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.security.JwtUtils;
import ssu.cromi.teamit.service.AuthService;
import ssu.cromi.teamit.service.RefreshTokenService;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, RefreshTokenService refreshTokenService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public JwtResponse authenticateAndCreateToken(LoginRequest req){
        User user = userRepository.findByUid(req.getUid())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없음"));
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않음");
        }

        String accesstoken = jwtUtils.generateJwtToken(user);
        Long expiresIn = jwtUtils.getJwtExpirationMs();
        RefreshToken refreshTokenEntity = refreshTokenService.createRefreshToken(user);

        return new JwtResponse(accesstoken, "Bearer", expiresIn, refreshTokenEntity.getToken()  ,user.getUid());
    }
}