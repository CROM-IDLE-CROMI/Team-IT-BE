package ssu.cromi.teamit.service;

import ssu.cromi.teamit.DTO.mypage.MypageResponse;

public interface MypageService {
    MypageResponse getMypageInfo(String uid);
}
