package renovation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * The task - Data structures for Home Renovation Project
 *
 * You have started a major home renovation project. Trades needed are carpenter, plumber, electrician etc.
 * Please design data structures for simple accounting software to support accounts payable and budget calculation.
 *
 * Functional requirements:
 * 1. Trades are paid on hour basis.
 * 2. Payment cycle is two weeks
 * 3. Convenient way to enter amount of work done by trades should be supported
 * 4. Trade's payslip should be available upon request
 * 5. Total amount of already paid and owe with grand total should be available upon request
 *
 * Technical notes: data persistence is out of project scope.
 */
public class AccountingForHomeRenovation {

    private final List<Worker> workers = new ArrayList<>();
    private final List<WorkEntry> workEntries = new ArrayList<>();
    private final List<PayPeriod> payPeriods = new ArrayList<>();
    private final Set<PaySlipKey> paidPaySlips = new HashSet<>();

    // ---Worker Management ---

    public Worker registerWorker(String name, Trade trade, BigDecimal hourlyRate) {
        Worker worker = new Worker(name, trade, hourlyRate);
        workers.add(worker);
        return worker;
    }

    public List<Worker> getWorkers() {
        return Collections.unmodifiableList(workers);
    }

    // --- Pay Period Management --

    public PayPeriod addPayPeriod(LocalDate start) {
        PayPeriod period = new PayPeriod(start);
        boolean overlaps = payPeriods.stream().anyMatch(existing -> existing.overlaps(period));
        if (overlaps) {
            throw new IllegalArgumentException(
                    "Pay period starting " + start + " overlaps with an existing period");
        }
        payPeriods.add(period);
        return period;
    }

    public List<PayPeriod> getPayPeriods() {
        return Collections.unmodifiableList(payPeriods);
    }

    // --- Work Entry (Requirement #3: convenient way to enter work) ---

    public void logWork(Worker worker, LocalDate date, BigDecimal hours, String description) {
        boolean covered = payPeriods.stream().anyMatch(p -> p.contains(date));
        if (!covered) {
            throw new IllegalArgumentException(
                    "Date " + date + " does not fall within any registered pay period");
        }
        workEntries.add(new WorkEntry(worker, date, hours, description));
    }

    // --- Pay Period Queries ---

    /** Get all work entries for a worker in a specific pay period */
    public List<WorkEntry> getEntriesForPeriod(Worker worker, PayPeriod period) {
        return workEntries.stream()
                .filter(e -> e.worker().equals(worker) && period.contains(e.date()))
                .sorted(Comparator.comparing(WorkEntry::date))
                .toList();
    }

    // --- Payslip (Requirement #4) ---

    public PaySlip generatePaySlip(Worker worker, PayPeriod period) {
        List<WorkEntry> entries = getEntriesForPeriod(worker, period);
        boolean paid = paidPaySlips.contains(new PaySlipKey(worker, period));
        return new PaySlip(worker, period, entries, paid);
    }

    // --- Payment Processing (Requirement #2: two-week cycle) ---

    public void markAsPaid(Worker worker, PayPeriod period) {
        paidPaySlips.add(new PaySlipKey(worker, period));
    }

    public boolean isPaid(Worker worker, PayPeriod period) {
        return paidPaySlips.contains(new PaySlipKey(worker, period));
    }

    // --- Budget Summary (Requirement #5) ---

    public BudgetSummary getBudgetSummary() {
        Map<Worker, BigDecimal> paidByWorker = new LinkedHashMap<>();
        Map<Worker, BigDecimal> owedByWorker = new LinkedHashMap<>();

        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalOwed = BigDecimal.ZERO;

        for (Worker worker : workers) {
            BigDecimal workerPaid = BigDecimal.ZERO;
            BigDecimal workerOwed = BigDecimal.ZERO;

            for (PayPeriod period : payPeriods) {
                PaySlip paySlip = generatePaySlip(worker, period);
                BigDecimal periodTotal = paySlip.totalAmount();

                if (periodTotal.signum() == 0) continue;

                if (paySlip.paid()) {
                    workerPaid = workerPaid.add(periodTotal);
                } else {
                    workerOwed = workerOwed.add(periodTotal);
                }
            }

            if (workerPaid.signum() > 0) paidByWorker.put(worker, workerPaid);
            if (workerOwed.signum() > 0) owedByWorker.put(worker, workerOwed);

            totalPaid = totalPaid.add(workerPaid);
            totalOwed = totalOwed.add(workerOwed);
        }

        return new BudgetSummary(totalPaid, totalOwed, paidByWorker, owedByWorker);
    }

    // --- Internal key for tracking paid periods per worker ---

    private record PaySlipKey(String workerId, PayPeriod period) {
        PaySlipKey(Worker worker, PayPeriod period) {
            this(worker.getId(), period);
        }
    }
}

