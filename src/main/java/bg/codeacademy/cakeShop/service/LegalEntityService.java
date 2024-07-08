package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.dto.ContractDTO;
import bg.codeacademy.cakeShop.error_handling.exception.LegalEntityNotFoundException;
import bg.codeacademy.cakeShop.error_handling.exception.RoleNotSupportedException;
import bg.codeacademy.cakeShop.error_handling.exception.UniqueIdentificationNumberExistException;
import bg.codeacademy.cakeShop.model.*;
import bg.codeacademy.cakeShop.repository.LegalEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public LegalEntity createLegalEntity(LegalEntity legalEntity) {
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
        bankAccountService.createBankAccount(legalEntity.getPersonalData().getBankAccount());

        legalEntityRepository.save(legalEntity);
        return legalEntity;
    }

    public LegalEntity getLegalEntity(int id) {
        LegalEntity legalEntity = legalEntityRepository.findLegalEntityByPersonalData_id(id);
        if (legalEntity != null) {
            return legalEntity;
        } else {
            throw new LegalEntityNotFoundException("Legal entity with id:" + id + " not found!");
        }
    }

    public LegalEntity getLegalEntity(String uin) {
        LegalEntity legalEntity = legalEntityRepository.findLegalEntityByUin(uin);
        if (legalEntity != null) {
            return legalEntity;
        } else {
            throw new LegalEntityNotFoundException("Legal entity with uin:" + uin + " not found!");
        }
    }

    public List<LegalEntity> getLegalEntities() {
        return (List<LegalEntity>) legalEntityRepository.findAll();
    }

    public Map<String, List<Offer>> getOffers(int id) {
        LegalEntity entity = getLegalEntity(id);
        Map<String, List<Offer>> allTypeOffers = new HashMap<>();
        allTypeOffers.put("offeror", entity.getOfferors());
        allTypeOffers.put("offered", entity.getOffered());
        return allTypeOffers;
    }

    public List<ScheduleTransaction> getScheduleTransactions(int id) {
        LegalEntity user = getLegalEntity(id);
        List<BankAccount> myBankAccounts = user.getPersonalData().getBankAccount();
        List<ScheduleTransaction> myTransactions = new ArrayList<>();
        System.out.println();
        for (BankAccount account : myBankAccounts) {

            myTransactions.addAll(account.getSender());
        }
        System.out.println(myTransactions.size()
        );
        return myTransactions;
    }

    public Map<String, List<Contract>> getLegalEntityContracts(int id) {
        LegalEntity entity = getLegalEntity(id);
        Map<String, List<Contract>> allContracts = new HashMap<>();
        allContracts.put("contractsFromMe", entity.getContactsFromMe());
        allContracts.put("contractsToMe", entity.getContractsToMe());
        return allContracts;
    }
}
