package ua.foxstudent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String HOME_PATH = "/";
    public static final String HOME_VIEW = "home";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(HOME_PATH).setViewName(HOME_VIEW);
    }
}
