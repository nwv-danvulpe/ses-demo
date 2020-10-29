package com.example.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
public class EmailController {
    private final SesClient client;
    private final String emailIdentityArn;

    @Autowired
    public EmailController(SesClient client,
                           @Value("${email.identity.arn}") String emailIdentityArn) {
        this.client = client;
        this.emailIdentityArn = emailIdentityArn;
    }

    @PostMapping("/email")
    public void sendMessage(@Valid @RequestBody SendEmailRequestDto request) {
        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(request.getRecipient()).build())
                .message(Message.builder()
                        .subject(Content.builder().charset("UTF-8").data(request.getSubject()).build())
                        .body(Body.builder()
                                .text(Content.builder().charset("UTF-8").data(request.getMessage()).build())
                                .build())
                        .build())
                .source(request.getFrom())
                .sourceArn(emailIdentityArn)
                .build();
        client.sendEmail(emailRequest);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return Map.of("errors", errors);
    }
}
