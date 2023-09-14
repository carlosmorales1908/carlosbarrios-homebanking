package com.mindhub.homebanking.utils;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class AccountUtilsTest {

    //Verificar si el numero generado no es 0
    @Test
    void getNumberAccount(){
        int number =  AccountUtils.getAccountRandomNumber();
        assertThat(number, notNullValue(Integer.class));
    }

}
