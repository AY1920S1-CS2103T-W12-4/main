package seedu.ichifund.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.ichifund.commons.core.GuiSettings;
import seedu.ichifund.logic.commands.CommandResult;
import seedu.ichifund.logic.commands.exceptions.CommandException;
import seedu.ichifund.logic.parser.exceptions.ParseException;
import seedu.ichifund.model.ReadOnlyFundBook;
import seedu.ichifund.model.budget.Budget;
import seedu.ichifund.model.person.Person;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the FundBook.
     *
     * @see seedu.ichifund.model.Model#getFundBook()
     */
    ReadOnlyFundBook getFundBook();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Budget> getFilteredBudgetList();

    /**
     * Returns the user prefs' fund book file path.
     */
    Path getFundBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}