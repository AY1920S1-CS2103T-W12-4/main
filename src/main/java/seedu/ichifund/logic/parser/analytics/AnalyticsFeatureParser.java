package seedu.ichifund.logic.parser.analytics;

import static seedu.ichifund.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import seedu.ichifund.logic.commands.Command;
import seedu.ichifund.logic.commands.analytics.BalanceTrendCommand;
import seedu.ichifund.logic.commands.analytics.ExpenditureTrendCommand;
import seedu.ichifund.logic.commands.analytics.IncomeTrendCommand;
import seedu.ichifund.logic.parser.FeatureParser;
import seedu.ichifund.logic.parser.exceptions.ParseException;

/**
 * Passes user input to the appropriate Parser for commands related to the analytics feature.
 */
public class AnalyticsFeatureParser implements FeatureParser {

    private final int tabIndex = 4;

    @Override
    public String getTabSwitchCommandWord() {
        return "analytics";
    }

    @Override
    public int getTabIndex() {
        return tabIndex;
    }

    @Override
    public Command parseCommand(String commandWord, String arguments) throws ParseException {
        switch(commandWord) {

        case ExpenditureTrendCommand.COMMAND_WORD:
            return new ExpenditureTrendCommandParser().parse(arguments);

        case IncomeTrendCommand.COMMAND_WORD:
            return new IncomeTrendCommandParser().parse(arguments);

        case BalanceTrendCommand.COMMAND_WORD:
            return new BalanceTrendCommandParser().parse(arguments);


        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}