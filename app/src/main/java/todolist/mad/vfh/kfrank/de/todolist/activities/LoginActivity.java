package todolist.mad.vfh.kfrank.de.todolist.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import todolist.mad.vfh.kfrank.de.todolist.R;
import todolist.mad.vfh.kfrank.de.todolist.TodoListApplication;
import todolist.mad.vfh.kfrank.de.todolist.model.User;
import todolist.mad.vfh.kfrank.de.todolist.util.AbstractTextWatcher;
import todolist.mad.vfh.kfrank.de.todolist.util.AsyncProgressDialogTask;

public class LoginActivity extends Activity {

    // REGEX
    private static final String MAIL_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
    private static final String PASSWORD_REGEX = "\\d{6}";

    // FLAGS
    private boolean validPassword = false;
    private TextView wrongMailView;

    // VIEWS
    private View all;
    private EditText emailField;
    private boolean validMail = false;
    private EditText passwordField;
    private TextView wrongPasswordView;
    private Button signinButton;
    private TextView loginFailedView;

    // OTHER
    private TodoListApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // init activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        all = findViewById(R.id.loginActivityLayout);
        all.setVisibility(View.INVISIBLE);

        // determine application
        application = (TodoListApplication) getApplication();

        // determine views
        emailField = (EditText) findViewById(R.id.email);
        wrongMailView = (TextView) findViewById(R.id.wrongMailMessage);
        passwordField = (EditText) findViewById(R.id.password);
        wrongPasswordView = (TextView) findViewById(R.id.wrongPasswordMessage);
        signinButton = (Button) findViewById(R.id.email_sign_in_button);
        loginFailedView = (TextView) findViewById(R.id.loginFailedMessage);


        // Listener um zur ListView zu kommen
        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    checkLogin();
                    return true;
                }
                return false;
            }
        });

        signinButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        // Handler zur Evaluation des Inputs
        emailField.setText("s@bht.de"); // TODO rausnehmen
        emailField.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginFailedView.setVisibility(View.INVISIBLE);
                validateEmail();
                updateSigninButton();
            }
        });

        passwordField.setText("000000"); // TODO rausnehmen
        passwordField.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginFailedView.setVisibility(View.INVISIBLE);
                validatePassword();
                updateSigninButton();
            }
        });

        // finally trigger async connection check on application
        new AsyncProgressDialogTask<Object, Object, Boolean>(this, "Verbindung wird geprüft", "Bitte warten während die Verbindung zum Webserver geprüft wird.") {

            @Override
            protected Boolean doInBackground(Object... params) {
                return application.checkForRemoteConnection();
            }

            @Override
            protected void onPostExecute(Boolean hasConnection) {
                super.onPostExecute(hasConnection);
                if (hasConnection) {
                    all.setVisibility(View.VISIBLE);
                } else {
                    showNoConnectionDialog();
                }
            }
        };
    }

    private void validateEmail() {
        String text = emailField.getText().toString();
        validMail = text.matches(MAIL_REGEX);
        wrongMailView.setVisibility(text.isEmpty() || validMail ? View.INVISIBLE : View.VISIBLE);
    }

    private void validatePassword() {
        String text = passwordField.getText().toString();
        validPassword = text.matches(PASSWORD_REGEX);
        wrongPasswordView.setVisibility(text.isEmpty() || validPassword ? View.INVISIBLE : View.VISIBLE);
    }

    private void updateSigninButton() {
        signinButton.setEnabled(validMail && validPassword);
    }

    private void showNoConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Keine Verbindung vorhanden");
        builder.setMessage("Es ist keine Verbindung zum Webserver vorhanden. Alle Daten werden nur lokal gespeichert!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                goToListView();
            }
        });
        builder.create().show();
    }

    private void checkLogin() {
        new AsyncProgressDialogTask<Object, Object, Object>(LoginActivity.this, "Bitten warten Sie...", "Nutzerdaten werden evaluiert.") {

            private User user;

            private boolean validAuthenticataion;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                user = new User(email, password);
            }

            @Override
            protected Object doInBackground(Object... params) {
                validAuthenticataion = application.getAuthenticationOperations().authenticateUser(user);
                return null;
            }

            @Override
            protected void onPostExecute(Object aVoid) {
                super.onPostExecute(aVoid);
                if (validAuthenticataion) {
                    goToListView();
                } else {
                    loginFailedView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private void goToListView() {
        startActivity(new Intent(this, TodoOverviewActivity.class));
    }
}

