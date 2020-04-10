package tasks;

import command.RepeatCommand;
import common.Messages;
import exceptions.AtasException;
import seedu.atas.Parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.StringJoiner;

//@@author e0309556
public class RepeatEvent extends Event {
    protected static final int NUM_OF_ENCODED_FIELDS = 11;
    public static final String REPEAT_ICON = "R";
    int numOfPeriod;
    String typeOfPeriod;
    int periodCounter;
    LocalDateTime originalDateAndTime;
    // nextDateAndTime stores when the next time the event will occur for usage in different list commands
    LocalDateTime nextDateAndTime;

    /**
     * Event object constructor.
     *
     * @param name                name of Event
     * @param location            location of Event
     * @param startDateTime       starting date and time of Event
     * @param endDateTime         ending date and time of Event
     * @param comments            comments for the Event
     * @param numOfPeriod         number of period it repeats for
     * @param typeOfPeriod        type of periods which could be (d)aily, (w)eekly, (m)onthly or (y)early
     * @param originalDateAndTime Original Date and Time when Event was set to Repeat, used to keep track
     *                            time lapses especially with respect to dates at end of the month
     * @param periodCounter       number of periods that has past since originalDateAndTime
     */
    public RepeatEvent(String name, String location, LocalDateTime startDateTime, LocalDateTime endDateTime,
                       String comments, int numOfPeriod, String typeOfPeriod, LocalDateTime originalDateAndTime,
                       int periodCounter) {
        super(name, location, startDateTime, endDateTime, comments);
        this.numOfPeriod = numOfPeriod;
        this.typeOfPeriod = typeOfPeriod;
        this.originalDateAndTime = originalDateAndTime;
        this.periodCounter = periodCounter;
    }

    public int getNumOfPeriod() {
        return numOfPeriod;
    }

    public String getTypeOfPeriod() {
        return typeOfPeriod;
    }

    public LocalDateTime getNextDateTime() {
        return nextDateAndTime;
    }

    public int getPeriodCounter() {
        return periodCounter;
    }

    public LocalDateTime getOriginalDateAndTime() {
        return originalDateAndTime;
    }

    /**
     * Update date of event to the next upcoming date (after today) if the recurring event
     * has already occurred.
     */
    public void updateDate() {
        switch (typeOfPeriod) {
        case (RepeatCommand.DAILY_ICON):
            updateDateByDays(numOfPeriod);
            break;
        case (RepeatCommand.WEEKLY_ICON):
            updateDateByDays(numOfPeriod * 7);
            break;
        case (RepeatCommand.MONTHLY_ICON):
            updateDateByMonth(numOfPeriod);
            break;
        case (RepeatCommand.YEARLY_ICON):
            updateDateByYear(numOfPeriod);
            break;
        default:
            assert false;
        }
    }

    /**
     * Update date of event if it is a daily recurring event.
     * @param numOfPeriod num of days before it recurs
     */
    private void updateDateByDays(int numOfPeriod) {
        LocalDate currDate = LocalDate.now();
        LocalDate startDate = startDateAndTime.toLocalDate();
        //Iterate through numOfPeriod of days to find the next date of task.
        while (startDate.compareTo(currDate) < 0) {
            startDate = startDate.plusDays(numOfPeriod);
            periodCounter += 1;
            this.setNotDone();
        }
        startDateAndTime = originalDateAndTime.plusDays(periodCounter * numOfPeriod);
        //Update date of endDateAndTime without changing time.
        endDateAndTime = LocalDateTime.of(originalDateAndTime.plusDays(periodCounter * numOfPeriod).toLocalDate(),
                endDateAndTime.toLocalTime());
        nextDateAndTime = startDateAndTime.plusDays(numOfPeriod);

    }

    /**
     * Update date of event if it is a monthly recurring event.
     * @param numOfPeriod num of months before it recurs
     */
    private void updateDateByMonth(int numOfPeriod) {
        LocalDate currDate = LocalDate.now();
        LocalDate startDate = startDateAndTime.toLocalDate();
        //Iterate through numOfPeriod of months to find the next date of task.
        while (startDate.compareTo(currDate) < 0) {
            startDate = startDate.plusMonths(numOfPeriod);
            periodCounter += 1;
            this.setNotDone();
        }
        startDateAndTime = originalDateAndTime.plusMonths(periodCounter * numOfPeriod);
        //Update date of endDateAndTime without changing time.
        endDateAndTime = LocalDateTime.of(originalDateAndTime.plusMonths(periodCounter * numOfPeriod).toLocalDate(),
                endDateAndTime.toLocalTime());
        nextDateAndTime = startDateAndTime.plusMonths(numOfPeriod);
    }

