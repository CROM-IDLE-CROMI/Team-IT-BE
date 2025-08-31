package ssu.cromi.teamit.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.mypage.MypageResponse;
import ssu.cromi.teamit.domain.*;
import ssu.cromi.teamit.entity.teamup.ProjectMember;
import ssu.cromi.teamit.entity.teamup.ProjectReview;
import ssu.cromi.teamit.repository.ProjectReviewRepository;
import ssu.cromi.teamit.repository.UserProfileRepository;
import ssu.cromi.teamit.repository.UserRepository;
import ssu.cromi.teamit.repository.teamup.ProjectMemberRepository;
import ssu.cromi.teamit.service.MypageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MypageServiceImpl implements MypageService {
    
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectReviewRepository projectReviewRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public MypageResponse getMypageInfo(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<ProjectMember> projectMembers = projectMemberRepository.findByUser(user);
        List<Long> projectIds = projectMembers.stream()
                .map(pm -> pm.getProject().getId())
                .collect(Collectors.toList());
        
        List<ProjectReview> reviews = projectReviewRepository.findByRevieweeId(uid);
        double averageStars = reviews.stream()
                .mapToDouble(ProjectReview::getScore)
                .average()
                .orElse(0.0);
        
        List<String> stacks = user.getStacks().stream()
                .map(Stack::getTag)
                .collect(Collectors.toList());
        
        String organization = user.getOrganizations().stream()
                .findFirst()
                .map(Organization::getName)
                .orElse("");
        
        String position = user.getPositions().stream()
                .findFirst()
                .map(Position::getTag)
                .orElse("");
        
        return MypageResponse.builder()
                .nickName(user.getNickName())
                .birthDay(LocalDateTime.of(user.getBirthday() / 10000, 
                                         (user.getBirthday() % 10000) / 100, 
                                         user.getBirthday() % 100, 0, 0))
                .organization(organization)
                .email(user.getEmail())
                .position(position)
                .description(getUserDescription(uid))
                .projects(projectIds)
                .stacks(stacks)
                .prize(getUserPrize(uid))
                .stars(averageStars)
                .build();
    }
    
    private String getUserDescription(String uid) {
        return userProfileRepository.findByUid(uid)
                .map(UserProfile::getDescription)
                .orElse("");
    }
    
    private String getUserPrize(String uid) {
        return userProfileRepository.findByUid(uid)
                .map(UserProfile::getPrize)
                .orElse("");
    }
}
