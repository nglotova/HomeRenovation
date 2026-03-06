package renovation;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Demo showing the full accounting workflow for a home renovation project
 */
public class AccountingDemo {

    public static void main(String[] args) {

        AccountingForHomeRenovation accounting = new AccountingForHomeRenovation();

        // --- 1. Register workers with their hourly rates ---
        Worker john = accounting.registerWorker("John Smith", Trade.CARPENTER, new BigDecimal("45.00"));
        Worker maria = accounting.registerWorker("Maria Garcia", Trade.PLUMBER, new BigDecimal("55.00"));
        Worker alex = accounting.registerWorker("Alex Dibu", Trade.ELECTRICIAN, new BigDecimal("60.00"));

        // --- 2. Set up pay periods ---
        PayPeriod firstPeriod = accounting.addPayPeriod(LocalDate.of(2026, 2, 9));
        accounting.addPayPeriod(LocalDate.of(2026, 2, 23));

        // --- 3. Log work entries ---
        // First pay period
        accounting.logWork(john, LocalDate.of(2026, 2, 16), new BigDecimal("8"), "Framing living room walls");
        accounting.logWork(john, LocalDate.of(2026, 2, 17), new BigDecimal("7.5"), "Installing door frames");
        accounting.logWork(john, LocalDate.of(2026, 2, 18), new BigDecimal("8"), "Cabinet rough-in");

        accounting.logWork(maria, LocalDate.of(2026, 2, 17), new BigDecimal("6"), "Bathroom rough-in plumbing");
        accounting.logWork(maria, LocalDate.of(2026, 2, 19), new BigDecimal("5.5"), "Kitchen sink piping");

        accounting.logWork(alex, LocalDate.of(2026, 2, 18), new BigDecimal("8"), "Main panel upgrade");
        accounting.logWork(alex, LocalDate.of(2026, 2, 20), new BigDecimal("6"), "Living room wiring");

        // Second pay period
        accounting.logWork(john, LocalDate.of(2026, 2, 23), new BigDecimal("8"), "Trim work");
        accounting.logWork(maria, LocalDate.of(2026, 2, 24), new BigDecimal("4"), "Fix leak in guest bath");
        accounting.logWork(alex, LocalDate.of(2026, 2, 25), new BigDecimal("7"), "Lighting fixtures install");

        // --- 4. Generate payslips before payment ---
        PaySlip johnSlip = accounting.generatePaySlip(john, firstPeriod);
        System.out.printf("John's payslip — paid: %b, total: $%s%n",
                johnSlip.paid(), johnSlip.totalAmount());

        // --- 5. Process payment for first pay period ---
        accounting.markAsPaid(john, firstPeriod);
        accounting.markAsPaid(maria, firstPeriod);
        accounting.markAsPaid(alex, firstPeriod);

        // --- 6. Verify payment ---
        System.out.printf("John paid for first period: %b%n", accounting.isPaid(john, firstPeriod));

        // --- 7. Budget summary ---
        BudgetSummary summary = accounting.getBudgetSummary();
        System.out.printf("Total paid:  $%s%n", summary.totalPaid());
        System.out.printf("Total owed:  $%s%n", summary.totalOwed());
        System.out.printf("Grand total: $%s%n", summary.grandTotal());
        summary.paidByWorker().forEach((w, amt) ->
                System.out.printf("  Paid — %s: $%s%n", w.getName(), amt));
        summary.owedByWorker().forEach((w, amt) ->
                System.out.printf("  Owed — %s: $%s%n", w.getName(), amt));
    }
}

