package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;
import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    EditText loginEmail,loginPassword;
    TextView loginError;


    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);


        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        loginError = findViewById(R.id.loginError);
        loginError.setVisibility(View.GONE);

        loginBtn.setEnabled(false);
        loginEmail.setError("Bitte E-Mail-Adresse eintragen");

        // TMP REDIRECT;
        // startActivity(new Intent(getApplicationContext(), MainActivity.class));

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginEmail.getText().toString().equals("a@bc.de") &&
                        loginPassword.getText().toString().equals("123456")) {
                    loginError.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else{
                    loginError.setVisibility(View.VISIBLE);
                }
            }
        });



        loginEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (isValidEmail(loginEmail.getText().toString())) {
                    loginEmail.setError(null);
                } else {
                    loginEmail.setError("Fehlerhafte E-Mail");
                }
            }
        });

        loginPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (isValidPassword(loginPassword.getText().toString())) {
                    loginPassword.setError(null);
                } else {
                    loginPassword.setError("Fehlerhaftes Passwort");
                }
            }
        });

            TextWatcher loginWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginError.setVisibility(View.GONE);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidEmail(loginEmail.getText().toString()) && isValidPassword(loginPassword.getText().toString()) ) {
                    loginBtn.setEnabled(true);
                } else {
                    loginBtn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        loginEmail.addTextChangedListener(loginWatcher);
        loginPassword.addTextChangedListener(loginWatcher);

    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() == 6;
    }
}


