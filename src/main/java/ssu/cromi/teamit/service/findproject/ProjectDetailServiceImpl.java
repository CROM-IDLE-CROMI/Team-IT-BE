package ssu.cromi.teamit.service.findproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssu.cromi.teamit.DTO.findproject.ProjectDetailResponseDto;
import ssu.cromi.teamit.entity.teamup.Project;
import ssu.cromi.teamit.domain.User;
import ssu.cromi.teamit.entity.enums.*;
import ssu.cromi.teamit.exception.ProjectNotFoundException;
import ssu.cromi.teamit.exception.UserNotFoundException;
import ssu.cromi.teamit.repository.teamup.ProjectRepository;
import ssu.cromi.teamit.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ProjectDetailServiceImpl implements ProjectDetailService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional // 조회수 증가 때문에 readOnly=false 처리
    public ProjectDetailResponseDto getProjectDetail(Long projectId) {
        // 조회수 증가 (벌크 업데이트 방식)
        int updated = projectRepository.incrementViewCount(projectId);
        if (updated == 0) {
            throw new ProjectNotFoundException("해당 프로젝트가 존재하지 않습니다. id= " + projectId);
        }

        // 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("해당 프로젝트가 존재하지 않습니다. id= " + projectId));

        // 작성자 정보 조회
        User user = userRepository.findById(project.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("작성자 정보를 찾을 수 없습니다."));

        // 3. 응답 DTO 조립
        return ProjectDetailResponseDto.from(project, user);
    }
}
