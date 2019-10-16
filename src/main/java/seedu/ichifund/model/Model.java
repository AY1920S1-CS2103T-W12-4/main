package seedu.ichifund.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.ichifund.commons.core.GuiSettings;
import seedu.ichifund.model.budget.Budget;
import seedu.ichifund.model.context.TransactionContext;
import seedu.ichifund.model.person.Person;
import seedu.ichifund.model.repeater.Repeater;
import seedu.ichifund.model.transaction.Transaction;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    Predicate<Transaction> PREDICATE_SHOW_ALL_TRANSACTIONS = unused -> true;
    Predicate<Repeater> PREDICATE_SHOW_ALL_REPEATERS = unused -> true;
    Predicate<Budget> PREDICATE_SHOW_ALL_BUDGETS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' fund book file path.
     */
    Path getFundBookFilePath();

    /**
     * Sets the user prefs' fund book file path.
     */
    void setFundBookFilePath(Path fundBookFilePath);

    /**
     * Replaces fund book data with the data in {@code fundBook}.
     */
    void setFundBook(ReadOnlyFundBook fundBook);

    /** Returns the FundBook */
    ReadOnlyFundBook getFundBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the fund book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the fund book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the fund book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the fund book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the fund book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Adds the given transaction.
     */
    void addTransaction(Transaction transaction);

    /**
     * Deletes the given transaction.
     * The repeater must exist in the fund book.
     */
    void deleteTransaction(Transaction target);

    /**
     * Replaces the given transaction {@code target} with {@code editedTransaction}.
     * {@code target} must exist in the fund book.
     */
    void setTransaction(Transaction target, Transaction editedTransaction);

    /** Returns an unmodifiable view of the filtered transaction list */
    ObservableList<Transaction> getFilteredTransactionList();

    /**
     * Updates the filter of the filtered transaction list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredTransactionList(Predicate<Transaction> predicate);

    TransactionContext getTransactionContext();

    void setTransactionContext(TransactionContext transactionContext);

    /**
     * Returns true if a repeater with the same identity as {@code repeater} exists in the fund book.
     */
    boolean hasRepeater(Repeater repeater);

    /**
     * Deletes the given repeater.
     * The repeater must exist in the fund book.
     */
    void deleteRepeater(Repeater target);

    /**
     * Adds the given repeater.
     * {@code repeater} must not already exist in the fund book.
     */
    void addRepeater(Repeater repeater);

    /**
     * Replaces the given repeater {@code target} with {@code editedRepeater}.
     * {@code target} must exist in the fund book.
     * The repeater identity of {@code editedRepeater} must not be the same as another existing repeater in the fund
     * book.
     */
    void setRepeater(Repeater target, Repeater editedRepeater);

    /** Returns an unmodifiable view of the filtered repeater list */
    ObservableList<Repeater> getFilteredRepeaterList();

    /**
     * Updates the filter of the filtered repeater list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredRepeaterList(Predicate<Repeater> predicate);

    /**
     * Returns true if a budget with the same identity as {@code budget} exists in the fund book.
     */
    boolean hasBudget(Budget budget);

    /**
     * Deletes the given budget.
     * The budget must exist in the fund book.
     */
    void deleteBudget(Budget target);

    /**
     * Adds the given budget.
     * {@code budget} must not already exist in the fund book.
     */
    void addBudget(Budget budget);

    /**
     * Replaces the given budget {@code target} with {@code editedBudget}.
     * {@code target} must exist in the fund book.
     * The budget identity of {@code editedBudget} must not be the same as another existing budget in the fund book.
     */
    void setBudget(Budget target, Budget editedBudget);

    /** Returns an unmodifiable view of the filtered budget list */
    ObservableList<Budget> getFilteredBudgetList();

    /**
     * Updates the filter of the filtered budget list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredBudgetList(Predicate<Budget> predicate);

}
