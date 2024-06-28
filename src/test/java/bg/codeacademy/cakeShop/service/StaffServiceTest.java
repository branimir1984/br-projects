package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.enums.Role;
import bg.codeacademy.cakeShop.model.Address;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.PersonalData;
import bg.codeacademy.cakeShop.model.Staff;
import bg.codeacademy.cakeShop.repository.StaffRepository;
import org.aspectj.util.Reflection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.naming.OperationNotSupportedException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffServiceTest {

    private static StaffService staffService;
    private static final StaffRepository staffRepository = mock(StaffRepository.class);
    private static final List<String> staffRoles =
            new LinkedList<>(List.of("MANAGER", "WORKER"));
    private static final List<String> legalUserRole =
            new LinkedList<>(List.of("SHOP", "RENTIER", "DELIVER"));

    @BeforeAll
    public static void setup() {
        staffService = new StaffService(staffRepository);
        ReflectionTestUtils.setField(staffService, "roles", staffRoles);
    }

    @Test
    void shouldAddStaff() throws OperationNotSupportedException {
        Staff staff = formStaff();
        for (int i = 0; i < staffRoles.size(); i++) {
            staff.getPersonalData().setUserRole(Role.valueOf(staffRoles.get(i)));
            staffService.addStaff(staff);
            verify(staffRepository, times(i + 1)).save(staff);
        }
    }

    @Test
    void shouldThrowOperationNotSupportedException(){
        Staff staff = formStaff();
        for (String legalUserRole : legalUserRole) {
            staff.getPersonalData().setUserRole(Role.valueOf(legalUserRole));
            Assertions.assertThrows(OperationNotSupportedException.class, () -> {
                staffService.addStaff(staff);
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
}