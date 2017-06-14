package todolist.mad.vfh.kfrank.de.todolist.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kfrank on 03.06.2017.
 */

public class TodoItem implements Serializable {

    public static final SimpleDateFormat dueDateFormat = new SimpleDateFormat("dd.MM.yyyy' - 'hh:mm' Uhr'");

    private long id;

    private String name;

    private String description;

    private boolean done;

    private boolean favourite;

    private Date dueDate;

    private List<Contact> contacts = new LinkedList<>();

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

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
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
        contacts = item.getContacts();
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
                objectsEqual(favourite, item.isFavourite()) &&
                listsEqual(contacts, item.getContacts());
    }

    private boolean objectsEqual(Object o1, Object o2) {
        if (o1 == null ^ o2 == null) {
            return false;
        } else {
            return o1 == null ? true : o1.equals(o2);
        }
    }

    private boolean listsEqual(List<?> l1, List<?> l2) {
        if (l1 == null ^ l2 == null) {
            return false;
        } else {
            return l1.containsAll(l2) && l2.containsAll(l1);
        }
    }
}
