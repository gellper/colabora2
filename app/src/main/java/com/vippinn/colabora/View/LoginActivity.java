package com.vippinn.colabora.View;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.vippinn.colabora.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    //declaramos autenticacion
    private FirebaseAuth mAuth;
    //declaramos componentes de la vista
    private Button LoginButton;
    private EditText email,contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.editTextEmail);
        contraseña = (EditText) findViewById(R.id.editTextPassword);
        LoginButton = findViewById(R.id.cirLoginButton);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //llamamos al Login Firebase
                startLogin(email, contraseña);
            }
        });

    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

    public void startLogin(EditText email, EditText contraseña){
        //String resultado = "";
        String user = email.getText().toString();
        String pass = contraseña.getText().toString();

        if(user.equalsIgnoreCase("")||pass.equalsIgnoreCase("")){
            Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos.",
                    Toast.LENGTH_SHORT).show();
        }
        else{

            mAuth.signInWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("LOGIN_MESSAGE: ", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("LOGIN_MESSAGE: ", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed: "+task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });


        }

    }

    public void updateUI(FirebaseUser user){
        if (user == null){

        }
        else{
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "Bienvenido "+user.getEmail(),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
