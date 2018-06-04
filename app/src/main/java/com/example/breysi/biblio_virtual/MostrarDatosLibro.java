package com.example.breysi.biblio_virtual;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;


public class MostrarDatosLibro extends AppCompatActivity {

    public Libro libro;
    LibrosPrestadosClass libropres;
    List<Libro> listLibro;
    int posicion;
    TextView tv_titulo, tv_autor, tv_fecha_publicacion, tv_editorial;
    ImageView imagen_libro;
    FloatingActionButton actionButton;
    private DatabaseReference Refprestados;
    boolean reservado = false;
    Calendar calendarDevolucion;
    private Usuario usuactual;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_del_libro);
        tv_titulo = (TextView) findViewById(R.id.tv_titulo);
        tv_autor = (TextView) findViewById(R.id.tv_autor);
        tv_fecha_publicacion = (TextView) findViewById(R.id.tv_fecha_publicacion);
        tv_editorial = (TextView) findViewById(R.id.tv_editorial);
        imagen_libro = (ImageView) findViewById(R.id.imagen_libro);

        posicion = getIntent().getIntExtra("posicion", 0);

        listLibro = BuscarLibros.listaLi;
        usuactual = MainActivity.usuarioactual; //trae el usuario desde el main


        libro = listLibro.get(posicion);

        tv_titulo.setText(libro.getTitulo());
        tv_autor.setText(libro.getAutor());
        tv_editorial.setText(libro.getEditorial());
        tv_fecha_publicacion.setText(libro.getAñoEdicion());
        Picasso.with(getBaseContext()).load(libro.getPortada()).placeholder(R.mipmap.ic_launcher).into(imagen_libro);
        actionButton = findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MostrarDatosLibro.this);
                AlertDialog alerDialog = alert.create();
                alerDialog.show();
                final AlertDialog.Builder alert2 = new AlertDialog.Builder(MostrarDatosLibro.this);
                alert2.setMessage("¿Desea reservar el libro?");
                alert2.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean esReservado = false;
                     /*   Intent intent;
                        intent = new Intent(MostrarDatosLibro.this, LibrosPrestados.class);
                        intent.putExtra("libroreservado", libro.getClass());*/

                        esReservado = reservaLibro(libro.getId());///passa el id del libro

                        Log.e("id_Libro ", libro.getId());
                        // Toast.makeText(MostrarDatosLibro.this,libro.getTitulo(),Toast.LENGTH_LONG).show();

                    }
                });
                alert2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                AlertDialog alertDialog2 = alert2.create();
                alertDialog2.show();

            }
        });

    }

    public boolean reservaLibro(final String idlibro) {//¿esta el libro reservado?

        Refprestados = FirebaseDatabase.getInstance().getReference();
        final Query reserva = Refprestados.child("libros_prestados").orderByChild("id_libro").equalTo(idlibro);
        //busca en librosprestados el id_libro
        reserva.addValueEventListener(new ValueEventListener() {//llama solo una vez al avento
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {//si existe smt
                    for (DataSnapshot keysnap : dataSnapshot.getChildren()) {
                        libropres = keysnap.getValue(LibrosPrestadosClass.class);
                        //  Log.e("fecha NOP", libropres.getFecha_prestamo() + " mas " + libropres.getId_usuario());
                        reservado = true;
                    }

                } else {
                    // no existe puede reservar
                    Refprestados = FirebaseDatabase.getInstance().getReference().child("libros_prestados");
                    libropres = new LibrosPrestadosClass();
                    //recogemos la fecha actual String to Fecha  FECHA-STRING

                    libropres.setfecha_prestamo(Calendario.FechaaString(Calendar.getInstance()));
                    calendarDevolucion = Calendario.reservaMes(Calendar.getInstance());//agrega los 30 dias
                    libropres.setfecha_devolución(Calendario.FechaaString(calendarDevolucion)); //setDEVOLUCION
                    libropres.setid_libro(idlibro);
                    libropres.setid_usuario(usuactual.getDni());
                    Refprestados.push().setValue(libropres);

                     Log.e("fecha prestamo", libropres.getFecha_prestamo()+"usuario es"+libropres.getId_usuario());
                    Log.e("fecha info libro", libropres.getFecha_devolución()+"libro es"+libropres.getId_libro());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return reservado;
        // String devolucion, prestamo,idelibro,ideusuario;

    }



}
