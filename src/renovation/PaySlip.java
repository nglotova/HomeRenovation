package renovation;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * A payslip for a specific worker covering a specific pay period.
 */
public record PaySlip(Worker worker, PayPeriod payPeriod, List<WorkEntry> entries, boolean paid) {

    public PaySlip {
        entries = Collections.unmodifiableList(entries);
    }

    public BigDecimal totalAmount() {
        return entries.stream()
                .map(WorkEntry::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

