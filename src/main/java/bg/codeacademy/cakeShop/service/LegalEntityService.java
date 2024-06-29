package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.LegalEntityNotFoundException;
import bg.codeacademy.cakeShop.error_handling.exception.RoleNotSupportedException;
import bg.codeacademy.cakeShop.error_handling.exception.UniqueIdentificationNumberExistException;
import bg.codeacademy.cakeShop.model.Address;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.PersonalData;
import bg.codeacademy.cakeShop.repository.LegalEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegalEntityService {
    public final LegalEntityRepository legalEntityRepository;
    private final AddressService addressService;
    private final BankAccountService bankAccountService;
    private final PersonalDataService personalDataService;

    @Value("#{'${roles.legal-entity}'.split(',')}")
    private List<String> roles;

    public LegalEntityService(LegalEntityRepository legalEntityRepository
            , AddressService addressService
            , BankAccountService bankAccountService
            , PersonalDataService personalDataService) {
        this.legalEntityRepository = legalEntityRepository;
        this.addressService = addressService;
        this.bankAccountService = bankAccountService;
        this.personalDataService = personalDataService;
    }

    @Transactional
    public String addLegalEntity(LegalEntity legalEntity) {
        String role = String.valueOf(legalEntity.getPersonalData().getUserRole());
        if (!roles.contains(role)) {
            throw new RoleNotSupportedException("Allowed roles for legal entity are:" + roles);
        }

        if (legalEntityRepository.existsLegalEntityByUin(legalEntity.getUin())) {
            throw new UniqueIdentificationNumberExistException("Legal entity with UIN:"
                    + legalEntity.getUin() + " already exist!");
        }

        Address address = addressService.addAddress(legalEntity.getPersonalData().getAddress());
        legalEntity.getPersonalData().setAddress(address);
        personalDataService.addPersonalData(legalEntity.getPersonalData());
        bankAccountService.addBankAccount(legalEntity.getPersonalData().getBankAccount());

        legalEntityRepository.save(legalEntity);
        return legalEntity.getUin();
    }

    public LegalEntity getLegalEntity(PersonalData personalData) {
        LegalEntity legalEntity = legalEntityRepository.findLegalEntityByPersonalData(personalData);
        if (legalEntity != null) {
            return legalEntity;
        } else {
            throw new LegalEntityNotFoundException("Legal entity not found! name=" + personalData.getUserName());
        }
    }
}
