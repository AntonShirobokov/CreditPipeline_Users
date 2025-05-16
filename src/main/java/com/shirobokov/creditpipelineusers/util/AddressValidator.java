package com.shirobokov.creditpipelineusers.util;

import com.shirobokov.creditpipelineusers.entity.Passport;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AddressValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Passport.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Passport passport = (Passport) target;

        if (passport.getRegistrationAddress().length() > 300) {
            errors.rejectValue("registrationAddress", "addressMaxLength",
                    "Поле \"Адрес регистрации\" не должно превышать 300 символов");
        }
    }
}
