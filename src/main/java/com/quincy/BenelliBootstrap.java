package com.quincy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
@EnableMyBoot
@EnableWebMvc
@EnableAutoConfiguration
@EnableScheduling
@SpringBootApplication/*(exclude = {
        DataSourceAutoConfiguration.class
})*/
@ComponentScan(basePackages= {"com.*"})
public class BenelliBootstrap {
    public static void main(String[] args) {
    	SpringApplication sa = new SpringApplication(BenelliBootstrap.class);
        sa.addListeners(new ApplicationPidFileWriter());
        sa.run(args);
    }
}