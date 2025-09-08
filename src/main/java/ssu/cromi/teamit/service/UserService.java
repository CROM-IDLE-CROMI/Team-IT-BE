package ssu.cromi.teamit.service;

import ssu.cromi.teamit.DTO.auth.SignupRequest;
import ssu.cromi.teamit.DTO.user.ProfileUpdateRequest;
import ssu.cromi.teamit.domain.User;

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
    User updateProfile(String uid, ProfileUpdateRequest request);

    /**
     * 사용자 조회
     */
    User findByUid(String uid);
}
