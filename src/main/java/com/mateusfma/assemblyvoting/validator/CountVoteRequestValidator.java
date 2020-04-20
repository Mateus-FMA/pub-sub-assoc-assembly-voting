package com.mateusfma.assemblyvoting.validator;

import com.mateusfma.assemblyvoting.router.rest.request.CountVoteRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CountVoteRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CountVoteRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "topicName", "field.required");
    }
}
