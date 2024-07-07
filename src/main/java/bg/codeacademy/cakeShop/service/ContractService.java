package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.UniqueIdentificationNumberExistException;
import bg.codeacademy.cakeShop.error_handling.exception.UserNameExistException;
import bg.codeacademy.cakeShop.model.Contract;
import bg.codeacademy.cakeShop.repository.ContractRepository;
import org.springframework.stereotype.Service;

@Service
public class ContractService {
    public final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public String addContract(Contract contract) {
        if (contractRepository.existsContractByIdentifier(contract.getIdentifier())) {
            throw new UniqueIdentificationNumberExistException("Contract with the following identification number:"
                    + contract.getIdentifier() + " has already been created!");
        }
        contractRepository.save(contract);
        return contract.getIdentifier();
    }
}
