package renovation;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
public class Worker {
	private final String id;
	private final String name;
	private final Trade trade;
	private final BigDecimal hourlyRate;

	public Worker(String name, Trade trade, BigDecimal hourlyRate) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (trade == null) {
            throw new IllegalArgumentException("Trade cannot be null");
        }
        if (hourlyRate == null || hourlyRate.signum() <= 0) {
            throw new IllegalArgumentException("Hourly rate must be positive");
        }

		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.trade = trade;
		this.hourlyRate = hourlyRate;
	}
}

