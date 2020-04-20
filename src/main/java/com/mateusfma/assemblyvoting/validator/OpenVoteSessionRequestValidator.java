package com.mateusfma.assemblyvoting.validator;

import com.mateusfma.assemblyvoting.router.rest.request.OpenVoteSessionRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class OpenVoteSessionRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return OpenVoteSessionRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "topicName", "field.required");

        OpenVoteSessionRequest request = (OpenVoteSessionRequest) target;
        if (request.getDurationSec() != null && request.getDurationSec() <= 0)
            errors.rejectValue("durationSec", "field.invalid",
                    "Duração da sessão não pode ser negativa.");

    }
}
