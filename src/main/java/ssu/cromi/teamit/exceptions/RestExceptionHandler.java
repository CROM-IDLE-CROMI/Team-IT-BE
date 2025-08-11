package ssu.cromi.teamit.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import io.jsonwebtoken.JwtException;
import ssu.cromi.teamit.dto.common.ApiResponse;

import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /** 리프레시 토큰 검증 실패 → 403 */
    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ApiResponse<Void>> handleTokenRefresh(TokenRefreshException ex) {
        ApiResponse<Void> body = ApiResponse.error("403", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body);
    }

    /** JWT 서명/만료 등 검증 실패 → 401 */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleJwt(JwtException ex) {
        ApiResponse<Void> body = ApiResponse.error(
                "401",
                "Invalid or expired JWT: " + ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    /** 권한 부족 → 403 */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        ApiResponse<Void> body = ApiResponse.error(
                "403",
                "Access denied: " + ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body);
    }

    /** @Valid 검증 실패 → 400 (Request DTO) */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        // 필드별 에러 메시지만 추출
        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ApiResponse<Void> body = ApiResponse.error("400", "Validation failed: " + msg);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(headers)
                .body(body);
    }

    /** 그 외 예외 → 500 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAll(Exception ex) {
        ApiResponse<Void> body = ApiResponse.error(
                "500",
                "Internal error: " + ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}
