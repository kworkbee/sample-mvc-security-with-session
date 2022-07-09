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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final ItemService itemService;

    @GetMapping("/validate")
    public ResponseEntity<CommonResponseDto<?>> validate(HttpServletRequest request) {
        Optional<HttpSession> sessionOptional = Optional.ofNullable(request.getSession(false));
        if (sessionOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponseDto<>(HttpStatus.UNAUTHORIZED, AuthenticationMessage.UNAUTHORIZED));
        }

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK, AuthenticationMessage.OK));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<?>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        var token = new UsernamePasswordAuthenticationToken(loginRequestDto.id(), loginRequestDto.password());
        var authentication = authenticationManager.authenticate(token);

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(new CommonResponseDto<>(HttpStatus.UNAUTHORIZED, AuthenticationMessage.FAILED));
        }

        setContextAndSession(loginRequestDto, request, authentication);

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK, AuthenticationMessage.OK));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<CommonResponseDto<?>> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        return ResponseEntity.ok(new CommonResponseDto<>(HttpStatus.OK, AuthenticationMessage.OK));
    }

    private void setContextAndSession(LoginRequestDto loginRequestDto, HttpServletRequest request, Authentication authentication) {
        final String ATTR_PRINCIPAL_NAME = "principalName";

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        request.getSession().setAttribute(ATTR_PRINCIPAL_NAME, loginRequestDto.id());
    }
}

