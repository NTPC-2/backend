package Insuleng.Insuleng_Backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class InsulengExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected BaseResponse<BaseResponseStatus> methodValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();

        String errorMessage = null;

        FieldError fieldError = bindingResult.getFieldErrors().get(0);

        errorMessage = fieldError.getDefaultMessage();

        log.warn("MethodArgumentNotValidException has occured. please check request body. message: [{}]", e.getMessage());
        return new BaseResponse(BaseResponseStatus.VALIDATION_ERROR, errorMessage);

    }

    @ExceptionHandler(BaseException.class)
    protected BaseResponse<BaseResponseStatus> baseException(BaseException e){
        log.warn("BaseException has occured. this is business exception. message: [{}]", e.getMessage());
        return new BaseResponse<>(e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    protected BaseResponse<BaseResponseStatus> unexpectedException(Exception e){
        log.error("Exception has occured" , e);
        return new BaseResponse<>(BaseResponseStatus.UNEXPECTED_ERROR, e.getMessage());
    }

}
