package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.enums.PaymentCriteria;
import bg.codeacademy.cakeShop.error_handling.exception.BankNotAccountExistException;
import bg.codeacademy.cakeShop.error_handling.exception.InvalidScheduleTransactionException;
import bg.codeacademy.cakeShop.error_handling.exception.ScheduleTransactionExistException;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.ScheduleTransaction;
import bg.codeacademy.cakeShop.repository.ScheduleTransactionRepository;
import bg.codeacademy.cakeShop.shedule.TransactionTaskExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleTransactionService {
    public final ScheduleTransactionRepository scheduleTransactionRepository;
    public final LegalEntityService legalEntityService;
    private final ApplicationContext springContext;
    private final TransactionTaskExecutor taskExecutor;

    public ScheduleTransactionService(ScheduleTransactionRepository scheduleTransactionRepository,
                                      LegalEntityService legalEntityService, ApplicationContext springContext) {
        this.scheduleTransactionRepository = scheduleTransactionRepository;
        this.legalEntityService = legalEntityService;
        this.springContext = springContext;
        taskExecutor = springContext.getBean(TransactionTaskExecutor.class);
        taskExecutor.start();
    }

    public ScheduleTransaction createScheduleTransaction(
            int senderId,
            String senderBankAccountIban,
            int recipientId,
            String recipientBankAccountIban,
            int amountPercentage,
            PaymentCriteria paymentCriteria) {

        if (senderId == recipientId) {
            throw new InvalidScheduleTransactionException("The sender and recipient can not be same!");
        }
        LegalEntity sender = legalEntityService.getLegalEntity(senderId);
        LegalEntity recipient = legalEntityService.getLegalEntity(recipientId);
        BankAccount senderBankAccount = getBankAccount(
                sender, senderBankAccountIban, " not exist for sender!");
        BankAccount recipientBankAccount = getBankAccount(
                recipient, recipientBankAccountIban, " not exist for recipient!");
        ScheduleTransaction transaction = new ScheduleTransaction();
        transaction.setSender(senderBankAccount);
        transaction.setRecipient(recipientBankAccount);

        transaction.setPaymentCriteria(paymentCriteria);
        transaction.setAmountPercentage(amountPercentage);

        if (scheduleTransactionRepository
                .existsScheduleTransactionBySenderAndRecipientAndPaymentCriteria(senderBankAccount
                        , recipientBankAccount, paymentCriteria)) {
            throw new ScheduleTransactionExistException("Schedule transaction exist!");
        }
        scheduleTransactionRepository.save(transaction);

        taskExecutor.updateTaskList(scheduleTransactionRepository.findAll());
        return transaction;
    }

    private static BankAccount getBankAccount(LegalEntity sender, String senderBankAccountIban
            , String x) {
        List<BankAccount> senderBankAccounts = sender.getPersonalData().getBankAccount();
        BankAccount senderBankAccount = null;
        for (BankAccount account : senderBankAccounts) {
            if (account.getIban().equals(senderBankAccountIban)) {
                senderBankAccount = account;
                break;
            }
        }
        if (senderBankAccount == null) {
            throw new BankNotAccountExistException("Bank account with IBAN:" + senderBankAccountIban
                    + x);
        }
        return senderBankAccount;
    }
}
