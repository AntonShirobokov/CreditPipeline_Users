package com.shirobokov.creditpipelineusers.entity.enums;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationEnums {

//    public enum Purpose {
//        CAR("Автомобиль"),
//        REPAIR("Ремонт"),
//        EDUCATION("Образование"),
//        REFINANCING("Рефинансирование"),
//        OTHER("Другое");
//
//        private final String value;
//
//        Purpose(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        private static final Map<String, Purpose> MAP =
//                Collections.unmodifiableMap(Stream.of(values())
//                        .collect(Collectors.toMap(Purpose::getValue, e -> e)));
//
//        public static Purpose fromValue(String value) {
//            Purpose result = MAP.get(value);
//            if (result == null) throw new IllegalArgumentException("Invalid value: " + value);
//            return result;
//        }
//
//        @Override
//        public String toString() {
//            return value;
//        }
//    }
//
//    public enum TypeOfEmployment {
//        ENTREPRENEUR("Предприниматель"),
//        EMPLOYEE("Работа по найму"),
//        STUDENT("Студент"),
//        RETIREE("Пенсионер"),
//        UNEMPLOYED("Безработный");
//
//        private final String value;
//
//        TypeOfEmployment(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        private static final Map<String, TypeOfEmployment> MAP =
//                Collections.unmodifiableMap(Stream.of(values())
//                        .collect(Collectors.toMap(TypeOfEmployment::getValue, e -> e)));
//
//        public static TypeOfEmployment fromValue(String value) {
//            TypeOfEmployment result = MAP.get(value);
//            if (result == null) throw new IllegalArgumentException("Invalid value: " + value);
//            return result;
//        }
//
//        @Override
//        public String toString() {
//            return value;
//        }
//    }
//
//    public enum Deposit {
//        REAL_ESTATE("Недвижимость"),
//        VEHICLE("Транспортное средство"),
//        NONE("Отсутствует");
//
//        private final String value;
//
//        Deposit(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        private static final Map<String, Deposit> MAP =
//                Collections.unmodifiableMap(Stream.of(values())
//                        .collect(Collectors.toMap(Deposit::getValue, e -> e)));
//
//        public static Deposit fromValue(String value) {
//            Deposit result = MAP.get(value);
//            if (result == null) throw new IllegalArgumentException("Invalid value: " + value);
//            return result;
//        }
//
//        @Override
//        public String toString() {
//            return value;
//        }
//    }
//
//    public enum Education {
//        NONE("Отсутствует"),
//        BASIC("Основное общее"),
//        SECONDARY("Среднее общее"),
//        VOCATIONAL("Среднее профессиональное"),
//        HIGHER("Высшее");
//
//        private final String value;
//
//        Education(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        private static final Map<String, Education> MAP =
//                Collections.unmodifiableMap(Stream.of(values())
//                        .collect(Collectors.toMap(Education::getValue, e -> e)));
//
//        public static Education fromValue(String value) {
//            Education result = MAP.get(value);
//            if (result == null) throw new IllegalArgumentException("Invalid value: " + value);
//            return result;
//        }
//
//        @Override
//        public String toString() {
//            return value;
//        }
//    }
//
//    public enum TypeOfHousing {
//        OWN("Собственное"),
//        RENTED("Съемное"),
//        RELATIVES("Проживаю с родственниками");
//
//        private final String value;
//
//        TypeOfHousing(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        private static final Map<String, TypeOfHousing> MAP =
//                Collections.unmodifiableMap(Stream.of(values())
//                        .collect(Collectors.toMap(TypeOfHousing::getValue, e -> e)));
//
//        public static TypeOfHousing fromValue(String value) {
//            TypeOfHousing result = MAP.get(value);
//            if (result == null) throw new IllegalArgumentException("Invalid value: " + value);
//            return result;
//        }
//
//        @Override
//        public String toString() {
//            return value;
//        }
//    }
//
//    public enum MaritalStatus {
//        SINGLE("Никогда не состоял(а)"),
//        MARRIED("Замужем/женат"),
//        DIVORCED("Разведен/разведена"),
//        WIDOW("Вдова/вдовец");
//
//        private final String value;
//
//        MaritalStatus(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        private static final Map<String, MaritalStatus> MAP =
//                Collections.unmodifiableMap(Stream.of(values())
//                        .collect(Collectors.toMap(MaritalStatus::getValue, e -> e)));
//
//        public static MaritalStatus fromValue(String value) {
//            MaritalStatus result = MAP.get(value);
//            if (result == null) throw new IllegalArgumentException("Invalid value: " + value);
//            return result;
//        }
//
//        @Override
//        public String toString() {
//            return value;
//        }
//    }
//
//    public enum Status {
//        UNDER_REVIEW("В рассмотрении"),
//        FORM_ERROR("Ошибка оформления"),
//        APPROVED("Одобрено"),
//        REJECTED("Отказано");
//
//        private final String value;
//
//        Status(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        private static final Map<String, Status> MAP =
//                Collections.unmodifiableMap(Stream.of(values())
//                        .collect(Collectors.toMap(Status::getValue, e -> e)));
//
//        public static Status fromValue(String value) {
//            Status result = MAP.get(value);
//            if (result == null) throw new IllegalArgumentException("Invalid value: " + value);
//            return result;
//        }
//
//        @Override
//        public String toString() {
//            return value;
//        }
//    }
public enum Purpose {
    CAR,
    REPAIR,
    EDUCATION,
    REFINANCING,
    OTHER;

}

    public enum TypeOfEmployment {
        ENTREPRENEUR,
        EMPLOYEE,
        STUDENT,
        RETIREE,
        UNEMPLOYED;
    }

    public enum Deposit {
        REAL_ESTATE,
        VEHICLE,
        NONE;
    }

    public enum Education {
        NONE,
        BASIC,
        SECONDARY,
        VOCATIONAL,
        HIGHER;
    }

    public enum TypeOfHousing {
        OWN,
        RENTED,
        RELATIVES;


    }

    public enum MaritalStatus {
        SINGLE,
        MARRIED,
        DIVORCED,
        WIDOW;


    }

    public enum Status {
        UNDER_REVIEW,
        FORM_ERROR,
        APPROVED,
        REJECTED;

    }
}

