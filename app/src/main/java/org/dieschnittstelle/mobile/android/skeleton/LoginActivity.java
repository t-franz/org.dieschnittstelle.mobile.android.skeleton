package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    public Button loginBtn;
    public EditText loginEmail,loginPassword;
    public TextView loginError;
    public ProgressBar progressBar;


    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((DataItemApplication) getApplication()).verifyWebappAvailable(available -> {
            if (!available) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                initialiseLogin();
            }
        });
    }

    private void initialiseLogin() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        loginError = findViewById(R.id.loginError);
        loginError.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progressBar);

        loginBtn.setEnabled(false);
        loginEmail.setError("Bitte E-Mail-Adresse eintragen");

        // PREFILL ####################
        loginEmail.setText("s@bht.de");
        loginPassword.setText("000000");
        // PREFILL ####################

        loginBtn.setOnClickListener(v -> {

            String email = loginEmail.getText().toString();
            String password = loginPassword.getText().toString();
            progressBar.setVisibility(View.VISIBLE);


            new Handler().postDelayed(() -> {

                if(email.equals("s@bht.de") && password.equals("000000")) {
                    loginError.setVisibility(View.GONE);
                    if (progressBar != null ) {
                        progressBar.setVisibility(View.GONE);
                        progressBar = null;
                    }
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else {
                    if (progressBar != null ) {
                        progressBar.setVisibility(View.GONE);
                    }
                    loginError.setVisibility(View.VISIBLE);
                    loginBtn.setEnabled(false);
                }
           }, 2000);
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


