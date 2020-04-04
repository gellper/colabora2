package com.vippinn.colabora.View;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private Button RegisterButton;
    private EditText nombre, apellido, email, contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        nombre = (EditText) findViewById(R.id.editTextName);
        apellido = (EditText) findViewById(R.id.editTextLastName);
        email = (EditText) findViewById(R.id.editTextEmail);
        contraseña = (EditText) findViewById(R.id.editTextPassword);
        RegisterButton = findViewById(R.id.cirRegisterButton);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //llamamos al Login Firebase
                startRegister(nombre, apellido, email, contraseña);
            }
        });


    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }

    public void startRegister(EditText nombre, EditText apellido, EditText email, EditText contraseña){
        String name = nombre.getText().toString();
        String lastname = apellido.getText().toString();
        String user = email.getText().toString();
        String pass = contraseña.getText().toString();

        if(user.equalsIgnoreCase("")||pass.equalsIgnoreCase("")){
            Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos.",
                    Toast.LENGTH_SHORT).show();
        }
        else {

            mAuth.createUserWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("REGISTER_MESSAGE: ", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("REGISTER_MESSAGE: ", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed: "+task.getException().getMessage(),
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
            //usuario y contraseña registrados con exito, ahora guardamos resto de datos
            boolean flag = saveUserData();
            if(flag == true){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(RegisterActivity.this, "Usuario registrado con éxito!",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(RegisterActivity.this, "Error al grabar datos de usuario!",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    public boolean saveUserData(){
        String name = nombre.getText().toString();
        String lastname = apellido.getText().toString();
        String user = email.getText().toString();
        boolean flag = false;
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        Map newPost = new HashMap();
        newPost.put("Nombre", name);
        newPost.put("Apellido", lastname);
        newPost.put("Email", user);

        current_user_db.setValue(newPost);
        flag = true;
        return flag;
    }



}