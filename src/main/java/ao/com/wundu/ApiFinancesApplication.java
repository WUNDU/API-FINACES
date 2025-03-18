package ao.com.wundu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "ao.com.wundu.repository")
@SpringBootApplication
public class ApiFinancesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiFinancesApplication.class, args);
	}

}
