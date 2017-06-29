package todolist.mad.vfh.kfrank.de.todolist.util;

import java.util.Comparator;

import todolist.mad.vfh.kfrank.de.todolist.model.TodoItem;

/**
 * Created by Kevin Frank on 29.06.2017.
 */

public class FavouriteComparator implements Comparator<TodoItem> {

    private static final FavouriteComparator instance = new FavouriteComparator();

    public static FavouriteComparator getInstance() {
        return instance;
    }

    private FavouriteComparator() {
    }

    @Override
    public int compare(TodoItem o1, TodoItem o2) {
        if (o1.isDone() == o2.isDone()) {
            if (o1.isFavourite() == o2.isFavourite()) {
                return (o1.getDueDate() == null || o2.getDueDate() == null) ? 0 : (int) (o1.getDueDate().getTime() - o2.getDueDate().getTime());
            }
            return o1.isFavourite() ? -1 : 1;
        }
        return o1.isDone() ? 1 : -1;
    }
}
