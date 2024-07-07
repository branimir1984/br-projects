package bg.codeacademy.cakeShop.repository;

import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.Contract;
import org.springframework.data.repository.CrudRepository;

public interface ContractRepository extends CrudRepository<Contract, Integer> {
    boolean existsContractByIdentifier(String identifier);

    Contract findContractByIdentifier(String identifier);
}
