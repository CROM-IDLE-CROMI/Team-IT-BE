package ssu.cromi.teamit.service;

import ssu.cromi.teamit.DTO.auth.SignupRequest;
import ssu.cromi.teamit.DTO.mypage.AwardResponse;
import ssu.cromi.teamit.DTO.user.*;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.UserAward;

import java.util.List;

public interface UserService {
    /**
     * 회원가입 처리
     */
    User register(SignupRequest DTO);

    /**
     * 아이디 중복 여부 확인
     * @return true면 이미 사용 중, false면 사용 가능
     */
    boolean existsByUid(String uid);

    /**
     * 사용자 프로필 업데이트
     */
    ProfileUpdateResponse updateProfile(String uid, ProfileUpdateRequest request);

    /**
     * 사용자 조회
     */
    User findByUid(String uid);

    /**
     * 사용자 스택 정보 업데이트
     */
    List<StackResponse> updateUserStacks(String uid, UserStackUpdateRequest request);

    /**
     * 사용자 스택 정보 조회
     */
    List<StackResponse> getUserStacks(String uid);

    /**
     * 사용자 수상 정보 업데이트
     */
    List<UserAward> updateUserAwards(String uid, UserAwardUpdateRequest request);

    /**
     * 사용자 수상 정보 업데이트 (DTO 형태 반환)
     */
    List<AwardResponse> updateUserAwardsAsDto(String uid, UserAwardUpdateRequest request);

    /**
     * 사용자 수상 정보 조회
     */
    List<UserAward> getUserAwards(String uid);

    /**
     * 사용자 수상 정보 조회 (DTO 형태)
     */
    List<AwardResponse> getUserAwardsAsDto(String uid);
}
