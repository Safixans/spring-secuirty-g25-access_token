package uz.pdp.appspringsecurity.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<E> {

    private final boolean success;

    private E data;

    private String message;

    private List<ErrorData> errors;

    private ApiResult() {
        this.success = true;
    }

    private ApiResult(E data) {
        this();
        this.data = data;
    }

    private ApiResult(String message) {
        this();
        this.message = message;
    }

    private ApiResult(E data, String message) {
        this();
        this.data = data;
        this.message = message;
    }

    private ApiResult(List<ErrorData> errors) {
        this.success = false;
        this.errors = errors;
    }

    public static <T> ApiResult<T> successResponse() {
        return new ApiResult<>();
    }

    public static <T> ApiResult<T> successResponse(String message) {
        return new ApiResult<>(message);
    }

    public static <T> ApiResult<T> successResponse(T data) {
        return new ApiResult<>(data);
    }

    public static <T> ApiResult<T> successResponse(boolean noString, T data) {
        return new ApiResult<>(data);
    }

    public static <T> ApiResult<T> successResponse(T data, String message) {
        return new ApiResult<>(data, message);
    }

    public static ApiResult<List<ErrorData>> errorResponse(String msg, int status) {
        ErrorData errorData = ErrorData.errorObj(msg, status);
        return new ApiResult<>(List.of(errorData));
    }

    public static ApiResult<List<ErrorData>> errorResponse(List<ErrorData> errors) {
        return new ApiResult<>(errors);
    }


}
