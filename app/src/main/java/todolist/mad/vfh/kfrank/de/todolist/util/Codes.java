package todolist.mad.vfh.kfrank.de.todolist.util;

/**
 * Created by Kevin Frank on 29.06.2017.
 */

public interface Codes {

    public interface Request {

        int NEW_ITEM_CODE = 1;
        int UPDATE_ITEM_CODE = 2;
        int PICK_CONTACT_CODE = 3;
    }

    public interface Response {

        int SAVE_ITEM_CODE = 4;
        int DELETE_ITEM_CODE = 5;
        int NO_OP_CODE = 6;
    }
}
