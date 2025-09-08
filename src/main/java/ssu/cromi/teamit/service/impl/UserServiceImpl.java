package ssu.cromi.teamit.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssu.cromi.teamit.DTO.auth.SignupRequest;
import ssu.cromi.teamit.DTO.mypage.AwardResponse;
import ssu.cromi.teamit.DTO.user.ProfileUpdateRequest;
import ssu.cromi.teamit.DTO.user.ProfileUpdateResponse;
import ssu.cromi.teamit.DTO.user.UserAwardUpdateRequest;
import ssu.cromi.teamit.DTO.user.UserStackUpdateRequest;
import ssu.cromi.teamit.domain.Position;
import ssu.cromi.teamit.domain.Stack;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.UserAward;
import ssu.cromi.teamit.entity.UserStack;
import ssu.cromi.teamit.exception.UserNotFoundException;
import ssu.cromi.teamit.exceptions.DuplicateEmailException;
import ssu.cromi.teamit.exceptions.DuplicateUsernameException;
import ssu.cromi.teamit.repository.*;
import ssu.cromi.teamit.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserStackRepository userStackRepository;
    private final UserAwardRepository userAwardRepository;
    private final StackRepository stackRepository;
    private final PositionRepository positionRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserStackRepository userStackRepository,
                          UserAwardRepository userAwardRepository, StackRepository stackRepository,
                          PositionRepository positionRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.userStackRepository = userStackRepository;
        this.userAwardRepository = userAwardRepository;
        this.stackRepository = stackRepository;
        this.positionRepository = positionRepository;
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

    @Override
    @Transactional
    public ProfileUpdateResponse updateProfile(String uid, ProfileUpdateRequest request) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + uid));
        
        user.setNickName(request.getNickName());
        user.setBirthday(request.getBirthday());
        user.setEmail(request.getEmail());
        user.setIntroduction(request.getIntroduction());
        
        // position 처리
        if (request.getPosition() != null && !request.getPosition().trim().isEmpty()) {
            Position position = positionRepository.findByTag(request.getPosition())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포지션입니다: " + request.getPosition()));
            user.getPositions().clear();
            user.getPositions().add(position);
        }
        
        User savedUser = userRepository.save(user);
        
        String position = savedUser.getPositions().stream()
                .findFirst()
                .map(Position::getTag)
                .orElse("");
        
        // birthday를 LocalDate로 변환
        LocalDate birthday = LocalDate.of(
                savedUser.getBirthday() / 10000,
                (savedUser.getBirthday() % 10000) / 100,
                savedUser.getBirthday() % 100
        );
        
        return ProfileUpdateResponse.builder()
                .nickName(savedUser.getNickName())
                .birthday(birthday)
                .email(savedUser.getEmail())
                .introduction(savedUser.getIntroduction())
                .position(position)
                .build();
    }

    @Override
    public User findByUid(String uid) {
        return userRepository.findByUid(uid)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + uid));
    }

    @Override
    @Transactional
    public List<UserStack> updateUserStacks(String uid, UserStackUpdateRequest request) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + uid));

        // 대표 스택 개수 검증 (최대 3개)
        long representativeCount = request.getStacks().stream()
                .mapToLong(stack -> stack.getIsRepresentative() ? 1 : 0)
                .sum();
        
        if (representativeCount > 3) {
            throw new IllegalArgumentException("대표 스택은 최대 3개까지만 선택할 수 있습니다.");
        }

        // 기존 사용자 스택 모두 삭제
        List<UserStack> existingStacks = userStackRepository.findByUser(user);
        userStackRepository.deleteAll(existingStacks);

        // 새로운 스택 정보 저장
        List<UserStack> newUserStacks = new ArrayList<>();
        for (UserStackUpdateRequest.StackInfo stackInfo : request.getStacks()) {
            Stack stack = stackRepository.findById(stackInfo.getStackId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스택입니다: " + stackInfo.getStackId()));
            
            UserStack userStack = UserStack.builder()
                    .user(user)
                    .stack(stack)
                    .level(stackInfo.getLevel())
                    .isRepresentative(stackInfo.getIsRepresentative())
                    .build();
            
            newUserStacks.add(userStack);
        }

        return userStackRepository.saveAll(newUserStacks);
    }

    @Override
    public List<UserStack> getUserStacks(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + uid));
        
        return userStackRepository.findByUser(user);
    }

    @Override
    @Transactional
    public List<UserAward> updateUserAwards(String uid, UserAwardUpdateRequest request) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + uid));

        // 기존 수상 정보 모두 삭제
        List<UserAward> existingAwards = userAwardRepository.findByUser(user);
        userAwardRepository.deleteAll(existingAwards);

        // 새로운 수상 정보 저장
        List<UserAward> newUserAwards = new ArrayList<>();
        for (UserAwardUpdateRequest.AwardInfo awardInfo : request.getAwards()) {
            UserAward userAward = UserAward.builder()
                    .user(user)
                    .awardName(awardInfo.getAwardName())
                    .organization(awardInfo.getOrganization())
                    .awardDate(awardInfo.getAwardDate())
                    .description(awardInfo.getDescription())
                    .build();
            
            newUserAwards.add(userAward);
        }

        return userAwardRepository.saveAll(newUserAwards);
    }

    @Override
    public List<UserAward> getUserAwards(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + uid));
        
        return userAwardRepository.findByUser(user);
    }

    @Override
    public List<AwardResponse> getUserAwardsAsDto(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + uid));
        
        return userAwardRepository.findByUser(user).stream()
                .map(userAward -> AwardResponse.builder()
                        .awardName(userAward.getAwardName())
                        .organization(userAward.getOrganization())
                        .awardDate(userAward.getAwardDate())
                        .description(userAward.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<AwardResponse> updateUserAwardsAsDto(String uid, UserAwardUpdateRequest request) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + uid));

        // 기존 수상 정보 모두 삭제
        List<UserAward> existingAwards = userAwardRepository.findByUser(user);
        userAwardRepository.deleteAll(existingAwards);

        // 새로운 수상 정보 저장
        List<UserAward> newUserAwards = new ArrayList<>();
        for (UserAwardUpdateRequest.AwardInfo awardInfo : request.getAwards()) {
            UserAward userAward = UserAward.builder()
                    .user(user)
                    .awardName(awardInfo.getAwardName())
                    .organization(awardInfo.getOrganization())
                    .awardDate(awardInfo.getAwardDate())
                    .description(awardInfo.getDescription())
                    .build();
            
            newUserAwards.add(userAward);
        }

        List<UserAward> savedAwards = userAwardRepository.saveAll(newUserAwards);
        
        // DTO 형태로 변환하여 반환
        return savedAwards.stream()
                .map(userAward -> AwardResponse.builder()
                        .awardName(userAward.getAwardName())
                        .organization(userAward.getOrganization())
                        .awardDate(userAward.getAwardDate())
                        .description(userAward.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
