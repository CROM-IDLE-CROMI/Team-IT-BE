package ssu.cromi.teamit.response;
// 더이상 사용하지 않습니다.
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
}