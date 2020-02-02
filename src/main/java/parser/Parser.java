package parser;

import commands.*;
import dukeexception.DukeException;
import dukeexception.InvalidDateTimeException;
import dukeexception.WrongInputException;
import task.Deadline;
import task.Event;
import task.Task;
import task.Todo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This class deals with making sense of the user command.
 */
public class Parser {

    /**
     * Constructor for creating new Parser object.
     */
    public Parser() { }

    /**
     * Takes in user command input and make sense of it to create the specific Task object and Command object.
     * @param fullCommand This is the command input of user.
     * @throws DukeException thrown when user input date/time is in the wrong format.
     * @return Command object specified by user input.
     */
    public static Command parse(String fullCommand) throws DukeException {
        Command com = null;
        String[] temp = fullCommand.split(" ");
        String command = temp[0];

        DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter inputTimeFormat = DateTimeFormatter.ofPattern("HHmm");
        String[] arr;
        LocalDate ld;
        LocalTime lt;

        try {
            if (command.equals("delete")) {
                Parser.checkInput(fullCommand, command, temp);
                com = new DeleteCommand(Integer.parseInt(temp[1]));
            } else if (command.equals("done")) {
                checkInput(fullCommand, command, temp);
                com = new DoneCommand(Integer.parseInt(temp[1]));
            } else if (command.equals(("todo"))) {
                checkInput(fullCommand, command, temp);
                Task t = new Todo(fullCommand.substring(5));
                com = new AddCommand(t);
            } else if (command.equals("deadline")) {
                checkInput(fullCommand, command, temp);
                arr = fullCommand.split(" /by ");
                String[] dt = arr[1].split(" ");
                ld = LocalDate.parse(dt[0], inputDateFormat);
                lt = LocalTime.parse(dt[1], inputTimeFormat);
                Task d = new Deadline(arr[0].substring(9), ld, lt);
                com = new AddCommand(d);
            } else if (command.equals("event")) {
                checkInput(fullCommand, command, temp);
                arr = fullCommand.split(" /at ");
                String[] dt = arr[1].split(" ");
                ld = LocalDate.parse(dt[0], inputDateFormat);
                lt = LocalTime.parse(dt[1], inputTimeFormat);
                Task e = new Event(arr[0].substring(6), ld, lt);
                com = new AddCommand(e);
            } else if (command.equals("search")) {
                checkInput(fullCommand, command, temp);
                String description = fullCommand.substring(command.length() + 1);
                com = new SearchCommand(LocalDate.parse(description, inputDateFormat));
            } else if (command.equals("list")) {
                com = new ListCommand();
            } else if (command.equals("bye")) {
                com = new ExitCommand();
            } else {
                com = new UnknownCommand();
            }

        } catch (DateTimeParseException e) {
            throw new InvalidDateTimeException();
        }

        return com;
    }

    /**
     * Takes in user command input and make sense of it to create the specific Task object and Command object.
     * @param fullCommand This is the full command input of user.
     * @param command This is the identifier for the full command.
     * @param fullComArr This is the String array for the broken up full command.
     * @throws DukeException thrown when user input is missing information.
     * @return Nothing.
     */
    private static void checkInput(String fullCommand, String command, String[] fullComArr) throws DukeException {
        if (command.equals("done") || command.equals("delete") ||
                command.equals("todo") || command.equals("search")) {
            if (fullComArr.length <= 1) {
                throw new WrongInputException();
            }
        } else if (command.equals("deadline")) {
            if (!fullCommand.contains("/by")) {
                throw new WrongInputException();
            } else {
                String[] dt = fullComArr[fullComArr.length -1].split(" ");
                if (dt.length < 2) {
                    throw new WrongInputException();
                }
            }
        } else if (command.equals("event")) {
            if (!fullCommand.contains("/at")) {
                throw new WrongInputException();
            } else {
                String[] dt = fullComArr[fullComArr.length -1].split(" ");
                if (dt.length < 2) {
                    throw new WrongInputException();
                }
            }
        }
    }


}
