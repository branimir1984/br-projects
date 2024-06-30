package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.enums.Role;
import bg.codeacademy.cakeShop.error_handling.exception.RoleNotSupportedException;
import bg.codeacademy.cakeShop.model.*;
import bg.codeacademy.cakeShop.repository.StaffRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.LinkedList;
import java.util.List;
import static org.mockito.Mockito.*;

class StaffServiceTest {

    private static StaffService staffService;
    private static final StaffRepository staffRepository = mock(StaffRepository.class);
    private static final AddressService addressService = mock(AddressService.class);
    private static final BankAccountService bankAccountService = mock(BankAccountService.class);
    private static final PersonalDataService personalDataService = mock(PersonalDataService.class);
    private static final LegalEntityService legalEntityService = mock(LegalEntityService.class);
    private static final List<String> staffRoles =
            new LinkedList<>(List.of("MANAGER", "WORKER"));
    private static final List<String> legalUserRole =
            new LinkedList<>(List.of("SHOP", "RENTIER", "DELIVER"));

    @BeforeAll
    public static void setup() {
        staffService = new StaffService(addressService
                , bankAccountService
                , personalDataService
                , legalEntityService
                , staffRepository);
        ReflectionTestUtils.setField(staffService, "roles", staffRoles);
    }

    @Test
    void shouldAddStaff() {
        Staff staff = formStaff();
        LegalEntity legalEntity = formLegalEntity();
        for (int i = 0; i < staffRoles.size(); i++) {
            staff.getPersonalData().setUserRole(Role.valueOf(staffRoles.get(i)));

            when(addressService.addAddress(any(Address.class)))
                    .thenReturn(staff.getPersonalData().getAddress());
            when(personalDataService.getByUserName(any(String.class)))
                    .thenReturn(staff.getPersonalData());
            when(legalEntityService.getLegalEntity(any(PersonalData.class)))
                    .thenReturn(legalEntity);

            Staff response = staffService.addStaff(staff.getPersonalData(), "principal");

            verify(addressService, times(i + 1))
                    .addAddress(staff.getPersonalData().getAddress());
            verify(personalDataService
                    , times(i + 1)).addPersonalData(staff.getPersonalData());
            verify(bankAccountService
                    , times(i + 1)).addBankAccount(staff.getPersonalData().getBankAccount());
            verify(personalDataService
                    , times(i + 1)).getByUserName("principal");
            verify(legalEntityService
                    , times(i + 1)).getLegalEntity(staff.getPersonalData());

            verify(staffRepository, times(1)).save(response);
        }
    }

    @Test
    void shouldThrowRoleNotSupportedException() {
        Staff staff = formStaff();
        staff.setEmployer(formLegalEntity());
        for (String legalUserRole : legalUserRole) {
            staff.getPersonalData().setUserRole(Role.valueOf(legalUserRole));
            Assertions.assertThrows(RoleNotSupportedException.class, () -> {
                staffService.addStaff(staff.getPersonalData()
                        , staff.getEmployer().getPersonalData().getUserName());
            });
        }
    }

    private Staff formStaff() {
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
        Staff staff = new Staff();
        staff.setPersonalData(personalData);
        return staff;
    }
    private LegalEntity formLegalEntity() {
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
        legalEntity.setUin("UIN");
        legalEntity.setPersonalData(personalData);
        return legalEntity;
    }
}
