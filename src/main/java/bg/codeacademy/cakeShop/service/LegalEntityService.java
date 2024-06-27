package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.BankAccountExistException;
import bg.codeacademy.cakeShop.error_handling.exception.UniqueIdentificationNumberExistException;
import bg.codeacademy.cakeShop.model.Address;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.repository.AddressRepository;
import bg.codeacademy.cakeShop.repository.LegalEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class LegalEntityService {
    public final LegalEntityRepository legalEntityRepository;

    public LegalEntityService(LegalEntityRepository legalEntityRepository) {
        this.legalEntityRepository = legalEntityRepository;
    }

    public String addLegalEntity(LegalEntity legalEntity) {
        if (legalEntityRepository.existsLegalEntityByUin(legalEntity.getUin())) {
            throw new UniqueIdentificationNumberExistException("Legal entity with UIN:"
                    + legalEntity.getUin() + " is already exist!");
        }
        legalEntityRepository.save(legalEntity);
        return legalEntity.getUin();
    }
}
