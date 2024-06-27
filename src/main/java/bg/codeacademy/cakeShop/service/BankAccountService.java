package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.BankAccountExistException;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public List<String> addBankAccount(List<BankAccount> bankAccount) {
        List<String> info = new LinkedList<>();
        for (BankAccount account : bankAccount) {
            if (bankAccountRepository.existsBankAccountByIban(account.getIban())) {
                throw new BankAccountExistException("Bank account with iban:"
                        + account.getIban() + " is already in use!");
            }
            bankAccountRepository.save(account);
            info.add(account.getIban());
        }

        return info;
    }
}
