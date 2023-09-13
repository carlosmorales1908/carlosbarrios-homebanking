package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccountsDTO();
    }

    @GetMapping("accounts/{id}")
    public AccountDTO getAccountById(@PathVariable Long id){
        return accountService.getAccountDTO(id);
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentClientAccounts(Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        return client.getAccounts().stream()
                .map(currentAccount -> new AccountDTO(currentAccount))
                .collect(Collectors.toList());
    }


    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null){
            //El cliente existe en la DB
            if(client.getAccounts().size() < 3 ){
                //Se crea una cuenta
                int number = AccountUtils.getAccountRandomNumber();
                while(accountService.findByNumber(number+"")!=null){
                    number = AccountUtils.getAccountRandomNumber();
                }
                Account account = new Account("VIN-"+number, LocalDate.now(),0.0);
                System.out.println(account.getNumber());
                client.addAccount(account);
                accountService.save(account);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            //Ya tiene las 3 cuentas
            return new ResponseEntity<>("The client already has 3 accounts.", HttpStatus.FORBIDDEN);
        }
        //El cliente no existe en la BD
        return new ResponseEntity<>("No se encontro el cliente.", HttpStatus.NOT_FOUND);
    }


}
