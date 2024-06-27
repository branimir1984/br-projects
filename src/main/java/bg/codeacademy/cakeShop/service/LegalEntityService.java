package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.LegalEntityNotFoundException;
import bg.codeacademy.cakeShop.error_handling.exception.UniqueIdentificationNumberExistException;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.repository.LegalEntityRepository;
import org.springframework.stereotype.Service;

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

    public LegalEntity getLegalEntityById(int id) {
        LegalEntity legalEntity = legalEntityRepository.findLegalEntityByPersonalData(id);
        if (legalEntity != null) {
            return legalEntity;
        } else {
            throw new LegalEntityNotFoundException("Legal entity not found! id=" + id);
        }
    }
}
