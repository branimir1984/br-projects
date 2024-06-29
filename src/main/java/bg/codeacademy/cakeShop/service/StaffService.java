package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.RoleNotSupportedException;
import bg.codeacademy.cakeShop.model.Address;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.PersonalData;
import bg.codeacademy.cakeShop.model.Staff;
import bg.codeacademy.cakeShop.repository.StaffRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;
import java.util.List;

@Service
public class StaffService {
    private final AddressService addressService;
    private final BankAccountService bankAccountService;
    private final PersonalDataService personalDataService;
    private final LegalEntityService legalEntityService;
    public final StaffRepository staffRepository;
    @Value("#{'${roles.staff}'.split(',')}")
    private List<String> roles;

    public StaffService(AddressService addressService, BankAccountService bankAccountService, PersonalDataService personalDataService, LegalEntityService legalEntityService, StaffRepository staffRepository) {
        this.addressService = addressService;
        this.bankAccountService = bankAccountService;
        this.personalDataService = personalDataService;
        this.legalEntityService = legalEntityService;
        this.staffRepository = staffRepository;
    }

    @Transactional
    public String addStaff(PersonalData personalData, String principal) {
        Address address = addressService.addAddress(personalData.getAddress());
        personalData.setAddress(address);
        personalDataService.addPersonalData(personalData);
        bankAccountService.addBankAccount(personalData.getBankAccount());

        //When we save new Staff must set to it the employer.For that reason here we
        //retrieve the LegalEntity according to the principal.
        PersonalData principalPersonalData = personalDataService.getByUserName(principal);
        LegalEntity legalEntity = legalEntityService.getLegalEntity(principalPersonalData);

        Staff staff = new Staff();
        staff.setEmployer(legalEntity);
        staff.setPersonalData(personalData);

        String role = String.valueOf(staff.getPersonalData().getUserRole());
        if (roles.contains(role)) {
            staffRepository.save(staff);
            return personalData.getUserName();
        } else {
            throw new RoleNotSupportedException("Allowed roles for staff are:" + roles);
        }
    }
}
