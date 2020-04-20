package com.mateusfma.assemblyvoting.validator;

import com.mateusfma.assemblyvoting.router.rest.request.VoteRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class VoteRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return VoteRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "topicName", "field.required");
        ValidationUtils.rejectIfEmpty(errors, "vote", "field.required");

        VoteRequest request = (VoteRequest) target;
        if (request.getAssociateId() == null)
            errors.rejectValue("associateId", "field.required");
    }
}
