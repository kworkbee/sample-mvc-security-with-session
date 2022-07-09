package me.g1tommy.samplesecuritysession.domain.dto;

import org.springframework.http.HttpStatus;

public class CommonResponseDto<T> {

    public int code;
    public T body;

    public CommonResponseDto(HttpStatus code, T body) {
        this.code = code.value();
        this.body = body;
    }
}
