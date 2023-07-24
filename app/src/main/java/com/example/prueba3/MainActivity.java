package com.example.prueba3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private Button btnGrabar;
    private Button btnEliminar;
    private Button btnBuscar;
    private TextInputLayout tilCodigo;
    private TextInputLayout tilDescripcion;
    private TextInputLayout tilStock;
    private TextInputLayout tilUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGrabar = findViewById(R.id.btnGrabar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnBuscar = findViewById(R.id.btnBuscar);
        tilCodigo = findViewById(R.id.etCodigo);
        tilDescripcion = findViewById(R.id.etDescripcion);
        tilStock = findViewById(R.id.etStock);
        tilUbicacion = findViewById(R.id.etUbicacion);

        eventos();
    }

    private void eventos() {
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabar();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = tilCodigo.getEditText().getText().toString();
                Intent intent = new Intent(MainActivity.this, MainBuscar.class);
                intent.putExtra("codigo", codigo);
                startActivityForResult (intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String codigo = data.getStringExtra("codigo");
                String descripcion = data.getStringExtra("descripcion");
                String stock = data.getStringExtra("stock");
                String ubicacion = data.getStringExtra("ubicacion");

                tilCodigo.getEditText().setText(codigo);
                tilDescripcion.getEditText().setText(descripcion);
                tilStock.getEditText().setText(stock);
                tilUbicacion.getEditText().setText(ubicacion);
            }
        }
    }

    public void grabar() {
        AdminBase admin = new AdminBase(this,"administracion",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String codigo = tilCodigo.getEditText().getText().toString();
        String descripcion = tilDescripcion.getEditText().getText().toString();
        String stock = tilStock.getEditText().getText().toString();
        String ubicacion = tilUbicacion.getEditText().getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("codigo", codigo);
        registro.put("descripcion", descripcion);
        registro.put("stock", stock);
        registro.put("ubicacion", ubicacion);

        Cursor fila = db.rawQuery("select codigo from producto where codigo=?", new String[]{codigo});

        if(fila.moveToFirst()) {
            // El registro ya existe, así que lo actualizamos
            db.update("producto", registro, "codigo=?", new String[]{codigo});
            Toast.makeText(this, "Se actualizó el producto con el código " + codigo, Toast.LENGTH_SHORT).show();
        } else {
            // El registro no existe, así que lo insertamos
            db.insert("producto", null, registro);
            Toast.makeText(this, "Se ingresó el nuevo producto con el código " + codigo, Toast.LENGTH_SHORT).show();
        }

        db.close();

        tilCodigo.getEditText().setText("");
        tilDescripcion.getEditText().setText("");
        tilStock.getEditText().setText("");
        tilUbicacion.getEditText().setText("");
    }


    private void eliminar(){
        AdminBase admin = new AdminBase(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String codigo = tilCodigo.getEditText().getText().toString();

        int cant = db.delete("producto", "codigo=" + codigo, null);
        db.close();

        if (cant == 1) {
            Toast.makeText(this, "Se borró el producto con código " + codigo, Toast.LENGTH_SHORT).show();
            limpiarPantalla();
        } else {
            Toast.makeText(this, "No existe un producto con el código " + codigo, Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarPantalla() {
        tilCodigo.getEditText().setText("");
        tilDescripcion.getEditText().setText("");
        tilStock.getEditText().setText("");
        tilUbicacion.getEditText().setText("");
    }
}

