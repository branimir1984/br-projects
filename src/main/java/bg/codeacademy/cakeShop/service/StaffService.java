package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.model.Staff;
import bg.codeacademy.cakeShop.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;
import java.util.Arrays;
import java.util.List;

@Service
public class StaffService {
    public final StaffRepository staffRepository;
    @Value("#{'${roles.staff}'.split(',')}")
    private List<String> roles;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Staff addStaff(Staff staff) throws OperationNotSupportedException {
        String role = String.valueOf(staff.getPersonalData().getUserRole());
        if (roles.contains(role)) {
            staffRepository.save(staff);
            return staff;
        }
        throw new OperationNotSupportedException("Allowed roles for staff are:" + roles);
    }
}
