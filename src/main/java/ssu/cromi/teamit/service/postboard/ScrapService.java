package ssu.cromi.teamit.service.postboard;

public interface ScrapService {
    void createScrap(Long postId, String userId);
    void deleteScrap(Long postId, String userId);
}