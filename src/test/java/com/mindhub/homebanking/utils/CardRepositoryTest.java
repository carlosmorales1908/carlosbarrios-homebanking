package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class CardRepositoryTest {
    @Autowired
    private CardRepository cardRepository;


    //Verificar que siempre exista al menos una card en la bd
    @Test
    void existAnyCard(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,not(empty()));
    }

    //Verificar que un numero de card exista
    @Test
    void existCard(){
        Card card = cardRepository.findByNumber("3214-6547-9877-6541");
        assertThat(card,notNullValue());
    }

}
