package com.shirobokov.creditpipelineusers.restcontroller;


import com.shirobokov.creditpipelineusers.config.jwtauth.token.TokenUser;
import com.shirobokov.creditpipelineusers.entity.Application;
import com.shirobokov.creditpipelineusers.entity.Passport;
import com.shirobokov.creditpipelineusers.entity.User;
import com.shirobokov.creditpipelineusers.entity.enums.ApplicationEnums;
import com.shirobokov.creditpipelineusers.service.ApplicationService;
import com.shirobokov.creditpipelineusers.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApplicationController {

    private final UserService userService;

    private final ApplicationService applicationService;

    public ApplicationController(UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
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



        // 1. Проверка паспорта
        String passportCheckResult = checkPassport(currentUser.getPassport().getSeries(),
                currentUser.getPassport().getNumber());

        if (!passportCheckResult.equals("паспорт действителен")) {
            application.setStatus("Ошибка оформления");
            application.setReasonForRefusal("Недействительный паспорт");
            applicationService.save(application);
            return ResponseEntity.ok(Map.of("valid", false, "message", "Паспорт недействителен"));
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
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/passport-check?series={series}&number={number}";

        try {
            return restTemplate.getForObject(url, String.class, series, number);
        } catch (Exception e) {
            return "ошибка при проверке паспорта";
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
