package com.mindhub.homebanking.utils;



import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

    //Verificar que siempre exista al menos una transaccion en la bd
    @Test
    void existAnyTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,not(empty()));
    }

}
