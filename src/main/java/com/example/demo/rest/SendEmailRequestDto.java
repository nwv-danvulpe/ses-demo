package com.example.demo.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SendEmailRequestDto {
    @Email
    @NotBlank
    private final String from;
    @Email
    @NotBlank
    private final String recipient;
    @NotBlank
    private final String message;
    @NotBlank
    private final String subject;

    public SendEmailRequestDto(
            @JsonProperty("recipient") String recipient,
            @JsonProperty("from") String from,
            @JsonProperty("message") String message,
            @JsonProperty("subject") String subject) {
        this.recipient = recipient;
        this.from = from;
        this.message = message;
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }
}
