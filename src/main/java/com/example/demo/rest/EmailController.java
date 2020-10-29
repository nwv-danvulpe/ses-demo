package com.example.demo.rest;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
public class EmailController {
    private final AmazonSimpleEmailService client;
    private final String emailIdentityArn;

    @Autowired
    public EmailController(AmazonSimpleEmailService client,
                           @Value("${email.identity.arn}") String emailIdentityArn) {
        this.client = client;
        this.emailIdentityArn = emailIdentityArn;
    }

    @PostMapping("/email")
    public void sendMessage(@Valid @RequestBody SendEmailRequestDto request) {
        SendEmailRequest emailRequest = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(request.getRecipient()))
                .withMessage(new Message()
                        .withSubject(new Content().withCharset("UTF-8").withData(request.getSubject()))
                        .withBody(new Body()
                                .withText(new Content()
                                        .withCharset("UTF-8")
                                        .withData(request.getMessage()))))
                .withSource(request.getFrom())
                .withSourceArn(emailIdentityArn);
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
