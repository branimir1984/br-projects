package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.model.Address;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.PersonalData;
import bg.codeacademy.cakeShop.model.Staff;
import bg.codeacademy.cakeShop.repository.StaffRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;

@Service
public class RegistrationService {
    private final AddressService addressService;
    private final BankAccountService bankAccountService;
    private final PersonalDataService personalDataService;
    private final LegalEntityService legalEntityService;
    private final StaffService staffService;

    public RegistrationService(AddressService addressService,
                               BankAccountService bankAccountService,
                               PersonalDataService personalDataService,
                               LegalEntityService legalEntityService,
                               StaffService staffService) {
        this.addressService = addressService;
        this.bankAccountService = bankAccountService;
        this.personalDataService = personalDataService;
        this.legalEntityService = legalEntityService;
        this.staffService = staffService;
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

    @Transactional
    public String registerStaff(PersonalData personalData, String principal) {
        Address address = addressService.addAddress(personalData.getAddress());
        personalData.setAddress(address);
        personalDataService.addPersonalData(personalData);
        bankAccountService.addBankAccount(personalData.getBankAccount());
        Staff staff = new Staff();

        PersonalData principalPersonalData = personalDataService.getByUserName(principal);
        LegalEntity legalEntity = legalEntityService.getLegalEntity(principalPersonalData);

        staff.setEmployer(legalEntity);
        staff.setPersonalData(personalData);
        try {
            staffService.addStaff(staff);
        } catch (OperationNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return personalData.getUserName();
    }
}
