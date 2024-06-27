package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.enums.Role;
import bg.codeacademy.cakeShop.error_handling.exception.UniqueIdentificationNumberExistException;
import bg.codeacademy.cakeShop.error_handling.exception.UserNameExistException;
import bg.codeacademy.cakeShop.model.Address;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.PersonalData;
import bg.codeacademy.cakeShop.repository.LegalEntityRepository;
import bg.codeacademy.cakeShop.repository.PersonalDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class LegalEntityServiceTest {
    static LegalEntityService legalEntityService;
    static LegalEntityRepository legalEntityRepository = mock(LegalEntityRepository.class);

    @BeforeAll
    public static void setup() {
        legalEntityService = new LegalEntityService(legalEntityRepository);
    }

    @Test
    void shouldSaveLegalEntity() {
        Address address = new Address();
        address.setCity("city");
        address.setStreet("street");

        BankAccount account = new BankAccount();
        account.setIban("BG18RZBB91550123456789");
        account.setCurrency(Currency.BG);

        PersonalData personalData = new PersonalData();
        personalData.setUserName("test");
        personalData.setUserRole(Role.DELIVER);
        personalData.setAddress(address);
        personalData.setBankAccount(List.of(account));
        personalData.setUserPassword("password");
        personalData.setPersonalName("personalName");

        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setEmail("someEmail");
        legalEntity.setUin("UIN");
        legalEntity.setPersonalData(personalData);

        when(legalEntityRepository.existsLegalEntityByUin("UIN")
        ).thenReturn(false);
        String response = legalEntityService.addLegalEntity(legalEntity);
        Assertions.assertEquals(legalEntity.getUin(), response);
        verify(legalEntityRepository, times(1)).save(legalEntity);
    }

    @Test
    void shouldThrowSaveLegalEntity() {
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setUin("UIN");

        when(legalEntityRepository.existsLegalEntityByUin("UIN")
        ).thenReturn(true);
        Assertions.assertThrows(UniqueIdentificationNumberExistException.class, () -> {
            legalEntityService.addLegalEntity(legalEntity);
        });
    }
}