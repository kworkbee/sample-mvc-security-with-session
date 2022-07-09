package me.g1tommy.samplesecuritysession.controller;

import lombok.RequiredArgsConstructor;
import me.g1tommy.samplesecuritysession.domain.AuthenticationMessage;
import me.g1tommy.samplesecuritysession.domain.dto.CommonResponseDto;
import me.g1tommy.samplesecuritysession.domain.dto.LoginRequestDto;
import me.g1tommy.samplesecuritysession.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final ItemService itemService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<?>> login(@RequestBody LoginRequestDto loginRequestDto) {
        var token = new UsernamePasswordAuthenticationToken(loginRequestDto.id(), loginRequestDto.password());
        var authentication = authenticationManager.authenticate(token);

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(new CommonResponseDto<>(HttpStatus.UNAUTHORIZED, AuthenticationMessage.FAILED));
        }

        return ResponseEntity.ok()
                             .body(new CommonResponseDto<>(HttpStatus.OK, AuthenticationMessage.OK));
    }
}

