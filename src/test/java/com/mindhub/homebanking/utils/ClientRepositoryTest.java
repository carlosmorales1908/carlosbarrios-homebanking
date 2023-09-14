package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
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
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;


    //Verificar que siempre exista al menos un cliente en la bd
    @Test
    void existAnyClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,not(empty()));
    }

    //Verificar que un correo de cliente exista
    @Test
    void existClient(){
        Client client = clientRepository.findByEmail("melba.morel@gmail.com");
        assertThat(client,notNullValue());
    }
}
