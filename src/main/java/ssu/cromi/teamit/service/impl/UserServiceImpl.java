package ssu.cromi.teamit.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssu.cromi.teamit.dto.auth.SignupRequest;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.exceptions.DuplicateEmailException;
import ssu.cromi.teamit.exceptions.DuplicateUsernameException;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.service.UserService;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(SignupRequest dto){
        if(userRepository.existsByUid(dto.getUid())){
            throw new DuplicateUsernameException("이미 사용중인 아이디입니다.");
        }
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new DuplicateEmailException("이미 사용중인 이메일입니다.");
        }
        User user = User.builder()
                .uid(dto.getUid())
                .email(dto.getEmail())
                .emailVerified(dto.isEmailVerified())
                .nickName(dto.getNickName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .birthday(dto.getBirthDay())
                .roles(Set.of(User.RoleEnum.ROLE_USER).toString())
                .point(0L)
                .build();
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return userRepository.save(user);
    }
    @Transactional
    @Override
    public boolean existsByUid(String uid){
        return userRepository.existsByUid(uid);
    }
}
