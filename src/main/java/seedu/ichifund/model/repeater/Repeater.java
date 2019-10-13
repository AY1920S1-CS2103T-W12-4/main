package seedu.ichifund.model.repeater;

import static seedu.ichifund.commons.util.CollectionUtil.requireAllNonNull;

import seedu.ichifund.model.Amount;
import seedu.ichifund.model.Description;
import seedu.ichifund.model.transaction.Category;

/**
 * Represents a Repeater in IchiFund.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Repeater {
    private final Amount amount;
    private final Description description;
    private final Category category;
    private final MonthOffset monthStartOffset;
    private final MonthOffset monthEndOffset;

    public Repeater(Description description, Amount amount, Category category,
            MonthOffset monthStartOffset, MonthOffset monthEndOffset) {
        requireAllNonNull(description, amount, category, monthStartOffset, monthEndOffset);

        this.description = description;
        this.amount = amount;
        this.category = category;
        this.monthStartOffset = monthStartOffset;
        this.monthEndOffset = monthEndOffset;
    }

    public Amount getAmount() {
        return this.amount;
    }

    public Description getDescription() {
        return this.description;
    }

    public Category getCategory() {
        return this.category;
    }

    public MonthOffset getMonthStartOfset() {
        return this.monthStartOffset;
    }

    public MonthOffset getMonthEndOffset() {
        return this.monthEndOffset;
    }

}
