public class Event extends Task {

    protected String at;

    public Event(String description, String at) {
        super(description);
        this.at = at;
    }

    @Override
    public String toString() {
        return "[E][" + super.getStatusIcon() + "] " + super.toString() + " (at: " + at + ")";
    }
}