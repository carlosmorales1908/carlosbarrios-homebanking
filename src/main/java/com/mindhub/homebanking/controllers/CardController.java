package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;


    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(
            @RequestParam CardColor cardColor,
            @RequestParam CardType cardType,
            Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
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
            String number = getCardRandomNumber();
            while(cardRepository.findByNumber(number)!=null){
                number = getCardRandomNumber();
            }
            Card card = new Card(client.getFirstName()+" "+client.getLastName(),
                    cardType,
                    cardColor,
                    number,
                    getRandomNumber(1,999),
                    LocalDate.now(),
                    LocalDate.now().plusYears(5)
            );
            client.addCard(card);
            cardRepository.save(card);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        //El cliente no esta autenticado
        return new ResponseEntity<>("Customer not found.", HttpStatus.NOT_FOUND);
    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String getCardRandomNumber() {
        return getRandomNumber(1,9999)+"-"+getRandomNumber(1,9999)+"-"+getRandomNumber(1,9999)+"-"+getRandomNumber(1,9999);
    }
}
