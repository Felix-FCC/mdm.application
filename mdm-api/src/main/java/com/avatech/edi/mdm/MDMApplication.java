package com.avatech.edi.mdm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Fancy
 * @date 2018/9/4
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.avatech.edi")
public class MDMApplication  {

    public static void main(String args[]){
        SpringApplication.run(MDMApplication.class,args);
    }
}
