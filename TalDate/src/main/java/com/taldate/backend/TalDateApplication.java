package com.taldate.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TalDateApplication {

    //private static final String LOCALHOST_URL = "http://localhost:5173";
    public static void main(String[] args) {
        SpringApplication.run(TalDateApplication.class, args);
    }


    //@Bean
    //public WebMvcConfigurer corsConfigurer() {
    //    return new WebMvcConfigurer() {
    //        @Override
    //        public void addCorsMappings(CorsRegistry registry) {
    //            registry.addMapping("/api/auth/register")
    //                    .allowedOrigins(LOCALHOST_URL)
    //                    .allowedMethods("POST")
    //                    .allowedHeaders("*");
//
    //            registry.addMapping("/api/auth/login")
    //                    .allowedOrigins(LOCALHOST_URL)
    //                    .allowedMethods("POST")
    //                    .allowedHeaders("*")
    //                    .allowCredentials(true);
    //            registry.addMapping("/api/profile")
    //                    .allowedOrigins(LOCALHOST_URL)
    //                    .allowedMethods("POST", "PUT")
    //                    .allowedHeaders("*")
    //                    .allowCredentials(true);
    //        }
    //    };
    //}
}
