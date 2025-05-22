package com.shirobokov.creditpipelineusers.restcontroller;


import com.shirobokov.creditpipelineusers.config.jwtauth.token.TokenUser;
import com.shirobokov.creditpipelineusers.dto.ApplicationDto;
import com.shirobokov.creditpipelineusers.entity.Application;
import com.shirobokov.creditpipelineusers.entity.User;
import com.shirobokov.creditpipelineusers.mapper.ApplicationMapper;
import com.shirobokov.creditpipelineusers.service.ApplicationService;
import com.shirobokov.creditpipelineusers.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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



        List<ApplicationDto> applicationsDto =
                currentUser.getApplications().stream()
                        .map(application -> applicationMapper.toApplicationDto(application))
                        .collect(Collectors.toList());


        System.out.println("Тестирование Dto: " + applicationsDto);

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
            System.out.println(currentUser.getPassport());
            return ResponseEntity.ok(Map.of("valid", false));
        }

    }

    @PostMapping("/api/credit/applicationProcessing")
    @ResponseBody
    public ResponseEntity<?> applicationProcessing(@ModelAttribute Application application) {

        TokenUser tokenUser = (TokenUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userService.findById(Integer.parseInt(tokenUser.getToken().subject()));

        application.setUser(currentUser);

        System.out.println(application);

        application.setDateOfCreation(LocalDate.now());


        // 1. Проверка паспорта
        String passportCheckResult = checkPassport(currentUser.getPassport().getSeries(),
                currentUser.getPassport().getNumber());

        System.out.println("Отладка:" + passportCheckResult);

        if (!passportCheckResult.equals("Паспорт действителен")) {
            application.setStatus("Ошибка оформления");
            application.setReasonForRefusal(passportCheckResult);
            applicationService.save(application);
            return ResponseEntity.ok(Map.of("valid", false, "message", passportCheckResult));
        }

        // 2. Проверка задолженности
        String debtCheckResult = checkDebt(currentUser.getPassport().getSeries(),
                currentUser.getPassport().getNumber());

        if (debtCheckResult.startsWith("задолженности обнаружены")) {
            // Извлекаем сумму долга из строки ответа
            String debtAmountStr = debtCheckResult.replaceAll("[^0-9]", "");
            int debtAmount = debtAmountStr.isEmpty() ? 0 : Integer.parseInt(debtAmountStr);

            application.setWasBankrupt(true);
            application.setDept(debtAmount);
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

        // Сохраняем обновленную заявку

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




    private String checkDebt(String series, String number) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/debt-check?series={series}&number={number}";

        try {
            return restTemplate.getForObject(url, String.class, series, number);
        } catch (Exception e) {
            return "ошибка при проверке задолженности";
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

}
