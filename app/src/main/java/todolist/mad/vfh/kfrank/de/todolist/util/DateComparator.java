package todolist.mad.vfh.kfrank.de.todolist.util;

import java.util.Comparator;

import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;

/**
 * Created by Kevin Frank on 29.06.2017.
 */

public class DateComparator implements Comparator<TodoItem> {

    private static final DateComparator instance = new DateComparator();

    public static DateComparator getInstance() {
        return instance;
    }

    private DateComparator() {
    }

    @Override
    public int compare(TodoItem o1, TodoItem o2) {
        if (o1.isDone() == o2.isDone()) {
            if (o1.getDueDate() == null || o2.getDueDate() == null || o1.getDueDate().getTime() - o2.getDueDate().getTime() == 0) {
                return o1.isFavourite() ? -1 : 1;
            }
            return (int) (o1.getDueDate().getTime() - o2.getDueDate().getTime());
        }
        return o1.isDone() ? 1 : -1;
    }
}
