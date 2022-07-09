package me.g1tommy.samplesecuritysession.domain;

public enum AuthenticationMessage {
    OK("ok"),
    FAILED("Authentication Failed.");

    private String message;

    AuthenticationMessage(String message) {
        this.message = message;
    }
}
