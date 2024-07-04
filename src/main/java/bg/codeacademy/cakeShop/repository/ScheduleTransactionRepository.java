package bg.codeacademy.cakeShop.repository;

import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.ScheduleTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface ScheduleTransactionRepository extends CrudRepository<ScheduleTransaction, Integer> {
    boolean existsScheduleTransactionBySenderAndRecipientAndTransactionTime(BankAccount sender,
                                                                            BankAccount recipient,
                                                                            Date transactionTime);
}
