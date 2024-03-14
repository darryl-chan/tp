package educonnect.logic.commands;

import static educonnect.logic.parser.CliSyntax.PREFIX_EMAIL;
import static educonnect.logic.parser.CliSyntax.PREFIX_NAME;
import static educonnect.logic.parser.CliSyntax.PREFIX_STUDENT_ID;
import static educonnect.logic.parser.CliSyntax.PREFIX_TAG;
import static educonnect.logic.parser.CliSyntax.PREFIX_TELEGRAM_HANDLE;
import static java.util.Objects.requireNonNull;

import educonnect.commons.util.ToStringBuilder;
import educonnect.logic.Messages;
import educonnect.logic.commands.exceptions.CommandException;
import educonnect.model.Model;
import educonnect.model.student.Student;

/**
 * Adds a student to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_STUDENT_ID + "STUDENT_ID "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_TELEGRAM_HANDLE + "TELEGRAM_HANDLE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_STUDENT_ID + "A1234567X "
            + PREFIX_TELEGRAM_HANDLE + "@john.doe "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_TAG + "tutorial-1 "
            + PREFIX_TAG + "high-ability";

    public static final String MESSAGE_SUCCESS =
            "New student added: %1$s";
    public static final String MESSAGE_DUPLICATE_STUDENT_ID =
            "This student Id already exists in the address book";
    public static final String MESSAGE_DUPLICATE_EMAIL =
            "This email already exists in the address book";
    public static final String MESSAGE_DUPLICATE_TELEGRAM_HANDLE =
            "This telegram handle already exists in the address book";

    private final Student toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Student}
     */
    public AddCommand(Student student) {
        requireNonNull(student);
        toAdd = student;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasStudentId(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT_ID);
        }
        if (model.hasEmail(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_EMAIL);
        }
        if (model.hasTelegramHandle(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TELEGRAM_HANDLE);
        }

        model.addStudent(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}