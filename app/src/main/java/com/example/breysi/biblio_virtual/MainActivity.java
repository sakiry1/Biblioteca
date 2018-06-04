package com.example.breysi.biblio_virtual;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_ingresar;
    AutoCompleteTextView id_usuario;
    EditText id_pass;
    FirebaseAuth mAuth;
    CheckBox checkBox_remember;
    private FirebaseAuth.AuthStateListener AuthListener;
    private ProgressDialog mProgress;
    private static final String TAG = "CustomAuthActivity";
    public ArrayList<Usuario> listUsuario = new ArrayList<>();

    public static Usuario usuarioactual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id_usuario = (AutoCompleteTextView) findViewById(R.id.id_usuario);
        id_pass = (EditText) findViewById(R.id.id_pass);
        btn_ingresar = (Button) findViewById(R.id.btn_ingresar);
        checkBox_remember = (CheckBox) findViewById(R.id.checkBox_remember);
        mAuth = FirebaseAuth.getInstance();
        btn_ingresar.setOnClickListener(this);

        mProgress = new ProgressDialog(this);

        SharedPreferences prefs = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        String e_mail = prefs.getString("e_mail", "");//"No name defined" is the default value.
        String password = prefs.getString("password", "");//"No name defined" is the default value.

        String recuerda = prefs.getString("recuerda", "si");//"No name defined" is the default value.

        id_usuario.setText(e_mail);
        id_pass.setText(password);
        if (recuerda == "no") {
            checkBox_remember.setChecked(false);
        }
    }


    @Override
    public void onClick(View view) {

        String usuario, pass;
        usuario = id_usuario.getText().toString();
        pass = id_pass.getText().toString();

        if (usuario.equals("") || pass.equals("")) {
            Toast.makeText(this, "Vacio ", Toast.LENGTH_LONG).show();
            return;
        } else {
            this.onStart();
            mProgress.setMessage("Ingresando, por favor espere...");
            mProgress.show();
            autentificar_usuario(usuario, pass);


        }


    }

    @Override
    protected void onStart() {// verifica q el usuario haya accedido
        super.onStart();
        //FirebaseUser user = mAuth.getCurrentUser();
        //updateUI(user);
    }


    String emailUsuario;
    String claveUsuario;

    public void autentificar_usuario(final String auth_email, final String auth_password) {
        startSignIn();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        final Query q_email = myRef.child("usuario").orderByChild("email").equalTo(auth_email);

        emailUsuario = auth_email;
        claveUsuario = auth_password;
        //consulta


        q_email.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean claveCorrecta = false;

                if (dataSnapshot.exists()) {
                    //Usuario tokenUser = new Usuario();
                    usuarioactual = new Usuario();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {

                        //password
                        usuarioactual = item.getValue(Usuario.class);
                        if (usuarioactual.getClave().equals(claveUsuario)) {

                            claveCorrecta = true;

                        }

                        listUsuario.add(usuarioactual);
                    }
                    //q_pass.getRef().getDatabase();
                    if (claveCorrecta) {

                        if (checkBox_remember.isChecked()) {

                            SharedPreferences.Editor editor = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE).edit();
                            editor.putString("e_mail", auth_email);
                            editor.putString("password", auth_password);

                            editor.putString("recuerda", "si");
                            editor.apply();


                        } else {
                            SharedPreferences.Editor editor = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE).edit();

                            editor.putString("e_mail", "");
                            editor.putString("password", "");

                            editor.putString("recuerda", "no");

                            editor.apply(); //guarda cambios

                        }
                        mProgress.dismiss();
                        Intent intent;
                        intent = new Intent(MainActivity.this, Perfil.class);
                        //intent.putExtra("nombre", tokenUser.getNombre());
                        intent.putExtra("usuarioo", usuarioactual);

                        Toast.makeText(MainActivity.this, "Clave correcto", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Clave incorrecto", Toast.LENGTH_LONG).show();
                    }


                } else {

                    //Toast.makeText(MainActivity.this, "NO LISTA", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void limpiarcampos() {


    }

    private void startSignIn() {
        //!!!!!!!!!!!!LOGIN!!!!!!!!!!!!!!!!!!!!!!
        String auth_email = "k@gmail.com";
        //  Log.e("email", auth_email);
        String auth_password = "123456";
        //Log.e("pass", auth_password);

        mAuth.signInWithEmailAndPassword(auth_email, auth_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());


                }

            }
        });

    }

}
