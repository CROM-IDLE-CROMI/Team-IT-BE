package ssu.cromi.teamit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssu.cromi.teamit.DTO.mypage.AwardResponse;
import ssu.cromi.teamit.DTO.mypage.MyRatingResponse;
import ssu.cromi.teamit.DTO.mypage.MypageResponse;
import ssu.cromi.teamit.DTO.user.*;
import ssu.cromi.teamit.security.JwtUtils;
import ssu.cromi.teamit.service.MypageService;
import ssu.cromi.teamit.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/mypage")
@RequiredArgsConstructor
public class MypageController {
    
    private final MypageService mypageService;
    private final UserService userService;
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

    @PatchMapping("/profile")
    public ResponseEntity<ProfileUpdateResponse> updateProfile(
            @Valid @RequestBody ProfileUpdateRequest request,
            @RequestHeader("Authorization") String token) {
        String uid = jwtUtils.getUsernameFromJwt(token.replace("Bearer ", ""));
        ProfileUpdateResponse response = userService.updateProfile(uid, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/stacks")
    public ResponseEntity<List<StackResponse>> updateUserStacks(
            @Valid @RequestBody UserStackUpdateRequest request,
            @RequestHeader("Authorization") String token) {
        String uid = jwtUtils.getUsernameFromJwt(token.replace("Bearer ", ""));
        List<StackResponse> updatedStacks = userService.updateUserStacks(uid, request);
        return ResponseEntity.ok(updatedStacks);
    }

    @GetMapping("/stacks")
    public ResponseEntity<List<StackResponse>> getUserStacks(@RequestHeader("Authorization") String token) {
        String uid = jwtUtils.getUsernameFromJwt(token.replace("Bearer ", ""));
        List<StackResponse> userStacks = userService.getUserStacks(uid);
        return ResponseEntity.ok(userStacks);
    }

    @PostMapping("/{uid}/awards")
    public ResponseEntity<List<AwardResponse>> createUserAwards(
            @PathVariable String uid,
            @Valid @RequestBody UserAwardUpdateRequest request) {
        List<AwardResponse> createdAwards = userService.updateUserAwardsAsDto(uid, request);
        return ResponseEntity.ok(createdAwards);
    }

    @PatchMapping("/{uid}/awards")
    public ResponseEntity<List<AwardResponse>> updateUserAwards(
            @PathVariable String uid,
            @Valid @RequestBody UserAwardUpdateRequest request) {
        List<AwardResponse> updatedAwards = userService.updateUserAwardsAsDto(uid, request);
        return ResponseEntity.ok(updatedAwards);
    }

    @GetMapping("/{uid}/awards")
    public ResponseEntity<List<AwardResponse>> getUserAwards(@PathVariable String uid) {
        List<AwardResponse> userAwards = userService.getUserAwardsAsDto(uid);
        return ResponseEntity.ok(userAwards);
    }
}