    /**
     * Update date of event if it is a yearly recurring event.
     * @param numOfPeriod num of years before it recurs
     */
    private void updateDateByYear(int numOfPeriod) {
        LocalDate currDate = LocalDate.now();
        LocalDate startDate = startDateAndTime.toLocalDate();
        //Iterate through numOfPeriod of years to find the next date of task.
        while (startDate.compareTo(currDate) < 0) {
            startDate = startDate.plusYears(numOfPeriod);
            periodCounter += 1;
            this.setNotDone();
        }
        startDateAndTime = originalDateAndTime.plusYears(periodCounter * numOfPeriod);
        //Update date of endDateAndTime without changing time.
        endDateAndTime = LocalDateTime.of(originalDateAndTime.plusYears(periodCounter * numOfPeriod).toLocalDate(),
                endDateAndTime.toLocalTime());
        nextDateAndTime = startDateAndTime.plusYears(numOfPeriod);

    }

    @Override
    public String toString() {
        return "[" + REPEAT_ICON + "]"
                + String.format("%s %s", getStatusIcon(), name)
                + " (at: "
                + location
                + " | "
                + startDateAndTime.format(Parser.PRINT_DATE_FORMAT)
                + " - "
                + endDateAndTime.format(Parser.PRINT_TIME_FORMAT)
                + ")"
                + System.lineSeparator()
                + String.format(Messages.REPEAT_EVENT_WITH_COMMENTS_INDENT, numOfPeriod + typeOfPeriod)
                + comments;
    }

    @Override
    public String encodeTask() {
        StringJoiner sj = new StringJoiner(STORAGE_DELIMITER);
        sj.add(REPEAT_ICON);
        sj.add(isDone ? "true" : "false");
        sj.add(name);
        sj.add(location);
        sj.add(startDateAndTime.format(Parser.INPUT_DATE_TIME_FORMAT));
        sj.add(endDateAndTime.format(Parser.INPUT_DATE_TIME_FORMAT));
        sj.add(Integer.toString(numOfPeriod));
        sj.add(typeOfPeriod);
        sj.add(Integer.toString(periodCounter));
        sj.add(originalDateAndTime.format(Parser.INPUT_DATE_TIME_FORMAT));
        sj.add(comments);
        return sj.toString();
    }

    /**
     * Converts an encoded RepeatEvent back to a RepeatEvent object.
     * @param encodedTask RepeatEvent encoded using encodedTask()
     * @return repeatEvent with the correct attributes set
     * @throws AtasException if there are invalid characters found in the encoded RepeatEvent
     * @throws DateTimeParseException if encoded startDateAndTime or endDateAndTime cannot be parsed
     * @throws IndexOutOfBoundsException if encodedTask is not a String returned by calling encodeTask() on
     *              an RepeatEvent
     */
    public static RepeatEvent decodeTask(String encodedTask)
            throws AtasException, DateTimeParseException, IndexOutOfBoundsException {
        String[] tokens = encodedTask.split("\\" + STORAGE_DELIMITER);
        if (tokens.length != NUM_OF_ENCODED_FIELDS) {
            throw new AtasException(Messages.INCORRECT_STORAGE_FORMAT_ERROR);
        }
        assert tokens[0].equals(REPEAT_ICON);
        boolean isDone = Boolean.parseBoolean(tokens[1]);
        String name = tokens[2];
        String location = tokens[3];
        LocalDateTime startDateAndTime = Parser.parseDate(tokens[4]);
        LocalDateTime endDateAndTime = Parser.parseDate(tokens[5]);
        int numOfPeriod = Integer.parseInt(tokens[6]);
        String typeOfPeriod = tokens[7];
        int periodCounter = Integer.parseInt(tokens[8]);
        LocalDateTime originalDateAndTime = Parser.parseDate(tokens[9]);
        String comments = tokens[10];
        RepeatEvent repeatEvent = new RepeatEvent(name, location, startDateAndTime, endDateAndTime,
                comments, numOfPeriod, typeOfPeriod, originalDateAndTime, periodCounter);
        if (isDone) {
            repeatEvent.setDone();
        }
        return repeatEvent;
    }
}
