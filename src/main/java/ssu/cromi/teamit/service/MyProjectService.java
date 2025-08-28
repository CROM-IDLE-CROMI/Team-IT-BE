package ssu.cromi.teamit.service;

import jakarta.validation.Valid;
import ssu.cromi.teamit.DTO.myproject.*;

import java.util.List;

public interface MyProjectService {
    /**
     특정 사용자가 참여하고 있는 '진행중'인 프로젝트 목록 조회
     @param uid 사용자(User)의 고유 ID
     @return 진행중인 프로젝트 목록 (List<InProgressProject>)
     */
    List<InProgressProject> getInProgressProjects(String uid);
    /**
     특정 사용자가 참여했던 '완료'된 프로젝트 목록을 조회
     @param uid 사용자(User)의 고유 ID
     @return 완료된 프로젝트 목록 (List<CompletedProject>)
     */
    List<CompletedProject> getCompletedProjects(String uid);
    /**
     특정 사용자가 참여한 모든 프로젝트 목록(진행중, 완료)을 조회
     @param uid 사용자(User)의 고유 ID
     @return 진행중, 완료 프로젝트 목록을 모두 담은 응답 객체 (MyProjectResponse)
     */
    MyProjectResponse getAllMyProjects(String uid);

    /**
     특정 프로젝트의 모든 마일스톤 목록 조회
     * @param projectId 프로젝트 ID
     * @return 해당 프로젝트의 마일스톤 DTO 목록
     */
    List<MilestoneResponse> getMilestone(Long projectId);

    /**
     * 내 프로젝트의 상세 정보를 조회 (소개, 멤버, 진행률, 마일스톤 상위 N개 포함)
     * @param projectId 조회할 프로젝트 ID
     * @param milestoneLimit 조회할 마일스톤 개수
     * @return MyProjectDetailResponse
     */
    MyProjectDetailResponse getMyProjectDetail(Long projectId, int milestoneLimit);

    /**
     * 새 마일스톤 생성
     * @param projectId 마일스톤을 추가할 프로젝트 ID
     * @param milestoneRequest 생성할 마일스톤 정보
     * @return 생성된 마일스톤 정보
     */
    MilestoneResponse createMilestone(Long projectId, MilestoneRequest milestoneRequest);

    /**
     * 마일스톤 내용 수정
     * @param projectId 프로젝트 ID
     * @param milestoneId 수정할 마일스톤 ID
     * @param milestoneRequest 수정할 마일스톤 정보
     * @return 수정된 마일스톤 정보
     */
    MilestoneResponse updateMilestone(Long projectId, Long milestoneId, MilestoneRequest milestoneRequest);

    //프로젝트 수정하기
    void updateProjectDetails(Long projectId, @Valid MyProjectUpdateRequest updateRequestDto);

    /**
     * 프로젝트 소개 수정
     * @param projectId 프로젝트 ID
     * @param descriptionRequest 수정할 소개 정보
     */
    void updateProjectDescription(Long projectId, ProjectDescriptionUpdateRequest descriptionRequest);

    /**
     * 특정 프로젝트에 참여중인 멤버 목록을 상세 정보와 함께 조회
     * @param projectId 조회할 프로젝트 ID
     * @return 해당 프로젝트의 멤버 상세 정보 DTO 목록
     */
    List<ProjectMemberDetailResponse> getProjectMembers(Long projectId);

    /**
     * 특정 프로젝트에 새로운 멤버를 추가
     * @param projectId 멤버를 추가할 프로젝트 ID
     * @param requestDto 추가할 멤버의 정보 (userId, position)
     * @return 추가된 멤버의 정보
     */
    ProjectMemberResponse addProjectMember(Long projectId, AddProjectMemberRequest requestDto);

    /**
     * 특정 프로젝트의 멤버 정보를 수정
     * @param projectId 프로젝트 ID
     * @param userId 수정할 멤버의 사용자 ID
     * @param requestDto 수정할 멤버 정보 (position, role)
     * @return 수정된 멤버 정보
     */
    ProjectMemberResponse updateProjectMember(Long projectId, String userId, UpdateProjectMemberRequest requestDto);
}
