package ssu.cromi.teamit.service;

import ssu.cromi.teamit.DTO.mypage.MyRatingResponse;
import ssu.cromi.teamit.DTO.mypage.MypageResponse;

import java.util.List;

public interface MypageService {
    MypageResponse getMypageInfo(String uid);
    List<MyRatingResponse> getMyRatings(String uid);
}
