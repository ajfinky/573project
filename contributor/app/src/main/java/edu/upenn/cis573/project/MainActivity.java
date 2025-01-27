package edu.upenn.cis573.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    protected static Contributor contributor;
    private DataManager dataManager = new DataManager(new WebClient("10.0.2.2", 3001));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoginButtonClick(View view) {

        EditText loginField = findViewById(R.id.loginField);
        String login = loginField.getText().toString();

        EditText passwordField = findViewById(R.id.passwordField);
        String password = passwordField.getText().toString();

        try {
            contributor = dataManager.attemptLogin(login, password);
            Intent i = new Intent(this, MenuActivity.class);

            startActivity(i);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Login failed!", Toast.LENGTH_LONG).show();
        }
    }

    public void onSignUpButtonClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}