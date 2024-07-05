package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.BankNotAccountExistException;
import bg.codeacademy.cakeShop.error_handling.exception.InvalidScheduleTransactionException;
import bg.codeacademy.cakeShop.error_handling.exception.ScheduleTransactionExistException;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.ScheduleTransaction;
import bg.codeacademy.cakeShop.repository.ScheduleTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleTransactionService {
    public final ScheduleTransactionRepository scheduleTransactionRepository;
    public final LegalEntityService legalEntityService;
    private final List<ScheduleTransaction> scheduleTransactionList;

    public ScheduleTransactionService(ScheduleTransactionRepository scheduleTransactionRepository,
                                      LegalEntityService legalEntityService) {
        this.scheduleTransactionRepository = scheduleTransactionRepository;
        this.legalEntityService = legalEntityService;
        scheduleTransactionList = new ArrayList<>();
    }

    public ScheduleTransaction createScheduleTransaction(
            int senderId,
            String senderBankAccountIban,
            int recipientId,
            String recipientBankAccountIban,
            int amountPercentage,
            LocalDateTime transactionDate) {

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

        transaction.setTransactionDate(transactionDate);
        transaction.setAmountPercentage(amountPercentage);

        if (scheduleTransactionRepository
                .existsScheduleTransactionBySenderAndRecipientAndTransactionDate(senderBankAccount
                        , recipientBankAccount, transactionDate)) {
            throw new ScheduleTransactionExistException("Schedule transaction in time:" + transactionDate + " exist!");
        }
        System.out.println(transaction.getTransactionDate());
        scheduleTransactionRepository.save(transaction);

        updateScheduleTransactionTaskList();
        System.out.println(scheduleTransactionList.size());
        for (ScheduleTransaction transaction1 : scheduleTransactionList) {
            System.out.println(transaction1);
        }
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

    public void updateScheduleTransactionTaskList() {
        Iterable<ScheduleTransaction> scheduleTransactions = scheduleTransactionRepository.findAll();
        for (ScheduleTransaction transaction : scheduleTransactions) {
            if (!scheduleTransactionList.contains(transaction)) {
                scheduleTransactionList.add(transaction);
            }
        }
    }
}
