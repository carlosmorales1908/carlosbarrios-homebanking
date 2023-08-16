package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository
									){
		return (args) -> {
			/*CREACION DE LOS PRESTAMOS*/
			Loan prestamoHipotecario = new Loan("Hipotecario",500000.0, List.of(12,24,36,48,60));
			loanRepository.save(prestamoHipotecario);
			Loan prestamoPersonal = new Loan("Personal",100000.0, List.of(6,12,24));
			loanRepository.save(prestamoPersonal);
			Loan prestamoAutomotriz = new Loan("Automotriz",300000.0, List.of(6,12,24,36));
			loanRepository.save(prestamoAutomotriz);


			/*CREACION DEL CLIENTE MELBA*/
			Client melba = new Client("Melba","Morel","melba.morel@gmail.com");
			clientRepository.save(melba);

			/*CREACION DE CUENTA DE MELBA*/
			Account melbaAccount1 = new Account("VIN001", LocalDate.now(),5300.0);
			melba.addAccount(melbaAccount1);
			Transaction transaction = new Transaction(TransactionType.CREDIT,300.0,"Deposito por transferencia",LocalDate.now());
			melbaAccount1.addTransaction(transaction);
			accountRepository.save(melbaAccount1);
			transactionRepository.save(transaction);

			transaction = new Transaction(TransactionType.DEBIT,-200.0,"Compras varias",LocalDate.now());
			melbaAccount1.addTransaction(transaction);
			transactionRepository.save(transaction);
			accountRepository.save(melbaAccount1);

			/*CREACION DE LA 2DA CUENTA DE MELBA*/
			Account melbaAccount2 = new Account("VIN002", LocalDate.now().plusDays(1),7500.0);
			melba.addAccount(melbaAccount2);
			accountRepository.save(melbaAccount2);

			/*TRANSACCION DE LA CUENTA DE MELBA "VIN002"*/
			transaction = new Transaction(TransactionType.CREDIT,1000.0,"Deposito por transferencia",LocalDate.now());
			melbaAccount2.addTransaction(transaction);
			transactionRepository.save(transaction);

			//Préstamo Hipotecario, 400.000, 60 cuotas.
			ClientLoan clientLoan = new ClientLoan(400000.0,60,melba,prestamoHipotecario);
			clientLoanRepository.save(clientLoan);
			//Préstamo Personal, 50.000, 12 cuotas
			clientLoan = new ClientLoan(50000.0,12,melba,prestamoPersonal);
			clientLoanRepository.save(clientLoan);


			//CREACION DE LA CARD DE DEBITO GOLD
			Card card1Melba = new Card(melba.getFirstName()+" "+melba.getLastName(),
										CardType.DEBIT,
										CardColor.GOLD,
										"3214-6547-9877-6541",
										321,
										LocalDate.now(),
					LocalDate.now().plusYears(5)
			);
			melba.addCard(card1Melba);
			cardRepository.save(card1Melba);
			clientRepository.save(melba);

			//CREACION DE LA CARD DE CREDITO TITANIUM
			Card card2Melba = new Card(melba.getFirstName()+" "+melba.getLastName(),
					CardType.CREDIT,
					CardColor.TITANIUM,
					"3287-6287-1871-6742",
					987,
					LocalDate.now(),
					LocalDate.now().plusYears(5)
			);
			melba.addCard(card2Melba);
			cardRepository.save(card2Melba);
			clientRepository.save(melba);




			//****************************** SEGUNDO CLIENTE ********************************************************************************

			/*CREACION DEL CLIENTE CARLOS*/
			Client carlos = new Client("Carlos", "Morales","carlosm@gmail.com");
			clientRepository.save(carlos);
			/*CREACION DE LA CUENTA DE CARLOS "WTN001"*/
			Account carlosAccount1 = new Account("WTN001", LocalDate.now(),300.0);
			carlos.addAccount(carlosAccount1);
			accountRepository.save(carlosAccount1);


			transaction = new Transaction(TransactionType.CREDIT,1000.0,"Deposito por transferencia",LocalDate.now());
			carlosAccount1.addTransaction(transaction);
			transactionRepository.save(transaction);

			Account carlosAccount2 = new Account("WTN023", LocalDate.now().plusDays(1),700.0);
			carlos.addAccount(carlosAccount2);
			accountRepository.save(carlosAccount2);

			transaction = new Transaction(TransactionType.CREDIT,100.0,"Deposito por transferencia",LocalDate.now());
			carlosAccount2.addTransaction(transaction);
			transactionRepository.save(transaction);

			Account carlosAccount3 = new Account("WTN182", LocalDate.now().plusDays(30),1000.0);
			carlos.addAccount(carlosAccount3);
			accountRepository.save(carlosAccount3);

			transaction = new Transaction(TransactionType.CREDIT,10000.0,"Deposito por transferencia",LocalDate.now());
			carlosAccount3.addTransaction(transaction);
			transactionRepository.save(transaction);

			//Préstamo Personal, 100.000, 24 cuotas
			clientLoan = new ClientLoan(100000.0,24,carlos,prestamoPersonal);
			clientLoanRepository.save(clientLoan);
			//Préstamo Automotriz, 200.000, 36 cuotas
			clientLoan = new ClientLoan(200000.0,36,carlos,prestamoAutomotriz);
			clientLoanRepository.save(clientLoan);

			//CREACION DE LA CARD DE CREDITO SILVER
			Card card1Carlos = new Card(carlos.getFirstName()+" "+carlos.getLastName(),
					CardType.CREDIT,
					CardColor.SILVER,
					"9875-7766-4242-1230",
					654,
					LocalDate.now(),
					LocalDate.now().plusYears(5)
			);
			carlos.addCard(card1Carlos);
			cardRepository.save(card1Carlos);
			clientRepository.save(carlos);

		};
	}
}
