package ssu.cromi.teamit.service.postboard;

import ssu.cromi.teamit.DTO.postboard.PostDetailResponse;

public interface PostDetailService {

    PostDetailResponse getPostDetail(Long postId);
}