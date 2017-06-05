package todolist.mad.vfh.kfrank.de.todolist.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Kevin Frank on 01.06.2017.
 */

public abstract class AbstractTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
