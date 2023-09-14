package com.mindhub.homebanking.utils;



import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
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
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;


    //Verificar que siempre exista al menos una cuenta en la bd
    @Test
    void existAnyAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,not(empty()));
    }

    //Verificar que un numero de cuenta exista
    @Test
    void existAccount(){
        Account account = accountRepository.findByNumber("VIN-001");
        assertThat(account,notNullValue());
    }

}



