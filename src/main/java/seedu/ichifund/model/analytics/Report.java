package seedu.ichifund.model.analytics;

import seedu.ichifund.model.Model;
import seedu.ichifund.model.analytics.exceptions.ReportException;
import seedu.ichifund.model.date.Month;
import seedu.ichifund.model.date.Year;
import seedu.ichifund.model.transaction.Category;

/**
 * Represents a report with hidden internal logic and the ability to be generated.
 */
public abstract class Report {

    private final Month month;
    private final Year year;

    /**
     * Constructs a {@code Report}.
     *
     * @param month A month.
     * @param year A year.
     */
    public Report(Month month, Year year) {
        this.month = month;
        this.year = year;
    }

    /**
     * Generates the report.
     *
     * @param model {@code Model} which the report should be based on.
     * @return feedback message of the operation result for display
     * @throws ReportException If an error occurs during report generation.
     */
    public abstract ReportException generate(Model model) throws ReportException;
}
