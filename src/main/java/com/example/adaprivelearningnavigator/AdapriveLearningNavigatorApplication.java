package com.example.adaprivelearningnavigator;

import com.example.adaprivelearningnavigator.web.BrowserLaunchOnStartup;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdapriveLearningNavigatorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(AdapriveLearningNavigatorApplication.class, args);
        if (context.isActive()) {
            context.getBean(BrowserLaunchOnStartup.class).openFromContext(context);
        }
    }

}

