package com.example.breysi.biblio_virtual;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by breysi on 28/04/2018.
 */

public class LibrosPrestados extends AppCompatActivity {
    final ArrayList<LibrosPrestadosClass> listaLibrosPrestados = new ArrayList<LibrosPrestadosClass>(6);
    int contador = 0;
    LibrosPrestadosClass itemprestadosClass = new LibrosPrestadosClass();
    private DatabaseReference Refprestados;
    /////////////
    // public static ArrayList<LibrosPrestadosClass> listaLP = new ArrayList<LibrosPrestadosClass>();
    ArrayList<HashMap<String, String>> valores = new ArrayList<HashMap<String, String>>();
    //ArrayList<Libro> l_libro = new ArrayList<>();
    ListView ListaViewPrestados;
    Libro itemlibro = new Libro();
    private DatabaseReference myRef;
    private Usuario usuactual;
    TextView nprestados;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libros_prestados);
        ListaViewPrestados = (ListView) findViewById(R.id.listaprestados);
        //  tx = (TextView) findViewById(R.id.textView);
        nprestados = (TextView) findViewById(R.id.no_prestado);
        usuactual = MainActivity.usuarioactual;

        myRef = FirebaseDatabase.getInstance().getReference();
        //consulta sobre los librosPrestados que tiene el usuario, usando su dni
        final Query q_librosPrestados = myRef.child("libros_prestados").orderByChild("id_usuario").equalTo(usuactual.getDni());
        q_librosPrestados.addValueEventListener(new ValueEventListener() {//valueEventListener ya que leera una lista de datos o no
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (contador <= listaLibrosPrestados.size()) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshotlibrosPrestados : dataSnapshot.getChildren()) {
                            itemprestadosClass = snapshotlibrosPrestados.getValue(LibrosPrestadosClass.class);//guarda los prestamos encontrados
                            listaLibrosPrestados.add(itemprestadosClass);//agrega los prestamos a la lista
                            contador++;
                        }
                    } else {//si el usuario no tiene NADA prestado te envia
                        nprestados.setText(R.string.no_prestamos);
                    }

                }else{
                    System.out.println("cantidad maxima de prestamos");
                }
                //Log.e("tamañolista2", "--->" + listaLibrosPrestados.size());
                for (final LibrosPrestadosClass prestadosClass : listaLibrosPrestados) {
                    //  final String prestamo = itemprestadosClass.getFecha_prestamo();
                    final String entrega = prestadosClass.getFecha_devolución();
                    final String libro = prestadosClass.getId_libro();
                    // Log.e("entrega y libroid", entrega + "---->" + libro);
                    Refprestados = FirebaseDatabase.getInstance().getReference();

                    final Query info_libro = Refprestados.child("libro").orderByChild("id").equalTo(libro);
                    info_libro.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot keysnap : dataSnapshot.getChildren()) {
                                itemlibro = keysnap.getValue(Libro.class);
                                HashMap hashMap = new HashMap<String, String>();
                                hashMap.put("entrega", entrega);
                                hashMap.put("nom_libro", itemlibro.getTitulo());
                                valores.add(hashMap);
                                ListAdapter adapter = new SimpleAdapter(
                                        LibrosPrestados.this, valores,
                                        R.layout.lista_libros_prestados,
                                        new String[]{"entrega", "nom_libro"},
                                        new int[]{R.id.tv_fentrega, R.id.tv_titulo});
                                ListaViewPrestados.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        // listaLP = getIntent().getParcelableArrayListExtra("ListaViewPrestados");
        //  Log.e("tamañolista", "--->" + listaLibrosPrestados.size());


    }

  /*  public void librosPrestados() { ///lista los libros prestados
        //----------LIBRO------------------------

    }*
    // keydb = keysnap.getKey();
    //recoge la llave "0"
    // String key = Refprestados.child("libro").push().getKey();
    // Log.e("tamañolista","--->"+key);
    //  Log.e("MIAU011","--->"+id_libro);

  /* public void datosLibro(String id_libro) {

        Refprestados = FirebaseDatabase.getInstance().getReference();
        //recoge la llave "0"
        // String key = Refprestados.child("libro").push().getKey();
        // Log.e("tamañolista","--->"+key);
        //  Log.e("MIAU011","--->"+id_libro);
        final Query info_libro = Refprestados.child("libro").orderByChild("id").equalTo(id_libro);

        info_libro.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keysnap : dataSnapshot.getChildren()) {
                    // keydb = keysnap.getKey();
                    itemlibro = keysnap.getValue(Libro.class);
                    // Log.e("MIAU","--->"+itemlibro.getTitulo());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }*/


}
