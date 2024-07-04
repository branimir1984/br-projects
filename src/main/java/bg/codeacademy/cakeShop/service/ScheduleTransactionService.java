package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.BankNotAccountExistException;
import bg.codeacademy.cakeShop.error_handling.exception.InvalidScheduleTransactionException;
import bg.codeacademy.cakeShop.error_handling.exception.ScheduleTransactionExistException;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.ScheduleTransaction;
import bg.codeacademy.cakeShop.repository.ScheduleTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleTransactionService {
    public final ScheduleTransactionRepository scheduleTransactionRepository;
    public final LegalEntityService legalEntityService;

    public ScheduleTransactionService(ScheduleTransactionRepository scheduleTransactionRepository, LegalEntityService legalEntityService) {
        this.scheduleTransactionRepository = scheduleTransactionRepository;
        this.legalEntityService = legalEntityService;
    }

    public ScheduleTransaction createScheduleTransaction(
            int senderId,
            String senderBankAccountIban,
            int recipientId,
            String recipientBankAccountIban,
            int amountPercentage,
            Date transactionTime) {

        if (senderId == recipientId) {
            throw new InvalidScheduleTransactionException("The sender and recipient can ton be same!");
        }
        LegalEntity sender = legalEntityService.getLegalEntity(senderId);
        LegalEntity recipient = legalEntityService.getLegalEntity(recipientId);
        BankAccount senderBankAccount = getBankAccount(sender, senderBankAccountIban,
                senderBankAccountIban, " not exist for sender!");
        BankAccount recipientBankAccount = getBankAccount(recipient, recipientBankAccountIban,
                senderBankAccountIban, " not exist for recipient!");
        ScheduleTransaction transaction = new ScheduleTransaction();
        transaction.setSender(senderBankAccount);
        transaction.setRecipient(recipientBankAccount);
        transaction.setTransactionTime(transactionTime);
        transaction.setAmountPercentage(amountPercentage);

        if (scheduleTransactionRepository
                .existsScheduleTransactionBySenderAndRecipientAndTransactionTime(senderBankAccount
                        , recipientBankAccount, transactionTime)) {
            throw new ScheduleTransactionExistException("Schedule transaction in time:" + transactionTime + " exist!");
        }
        scheduleTransactionRepository.save(transaction);
        return transaction;
    }

    private static BankAccount getBankAccount(LegalEntity sender, String senderBankAccountIban
            , String senderBankAccountIban1, String x) {
        List<BankAccount> senderBankAccounts = sender.getPersonalData().getBankAccount();
        BankAccount senderBankAccount = null;
        for (BankAccount account : senderBankAccounts) {
            if (account.getIban().equals(senderBankAccountIban)) {
                senderBankAccount = account;
                break;
            }
        }
        if (senderBankAccount == null) {
            throw new BankNotAccountExistException("Bank account with IBAN:" + senderBankAccountIban1
                    + x);
        }
        return senderBankAccount;
    }
}
