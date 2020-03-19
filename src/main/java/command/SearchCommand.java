package command;

import common.Messages;
import exceptions.AtasException;
import seedu.atas.TaskList;
import seedu.atas.Ui;
import tasks.Task;

import exceptions.AtasException;

import java.util.ArrayList;

public class SearchCommand extends Command {
    public static final String SEARCH_COMMAND_WORD = "search";
    protected String taskType;
    protected String searchParam;
    protected static final String allTasks = "all";
    protected static final String eventTasks = "event";
    protected static final String assignmentTasks = "assignment";

    public SearchCommand(String searchParam, String taskType) {
        this.searchParam = searchParam.toLowerCase();
        this.taskType = taskType;
    }

    /**
     * Returns an arrayList of all the tasks that matches the search query.
     * @param taskList taskList object containing all the tasks
     * @return arrayList of all tasks that match the search query
     */
    private ArrayList<Task> getSearchQueryAllTasks(TaskList taskList) {
        ArrayList<Task> tasks = taskList.getTaskArray();
        ArrayList<Task> results = new ArrayList<>();
        for (Task task: tasks) {
            if (task.getName().toLowerCase().contains(searchParam)) {
                results.add(task);
            }
        }
        return results;
    }

    /**
     * Returns an ArrayList of all event objects that matches the search query.
     * @param taskList taskList object containing all the tasks
     * @return ArrayList of all event objects that matches the search query
     */
    private ArrayList<Task> getSearchQueryEvents(TaskList taskList) {
        ArrayList<Task> events = taskList.getEventsArray();
        assert events.size() == taskList.getEventsArray().size();
        ArrayList<Task> results = new ArrayList<>();
        for (Task event: events) {
            if (event.getName().toLowerCase().contains(searchParam)) {
                results.add(event);
            }
        }
        return results;
    }

    /**
     * Returns an ArrayList of all assignments objects that matches the search query.
     * @param taskList taskList objects containing all assignment tasks
     * @return ArrayList of all assignment object that matches the search query
     */
    private ArrayList<Task> getSearchQueryAssignments(TaskList taskList) {
        ArrayList<Task> assignments = taskList.getAssignmentsArray();
        ArrayList<Task> results = new ArrayList<>();
        assert assignments.size() == taskList.getAssignmentsArray().size();
        for (Task assignment: assignments) {
            if (assignment.getName().toLowerCase().contains(searchParam)) {
                results.add(assignment);
            }
        }
        return results;
    }

    /**
     * prints out the search query.
     * @param results ArrayList containing the results of the search query
     */
    private void printResultsList(ArrayList<Task> results) {
        if (results.size() == 0) {
            System.out.println(String.format(Messages.EMPTY_SEARCH_RESULTS_ERROR));
        } else {
            assert results.size() > 0;
            int position = 1;
            System.out.println("Here are the search results:");
            for (Task task: results) {
                System.out.println(position + ". " + task.toString());
                position++;
            }
        }
    }

    @Override
    public CommandResult execute(TaskList taskList, Ui ui) {
        if (taskList.getListSize() == 0) {
            return new CommandResult(String.format(Messages.EMPTY_TASKLIST_MESSAGE));
        }
        switch (taskType) {
        case allTasks:
            ArrayList<Task> results = getSearchQueryAllTasks(taskList);
            printResultsList(results);
            return new CommandResult(String.format(Messages.SEARCH_SUCCESS_MESSAGE, results.size()));
        case eventTasks:
            ArrayList<Task> eventResults = getSearchQueryEvents(taskList);
            printResultsList(eventResults);
            return new CommandResult(String.format(Messages.SEARCH_SUCCESS_MESSAGE, eventResults.size()));
        case assignmentTasks:
            ArrayList<Task> assignmentResults = getSearchQueryAssignments(taskList);
            printResultsList(assignmentResults);
            return new CommandResult(String.format(Messages.SEARCH_SUCCESS_MESSAGE, assignmentResults.size()));
        default:
            return new CommandResult(String.format(Messages.INVALID_SEARCH_FORMAT));
        }
    }
}
