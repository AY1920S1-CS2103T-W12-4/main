package seedu.ichifund.logic.tasks;

import java.util.ArrayList;
import java.util.List;

import seedu.ichifund.logic.tasks.budget.ComputeBudgetTask;
import seedu.ichifund.model.Model;

/**
 * Manages all the task to be executed.
 */
public class TaskManager {

    private final List<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
        tasks.add(new ComputeBudgetTask());
    }

    public void executeAll(Model model) {
        tasks.forEach(task -> task.execute(model));
    }

}
