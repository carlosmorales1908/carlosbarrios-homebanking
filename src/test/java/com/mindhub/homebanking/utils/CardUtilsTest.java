package com.mindhub.homebanking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class CardUtilsTest {


    //Verificar que el numero generado sea menor o igual que 999
    @Test
    void generateCvvNumber() {
        int number = CardUtils.getCvvRandomNumber();
        assertThat(number, lessThanOrEqualTo(999));
    }

    //Verificar que el numero generado sea mayor o igual a 111
    @Test
    void generateCvvNumber2() {
        int number = CardUtils.getCvvRandomNumber();
        assertThat(number, greaterThanOrEqualTo(111));
    }

    //Verificar que el String generado sea
    @Test
    void getCardRandomNumber() {
        String numberCard = CardUtils.getCardRandomNumber();
        assertThat(numberCard, not(isEmptyString()));
    }
}