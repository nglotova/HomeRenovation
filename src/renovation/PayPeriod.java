package renovation;

import java.time.LocalDate;

/**
 * Represents a two-week pay period (14 days starting from a given date).
 */
public record PayPeriod(LocalDate start, LocalDate end) {

	/** Number of days to add to the start date to get the end date (14-day period, inclusive). */
	private static final long PAY_PERIOD_OFFSET = 13;

	public PayPeriod {
		if (start == null) {
			throw new IllegalArgumentException("Start date cannot be null");
		}
		if (!end.equals(start.plusDays(PAY_PERIOD_OFFSET))) {
			throw new IllegalArgumentException(
					"Pay period must be exactly " + (PAY_PERIOD_OFFSET + 1) + " days");
		}
	}

	public PayPeriod(LocalDate start) {
		this(start, start.plusDays(PAY_PERIOD_OFFSET));
	}

	public boolean contains(LocalDate date) {
		if (date == null) {
			throw new IllegalArgumentException("Date cannot be null");
		}
		return !date.isBefore(start) && !date.isAfter(end);
	}

	public boolean overlaps(PayPeriod other) {
		return !this.end.isBefore(other.start) && !other.end.isBefore(this.start);
	}
}

