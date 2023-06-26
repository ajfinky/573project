package edu.upenn.cis573.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private DataManager dataManager = new DataManager(new WebClient("10.0.2.2", 3001));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void onSignUpButtonClick(View view) {
        String name = getTextFieldContent(R.id.nameTextInputEditText);
        String login = getTextFieldContent(R.id.loginTextInputEditText);
        String password = getTextFieldContent(R.id.passwordTextInputEditText);
        String email = getTextFieldContent(R.id.emailTextInputEditText);
        String cardNumber = getTextFieldContent(R.id.cardNumberTextInputEditText);
        String cvv = getTextFieldContent(R.id.cvvTextInputEditText);
        String month = getTextFieldContent(R.id.expiryMonthTextInputEditText);
        String year = getTextFieldContent(R.id.expiryYearTextInputEditText);
        String postCode = getTextFieldContent(R.id.postCodeTextInputEditText);

        // Check if any field is empty
        if (isAnyFieldEmpty(name, login, password, email, cardNumber, cvv, month, year, postCode)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_LONG).show();
            return;
        }

        // Validate email and CVV
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isValidCVV(cvv)) {
            Toast.makeText(this, "Invalid CVV!", Toast.LENGTH_LONG).show();
            return;
        }

        // Validate month and year
        if (!isValidMonth(month)) {
            Toast.makeText(this, "Invalid month! Must be between 1 and 12", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isValidYear(year)) {
            Toast.makeText(this, "Invalid year! Must be 4 digits", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            MainActivity.contributor = dataManager.attemptSignUp(name, login, password, email, cardNumber, cvv, month, year, postCode);

            Toast.makeText(this, "Successfully Sign up", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Sign Up failed", Toast.LENGTH_LONG).show();
        }
    }

    // Helper method to get text from EditText
    private String getTextFieldContent(int fieldId) {
        EditText field = findViewById(fieldId);
        return field.getText().toString().trim();
    }

    // Check if any field is empty
    private boolean isAnyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Check if email is valid
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    // Check if CVV is valid, CVV should be 3 or 4 digits
    private boolean isValidCVV(String cvv) {
        String cvvPattern = "\\d{3,4}";
        return cvv.matches(cvvPattern);
    }

    // Check if month is valid, month should be from 1 to 12
    private boolean isValidMonth(String month) {
        try {
            int monthNum = Integer.parseInt(month);
            return monthNum >= 1 && monthNum <= 12;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check if year is valid, year should be 4 digits
    private boolean isValidYear(String year) {
        String yearPattern = "\\d{4}";
        return year.matches(yearPattern);
    }

}


