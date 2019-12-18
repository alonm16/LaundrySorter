package com.example.laundrysorter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edEmail;
    private EditText edPass,edPassConfirm;
    private TextView tvWarning;
    private boolean passwordConfirmEdited = false;
    private boolean warningVisible = false;
    private final String TAG = SignUpActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth=FirebaseAuth.getInstance();
        edEmail = findViewById(R.id.ed_sign_up_email);
        edPass =  findViewById(R.id.ed_sign_up_pass);
        edPassConfirm = findViewById(R.id.edSignUpConfirmPassword);
        tvWarning = findViewById(R.id.tvWarning);
        edPassConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setPasswordConfirmEdited(true);
                handleWarningText();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handleWarningText();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setPasswordConfirmEdited (boolean value){
        this.passwordConfirmEdited = value;
    }
    private void handleWarningText (){
        String password = edPass.getText().toString();
        String passwordConfirm = edPassConfirm.getText().toString();
        int passwordLength = password.length();
        int passwordConfirmLength = passwordConfirm.length();

        if (!passwordConfirmEdited)
            return;
        else {
            if (!passwordConfirm.equals(password)){
                if (!warningVisible){
                    warningVisible = true;
                    tvWarning.setAlpha(0);
                    tvWarning.setVisibility(View.VISIBLE);
                    tvWarning.animate().alpha(1).setDuration(150);
                }
            }
            else {
                if (warningVisible){
                    warningVisible = false;
                    tvWarning.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void signUpOnClick(View view){
        String email = edEmail.getText().toString().trim();
        String password = edPass.getText().toString();
        String passwordConfirm = edPassConfirm.getText().toString();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length()<8) {
            Toast.makeText(this, "Enter valid password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(passwordConfirm)){
            Toast.makeText(this, this.getResources().getString(R.string.passwords_not_matching),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
                    }
                });
    }
    void updateUI(){

        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null) {
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

}
