package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam CardColor cardColor,
            @RequestParam CardType cardType,
            Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null){
            //El cliente esta autenticado
            byte count = 0;
            for (Card card: client.getCards()) {
                if(card.getType().equals(cardType)){
                    count++;
                }
            }
            if(count>=3){
                //El cliente ya tiene las 3 tarjetas del mismo tipo
                return new ResponseEntity<>("The client already has 3 "+cardType+" cards.", HttpStatus.FORBIDDEN);
            }
            String number = CardUtils.getCardRandomNumber();
            while(cardService.findByNumber(number)!=null){
                number = CardUtils.getCardRandomNumber();
            }
            Card card = new Card(client.getFirstName()+" "+client.getLastName(),
                    cardType,
                    cardColor,
                    number,
                    CardUtils.getCvvRandomNumber(),
                    LocalDate.now(),
                    LocalDate.now().plusYears(5)
            );
            client.addCard(card);
            cardService.save(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        //El cliente no esta autenticado
        return new ResponseEntity<>("Customer not found.", HttpStatus.NOT_FOUND);
    }





}
