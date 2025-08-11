package ssu.cromi.teamit.dto.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse <T>{
    private boolean success;
    private String message;
    private String code;
    private T data;

    /** 성공 응답 생성
     * @param data 실제 반환 데이터
     * @param code 응답코드
     * @param message 응답 메시지
     */
    @Builder(builderMethodName = "ofSuccess")
    public static <T> ApiResponse<T> success(T data, String code, String message){
        return new ApiResponse<>(true, code, message, data);
    }
    /**
     * 성공 응답 생성(메시지 없지)
     * @param data 실제 반환 데이터
     * @param code 응답코드
     */
    public static <T> ApiResponse<T> success(T data, String code){
        return success(data, code,"요청에 성공했습니다.");
    }
    /**
     * 실패 응답 생성
     * @param message 실패 이유 메시지
     * @param code 에러 코드
     */
    public static <T> ApiResponse<T> error(String code, String message){
        return new ApiResponse<>(false, code, message, null);
    }
}
