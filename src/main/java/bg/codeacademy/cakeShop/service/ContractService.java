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
import org.springframework.stereotype.Service;

import java.util.List;

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
            throw new InvalidContractException("Offeror and recipient UIN can not be same!");
        }

        if (contractRepository.existsContractByOfferorAndRecipient(offeror, recipient)) {
            throw new UniqueIdentificationNumberExistException("Contract between uin:" + offeror.getUin() + " and "
                    + recipient.getUin() + " has already been created!");
        }
        String ident = offeror.getPersonalData().getUserRole() + "-" +
                recipient.getPersonalData().getUserRole() + "-" + offeror.getPersonalData().getId();
        Contract contract = new Contract();
        contract.setIdentifier(ident);
        contract.setAmount(amount);
        contract.setCurrency(Currency.valueOf(currency));
        contract.setOfferor(offeror);
        contract.setRecipient(recipient);
        contract.setStatus(Status.PENDING);
        contractRepository.save(contract);
        return contract;
    }

    @Transactional
    public Contract validateContract(int id, String identifier) {
        Contract contract = contractRepository.findContractByIdentifier(identifier);
        if (contract == null) {
            throw new ContractNotFoundException("Contract with identifier:" + identifier + " not found!");
        }
        LegalEntity legalEntity = legalEntityService.getLegalEntity(id);
        List<Contract> recipientContracts = legalEntity.getContractsToMe();
        if (!recipientContracts.contains(contract)) {
            throw new ContractNotFoundException("User with UIN:" + legalEntity.getUin()
                    + " have no contract with identifier:" + identifier);
        }
        if (contract.getStatus().equals(Status.PENDING)) {
            contract.setStatus(Status.SIGNED);
        } else {
            throw new ContractAlreadyValidatedException("Contract is already validated");
        }
        contractRepository.save(contract);
        return contract;
    }
}
