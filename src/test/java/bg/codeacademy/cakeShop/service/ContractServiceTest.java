package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.enums.Status;
import bg.codeacademy.cakeShop.error_handling.exception.InvalidContractException;
import bg.codeacademy.cakeShop.error_handling.exception.UniqueIdentificationNumberExistException;
import bg.codeacademy.cakeShop.model.*;
import bg.codeacademy.cakeShop.repository.ContractRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class ContractServiceTest {
    public static final ContractRepository contractRepository = mock(ContractRepository.class);
    public static final LegalEntityService legalEntityService = mock(LegalEntityService.class);
    public static ContractService contractService;

    @BeforeAll
    public static void setup() {
        contractService = new ContractService(contractRepository, legalEntityService);
    }

    @Test
    void shouldCreateContract() {
        LegalEntity offeror = formLegalEntity("A");
        LegalEntity recipient = formLegalEntity("B");
        when(legalEntityService.getLegalEntity(1)).thenReturn(offeror);
        when(legalEntityService.getLegalEntity("B")).thenReturn(recipient);
        when(contractRepository.existsContractByOfferorAndRecipient(
                offeror, recipient)).thenReturn(false);
        String ident = offeror.getPersonalData().getUserRole() + "-" +
                recipient.getPersonalData().getUserRole() + "-" + offeror.getPersonalData().getId();
        Contract contract = new Contract();
        contract.setIdentifier(ident);
        contract.setAmount(100);
        contract.setCurrency(Currency.BG);
        contract.setOfferor(offeror);
        contract.setRecipient(recipient);
        contract.setStatus(Status.PENDING);
        Contract response = contractService.createContract(1, 100, "BG", "B");
        verify(contractRepository, times(1)).save(response);
    }

    @Test
    void shouldThrowInvalidContractException() {
        LegalEntity offeror = formLegalEntity("A");
        LegalEntity recipient = formLegalEntity("A");
        when(legalEntityService.getLegalEntity(1)).thenReturn(offeror);
        when(legalEntityService.getLegalEntity("A")).thenReturn(recipient);
        Assertions.assertThrows(InvalidContractException.class, () -> {
            contractService.createContract(1, 100, "BG", "A");
        });
    }

    @Test
    void shouldThrowUniqueIdentificationNumberExistException() {
        LegalEntity offeror = formLegalEntity("A");
        LegalEntity recipient = formLegalEntity("B");
        when(legalEntityService.getLegalEntity(1)).thenReturn(offeror);
        when(legalEntityService.getLegalEntity("B")).thenReturn(recipient);
        when(contractRepository.existsContractByOfferorAndRecipient(
                offeror, recipient)).thenReturn(true);
        Assertions.assertThrows(UniqueIdentificationNumberExistException.class, () -> {
            contractService.createContract(1, 100, "BG", "B");
        });
    }

    private LegalEntity formLegalEntity(String uin) {
        Address address = new Address();
        address.setCity("city");
        address.setStreet("street");
        BankAccount account = new BankAccount();
        account.setIban("BG18RZBB91550123456789");
        account.setCurrency(Currency.BG);
        PersonalData personalData = new PersonalData();
        personalData.setUserName("test");
        personalData.setAddress(address);
        personalData.setBankAccount(List.of(account));
        personalData.setUserPassword("password");
        personalData.setPersonalName("personalName");
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setEmail("someEmail");
        legalEntity.setUin(uin);
        legalEntity.setPersonalData(personalData);
        return legalEntity;
    }
}