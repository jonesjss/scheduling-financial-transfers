package com.cvc.financial;

import com.cvc.financial.domain.model.User;
import com.cvc.financial.domain.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackageClasses = User.class)
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@SpringBootApplication
public class SchedulingFinancialTransfersApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulingFinancialTransfersApplication.class, args);
	}

}
