package ssu.cromi.teamit.service.postboard;

public interface PostScrapService {
    void createScrap(Long postId, String userId);
    void deleteScrap(Long postId, String userId);
}