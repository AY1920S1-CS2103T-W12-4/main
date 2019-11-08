package seedu.ichifund.model;

import static java.util.Objects.requireNonNull;
import static seedu.ichifund.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.ichifund.commons.core.GuiSettings;
import seedu.ichifund.commons.core.LogsCenter;
import seedu.ichifund.model.analytics.Data;
import seedu.ichifund.model.budget.Budget;
import seedu.ichifund.model.context.TransactionContext;
import seedu.ichifund.model.date.Date;
import seedu.ichifund.model.date.Day;
import seedu.ichifund.model.date.Month;
import seedu.ichifund.model.date.Year;
import seedu.ichifund.model.repeater.Repeater;
import seedu.ichifund.model.repeater.RepeaterUniqueId;
import seedu.ichifund.model.transaction.Transaction;
import seedu.ichifund.model.transaction.TransactionList;

/**
 * Represents the in-memory model of the fund book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final FundBook fundBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Transaction> filteredTransactions;
    private final FilteredList<Repeater> filteredRepeaters;
    private final FilteredList<Budget> filteredBudgets;
    private final FilteredList<Data> datas;

    private SimpleObjectProperty<TransactionContext> transactionContext;

    /**
     * Initializes a ModelManager with the given fundBook and userPrefs.
     */
    public ModelManager(ReadOnlyFundBook fundBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(fundBook, userPrefs);

        logger.fine("Initializing with fund book: " + fundBook + " and user prefs " + userPrefs);

        this.fundBook = new FundBook(fundBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredTransactions = new FilteredList<>(this.fundBook.getTransactionList());
        filteredRepeaters = new FilteredList<>(this.fundBook.getRepeaterList());
        filteredBudgets = new FilteredList<>(this.fundBook.getBudgetList());
        TransactionContext transactionContext = new TransactionContext(this.fundBook.getLatestTransaction());
        updateFilteredTransactionList(transactionContext.getPredicate());
        this.transactionContext = new SimpleObjectProperty<>(transactionContext);
        datas = new FilteredList<>(this.fundBook.getDataList());
        setTransactionContext(new TransactionContext(this.fundBook.getLatestTransaction()));
    }

    public ModelManager() {
        this(new FundBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getFundBookFilePath() {
        return userPrefs.getFundBookFilePath();
    }

    @Override
    public void setFundBookFilePath(Path fundBookFilePath) {
        requireNonNull(fundBookFilePath);
        userPrefs.setFundBookFilePath(fundBookFilePath);
    }

    //=========== FundBook ================================================================================

    @Override
    public void setFundBook(ReadOnlyFundBook fundBook) {
        this.fundBook.resetData(fundBook);
    }

    @Override
    public ReadOnlyFundBook getFundBook() {
        return fundBook;
    }

    @Override
    public RepeaterUniqueId getCurrentRepeaterUniqueId() {
        return fundBook.getCurrentRepeaterUniqueId();
    }

    @Override
    public void setCurrentRepeaterUniqueId(RepeaterUniqueId uniqueId) {
        fundBook.setCurrentRepeaterUniqueId(uniqueId);
    }

    @Override
    public void deleteTransaction(Transaction target) {
        fundBook.removeTransaction(target);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        fundBook.addTransaction(transaction);
    }

    @Override
    public void setTransaction(Transaction target, Transaction editedTransaction) {
        requireAllNonNull(target, editedTransaction);

        fundBook.setTransaction(target, editedTransaction);
    }

    @Override
    public ObservableList<Transaction> getAssociatedTransactions(RepeaterUniqueId repeaterUniqueId) {
        TransactionList list = new TransactionList();
        for (Transaction transaction : fundBook.getTransactionList()) {
            if (transaction.getRepeaterUniqueId().equals(repeaterUniqueId)) {
                list.add(transaction);
            }
        }
        return list.asUnmodifiableObservableList();
    }

    @Override
    public boolean hasRepeater(Repeater repeater) {
        requireNonNull(repeater);
        return fundBook.hasRepeater(repeater);
    }

    @Override
    public void deleteRepeater(Repeater target) {
        fundBook.removeRepeater(target);
    }

    @Override
    public void deleteRepeaterTransactions(RepeaterUniqueId repeaterUniqueId) {
        for (Transaction transaction : getAssociatedTransactions(repeaterUniqueId)) {
            deleteTransaction(transaction);
        }
    }

    @Override
    public void addRepeater(Repeater repeater) {
        fundBook.addRepeater(repeater);
        updateFilteredRepeaterList(PREDICATE_SHOW_ALL_REPEATERS);
    }

    @Override
    public void createRepeaterTransactions(Repeater repeater) {
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
                addTransaction(transaction);
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
                addTransaction(transaction);
            }

            currentMonth++;
            if (currentMonth == 13) {
                currentMonth = 1;
                currentYear++;
            }
        }
    }

    @Override
    public void setRepeater(Repeater target, Repeater editedRepeater) {
        requireAllNonNull(target, editedRepeater);

        fundBook.setRepeater(target, editedRepeater);
    }

    @Override
    public boolean hasBudget(Budget budget) {
        requireNonNull(budget);
        return fundBook.hasBudget(budget);
    }

    @Override
    public void deleteBudget(Budget target) {
        fundBook.removeBudget(target);
    }

    @Override
    public void addBudget(Budget budget) {
        fundBook.addBudget(budget);
        updateFilteredBudgetList(PREDICATE_SHOW_ALL_BUDGETS);
    }

    @Override
    public void setBudget(Budget target, Budget editedBudget) {
        requireAllNonNull(target, editedBudget);

        fundBook.setBudget(target, editedBudget);
    }


    //=========== Filtered Transaction List Accessors =============================================================

    @Override
    public ObservableList<Transaction> getFilteredTransactionList() {
        return filteredTransactions;
    }

    @Override
    public TransactionContext getTransactionContext() {
        return transactionContext.getValue();
    }

    @Override
    public void setTransactionContext(TransactionContext transactionContext) {
        this.transactionContext.setValue(transactionContext);
        updateFilteredTransactionList(transactionContext.getPredicate());
    }

    @Override
    public void updateTransactionContext(Transaction transaction) {
        TransactionContext newContext = getTransactionContext().getAccommodatingContext(transaction);
        setTransactionContext(newContext);
    }

    /**
     * Updates the filter of the filtered transaction list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    private void updateFilteredTransactionList(Predicate<Transaction> predicate) {
        requireNonNull(predicate);
        filteredTransactions.setPredicate(predicate);
    }

    public ObservableValue<TransactionContext> getTransactionContextProperty() {
        return transactionContext;
    }

    //=========== Filtered Repeater List Accessors =============================================================

    @Override
    public ObservableList<Repeater> getFilteredRepeaterList() {
        return filteredRepeaters;
    }

    @Override
    public void updateFilteredRepeaterList(Predicate<Repeater> predicate) {
        requireNonNull(predicate);
        filteredRepeaters.setPredicate(predicate);
    }

    //=========== Filtered Budget List Accessors =============================================================

    @Override
    public ObservableList<Budget> getFilteredBudgetList() {
        return filteredBudgets;
    }

    @Override
    public void updateFilteredBudgetList(Predicate<Budget> predicate) {
        requireNonNull(predicate);
        filteredBudgets.setPredicate(predicate);
    }

    //=========== Analytics Accessors =============================================================
    @Override
    public ObservableList<Data> getDataList() {
        return datas;
    }

    @Override
    public void updateDataList(List<Data> datas) {
        requireNonNull(datas);
        fundBook.setData(datas);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;

        return fundBook.equals(other.fundBook)
                && userPrefs.equals(other.userPrefs)
                && filteredTransactions.equals(other.filteredTransactions)
                && filteredRepeaters.equals(other.filteredRepeaters)
                && filteredBudgets.equals(other.filteredBudgets)
                && datas.equals(other.datas)
                && transactionContext.getValue().equals(other.transactionContext.getValue());
    }

}
