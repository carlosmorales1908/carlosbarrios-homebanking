package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;


    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getClientsDTO();
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        //Se crea una cuenta para el cliente
        int number = getAccountRandomNumber();
        while(accountService.findByNumber(number+"")!=null){
            number = getAccountRandomNumber();
        }
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.save(client);
        Account account = new Account("VIN-"+number, LocalDate.now(),0.0);
        client.addAccount(account);
        accountService.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        Client client = clientService.getCurrent(authentication.getName());
        return new ClientDTO(client);
    }


    private int getAccountRandomNumber() {
        return (int) ((Math.random() * (99999999 - 1)) + 1);
    }
}
