package seedu.ichifund.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.ichifund.model.Description;
import seedu.ichifund.model.FundBook;
import seedu.ichifund.model.ReadOnlyFundBook;
import seedu.ichifund.model.amount.Amount;
import seedu.ichifund.model.budget.Budget;
import seedu.ichifund.model.date.Date;
import seedu.ichifund.model.date.Day;
import seedu.ichifund.model.date.Month;
import seedu.ichifund.model.date.Year;
import seedu.ichifund.model.repeater.MonthOffset;
import seedu.ichifund.model.repeater.Repeater;
import seedu.ichifund.model.repeater.RepeaterUniqueId;
import seedu.ichifund.model.tag.Tag;
import seedu.ichifund.model.transaction.Category;
import seedu.ichifund.model.transaction.Transaction;
import seedu.ichifund.model.transaction.TransactionType;

/**
 * Contains utility methods for populating {@code FundBook} with sample data.
 */
public class SampleDataUtil {
    public static Transaction[] getSampleTransactions() {
        return new Transaction[] {
            new Transaction(new Description("Lunch at Menya Kokoro"), new Amount("14.00"), new Category("Food"),
                    new Date(new Day("2"), new Month("11"), new Year("2019")), new TransactionType("exp"),
                    new RepeaterUniqueId("")),
            new Transaction(new Description("Joker movie ticket"), new Amount("12.00"),
                    new Category("Entertainment"), new Date(new Day("2"), new Month("11"), new Year("2019")),
                    new TransactionType("exp"), new RepeaterUniqueId("")),
            new Transaction(new Description("Dinner at Marche"), new Amount("21.03"), new Category("Food"),
                    new Date(new Day("2"), new Month("11"), new Year("2019")), new TransactionType("exp"),
                    new RepeaterUniqueId("")),
            new Transaction(new Description("Allowance"), new Amount("1337"), new Category("Allowance"),
                    new Date(new Day("1"), new Month("11"), new Year("2019")), new TransactionType("in"),
                    new RepeaterUniqueId("")),
            new Transaction(new Description("Grab to school"), new Amount("21.03"), new Category("Transportation"),
                    new Date(new Day("31"), new Month("10"), new Year("2019")), new TransactionType("exp"),
                    new RepeaterUniqueId("")),
            new Transaction(new Description("LiHo honey green tea"), new Amount("2.80"), new Category("Food"),
                    new Date(new Day("31"), new Month("10"), new Year("2019")), new TransactionType("exp"),
                    new RepeaterUniqueId("")),
            new Transaction(new Description("Lunch at school"), new Amount("4.80"), new Category("Food"),
                    new Date(new Day("31"), new Month("10"), new Year("2019")), new TransactionType("exp"),
                    new RepeaterUniqueId("")),
            new Transaction(new Description("Grab back home"), new Amount("21.01"), new Category("Transportation"),
                    new Date(new Day("30"), new Month("10"), new Year("2019")), new TransactionType("exp"),
                    new RepeaterUniqueId(""))
        };
    }

    public static Repeater[] getSampleRepeaters() {
        return new Repeater[] {
            new Repeater(new RepeaterUniqueId("0"), new Description("Phone bills"), new Amount("65.00"),
                    new Category("Utilities"), new TransactionType("exp"), new MonthOffset("1"),
                    new MonthOffset("-1"), new Date(new Day("1"), new Month("10"), new Year("2019")),
                    new Date(new Day("1"), new Month("12"), new Year("2019"))),
            new Repeater(new RepeaterUniqueId("1"), new Description("Transport Concession"), new Amount("88.00"),
                    new Category("Transportation"), new TransactionType("exp"), new MonthOffset("3"),
                    new MonthOffset("-1"), new Date(new Day("3"), new Month("10"), new Year("2019")),
                    new Date(new Day("1"), new Month("12"), new Year("2019")))
        };
    }

    public static Budget[] getSampleBudgets() {
        return new Budget[] {
            new Budget(new Description("Lose weight gain moolah"), new Amount("300"), new Month("11"),
                    new Year("2019"), new Category("Food")),
            new Budget(new Description("Grab less spend less"), new Amount("200"), null, null,
                    new Category("Transportation")),
            new Budget(new Description("Play hard spend hard"), new Amount("50"), new Month("11"),
                    new Year("2019"), new Category("Entertainment"))
        };
    }

    public static ReadOnlyFundBook getSampleFundBook() {
        FundBook sampleFundBook = new FundBook();
        for (Transaction sampleTransaction : getSampleTransactions()) {
            sampleFundBook.addTransaction(sampleTransaction);
        }

        for (Repeater sampleRepeater : getSampleRepeaters()) {
            sampleFundBook.addRepeater(sampleRepeater);
            createRepeaterTransactions(sampleFundBook, sampleRepeater);
        }
        sampleFundBook.setCurrentRepeaterUniqueId(new RepeaterUniqueId(getSampleRepeaters().length + ""));

        for (Budget sampleBudget : getSampleBudgets()) {
            sampleFundBook.addBudget(sampleBudget);
        }
        return sampleFundBook;
    }

    /**
     * Private method to add transactions to a fund book for a repeater.
     */
    private static void createRepeaterTransactions(FundBook fundBook, Repeater repeater) {
        int currentMonth = repeater.getStartDate().getMonth().monthNumber;
        int currentYear = repeater.getStartDate().getYear().yearNumber;
        int endMonth = repeater.getEndDate().getMonth().monthNumber;
        int endYear = repeater.getEndDate().getYear().yearNumber;

        while ((currentYear < endYear) || (currentYear == endYear && currentMonth <= endMonth)) {
            if (!repeater.getMonthStartOffset().isIgnored()) {
                Transaction transaction = new Transaction(
                        repeater.getDescription(),
                        repeater.getAmount(),
                        repeater.getCategory(),
                        new Date(
                                new Day(repeater.getMonthStartOffset().toString()),
                                new Month(String.valueOf(currentMonth)),
                                new Year(String.valueOf(currentYear))),
                        repeater.getTransactionType(),
                        repeater.getUniqueId());
                fundBook.addTransaction(transaction);
            }

            if (!repeater.getMonthEndOffset().isIgnored()) {
                int daysInMonth;
                if ((new Month(String.valueOf(currentMonth))).has30Days()) {
                    daysInMonth = 30;
                } else if ((new Month(String.valueOf(currentMonth))).has31Days()) {
                    daysInMonth = 31;
                } else if ((new Year(String.valueOf(currentYear))).isLeapYear()) {
                    daysInMonth = 29;
                } else {
                    daysInMonth = 28;
                }

                Transaction transaction = new Transaction(
                        repeater.getDescription(),
                        repeater.getAmount(),
                        repeater.getCategory(),
                        new Date(
                                new Day(String.valueOf(daysInMonth - (repeater.getMonthEndOffset().value - 1))),
                                new Month(String.valueOf(currentMonth)),
                                new Year(String.valueOf(currentYear))),
                        repeater.getTransactionType(),
                        repeater.getUniqueId());
                fundBook.addTransaction(transaction);
            }

            currentMonth++;
            if (currentMonth == 12) {
                currentMonth = 1;
                currentYear++;
            }
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
