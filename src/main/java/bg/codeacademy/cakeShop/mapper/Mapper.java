package bg.codeacademy.cakeShop.mapper;

import bg.codeacademy.cakeShop.dto.*;
import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.enums.Role;
import bg.codeacademy.cakeShop.model.*;
import org.springframework.stereotype.Component;

import java.util.*;

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

    public LegalEntityResponse mapLegalEntityToResponse(LegalEntity legalEntity) {
        List<BankAccount> bankAccountList = legalEntity.getPersonalData().getBankAccount();
        List<BankAccountDTO> bankAccountDTOList = new LinkedList<>();
        for (BankAccount bAcc : bankAccountList) {
            BankAccountDTO bAccDto = new BankAccountDTO(
                    bAcc.getIban(),
                    bAcc.getAmount(),
                    String.valueOf(bAcc.getCurrency()),
                    bAcc.isRental()
            );
            bankAccountDTOList.add(bAccDto);
        }
        Address address = legalEntity.getPersonalData().getAddress();
        AddressDTO addressDTO = new AddressDTO(address.getCity(), address.getStreet());
        PersonalData pd = legalEntity.getPersonalData();
        PersonalDataDTO personalData = new PersonalDataDTO(
                pd.getUserName(),
                "",
                String.valueOf(pd.getUserRole()),
                pd.getPersonalName(),
                addressDTO,
                bankAccountDTOList
        );
        return new LegalEntityResponse(
                legalEntity.getId(),
                legalEntity.getEmail(),
                legalEntity.getUin(),
                personalData);
    }

    public List<LegalEntityResponse> mapLegalEntityToResponseList(List<LegalEntity> legalEntities) {
        List<LegalEntityResponse> responseList = new ArrayList<>();
        for (LegalEntity le : legalEntities) {
            LegalEntityResponse response = mapLegalEntityToResponse(le);
            responseList.add(response);
        }
        return responseList;
    }

    public Map<String, List<OfferResponceDTO>> mapToOfferResponceDTOList(Map<String, List<Offer>> offers) {

        Map<String, List<OfferResponceDTO>> dtoResponse = new HashMap<>();

        for (Map.Entry<String, List<Offer>> entry : offers.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
            List<OfferResponceDTO> list = new ArrayList<>();

            for (Offer of : entry.getValue()) {
                OfferResponceDTO offerResponse = new OfferResponceDTO(
                        of.getMoney(),
                        of.getOfferor().getUin(),
                        of.getOfferor().getPersonalData().getPersonalName(),
                        of.getOfferor().getEmail()
                );
                list.add(offerResponse);
            }
            dtoResponse.put(entry.getKey(), list);
        }
        return dtoResponse;
    }

    public List<ScheduleTransactionDTO> mapToScheduleTransactionDTOList(List<ScheduleTransaction> transactionList) {
        List<ScheduleTransactionDTO> scheduleTransactionDTOS = new ArrayList<>();
        for (ScheduleTransaction account : transactionList) {
            ScheduleTransactionDTO dto = new ScheduleTransactionDTO(
                    account.getSender().getIban(),
                    account.getRecipient().getId(),
                    account.getRecipient().getIban(),
                    account.getAmountPercentage(),
                    account.getTransactionDate());
            scheduleTransactionDTOS.add(dto);
        }
        return scheduleTransactionDTOS;
    }
}
