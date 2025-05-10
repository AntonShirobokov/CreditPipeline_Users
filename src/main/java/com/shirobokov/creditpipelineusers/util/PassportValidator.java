package com.shirobokov.creditpipelineusers.util;

import com.shirobokov.creditpipelineusers.entity.Passport;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PassportValidator implements Validator {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public PassportValidator() {
        dateFormat.setLenient(false);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Passport.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Passport passport = (Passport) target;

        // Проверка серии паспорта (4 цифры)
        if (!passport.getSeries().matches("\\d{4}")) {
            errors.rejectValue("series", "passport.series.invalid", "Серия паспорта должна состоять из 4 цифр");
        }

        // Проверка номера паспорта (6 цифр)
        if (!passport.getNumber().matches("\\d{6}")) {
            errors.rejectValue("number", "passport.number.invalid", "Номер паспорта должен состоять из 6 цифр");
        }

//        // Проверка даты рождения
//        if (!isValidDate(passport.getBirthDate())) {
//            errors.rejectValue("birthDate", "passport.birthDate.invalid", "Дата рождения должна быть в формате DD.MM.YYYY");
//        }

        // Проверка места рождения (не пустое и адекватной длины)
        if (passport.getBirthPlace() == null || passport.getBirthPlace().trim().length() < 3) {
            errors.rejectValue("birthPlace", "passport.birthPlace.invalid", "Место рождения должно содержать не менее 3 символов");
        } else if (passport.getBirthPlace().length() > 255) {
            errors.rejectValue("birthPlace", "passport.birthPlace.tooLong", "Место рождения не должно превышать 255 символов");
        }

        // Проверка кода подразделения (формат xxx-xxx)
        if (!passport.getDepartmentCode().matches("\\d{3}-\\d{3}")) {
            errors.rejectValue("departmentCode", "passport.departmentCode.invalid", "Код подразделения должен быть в формате xxx-xxx");
        }

        // Проверка кем выдан (не пустое и адекватной длины)
        if (passport.getIssuedBy() == null || passport.getIssuedBy().trim().length() < 3) {
            errors.rejectValue("issuedBy", "passport.issuedBy.invalid", "Поле 'Кем выдан' должно содержать не менее 3 символов");
        } else if (passport.getIssuedBy().length() > 255) {
            errors.rejectValue("issuedBy", "passport.issuedBy.tooLong", "Поле 'Кем выдан' не должно превышать 255 символов");
        }

//        // Проверка даты выдачи
//        if (!isValidDate(passport.getIssueDate())) {
//            errors.rejectValue("issueDate", "passport.issueDate.invalid", "Дата выдачи должна быть в формате DD.MM.YYYY");
//        }

        // Проверка ИНН (12 цифр)
        if (!passport.getInn().matches("\\d{12}")) {
            errors.rejectValue("inn", "passport.inn.invalid", "ИНН должен состоять из 12 цифр");
        }

        // Проверка СНИЛС (xxx-xxx-xxx xx)
        if (!passport.getSnils().matches("\\d{3}-\\d{3}-\\d{3} \\d{2}")) {
            errors.rejectValue("snils", "passport.snils.invalid", "СНИЛС должен быть в формате xxx-xxx-xxx xx");
        }
    }

    private boolean isValidDate(String dateStr) {
        try {
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException | NullPointerException e) {
            return false;
        }
    }
}
