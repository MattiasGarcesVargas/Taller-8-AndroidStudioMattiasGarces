package com.example.proyectoandroid;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private EditText txtid;
    private EditText txtnombre;
    private EditText txtapellido;
    private FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Asociar los EditText con el layout
        txtid = findViewById(R.id.txtid);
        txtnombre = findViewById(R.id.txtnombre);
        txtapellido = findViewById(R.id.txtapellido);

        // ✅ Inicializar el helper correctamente dentro de onCreate
        dbHelper = new FeedReaderDbHelper(this);
    }

    public void Listar(View vista) {
        Intent listar = new Intent(this, ListadoActivity.class);
        startActivity(listar);
    }

    public void Guardar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String nombre = txtnombre.getText().toString();
        String apellido = txtapellido.getText().toString();

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, nombre);
        values.put(FeedReaderContract.FeedEntry.column2, apellido);

        long newRowId = db.insert(FeedReaderContract.FeedEntry.nameTable, null, values);

        Toast.makeText(this, "Se guardó el registro con clave: " + newRowId, Toast.LENGTH_LONG).show();
        db.close();
    }

    public void Buscar(View vista) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String id = txtid.getText().toString();
        if (id.isEmpty()) {
            Toast.makeText(this, "Debes ingresar un ID para buscar.", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        String[] projection = {
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {id};
        String sortOrder = FeedReaderContract.FeedEntry.column2 + " ASC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            txtnombre.setText(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1)));
            txtapellido.setText(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2)));
        } else {
            Toast.makeText(this, "No se encontró registro con ese ID.", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    public void Eliminar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String id = txtid.getText().toString();
        if (id.isEmpty()) {
            Toast.makeText(this, "Debes ingresar un ID para eliminar.", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {id};

        int deletedRows = db.delete(FeedReaderContract.FeedEntry.nameTable, selection, selectionArgs);
        db.close();

        if (deletedRows > 0) {
            Toast.makeText(this, "Se eliminó " + deletedRows + " registro(s).", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No se encontró registro con ese ID.", Toast.LENGTH_SHORT).show();
        }
    }

    public void Actualizar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String id = txtid.getText().toString();
        String nombre = txtnombre.getText().toString();
        String apellido = txtapellido.getText().toString();

        if (id.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos antes de actualizar.", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, nombre);
        values.put(FeedReaderContract.FeedEntry.column2, apellido);

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {id};

        int count = db.update(
                FeedReaderContract.FeedEntry.nameTable,
                values,
                selection,
                selectionArgs
        );

        db.close();

        if (count > 0) {
            Toast.makeText(this, "Se actualizó " + count + " registro(s).", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No se encontró registro con ese ID.", Toast.LENGTH_SHORT).show();
        }
    }
}
