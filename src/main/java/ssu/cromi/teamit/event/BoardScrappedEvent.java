package ssu.cromi.teamit.event;

import lombok.Getter;
import ssu.cromi.teamit.entity.postboard.Scrap;

@Getter
public class BoardScrappedEvent {
    private final Scrap scrap; // 스크랩 객체 자체를 전달

    public BoardScrappedEvent(Scrap scrap) {
        this.scrap = scrap;
    }
}