package seedu.duke;

import command.Command;
import command.CommandResult;
import command.ExitCommand;
import common.Messages;

public class Duke {
    private Ui ui;
    private TaskList taskList;

    /**
     * Instantiate Ui and TaskList.
     */
    public Duke() {
        this.ui = new Ui();
        this.taskList = new TaskList();
    }

    /**
     * Starts Duke Process.
     */
    public void run() {
        ui.printWelcomeMessage();
        runLoop();
    }

    /**
     * Run loop until exit command is received.
     */
    public void runLoop() {
        while (!ExitCommand.isExit()) {
            String input = ui.getUserInput();
            Command command = Parser.parseCommand(input);
            CommandResult result = command.execute(taskList, ui);
            ui.showToUser(result.feedbackToUser);
            ui.showToUser(Messages.DIVIDER);
        }
    }

    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        new Duke().run();
    }
}
