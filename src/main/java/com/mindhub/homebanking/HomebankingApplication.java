package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return (args) -> {
			/*CREACION DEL CLIENTE MELBA*/
			Client melba = new Client("Melba","Morel","melba.morel@gmail.com");
			clientRepository.save(melba);

			/*CREACION DE CUENTA DE MELBA*/
			Account melbaAccount1 = new Account("VIN001", LocalDate.now(),5300.0);
			melba.addAccount(melbaAccount1);
			Transaction transaction = new Transaction(TransactionType.CREDITO,300.0,"Deposito por transferencia",LocalDate.now());
			melbaAccount1.addTransaction(transaction);
			accountRepository.save(melbaAccount1);
			transactionRepository.save(transaction);

			transaction = new Transaction(TransactionType.DEBITO,-200.0,"Compras varias",LocalDate.now());
			melbaAccount1.addTransaction(transaction);
			transactionRepository.save(transaction);
			accountRepository.save(melbaAccount1);

			/*CREACION DE LA 2DA CUENTA DE MELBA*/
			Account melbaAccount2 = new Account("VIN002", LocalDate.now().plusDays(1),7500.0);
			melba.addAccount(melbaAccount2);
			accountRepository.save(melbaAccount2);

			/*TRANSACCION DE LA CUENTA DE MELBA "VIN002"*/
			transaction = new Transaction(TransactionType.CREDITO,1000.0,"Deposito por transferencia",LocalDate.now());
			melbaAccount2.addTransaction(transaction);
			transactionRepository.save(transaction);



			/*CREACION DEL CLIENTE CARLOS*/
			Client carlos = new Client("Carlos", "Morales","carlosm@gmail.com");
			clientRepository.save(carlos);
			/*CREACION DE LA CUENTA DE CARLOS "WTN001"*/
			Account carlosAccount1 = new Account("WTN001", LocalDate.now(),300.0);
			carlos.addAccount(carlosAccount1);
			accountRepository.save(carlosAccount1);


			transaction = new Transaction(TransactionType.CREDITO,1000.0,"Deposito por transferencia",LocalDate.now());
			carlosAccount1.addTransaction(transaction);
			transactionRepository.save(transaction);

			Account carlosAccount2 = new Account("WTN023", LocalDate.now().plusDays(1),700.0);
			carlos.addAccount(carlosAccount2);
			accountRepository.save(carlosAccount2);

			transaction = new Transaction(TransactionType.CREDITO,100.0,"Deposito por transferencia",LocalDate.now());
			carlosAccount2.addTransaction(transaction);
			transactionRepository.save(transaction);

			Account carlosAccount3 = new Account("WTN182", LocalDate.now().plusDays(30),1000.0);
			carlos.addAccount(carlosAccount3);
			accountRepository.save(carlosAccount3);

			transaction = new Transaction(TransactionType.CREDITO,10000.0,"Deposito por transferencia",LocalDate.now());
			carlosAccount3.addTransaction(transaction);
			transactionRepository.save(transaction);



		};
	}
}
