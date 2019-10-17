package seedu.ichifund.model;

import static java.util.Objects.requireNonNull;
import static seedu.ichifund.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.ichifund.commons.core.GuiSettings;
import seedu.ichifund.commons.core.LogsCenter;
import seedu.ichifund.model.budget.Budget;
import seedu.ichifund.model.context.TransactionContext;
import seedu.ichifund.model.person.Person;
import seedu.ichifund.model.repeater.Repeater;
import seedu.ichifund.model.transaction.Transaction;

/**
 * Represents the in-memory model of the fund book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final FundBook fundBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Transaction> filteredTransactions;
    private final FilteredList<Repeater> filteredRepeaters;
    private final FilteredList<Budget> filteredBudgets;

    private TransactionContext transactionContext;

    /**
     * Initializes a ModelManager with the given fundBook and userPrefs.
     */
    public ModelManager(ReadOnlyFundBook fundBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(fundBook, userPrefs);

        logger.fine("Initializing with fund book: " + fundBook + " and user prefs " + userPrefs);

        this.fundBook = new FundBook(fundBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.fundBook.getPersonList());
        filteredTransactions = new FilteredList<>(this.fundBook.getTransactionList());
        filteredRepeaters = new FilteredList<>(this.fundBook.getRepeaterList());
        filteredBudgets = new FilteredList<>(this.fundBook.getBudgetList());
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
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return fundBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        fundBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        fundBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        fundBook.setPerson(target, editedPerson);
    }


    @Override
    public void deleteTransaction(Transaction target) {
        fundBook.removeTransaction(target);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        fundBook.addTransaction(transaction);
        updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
    }

    @Override
    public void setTransaction(Transaction target, Transaction editedTransaction) {
        requireAllNonNull(target, editedTransaction);

        fundBook.setTransaction(target, editedTransaction);
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
    public void addRepeater(Repeater repeater) {
        fundBook.addRepeater(repeater);
        updateFilteredRepeaterList(PREDICATE_SHOW_ALL_REPEATERS);
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

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedFundBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }



    //=========== Filtered Repeater List Accessors =============================================================

    @Override
    public ObservableList<Transaction> getFilteredTransactionList() {
        return filteredTransactions;
    }

    @Override
    public TransactionContext getTransactionContext() {
        return transactionContext;
    }

    @Override
    public void setTransactionContext(TransactionContext transactionContext) {
        this.transactionContext = transactionContext;
        updateFilteredTransactionList(transactionContext.getPredicate());
    }

    @Override
    public void updateFilteredTransactionList(Predicate<Transaction> predicate) {
        requireNonNull(predicate);
        filteredTransactions.setPredicate(predicate);
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
                && filteredPersons.equals(other.filteredPersons);
    }

}
