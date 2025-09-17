package ssu.cromi.teamit.service.postboard;

import ssu.cromi.teamit.entity.postboard.Scrap;

public interface ScrapService {
    Scrap createScrap(Long postId, String userId);
    void deleteScrap(Long postId, String userId);
}