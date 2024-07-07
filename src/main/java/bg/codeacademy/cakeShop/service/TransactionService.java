package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.UniqueIdentificationNumberExistException;
import bg.codeacademy.cakeShop.model.Transaction;
import bg.codeacademy.cakeShop.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    public final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public int addTransaction(Transaction transaction) {
        if (transactionRepository.existsTransactionById(transaction.getId())) {
            throw new UniqueIdentificationNumberExistException("Transaction with the following identification number:"
                    + transaction.getId() + " has already been created!");
        }
        transactionRepository.save(transaction);
        return transaction.getId();
    }
}
