package todoflux.data;

import java.util.UUID;

/**
 * @author manuel.mauky
 */
public class TodoItem {

    private final String id;
	private String text;
	private boolean completed = false;

    public TodoItem(String text) {
        this.text = text;

        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
