    package ssu.cromi.teamit.exception;

    public class ProjectNotFoundException extends RuntimeException {
        public ProjectNotFoundException(String projectId) {
            super("존재하지 않는 프로젝트입니다. projectId=" + projectId);
        }
    }
