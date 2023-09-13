package com.mindhub.homebanking.controllers;



import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;





    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoansDTO();
    }


    /*
    loanId: this.loanTypeId, amount: this.amount, payments: this.payments, toAccountNumber: this.accountToNumber
     */
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> newLoan(@RequestBody LoanApplicationDTO loanApplicationDTO,
                                             Authentication authentication
                                             ) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client != null){
            if (loanApplicationDTO == null) {
                return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
            }

            //Verificar que los datos sean correctos, es decir no estén vacíos, que el monto no sea 0 o que las cuotas no sean 0.
            if(loanApplicationDTO.getAmount()<=0){
                return new ResponseEntity<>("Amount invalid", HttpStatus.FORBIDDEN);
            }
            if(loanApplicationDTO.getPayments()<=0){
                return new ResponseEntity<>("Payments invalid", HttpStatus.FORBIDDEN);
            }

            //Verificar que el préstamo exista
            Loan loan = loanService.findById(loanApplicationDTO.getLoanId());
            if(loan == null){
                return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND);
            }

            //Verificar que el monto solicitado no exceda el monto máximo del préstamo
            if(loanApplicationDTO.getAmount()>loan.getMaxAmount()){
                return new ResponseEntity<>("Amount invalid", HttpStatus.FORBIDDEN);
            }

            //Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
            boolean paymentInvalid = true;
            for (Integer payment: loan.getPayments()) {
                if(payment.equals(loanApplicationDTO.getPayments())){
                    paymentInvalid = false;
                    break;
                }
            }
            if(paymentInvalid){
                return new ResponseEntity<>("Payment not found", HttpStatus.NOT_FOUND);
            }

            //Verificar que la cuenta de destino exista
            if(accountService.findByNumber(loanApplicationDTO.getToAccountNumber())==null){
                return new ResponseEntity<>("Target account not found", HttpStatus.NOT_FOUND);
            }

            //Verificar que la cuenta de destino pertenezca al cliente autenticado
            Account clientAccount = null;
            boolean accountInvalid = true;
            for (Account account: client.getAccounts()){
                if(account.getNumber().equals(loanApplicationDTO.getToAccountNumber())){
                    clientAccount = account;
                    accountInvalid = false;
                    break;
                }
            }
            if (accountInvalid){
                return new ResponseEntity<>("Invalid target account", HttpStatus.FORBIDDEN);
            }

            //CREAR PRESTAMO
            Double loanAmount = loanApplicationDTO.getAmount()+loanApplicationDTO.getAmount()*0.2;
            ClientLoan clientLoan = new ClientLoan(loanAmount,loanApplicationDTO.getPayments());
            //loanRepository.save()
            Transaction transaction = new Transaction(TransactionType.CREDIT,
                    loanAmount,
                    loan.getName()+" "+"loan approved.",
                    LocalDate.now());
            client.addLoan(clientLoan);
            loan.addClient(clientLoan);
            clientLoanService.save(clientLoan);
            clientAccount.addTransaction(transaction);
            transactionService.save(transaction);
            clientAccount.setBalance(clientAccount.getBalance()+loanAmount);
            accountService.save(clientAccount);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
