package com.shirobokov.creditpipelineusers.restcontroller;


import com.shirobokov.creditpipelineusers.config.jwtauth.token.TokenUser;
import com.shirobokov.creditpipelineusers.dto.ApplicationMLInputDTO;
import com.shirobokov.creditpipelineusers.dto.ApplicationViewDTO;
import com.shirobokov.creditpipelineusers.entity.Application;
import com.shirobokov.creditpipelineusers.entity.User;
import com.shirobokov.creditpipelineusers.mapper.ApplicationMapper;
import com.shirobokov.creditpipelineusers.service.ApplicationService;
import com.shirobokov.creditpipelineusers.service.UserService;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ApplicationController {

    private final UserService userService;

    private final ApplicationService applicationService;

    private final ApplicationMapper applicationMapper;

    public ApplicationController(UserService userService, ApplicationService applicationService, ApplicationMapper applicationMapper) {
        this.userService = userService;
        this.applicationService = applicationService;
        this.applicationMapper = applicationMapper;
    }

    @GetMapping("/api/getAllApplications")
    @ResponseBody
    public ResponseEntity<?> getAllApplications() {
        TokenUser tokenUser = (TokenUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userService.findById(Integer.parseInt(tokenUser.getToken().subject()));

        List<ApplicationViewDTO> applicationsDto =
                currentUser.getApplications().stream()
                        .map(applicationMapper::toApplicationViewDto)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(applicationsDto);
    }

    @PostMapping("/api/credit/checkInformation")
    @ResponseBody
    public ResponseEntity<?> checkInformation(){
        TokenUser tokenUser = (TokenUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userService.findById(Integer.parseInt(tokenUser.getToken().subject()));

        if (currentUser.getPassport().isValid()){
            return ResponseEntity.ok(Map.of("valid", true));
        } else {
            return ResponseEntity.ok(Map.of("valid", false));
        }

    }

    @PostMapping("/api/credit/applicationProcessing")
    @ResponseBody
    public ResponseEntity<?> applicationProcessing(@ModelAttribute Application application) {

        TokenUser tokenUser = (TokenUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userService.findById(Integer.parseInt(tokenUser.getToken().subject()));

        application.setUser(currentUser);
        application.setDateOfCreation(LocalDateTime.now());
        application.setAge(Period.between(currentUser.getPassport().getBirthDate(),LocalDate.now()).getYears());

        // 1. Проверка паспорта
        String passportCheckResult = checkPassport(currentUser.getPassport().getSeries(),
                currentUser.getPassport().getNumber());

        System.out.println("Отладка: " + passportCheckResult);

        if (!passportCheckResult.equals("Паспорт действителен")) {
            application.setStatus("Ошибка оформления");
            application.setReasonForRefusal(passportCheckResult);
            applicationService.save(application);
            return ResponseEntity.ok(Map.of("valid", false, "message", passportCheckResult));
        }

        // 2. Проверка задолженности — теперь возвращаем Map с ключами cWasBankrupt и totalDebt
        Map<String, Object> debtCheckResult = checkDebt(currentUser.getPassport().getSeries(),
                currentUser.getPassport().getNumber());

        if (debtCheckResult.containsKey("cWasBankrupt") && Boolean.TRUE.equals(debtCheckResult.get("cWasBankrupt"))) {
            application.setWasBankrupt(true);

            // Берём сумму долга из totalDebt, учитывая возможный тип
            Object totalDebtObj = debtCheckResult.get("totalDebt");
            int totalDebt = 0;
            if (totalDebtObj instanceof Number) {
                totalDebt = ((Number) totalDebtObj).intValue();
            } else if (totalDebtObj instanceof String) {
                try {
                    totalDebt = Integer.parseInt((String) totalDebtObj);
                } catch (NumberFormatException ignored) {}
            }

            application.setDept(totalDebt);
        } else {
            application.setWasBankrupt(false);
            application.setDept(0);
        }

        // 3. Проверка дохода
        String incomeResult = getIncome(currentUser.getPassport().getInn());
        if (incomeResult.startsWith("доход:")) {
            // Извлекаем сумму дохода из строки ответа
            String incomeStr = incomeResult.replaceAll("[^0-9]", "");
            int income = incomeStr.isEmpty() ? 0 : Integer.parseInt(incomeStr);

            application.setRealIncome(income);
        } else {
            application.setRealIncome(0);
        }

        application.setStatus("В рассмотрении");

        scoreApplication(application);

        applicationService.save(application);

        return ResponseEntity.ok(Map.of("valid", true));
    }

    // Вспомогательные методы для вызова REST-сервиса
    private String checkPassport(String series, String number) {

        System.out.println("Данные паспорта:" + series + " " + number);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/passport-check?series={series}&number={number}";

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class, series, number);

            System.out.println("Что в hashMap" + response);
            if (response.containsKey("message")) {
                System.out.println("Отладка еще одна: " + response.get("message").toString());
                return response.get("message").toString();
            }
            else {
                return "Пользователя с таким паспортом не существует";
            }
        } catch (Exception e) {
            return "Ошибка при проверке паспорта";
        }
    }

    // Новый метод checkDebt, который возвращает Map<String, Object>
    private Map<String, Object> checkDebt(String series, String number) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/debt-check?series={series}&number={number}";

        try {
            // Возвращаем Map, а не String
            Map<String, Object> response = restTemplate.getForObject(url, Map.class, series, number);
            return response != null ? response : Collections.emptyMap();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private String getIncome(String inn) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/income?inn={inn}";

        try {
            return restTemplate.getForObject(url, String.class, inn);
        } catch (Exception e) {
            return "ошибка при проверке дохода";
        }
    }

    private Application scoreApplication(Application application) {
        ApplicationMLInputDTO applicationMLInputDTO = applicationMapper.toApplicationMLInputDTO(application);

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8000/predict";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ApplicationMLInputDTO> request = new HttpEntity<>(applicationMLInputDTO, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();

                Boolean rejected = (Boolean) responseBody.get("rejected");
                if (Boolean.TRUE.equals(rejected)) {
                    String reason = (String) responseBody.get("reason");

                    application.setStatus("Отказано");
                    application.setReasonForRefusal(reason);

                    System.out.println("Заявка отклонена: " + reason);
                    return application;
                }

                Float score = ((Number) responseBody.get("score")).floatValue();
                Float percentageRate = ((Number) responseBody.get("c_percentage_rate")).floatValue();

                System.out.println("Оценка: " + score);
                System.out.println("Ставка:  " + percentageRate);

                application.setScore(score);
                application.setPercentageRate(percentageRate);
            } else {
                throw new RuntimeException("Ошибка при получении ответа от сервиса ML");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обращении к сервису ML: " + e.getMessage(), e);
        }

        return application;
    }
}
