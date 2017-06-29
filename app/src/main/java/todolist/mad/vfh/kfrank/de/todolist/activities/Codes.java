package todolist.mad.vfh.kfrank.de.todolist.activities;

import android.content.Intent;

/**
 * Created by Kevin Frank on 29.06.2017.
 */

public interface Codes {

     interface Request {

        int NEW_ITEM_CODE = 1;
        int UPDATE_ITEM_CODE = 2;
        int PICK_CONTACT_CODE = 3;
    }

    interface Response {

        int SAVE_ITEM_CODE = 4;
        int DELETE_ITEM_CODE = 5;
        int NO_OP_CODE = 6;
    }

    interface Extra {

        String ITEM = "item";
        String SMS_BODY = "sms_body";
        String MAIL_SUBJECT = Intent.EXTRA_SUBJECT;
        String MAIL_BODY = Intent.EXTRA_TEXT;
    }

    interface Action {

        String PICK = Intent.ACTION_PICK;
        String SEND_TO = Intent.ACTION_SENDTO;
    }

    interface Uri {

        String SMS_TO = "smsto";
        String MAIL_TO = "mailto";
    }
}
