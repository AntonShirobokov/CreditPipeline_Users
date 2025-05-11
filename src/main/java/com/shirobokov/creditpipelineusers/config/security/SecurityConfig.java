package com.shirobokov.creditpipelineusers.config.security;


import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.shirobokov.creditpipelineusers.config.jwtauth.GetCsrfTokenFilter;
import com.shirobokov.creditpipelineusers.config.jwtauth.TokenCookieAuthenticationConfigurer;
import com.shirobokov.creditpipelineusers.config.jwtauth.TokenCookieSessionAuthenticationStrategy;
import com.shirobokov.creditpipelineusers.config.jwtauth.token.DefaultTokenCookieFactory;
import com.shirobokov.creditpipelineusers.config.jwtauth.token.TokenCookieJweStringDeserializer;
import com.shirobokov.creditpipelineusers.config.jwtauth.token.TokenCookieJweStringSerializer;
import com.shirobokov.creditpipelineusers.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity(debug=true)
public class SecurityConfig {



    @Bean
    public TokenCookieJweStringSerializer tokenCookieJweStringSerializer(@Value("${jwt.cookie-token-key}") String cookieTokenKey) throws Exception {
        return new TokenCookieJweStringSerializer(new DirectEncrypter(OctetSequenceKey.parse(cookieTokenKey)));
    }


    @Bean
    public TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer(
            @Value("${jwt.cookie-token-key}") String cookieTokenKey,
            JdbcTemplate jdbcTemplate) throws Exception {

        return new TokenCookieAuthenticationConfigurer()
                .tokenCookieStringDeserializer(new TokenCookieJweStringDeserializer(new DirectDecrypter(OctetSequenceKey.parse(cookieTokenKey))))
                .jdbcTemplate(jdbcTemplate);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer,
            TokenCookieJweStringSerializer tokenCookieJweStringSerializer,
            UserService userService) throws Exception {


        var tokenCookieSessionAuthenticationStrategy = new TokenCookieSessionAuthenticationStrategy();

        tokenCookieSessionAuthenticationStrategy.setTokenStringSerializer(tokenCookieJweStringSerializer);
        tokenCookieSessionAuthenticationStrategy.setTokenCookieFactory(defaultTokenCookieFactory(userService));

        http.with(tokenCookieAuthenticationConfigurer, Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/credit")
                        .permitAll())
                .addFilterAfter(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class)
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/registration").permitAll()
                                .requestMatchers("/error", "index.html").permitAll()
                                .requestMatchers("/css/*.css").permitAll()
                                .requestMatchers("/js/*.js").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionAuthenticationStrategy(tokenCookieSessionAuthenticationStrategy))
                .csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .sessionAuthenticationStrategy((authentication, request, response) -> {}));

        // Устарел найти новый способ

        //http.apply(tokenCookieAuthenticationConfigurer);

        // Возможный вариант решения

        //tokenCookieAuthenticationConfigurer.configure(http);

        return http.build();
    }


    @Bean
    public DefaultTokenCookieFactory defaultTokenCookieFactory(UserService userService) {
        return new DefaultTokenCookieFactory(userService);
    }
//    @Bean
//    public UserDetailsService userDetailsService(JdbcTemplate jdbcTemplate) {
//        return username -> jdbcTemplate.query("select * from t_user where c_username = ?",
//                (rs, i) -> User.builder()
//                        .username(rs.getString("c_username"))
//                        .password(rs.getString("c_password"))
//                        .authorities(
//                                jdbcTemplate.query("select c_authority from t_user_authority where id_user = ?",
//                                        (rs1, i1) ->
//                                                new SimpleGrantedAuthority(rs1.getString("c_authority")),
//                                        rs.getInt("id")))
//                        .build(), username).stream().findFirst().orElse(null);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
