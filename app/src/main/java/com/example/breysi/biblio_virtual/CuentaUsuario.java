package com.example.breysi.biblio_virtual;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by breysi on 28/04/2018.
 */

public class CuentaUsuario extends AppCompatActivity implements View.OnClickListener {
    TextView tv_nom_usuari;
    TextView tv_poblacion;
    TextView tv_provincia;
    TextView tv_curso;
    TextView tv_cognom;
    TextView tv_email;
    TextView tv_dni;
    String nom_user;
    Button btn_cerrar_sesion;
    String poblacion;
    String provincia;
    String curso;
    Usuario user;
    String email;
    String dni;
    String cognom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_cuenta);

        tv_nom_usuari = (TextView) findViewById(R.id.tv_nom_usuari);
        tv_poblacion = (TextView) findViewById(R.id.tv_poblacion);
        tv_provincia = (TextView) findViewById(R.id.tv_provincia);
        tv_curso = (TextView) findViewById(R.id.tv_curso);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_dni = (TextView) findViewById(R.id.tv_dni);
        tv_cognom = (TextView) findViewById(R.id.tv_cognom);
        user = (Usuario) getIntent().getParcelableExtra("usuarioo");

        btn_cerrar_sesion = (Button) findViewById(R.id.btn_cerrar_sesion);

        nom_user = user.getNombre();
        poblacion = user.getPoblacion();
        provincia = user.getProvincia();
        curso = user.getCurso();
        email = user.getEmail();
        cognom = user.getApellido();
        dni = user.getDni();

        tv_nom_usuari.setText(nom_user);
        tv_poblacion.setText(poblacion);
        tv_provincia.setText(provincia);
        tv_curso.setText(curso);
        tv_cognom.setText(cognom);
        tv_email.setText(email);
        tv_dni.setText(dni);

        btn_cerrar_sesion.setOnClickListener(this);
        // tx.setText(nombre); //  SOLO POR EL NOMBRE

        Toast.makeText(CuentaUsuario.this, "Bienvenido " + user.getApellido(), Toast.LENGTH_SHORT).show();
        Log.i("estudiante", user.getApellido());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cerrar_sesion:
                SharedPreferences.Editor editor = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE).edit();
                editor.putString("e_mail", "");
                editor.putString("password", "");
                editor.apply(); //guarda cambios
                Log.e("SharedPreferences HECHO","Se ha vaciado cuenta");
                    break;
            default:
                Log.e("Error","No se ha borrado y cerrado");
        }
    }
}