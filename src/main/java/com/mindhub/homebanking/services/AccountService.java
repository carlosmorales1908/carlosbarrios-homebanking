package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccountsDTO();
    Account findById(Long id);
    AccountDTO getAccountDTO(Long id);
    Account findByNumber(String number);
    void save(Account account);

}