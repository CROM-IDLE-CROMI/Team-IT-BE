package ssu.cromi.teamit.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.mypage.AwardResponse;
import ssu.cromi.teamit.DTO.mypage.MyRatingResponse;
import ssu.cromi.teamit.DTO.mypage.MypageResponse;
import ssu.cromi.teamit.DTO.mypage.StackWithLevel;
import ssu.cromi.teamit.domain.Organization;
import ssu.cromi.teamit.domain.Position;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.domain.UserProfile;
import ssu.cromi.teamit.entity.teamup.ProjectMember;
import ssu.cromi.teamit.entity.teamup.ProjectReview;
import ssu.cromi.teamit.repository.*;
import ssu.cromi.teamit.repository.teamup.ProjectMemberRepository;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
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
    private final UserStackRepository userStackRepository;
    private final UserAwardRepository userAwardRepository;
    private final ProjectRepository projectRepository;

    @Override
    public MypageResponse getMypageInfo(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<ProjectMember> projectMembers = projectMemberRepository.findByUser(user);
        List<String> projectNames = projectMembers.stream()
                .map(pm -> pm.getProject().getProjectName())
                .collect(Collectors.toList());
        
        List<ProjectReview> reviews = projectReviewRepository.findByRevieweeId(uid);
        double averageStars = reviews.stream()
                .mapToDouble(ProjectReview::getScore)
                .average()
                .orElse(0.0);
        
        List<StackWithLevel> stacks = userStackRepository.findByUserUidAndIsRepresentativeTrue(uid).stream()
                .limit(3)
                .map(userStack -> StackWithLevel.builder()
                        .stackName(userStack.getStack().getTag())
                        .icon(userStack.getStack().getIcon())
                        .level(userStack.getLevel().getDisplayName())
                        .build())
                .collect(Collectors.toList());
        
        String organization = user.getOrganizations().stream()
                .findFirst()
                .map(Organization::getName)
                .orElse("");
        
        String position = user.getPositions().stream()
                .findFirst()
                .map(Position::getTag)
                .orElse("");

        List<AwardResponse> awards = userAwardRepository.findByUser(user).stream()
                .map(userAward -> AwardResponse.builder()
                        .awardName(userAward.getAwardName())
                        .organization(userAward.getOrganization())
                        .awardDate(userAward.getAwardDate())
                        .description(userAward.getDescription())
                        .build())
                .collect(Collectors.toList());
        
        return MypageResponse.builder()
                .nickName(user.getNickName())
                .birthDay(LocalDateTime.of(user.getBirthday() / 10000, 
                                         (user.getBirthday() % 10000) / 100, 
                                         user.getBirthday() % 100, 0, 0))
                .organization(organization)
                .email(user.getEmail())
                .position(position)
                .description(getUserDescription(uid))
                .profileImg(user.getProfileImg())
                .backgroundImg(user.getBackgroundImg())
                .border(user.getBorder())
                .badge(user.getBadge())
                .projects(projectNames)
                .stacks(stacks)
                .awards(awards)
                .stars(averageStars)
                .build();
    }
    
    private String getUserDescription(String uid) {
        return userProfileRepository.findByUid(uid)
                .map(UserProfile::getDescription)
                .orElse("");
    }
    
    @Override
    public List<MyRatingResponse> getMyRatings(String uid) {
        List<ProjectReview> reviews = projectReviewRepository.findByRevieweeId(uid);
        
        return reviews.stream()
                .map(review -> {
                    String reviewerNickName = userRepository.findByUid(review.getReviewerId())
                            .map(User::getNickName)
                            .orElse("Unknown");
                    
                    String projectName = "Unknown Project";
                    String platform = "Unknown";
                    
                    if (review.getProjectId() != null) {
                        var project = projectRepository.findById(review.getProjectId());
                        if (project.isPresent()) {
                            projectName = project.get().getProjectName();
                            platform = project.get().getPlatform().name();
                        }
                    }
                    
                    return new MyRatingResponse(
                            review.getId(),
                            review.getReviewerId(),
                            reviewerNickName,
                            review.getScore(),
                            review.getContent(),
                            review.getUpdatedAt(),
                            projectName,
                            platform
                    );
                })
                .collect(Collectors.toList());
    }
}
