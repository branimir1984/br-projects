package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.BankAccountExistException;
import bg.codeacademy.cakeShop.error_handling.exception.BankAccountNotExistException;
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

    public List<String> createBankAccount(List<BankAccount> bankAccount) {
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

    public BankAccount getBankAccount(int id, String iban) {
        BankAccount account = bankAccountRepository.findBankAccountByBeneficiary_idAndIban(id, iban);
        if (account == null) {
            throw new BankAccountNotExistException("Not found bank account for user ID:" + id);
        }
        return account;
    }

    public BankAccount getBankAccount(String iban) {
        BankAccount account = bankAccountRepository.findBankAccountByIban(iban);
        if (account == null) {
            throw new BankAccountNotExistException("Not found bank account with IBAN:" + iban);
        }
        return account;
    }

    public BankAccount update(BankAccount account) {
        boolean isExist = bankAccountRepository.existsById(account.getId());
        if (!isExist) {
            throw new BankAccountNotExistException("Not found bank account with ID:" + account.getId());
        }
        bankAccountRepository.save(account);
        return account;
    }
}
