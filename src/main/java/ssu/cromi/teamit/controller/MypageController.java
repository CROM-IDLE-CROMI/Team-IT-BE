package ssu.cromi.teamit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssu.cromi.teamit.DTO.mypage.MyRatingResponse;
import ssu.cromi.teamit.DTO.mypage.MypageResponse;
import ssu.cromi.teamit.security.JwtUtils;
import ssu.cromi.teamit.service.MypageService;

import java.util.List;

@RestController
@RequestMapping("/v1/mypage")
@RequiredArgsConstructor
public class MypageController {
    
    private final MypageService mypageService;
    private final JwtUtils jwtUtils;
    
    @GetMapping("/{uid}")
    public ResponseEntity<MypageResponse> getMypage(@PathVariable String uid) {
        MypageResponse response = mypageService.getMypageInfo(uid);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{uid}/ratings")
    public ResponseEntity<List<MyRatingResponse>> getMyRatings(@PathVariable String uid) {
        List<MyRatingResponse> ratings = mypageService.getMyRatings(uid);
        return ResponseEntity.ok(ratings);
    }
}