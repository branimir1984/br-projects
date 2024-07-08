package bg.codeacademy.cakeShop.repository;

import bg.codeacademy.cakeShop.model.Contract;
import bg.codeacademy.cakeShop.model.LegalEntity;
import org.springframework.data.repository.CrudRepository;

public interface ContractRepository extends CrudRepository<Contract, Integer> {
    boolean existsContractByOfferorAndRecipient(LegalEntity offeror, LegalEntity recipient);

    Contract findContractByIdentifier(String identifier);
}
