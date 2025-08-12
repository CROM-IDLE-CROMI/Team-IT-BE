package ssu.cromi.teamit.service;

import ssu.cromi.teamit.DTO.myproject.CompletedProject;
import ssu.cromi.teamit.DTO.myproject.InProgressProject;
import ssu.cromi.teamit.DTO.myproject.MyProjectResponse;

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
}
