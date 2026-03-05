package renovation;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

/**
 * Immutable snapshot of the budget: amounts paid, amounts owed, and grand total.
 */
public record BudgetSummary(
        BigDecimal totalPaid,
        BigDecimal totalOwed,
        Map<Worker, BigDecimal> paidByWorker,
        Map<Worker, BigDecimal> owedByWorker
) {
    public BudgetSummary {
        paidByWorker = Collections.unmodifiableMap(paidByWorker);
        owedByWorker = Collections.unmodifiableMap(owedByWorker);
    }

    public BigDecimal grandTotal() {
        return totalPaid.add(totalOwed);
    }
}

