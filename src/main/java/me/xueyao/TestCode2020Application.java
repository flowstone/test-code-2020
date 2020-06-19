package me.xueyao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author simonxue
 */
@SpringBootApplication
@ServletComponentScan
public class TestCode2020Application {

    public static void main(String[] args) {
        SpringApplication.run(TestCode2020Application.class, args);
    }

}
