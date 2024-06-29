package bg.codeacademy.cakeShop.controller;

import bg.codeacademy.cakeShop.dto.LegalEntityRegistrationDTO;
import bg.codeacademy.cakeShop.dto.PersonalDataDTO;
import bg.codeacademy.cakeShop.mapper.Mapper;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.PersonalData;
import bg.codeacademy.cakeShop.service.RegistrationService;
import bg.codeacademy.cakeShop.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ver1/staff")
public class StaffController {
    private final StaffService staffService;
    private final Mapper mapper;

    public StaffController(StaffService staffService, Mapper mapper) {
        this.staffService = staffService;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/save")
    public ResponseEntity<String> save(@RequestBody PersonalDataDTO dto) {
        PersonalData personalData = mapper.mapToPersonalData(dto);
        return new ResponseEntity<>(staffService.addStaff(
                personalData, getPrincipalName()) +
                " successfully registered as " + dto.role() + " with employer:" + getPrincipalName(), HttpStatus.CREATED);
    }

    public String getPrincipalName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
