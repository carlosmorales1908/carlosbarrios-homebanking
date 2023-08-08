package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
		/*
		LocalDate fechaHoy = LocalDate.now();
		System.out.println("Fecha de hoy: "+fechaHoy);
		LocalDate fechaMañana=fechaHoy.plusDays(1);
		System.out.println("Fecha de mañana: "+fechaHoy.plusDays(1));
		 */
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return (args) -> {
			Client client = new Client("Melba","Morel","melba.morel@gmail.com");
			clientRepository.save(client);
			Account account = new Account("VIN001", LocalDate.now(),5000.0,client);
			accountRepository.save(account);
			account = new Account("VIN002", LocalDate.now().plusDays(1),7500.0,client);
			accountRepository.save(account);


			client = new Client("Carlos", "Morales","carlosm@gmail.com");
			clientRepository.save(client);
			account = new Account("WTN001", LocalDate.now(),300.0,client);
			accountRepository.save(account);
			account = new Account("WTN023", LocalDate.now().plusDays(1),700.0,client);
			accountRepository.save(account);
			account = new Account("WTN182", LocalDate.now().plusDays(30),1000.0,client);
			accountRepository.save(account);

		};
	}
}
