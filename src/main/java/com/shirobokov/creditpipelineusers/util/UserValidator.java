package com.shirobokov.creditpipelineusers.util;

import com.shirobokov.creditpipelineusers.entity.User;
import com.shirobokov.creditpipelineusers.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    // Регулярное выражение для телефона: +7 912 872-15-78
    private static final String PHONE_REGEX = "^\\+7 \\d{3} \\d{3}-\\d{2}-\\d{2}$";

    // Регулярное выражение для ФИО: только русские буквы, без пробелов и цифр
    private static final String NAME_REGEX = "^[А-Яа-яЁё]+$";

    // Регулярное выражение для email (стандартный формат)
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        boolean correctNumber = true;

        User user = (User) target;

        // Проверка на пустые значения
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "phone.empty", "Поле \"Телефон\" не может быть пустым");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.empty", "Поле \"Фамилия\" не может быть пустой");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.empty", "Поле \"Имя\" не может быть пустым");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "middleName", "middleName.empty", "Поле \"Отчество\" не может быть пустым");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty", "Поле \"Почта\" не может быть пустым");

        // Проверка телефона
        if (user.getPhone() != null && !user.getPhone().isEmpty() && !user.getPhone().matches(PHONE_REGEX)) {
            errors.rejectValue("phone", "phone.invalidFormat",
                    "Поле \"Телефон\" должен быть в формате: +7 XXX XXX-XX-XX");
            correctNumber = false;
        }

        // Проверка ФИО (только русские буквы, без пробелов и спецсимволов)
        if (user.getLastName() != null && !user.getLastName().matches(NAME_REGEX)) {
            errors.rejectValue("lastName", "lastName.invalidFormat",
                    "Поле \"Фамилия\" должна содержать только русские буквы");
        }

        if (user.getFirstName() != null && !user.getFirstName().matches(NAME_REGEX)) {
            errors.rejectValue("firstName", "firstName.invalidFormat",
                    "Поле \"Имя\" должно содержать только русские буквы");
        }

        if (user.getMiddleName() != null && !user.getMiddleName().isEmpty() && !user.getMiddleName().matches(NAME_REGEX)) {
            errors.rejectValue("middleName", "middleName.invalidFormat",
                    "Поле \"Отчество\" должно содержать только русские буквы");
        }

        // Проверка email (стандартный формат)
        if (user.getEmail() != null && !user.getEmail().isEmpty() && !user.getEmail().matches(EMAIL_REGEX)) {
            errors.rejectValue("email", "email.invalidFormat",
                    "Некорректный формат почты");
        }
    }
}