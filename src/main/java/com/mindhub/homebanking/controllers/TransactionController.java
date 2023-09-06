package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;


    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(
            @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
            @RequestParam Double amount, @RequestParam String description,
            Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null) {
            //Verificar que los parámetros no estén vacíos
            if (amount <= 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
                if (amount <= 0) {
                    return new ResponseEntity<>("The amount must be greater than 0.", HttpStatus.FORBIDDEN);
                } else {
                    return new ResponseEntity<>("Missing data2.", HttpStatus.FORBIDDEN);
                }
            }

            //Verificar que los números de cuenta no sean iguales
            if (fromAccountNumber.equals(toAccountNumber)) {
                return new ResponseEntity<>("Cannot transfer to the same source account.", HttpStatus.FORBIDDEN);
            }

            //Verificar que exista la cuenta de origen
            Account sourceAccount = accountService.findByNumber(fromAccountNumber);
            if (sourceAccount == null) {
                return new ResponseEntity<>("The source account does not exist.", HttpStatus.NOT_FOUND);
            }

            //Verificar que exista la cuenta de destino
            Account targetAccount = accountService.findByNumber(toAccountNumber);
            if (targetAccount == null) {
                return new ResponseEntity<>("The target account does not exist.", HttpStatus.NOT_FOUND);
            }

            //Verificar que la cuenta de origen pertenezca al cliente autenticado
            Account accountClient = null;
            for (Account account : client.getAccounts()) {
                if (account.getNumber().equals(fromAccountNumber)) {
                    accountClient = account;
                }
            }
            if (accountClient == null) {
                return new ResponseEntity<>("Error in the source account.", HttpStatus.FORBIDDEN);
            }

            //Verificar que la cuenta de origen tenga el monto disponible.
            if (accountClient.getBalance() < amount) {
                return new ResponseEntity<>("The account does not have sufficient balance to perform the operation.", HttpStatus.FORBIDDEN);
            }


            //Transaccion de DEBITO de la cta de origen
            Transaction transactionDebit = new Transaction(TransactionType.DEBIT, amount*-1, description+" "+fromAccountNumber, LocalDate.now());
            sourceAccount.addTransaction(transactionDebit);
            transactionRepository.save(transactionDebit);


            //Transaccion de CREDITO de la cta de destino
            Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, description+" "+toAccountNumber, LocalDate.now());
            targetAccount.addTransaction(transactionCredit);
            transactionRepository.save(transactionCredit);


            //Actualizacion de las cuentas
            sourceAccount.setBalance(sourceAccount.getBalance()+amount*-1);
            //accountRepository.save(sourceAccount);

            targetAccount.setBalance(targetAccount.getBalance()+amount);
            //accountRepository.save(targetAccount);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        //El cliente no esta autenticado
        return new ResponseEntity<>("Customer not found.", HttpStatus.NOT_FOUND);
    }
}
