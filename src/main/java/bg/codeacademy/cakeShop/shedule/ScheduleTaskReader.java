/*
package bg.codeacademy.cakeShop.shedule;

import bg.codeacademy.cakeShop.model.ScheduleTransaction;
import bg.codeacademy.cakeShop.service.ScheduleTransactionService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ScheduleTaskReader implements Runnable {

    private final ScheduleTransactionService scheduleTransactionService;
    private boolean awake = false;
    private final List<ScheduleTransaction> scheduleTransactionList;

    public ScheduleTaskReader(ScheduleTransactionService scheduleTransactionService) {
        this.scheduleTransactionService = scheduleTransactionService;
        scheduleTransactionList = new ArrayList<>();
        wakeUp();
    }

    public synchronized void wakeUp() {
        this.awake = true;
        notify();
    }

    @Override
    public synchronized void run() {
        try {
            while (!awake) {
                wait();
            }
            scheduleTransactionService.updateScheduleTransactionTaskList(scheduleTransactionList);
            System.out.println("Read the schedule transaction table...");
            for (ScheduleTransaction transaction:scheduleTransactionList){
                System.out.println(transaction);
            }
            awake = false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted");
        }
    }
}
*/
