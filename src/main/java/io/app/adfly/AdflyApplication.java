package io.app.adfly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2()
@Import(CustomExceptionHandler.class)
public class AdflyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdflyApplication.class, args);
	}

}
