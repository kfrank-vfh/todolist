package todolist.mad.vfh.kfrank.de.todolist.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kfrank on 03.06.2017.
 */

public class TodoItem implements Serializable {

    public static final SimpleDateFormat dueDateFormat =new SimpleDateFormat("dd.MM.yyyy' - 'hh:mm' Uhr'");

    private long id;

    private String name;

    private String description;

    private boolean done;

    private boolean favourite;

    private Date dueDate;

    public TodoItem() {
    }

    public TodoItem(long id, String name, String description, boolean done, boolean favourite, Date dueDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.done = done;
        this.favourite = favourite;
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void adoptData(TodoItem item) {
        if (item == null) {
            return;
        }
        setName(item.getName());
        setDescription(item.getDescription());
        setDueDate(item.getDueDate());
        setDone(item.isDone());
        setFavourite(item.isFavourite());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TodoItem)) {
            return false;
        }
        TodoItem item = (TodoItem) obj;
        return objectsEqual(name, item.getName()) &&
               objectsEqual(description, item.getDescription()) &&
               objectsEqual(dueDate, item.getDueDate()) &&
               objectsEqual(done, item.isDone()) &&
               objectsEqual(favourite, item.isFavourite());
    }

    private boolean objectsEqual(Object o1, Object o2) {
        if (o1 == null ^ o2 == null) {
            return false;
        } else {
            return o1 == null ? true : o1.equals(o2);
        }
    }
}
