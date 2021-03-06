package seedu.ichifund.logic.parser.analytics;

import static seedu.ichifund.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ichifund.logic.parser.CliSyntax.PREFIX_YEAR;

import java.util.Optional;

import seedu.ichifund.logic.commands.analytics.MonthlyExpenditureRankingCommand;
import seedu.ichifund.logic.parser.ArgumentMultimap;
import seedu.ichifund.logic.parser.ArgumentTokenizer;
import seedu.ichifund.logic.parser.Parser;
import seedu.ichifund.logic.parser.ParserUtil;
import seedu.ichifund.logic.parser.exceptions.ParseException;
import seedu.ichifund.model.date.Year;

/**
 * Parses input arguments and creates a new MonthlyExpenditureRankingCommand object
 */
public class MonthlyExpenditureRankingCommandParser implements Parser<MonthlyExpenditureRankingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MonthlyExpenditureRankingCommand
     * and returns an MonthlyExpenditureRankingCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MonthlyExpenditureRankingCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_YEAR);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MonthlyExpenditureRankingCommand.MESSAGE_USAGE));
        }
        Year year = null;

        if (argMultimap.getValue(PREFIX_YEAR).isPresent()) {
            year = ParserUtil.parseYear(argMultimap.getValue(PREFIX_YEAR).get());
        }

        Optional<Year> optionalYear = Optional.ofNullable(year);

        return new MonthlyExpenditureRankingCommand(optionalYear);
    }
}
