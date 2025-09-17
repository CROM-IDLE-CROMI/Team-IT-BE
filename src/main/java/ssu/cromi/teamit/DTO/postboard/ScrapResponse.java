package ssu.cromi.teamit.DTO.postboard;

import lombok.Builder;
import lombok.Getter;
import ssu.cromi.teamit.entity.postboard.Scrap;
import java.time.LocalDateTime;

@Getter
@Builder
public class ScrapResponse {

    private final Long postId;
    private final String postTitle;
    private final String userId;
    private final LocalDateTime scrappedAt;
    private final int totalScrapCount;

    public static ScrapResponse from(Scrap scrap) {
        return ScrapResponse.builder()
                .postId(scrap.getBoard().getId())
                .postTitle(scrap.getBoard().getTitle())
                .userId(scrap.getUser().getUid())
                .scrappedAt(scrap.getCreatedAt())
                .totalScrapCount(scrap.getBoard().getScrapCount())
                .build();
    }
}