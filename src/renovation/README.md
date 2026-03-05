# Home Renovation Accounting

Simple accounting data structures for a home renovation project - tracks trades, work entries, payslips, and budget summaries.

## The Task

You have started a major home renovation project. Trades needed are carpenter, plumber, electrician, etc.
Design data structures for simple accounting software to support accounts payable and budget calculation.

### Functional Requirements

1. Trades are paid on an hourly basis
2. Payment cycle is two weeks
3. Convenient way to enter amount of work done by trades
4. Trade's payslip available upon request
5. Total amount already paid and owed, with grand total, available upon request

### Technical Notes

- Data persistence is out of project scope
- All monetary values use `BigDecimal` to avoid floating-point rounding errors

## Class Overview

```
AccountingForHomeRenovation   - Main service: registers workers, logs work, generates payslips & budget summaries
├── Worker                    - A trade worker with name, trade type, and hourly rate (Lombok @Getter)
├── Trade                     - Enum: CARPENTER, PLUMBER, ELECTRICIAN, PAINTER, ROOFER, TILER
├── WorkEntry                 - Record: a single block of work (worker, date, hours, description)
├── PayPeriod                 - Record: a 14-day pay period with overlap detection
├── PaySlip                   - Record: payslip for a worker in a specific pay period
├── BudgetSummary             - Record: immutable snapshot of paid/owed amounts with grand total
└── AccountingDemo            - Demo with sample workflow
```

## Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| **`BigDecimal`** over `double` | Exact decimal arithmetic - no rounding surprises with money |
| **Java Records** for data carriers | Immutable by default, auto-generated `equals()`, `hashCode()`, `toString()`, minimal boilerplate |
| **Lombok** only on `Worker` | `Worker` has an auto-generated UUID `id` field, which prevents it from being a record |
| **Record-style accessor naming** | Custom computed methods (`amount()`, `totalAmount()`, `grandTotal()`) follow the no-`get`-prefix record convention |
| **Validation everywhere** | Null checks and business rule validation in constructors and service methods |
| **Unmodifiable collections** | All returned lists and maps are wrapped to prevent external mutation |

## Validation Rules

- **Worker**: name must not be blank, trade must not be null, hourly rate must be positive
- **WorkEntry**: worker and date must not be null, hours must be positive
- **PayPeriod**: enforces exactly 14-day duration; overlapping periods are rejected
- **logWork()**: date must fall within a registered pay period

## Usage

```java
AccountingForHomeRenovation accounting = new AccountingForHomeRenovation();

// Register workers
Worker john = accounting.registerWorker("John Smith", Trade.CARPENTER, new BigDecimal("45.00"));

// Set up two-week pay periods
PayPeriod period = accounting.addPayPeriod(LocalDate.of(2026, 2, 9));

// Log work
accounting.logWork(john, LocalDate.of(2026, 2, 16), new BigDecimal("8"), "Framing walls");

// Generate payslip
PaySlip slip = accounting.generatePaySlip(john, period);
System.out.printf("Total: $%s, Paid: %b%n", slip.totalAmount(), slip.paid());

// Mark as paid
accounting.markAsPaid(john, period);

// Budget summary
BudgetSummary summary = accounting.getBudgetSummary();
System.out.printf("Paid: $%s | Owed: $%s | Grand Total: $%s%n",
        summary.totalPaid(), summary.totalOwed(), summary.grandTotal());
```

See [`AccountingDemo.java`](AccountingDemo.java) for a full working example.

## Dependencies

- **Java 17+** (records, `Stream.toList()`)
- **Lombok** (used only in `Worker` for `@Getter` and `@EqualsAndHashCode`)

