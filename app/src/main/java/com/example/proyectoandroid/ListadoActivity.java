package com.example.proyectoandroid;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListadoActivity extends AppCompatActivity {
    private TableLayout tblistado;
    private String[] cabecera ={"ID","Nombre","Apellido"};
    private DynamicTable crearTabla;
    private ArrayList<String[]> datos=new ArrayList<>();
    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
        tblistado = findViewById(R.id.tblistado);
        crearTabla = new DynamicTable(tblistado, getApplicationContext());
        crearTabla.setCabecera(cabecera);
        TraerDatos();
        crearTabla.setDatos(datos);
        crearTabla.crearCabecera();
    }
    private void TraerDatos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };
        String sortOrder =
                FeedReaderContract.FeedEntry.column2 + " ASC";
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,     // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        while(cursor.moveToNext()){
            String[] fila = new String[3];
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));
            fila[0] = itemId + "";
            fila[1] = nombre;
            fila[2] = apellido;
            datos.add(fila);
        }
        db.close();
    }
    public void Regresar(View vista){
        Intent registro = new Intent(this, MainActivity2.class);
        startActivity(registro);
    }
}
