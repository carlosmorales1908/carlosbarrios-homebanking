package com.mindhub.homebanking.utils;



import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
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
public class LoanRepositoryTest {
    @Autowired
    private LoanRepository loanRepository;


    //Verificar que siempre exista al menos un Loan en la bd
    @Test
    void existAnyLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,not(empty()));
    }

    //Verificar que un nombre de Loan exista
    @Test
    void existLoan(){
        Loan loan = loanRepository.findByName("Hipotecario");
        assertThat(loan,notNullValue());
    }





}
