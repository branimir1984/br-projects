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
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.LocalDateTime.now;

@Service
public class ContractService {
    public final ContractRepository contractRepository;
    public final LegalEntityService legalEntityService;

    @Value("${spring.mail.username}")
    private String email;
    @Autowired
    EmailService emailService;

    public ContractService(ContractRepository contractRepository, LegalEntityService legalEntityService) {
        this.contractRepository = contractRepository;
        this.legalEntityService = legalEntityService;
    }


    public Contract createContract(int principal, float amount, String currency, String recipientUin) throws MessagingException {
        LegalEntity offeror = legalEntityService.getLegalEntity(principal);
        LegalEntity recipient = legalEntityService.getLegalEntity(recipientUin);
        if (offeror.getUin().equals(recipient.getUin())) {
            throw new InvalidContractException("Offeror and recipient UIN can not be same!");
        }
        Contract contract1 = contractRepository.findContractByOfferorAndRecipientAndStatus(offeror, recipient, Status.SIGNED);
        if (contract1 != null) {
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
//        emailService.sendSimpleMessage(contract.getRecipient().getEmail(),"Contract","This is a contract " +
//                "sent from an offeror with an email: "+contract.getOfferor().getEmail()+"!/n"+
//                "If you are pleased with the details of contract with identifier: "+contract.getIdentifier() +
//                ", please validate it!"+ contract.getRecipient().getPersonalData().getPersonalName());
        String htmlBody = emailService.generateHtmlContentCreateContract(
                contract.getRecipient().getPersonalData().getPersonalName(), contract.getOfferor().getEmail(),
                contract.getIdentifier());
        emailService.sendHtmlMessage(contract.getRecipient().getEmail(), "Contract", htmlBody);
        return contract;
    }

    @Transactional
    public Contract validateContract(int id, String identifier) throws MessagingException {
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

//        emailService.sendSimpleMessage(email,"Contract validation", "Contract with identifier:"+
//                contract.getIdentifier()+ " has been validated!");
//        emailService.sendSimpleMessage(contract.getRecipient().getEmail(),"Contract validation",
//                "Contract with identifier:"+ contract.getIdentifier()+ " has been validated!");
        String htmlBody = emailService.generateHtmlContentValidateContract(
                contract.getRecipient().getPersonalData().getPersonalName(),contract.getOfferor().getEmail(),
                contract.getIdentifier());
        emailService.sendHtmlMessage(contract.getRecipient().getEmail(),"Contract validation", htmlBody);
        emailService.sendHtmlMessage(email,"Contract validation", htmlBody);
        return contract;
    }
}
