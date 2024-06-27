package bg.codeacademy.cakeShop.mapper;

import bg.codeacademy.cakeShop.dto.BankAccountDTO;
import bg.codeacademy.cakeShop.dto.LegalEntityRegistrationDTO;
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
        Address address = new Address();
        address.setCity(dto.personalData().address().city());
        address.setStreet(dto.personalData().address().street());

        PersonalData personalData = new PersonalData();
        personalData.setUserName(dto.personalData().userName());
        personalData.setUserPassword(dto.personalData().password());
        personalData.setUserRole(Role.valueOf(dto.personalData().role()));
        personalData.setAddress(address);
        personalData.setPersonalName(dto.personalData().personalName());

        List<BankAccountDTO> dtoList = dto.personalData().bankAccount();
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
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setUin(dto.uin());
        legalEntity.setEmail(dto.email());
        legalEntity.setPersonalData(personalData);
        return legalEntity;
    }
}
