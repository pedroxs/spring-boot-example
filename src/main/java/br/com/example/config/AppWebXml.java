package br.com.example.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.SpringBootServletInitializer;

/**
 * This file is only used on war package
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 21/10/13
 * Time: 13:45
 * To change this template use File | Settings | File Templates.
 */
public class AppWebXml extends SpringBootServletInitializer {

    @Override
    protected void configure(SpringApplicationBuilder application) {
        application.sources(AppConfig.class);
        super.configure(application);
    }
}
