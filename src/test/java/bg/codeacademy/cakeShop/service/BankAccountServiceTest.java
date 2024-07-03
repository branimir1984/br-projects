package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.error_handling.exception.BankAccountExistException;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.repository.BankAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class BankAccountServiceTest {
    static BankAccountService bankAccountService;
    static BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);

    @BeforeAll
    public static void setup() {
        bankAccountService = new BankAccountService(bankAccountRepository);
    }

    @Test
    void shouldAddBankAccount() {
        BankAccount account = new BankAccount();
        account.setIban("BG18RZBB91550123456789");
        account.setCurrency(Currency.BG);
        when(bankAccountRepository.existsBankAccountByIban(account.getIban()))
                .thenReturn(false);
        List<String> iban = bankAccountService.addBankAccount(List.of(account));
        Assertions.assertEquals(iban.get(0), account.getIban());
        verify(bankAccountRepository, times(1)).save(account);
    }

    @Test
    void shouldThrowBankAccountExistExceptionWhenAddBankAccount() {
        BankAccount account = new BankAccount();
        account.setIban("BG18RZBB91550123456789");
        account.setCurrency(Currency.BG);
        when(bankAccountRepository.existsBankAccountByIban(account.getIban()))
                .thenReturn(true);
        Assertions.assertThrows(BankAccountExistException.class, () -> {
            bankAccountService.addBankAccount(List.of(account));
        });
    }
}