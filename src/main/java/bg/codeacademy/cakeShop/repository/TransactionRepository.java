package bg.codeacademy.cakeShop.repository;

import bg.codeacademy.cakeShop.model.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    boolean existsTransactionById(int id);
}
