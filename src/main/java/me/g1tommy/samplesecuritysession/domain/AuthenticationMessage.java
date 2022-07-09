package me.g1tommy.samplesecuritysession.domain;

public enum AuthenticationMessage {
    OK("ok"),
    FAILED("Authentication Failed."),
    UNAUTHORIZED("Unauthorized");

    AuthenticationMessage(String message) {
    }
}
