package bg.codeacademy.cakeShop.shedule;


import bg.codeacademy.cakeShop.enums.PaymentCriteria;
import bg.codeacademy.cakeShop.model.ScheduleTransaction;
import bg.codeacademy.cakeShop.service.BankAccountService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionTaskExecutor implements Runnable {
    private final BankAccountService bankAccountService;
    private int dailyExecutionHour = 21;
    private boolean awake = false;
    private final List<ScheduleTransaction> scheduleTransactionList;

    public TransactionTaskExecutor(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
        scheduleTransactionList = new ArrayList<>();
    }

    public synchronized void start() {
        this.awake = true;
        notify();
    }

    public synchronized void stop() {
        this.awake = false;
    }

    @Override
    public synchronized void run() {
        try {
            while (!awake) {
                wait();
            }
            while (awake) {
                LocalDateTime lt = LocalDateTime.now();
                for (ScheduleTransaction transaction : scheduleTransactionList) {
                    if (transaction.getPaymentCriteria().equals(PaymentCriteria.DAILY)) {
                        if (lt.getHour() == dailyExecutionHour) {
                            bankAccountService.executeTransaction(transaction);
                        }
                    } else if (transaction.getPaymentCriteria().equals(PaymentCriteria.MONTHLY)) {
                        LocalDate now = LocalDate.now();
                        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());
                        if (now.equals(lastDay)) {
                            bankAccountService.executeTransaction(transaction);
                        }
                    }
                }
                Thread.sleep(3600000);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted");
        }
    }

    public void updateTaskList(Iterable<ScheduleTransaction> scheduleTransactions) {
        for (ScheduleTransaction transaction : scheduleTransactions) {
            if (!scheduleTransactionList.contains(transaction)) {
                scheduleTransactionList.add(transaction);
            }
        }
    }
}
