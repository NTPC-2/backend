package Insuleng.Insuleng_Backend.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
public class BaseResponse<T> {
    //JsonPropertyOrder로 순서 정하기
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    private boolean isSuccess;
    private String message;
    int code;

    public BaseResponse(T data){
        this.data = data;
        this.code = BaseResponseStatus.Success.getCode();
        this.isSuccess = BaseResponseStatus.Success.isSuccess();
        this.message = BaseResponseStatus.Success.getMessage();
    }

    public BaseResponse(BaseResponseStatus status){
        this.data = null;
        this.code = status.getCode();
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();

    }

    //BaseException 이외의 예외 상황은 ExceptionHandler에서 구현하기


}
