package com.example.breysi.biblio_virtual;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by breysi on 20/05/2018.
 */

public class Autor implements Parcelable {

    private String apellido;
    private String  codigo;
    private String dni;
    private String fechaNacimiento;
    private String nombre;

    public Autor() {
    }



    protected Autor(Parcel in) {
        this.apellido = in.readString();
        this.codigo = in.readString();
        this.dni = in.readString();
        this.fechaNacimiento = in.readString();
        this.nombre = in.readString();
    }

    public String getApellido() {
        return apellido;
    }

    public void setapellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setcodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDni() {
        return dni;
    }

    public void setdni(String dni) {
        this.dni = dni;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setfechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setnombre(String nombre) {
        this.nombre = nombre;
    }

    public static final Creator<Autor> CREATOR = new Creator<Autor>() {
        @Override
        public Autor createFromParcel(Parcel in) {
            return new Autor(in);
        }

        @Override
        public Autor[] newArray(int size) {
            return new Autor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(apellido);
        dest.writeString(codigo);

        dest.writeString(dni);
        dest.writeString(fechaNacimiento);
        dest.writeString(nombre);

    }
}
