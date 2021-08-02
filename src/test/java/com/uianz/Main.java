package com.uianz;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author uianz
 * @date 2021/6/25
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        new SpringApplicationBuilder(Main.class).run(args);
    }

}
