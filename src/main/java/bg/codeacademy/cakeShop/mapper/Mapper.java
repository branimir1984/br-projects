package bg.codeacademy.cakeShop.mapper;

import bg.codeacademy.cakeShop.dto.BankAccountDTO;
import bg.codeacademy.cakeShop.dto.LegalEntityRegistrationDTO;
import bg.codeacademy.cakeShop.dto.PersonalDataDTO;
import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.enums.Role;
import bg.codeacademy.cakeShop.model.Address;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.PersonalData;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class Mapper {
    public LegalEntity mapToLegalEntity(LegalEntityRegistrationDTO dto) {
        PersonalData personalData = mapToPersonalData(dto.personalData());


        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setUin(dto.uin());
        legalEntity.setEmail(dto.email());
        legalEntity.setPersonalData(personalData);
        return legalEntity;
    }

    public PersonalData mapToPersonalData(PersonalDataDTO dto) {
        Address address = new Address();
        address.setCity(dto.address().city());
        address.setStreet(dto.address().street());
        PersonalData personalData = new PersonalData();
        personalData.setUserName(dto.userName());
        personalData.setUserPassword(dto.password());
        personalData.setUserRole(Role.valueOf(dto.role()));
        personalData.setAddress(address);
        personalData.setPersonalName(dto.personalName());
        List<BankAccountDTO> dtoList = dto.bankAccount();
        List<BankAccount> accounts = new LinkedList<>();

        for (BankAccountDTO b : dtoList) {
            BankAccount bankAccount = new BankAccount();
            bankAccount.setIban(b.iban());
            bankAccount.setAmount(b.amount());
            bankAccount.setCurrency(Currency.valueOf(b.currency()));
            bankAccount.setRental(b.isRental());
            bankAccount.setBeneficiary(personalData);
            accounts.add(bankAccount);
        }
        personalData.setBankAccount(accounts);
        return personalData;
    }
}
