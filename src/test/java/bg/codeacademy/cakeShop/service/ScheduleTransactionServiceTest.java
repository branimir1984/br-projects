package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.error_handling.exception.BankNotAccountExistException;
import bg.codeacademy.cakeShop.error_handling.exception.InvalidScheduleTransactionException;
import bg.codeacademy.cakeShop.error_handling.exception.ScheduleTransactionExistException;
import bg.codeacademy.cakeShop.model.*;
import bg.codeacademy.cakeShop.repository.ScheduleTransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScheduleTransactionServiceTest {
    public static ScheduleTransactionService scheduleTransactionService;
    private static final ScheduleTransactionRepository scheduleTransactionRepository
            = mock(ScheduleTransactionRepository.class);
    private static final LegalEntityService legalEntityService
            = mock(LegalEntityService.class);

    @BeforeAll
    public static void setup() {
        scheduleTransactionService = new ScheduleTransactionService(
                scheduleTransactionRepository, legalEntityService
        );
    }

    @Test
    void shouldCreateScheduleTransaction() {
        LegalEntity sender = formLegalEntity("A");
        LegalEntity recipient = formLegalEntity("B");
        when(legalEntityService.getLegalEntity(1)).thenReturn(sender);
        when(legalEntityService.getLegalEntity(2)).thenReturn(recipient);
        when(scheduleTransactionRepository
                .existsScheduleTransactionBySenderAndRecipientAndTransactionTime(any(BankAccount.class),
                        any(BankAccount.class),
                        any(Date.class))).thenReturn(false);
        ScheduleTransaction response
                = scheduleTransactionService.createScheduleTransaction(
                1,
                "A",
                2,
                "B",
                100,
                new Date());
        verify(scheduleTransactionRepository, times(1)).save(response);
    }

    @Test
    void shouldThrowInvalidScheduleTransactionException() {
        Assertions.assertThrows(InvalidScheduleTransactionException.class, () -> {
            scheduleTransactionService.createScheduleTransaction(
                    1,
                    "A",
                    1,
                    "B",
                    100,
                    new Date());
        });
    }

    @Test
    void shouldThrowBankNotAccountExistException() {
        LegalEntity sender = formLegalEntity("A");
        LegalEntity recipient = formLegalEntity("B");
        when(legalEntityService.getLegalEntity(1)).thenReturn(sender);
        when(legalEntityService.getLegalEntity(2)).thenReturn(recipient);
        Assertions.assertThrows(BankNotAccountExistException.class, () -> {
            scheduleTransactionService.createScheduleTransaction(
                    2,
                    "C",
                    1,
                    "B",
                    100,
                    new Date());
        });
        Assertions.assertThrows(BankNotAccountExistException.class, () -> {
            scheduleTransactionService.createScheduleTransaction(
                    2,
                    "A",
                    1,
                    "C",
                    100,
                    new Date());
        });
    }

    @Test
    void shouldThrowScheduleTransactionExistException() {
        LegalEntity sender = formLegalEntity("A");
        LegalEntity recipient = formLegalEntity("B");
        when(legalEntityService.getLegalEntity(1)).thenReturn(sender);
        when(legalEntityService.getLegalEntity(2)).thenReturn(recipient);
        when(scheduleTransactionRepository
                .existsScheduleTransactionBySenderAndRecipientAndTransactionTime(any(BankAccount.class),
                        any(BankAccount.class),
                        any(Date.class))).thenReturn(true);
        Assertions.assertThrows(ScheduleTransactionExistException.class, () -> {
            scheduleTransactionService.createScheduleTransaction(
                    1,
                    "A",
                    2,
                    "B",
                    100,
                    new Date());
        });
    }

    private LegalEntity formLegalEntity(String iban) {
        Address address = new Address();
        address.setCity("city");
        address.setStreet("street");
        BankAccount account = new BankAccount();
        account.setIban(iban);
        account.setCurrency(Currency.BG);
        PersonalData personalData = new PersonalData();
        personalData.setUserName("test");
        personalData.setAddress(address);
        personalData.setBankAccount(List.of(account));
        personalData.setUserPassword("password");
        personalData.setPersonalName("personalName");
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setEmail("someEmail");
        legalEntity.setUin("UIN");
        legalEntity.setPersonalData(personalData);
        return legalEntity;
    }
}