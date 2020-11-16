package hspt.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 * Main SpringBoot application class
 * @author alex
 *
 */
@SpringBootApplication
public class HsptBackendApplication  {

	
	public static void main(String[] args) {

		SpringApplication.run(HsptBackendApplication.class, args);
	}
	
}
