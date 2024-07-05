package bg.codeacademy.cakeShop.repository;

import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.ScheduleTransaction;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface ScheduleTransactionRepository extends CrudRepository<ScheduleTransaction, Integer> {
    boolean existsScheduleTransactionBySenderAndRecipientAndTransactionDate(
            BankAccount sender,
            BankAccount recipient,
            LocalDateTime transactionTime);
}
