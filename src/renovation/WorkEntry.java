package renovation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Records a single block of work done by a worker on a specific date.
 */
public record WorkEntry(Worker worker, LocalDate date, BigDecimal hoursWorked, String description) {

	public WorkEntry {
		if (worker == null) {
			throw new IllegalArgumentException("Worker cannot be null");
		}
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null");
		}
		if (hoursWorked == null || hoursWorked.signum() <= 0) {
			throw new IllegalArgumentException("Hours worked must be positive");
		}
		if (description == null) {
			description = "";
		}
	}

	/** Calculates amount owed for this entry: hours × hourly rate */
	public BigDecimal amount() {
		return hoursWorked.multiply(worker.getHourlyRate()).setScale(2, RoundingMode.HALF_UP);
	}
}



