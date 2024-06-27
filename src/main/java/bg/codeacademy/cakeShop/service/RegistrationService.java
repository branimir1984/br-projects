package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.model.Address;
import bg.codeacademy.cakeShop.model.LegalEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final AddressService addressService;
    private final BankAccountService bankAccountService;
    private final PersonalDataService personalDataService;
    private final LegalEntityService legalEntityService;

    public RegistrationService(AddressService addressService,
                               BankAccountService bankAccountService,
                               PersonalDataService personalDataService,
                               LegalEntityService legalEntityService) {
        this.addressService = addressService;
        this.bankAccountService = bankAccountService;
        this.personalDataService = personalDataService;
        this.legalEntityService = legalEntityService;
    }

    @Transactional
    public String registerLegalEntity(LegalEntity legalEntity) {
        Address address = addressService.addAddress(legalEntity.getPersonalData().getAddress());
        legalEntity.getPersonalData().setAddress(address);
        personalDataService.addPersonalData(legalEntity.getPersonalData());
        bankAccountService.addBankAccount(legalEntity.getPersonalData().getBankAccount());
        legalEntityService.addLegalEntity(legalEntity);
        return legalEntity.getUin();
    }
}
