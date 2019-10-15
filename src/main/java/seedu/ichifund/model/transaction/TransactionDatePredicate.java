package seedu.ichifund.model.transaction;

import seedu.ichifund.model.date.Month;
import seedu.ichifund.model.date.Year;

import java.util.function.Predicate;

public class TransactionDatePredicate implements Predicate<Transaction> {
    private final Month month;
    private final Year year;

    public TransactionDatePredicate(Month month, Year year) {
        this.month = month;
        this.year = year;
    }

    @Override
    public boolean test(Transaction transaction) {
        return transaction.isIn(month) && transaction.isIn(year);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TransactionDatePredicate // instanceof handles nulls
                && month.equals(((TransactionDatePredicate) other).month)
                && year.equals(((TransactionDatePredicate) other).year)); // state check
    }
}