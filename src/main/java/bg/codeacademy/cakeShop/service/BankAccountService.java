package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.error_handling.exception.BankAccountExistException;
import bg.codeacademy.cakeShop.error_handling.exception.BankAccountNotExistException;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.PersonalData;
import bg.codeacademy.cakeShop.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final PersonalDataService personalDataService;

    public BankAccountService(BankAccountRepository bankAccountRepository, PersonalDataService personalDataService) {
        this.bankAccountRepository = bankAccountRepository;
        this.personalDataService = personalDataService;
    }

    public List<String> createBankAccount(List<BankAccount> bankAccount) {
        List<String> info = new LinkedList<>();
        for (BankAccount account : bankAccount) {
            saveBankAccount(account);
            info.add(account.getIban());
        }

        return info;
    }

    private void saveBankAccount(BankAccount account) {
        if (bankAccountRepository.existsBankAccountByIban(account.getIban())) {
            throw new BankAccountExistException("Bank account with iban:"
                    + account.getIban() + " is already in use!");
        }
        bankAccountRepository.save(account);
    }

    public BankAccount getBankAccount(int id, String iban) {
        BankAccount account = bankAccountRepository.findBankAccountByBeneficiary_idAndIban(id, iban);
        if (account == null) {
            throw new BankAccountNotExistException("Not found bank account with IBAN:" + iban);
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

    public BankAccount deleteBankAccount(int principalId, String iban) {
        BankAccount account = getBankAccount(principalId, iban);
        bankAccountRepository.delete(account);
        return account;
    }

    public BankAccount changeBankAccountCurrency(int principalId, String iban, String currency) {
        BankAccount account = getBankAccount(principalId, iban);
        account.setCurrency(Currency.valueOf(currency));
        bankAccountRepository.save(account);
        return account;
    }

    public BankAccount createBankAccount(int id, String iban, float amount, String currency, boolean rental) {
        PersonalData personalData = personalDataService.getPersonalData(id);
        BankAccount account = new BankAccount();
        account.setIban(iban);
        account.setAmount(amount);
        account.setCurrency(Currency.valueOf(currency));
        account.setBeneficiary(personalData);
        account.setRental(rental);
        saveBankAccount(account);
        return account;
    }
}
