package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.LegalEntityNotFoundException;
import bg.codeacademy.cakeShop.error_handling.exception.UniqueIdentificationNumberExistException;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.PersonalData;
import bg.codeacademy.cakeShop.repository.LegalEntityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;
import java.util.List;

@Service
public class LegalEntityService {
    public final LegalEntityRepository legalEntityRepository;
    @Value("#{'${roles.legal-entity}'.split(',')}")
    private List<String> roles;

    public LegalEntityService(LegalEntityRepository legalEntityRepository) {
        this.legalEntityRepository = legalEntityRepository;
    }

    public String addLegalEntity(LegalEntity legalEntity) throws OperationNotSupportedException {
        String role = String.valueOf(legalEntity.getPersonalData().getUserRole());
        if (!roles.contains(role)) {
            throw new OperationNotSupportedException("Allowed roles for legal entity are:" + roles);
        }

        if (legalEntityRepository.existsLegalEntityByUin(legalEntity.getUin())) {
            throw new UniqueIdentificationNumberExistException("Legal entity with UIN:"
                    + legalEntity.getUin() + " is already exist!");
        }
       legalEntityRepository.save(legalEntity);
        return legalEntity.getUin();
    }

    public LegalEntity getLegalEntity(PersonalData personalData) {
        LegalEntity legalEntity = legalEntityRepository.findLegalEntityByPersonalData(personalData);
        if (legalEntity != null) {
            return legalEntity;
        } else {
            throw new LegalEntityNotFoundException("Legal entity not found! name=" + personalData.getUserName());
        }
    }
}
