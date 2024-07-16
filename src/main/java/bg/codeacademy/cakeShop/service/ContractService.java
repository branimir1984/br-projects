package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.enums.Status;
import bg.codeacademy.cakeShop.error_handling.exception.ContractAlreadyValidatedException;
import bg.codeacademy.cakeShop.error_handling.exception.ContractNotFoundException;
import bg.codeacademy.cakeShop.error_handling.exception.InvalidContractException;
import bg.codeacademy.cakeShop.error_handling.exception.UniqueIdentificationNumberExistException;
import bg.codeacademy.cakeShop.model.Contract;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.repository.ContractRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
public class ContractService {
    public final ContractRepository contractRepository;
    public final LegalEntityService legalEntityService;

    public ContractService(ContractRepository contractRepository, LegalEntityService legalEntityService) {
        this.contractRepository = contractRepository;
        this.legalEntityService = legalEntityService;
    }

    public Contract createContract(int principal, float amount, String currency, String recipientUin) {
        LegalEntity offeror = legalEntityService.getLegalEntity(principal);
        LegalEntity recipient = legalEntityService.getLegalEntity(recipientUin);
        if (offeror.getUin().equals(recipient.getUin())) {
            log.error("Service | Offeror and recipient UIN, can not be same!");
            throw new InvalidContractException("Offeror and recipient UIN, can not be same!");
        }
        Contract contract1 = contractRepository.findContractByOfferorAndRecipientAndStatus(offeror, recipient, Status.SIGNED);
        if (contract1 != null) {
            log.error("Service | Contract between uin:" + offeror.getUin() + " and "
                    + recipient.getUin() + " has already been created!");
            throw new UniqueIdentificationNumberExistException("Contract between uin:" + offeror.getUin() + " and "
                    + recipient.getUin() + " has already been created!");
        }
        Contract contract = new Contract();
        String ident = offeror.getPersonalData().getUserRole() + "-" +
                recipient.getPersonalData().getUserRole() + "-" + now();
        contract.setIdentifier(ident);
        contract.setAmount(amount);
        contract.setCurrency(Currency.valueOf(currency));
        contract.setOfferor(offeror);
        contract.setRecipient(recipient);
        contract.setStatus(Status.PENDING);
        contractRepository.save(contract);
        log.info("Service | Save contract with identifier:" + ident);
        return contract;
    }

    @Transactional
    public Contract validateContract(int id, String identifier) {
        Contract contract = contractRepository.findContractByIdentifier(identifier);
        if (contract == null) {
            log.error("Service | Contract with identifier:" + identifier + " not found!");
            throw new ContractNotFoundException("Contract with identifier:" + identifier + " not found!");
        }
        LegalEntity legalEntity = legalEntityService.getLegalEntity(id);
        List<Contract> recipientContracts = legalEntity.getContractsToMe();
        if (!recipientContracts.contains(contract)) {
            log.error("Service | User with UIN:" + legalEntity.getUin()
                    + " have no contract with identifier:" + identifier);
            throw new ContractNotFoundException("User with UIN:" + legalEntity.getUin()
                    + " have no contract with identifier:" + identifier);
        }
        if (contract.getStatus().equals(Status.PENDING)) {
            contract.setStatus(Status.SIGNED);
            log.info("Service | Set status of contract as SIGNED");
        } else {
            log.error("Service | Contract with identifier:" + identifier + " is already validated");
            throw new ContractAlreadyValidatedException(
                    "Contract with identifier:" + identifier + " is already validated");
        }
        log.info("Service | Save contract with identifier:" + identifier);
        contractRepository.save(contract);
        return contract;
    }

    public Contract getValidatedContract(int principalId, int deliveryId) {
        LegalEntity principal = legalEntityService.getLegalEntity(principalId);
        LegalEntity deliver = legalEntityService.getLegalEntity(deliveryId);
        return contractRepository.findContractByOfferorAndRecipientAndStatus(deliver, principal, Status.SIGNED);
    }
}
