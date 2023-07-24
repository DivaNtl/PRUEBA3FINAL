package com.example.prueba3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.AdapterView;
import android.widget.Button;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

public class MainBuscar extends AppCompatActivity {

    private static final String TABLE_NAME = "producto";
    private static final String COLUMN_ID = "codigo";
    private static final String COLUMN_DESC = "descripcion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_buscar);

        Button searchButton = (Button) findViewById(R.id.btnbuscar2);
        TextInputLayout searchFieldLayout = (TextInputLayout) findViewById(R.id.etbusqueda);
        ListView listView = (ListView) findViewById(R.id.lvLista);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchFieldLayout.getEditText ( ).getText ( ).toString ( );

                AdminBase dbHelper = new AdminBase ( MainBuscar.this, "administracion", null, 1 );
                SQLiteDatabase db = dbHelper.getReadableDatabase ( );


                Cursor cursor = db.rawQuery(
                        "SELECT *, codigo AS _id FROM producto WHERE codigo = ? OR descripcion LIKE ?",
                        new String[]{searchQuery, "%" + searchQuery + "%"}
                );

                if(cursor.getCount() <= 0){
                    Toast.makeText(MainBuscar.this, "No se encontró un producto con el código especificado.", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                            MainBuscar.this,
                            android.R.layout.simple_list_item_2,
                            cursor,
                            new String[]{"_id", COLUMN_DESC},
                            new int[]{android.R.id.text1, android.R.id.text2},
                            0);

                    listView.setAdapter(adapter);
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String codigo = cursor.getString(cursor.getColumnIndex("codigo"));
                String descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
                String stock = cursor.getString(cursor.getColumnIndex("stock"));
                String ubicacion = cursor.getString(cursor.getColumnIndex("ubicacion"));

                Intent data = new Intent();
                data.putExtra("codigo", codigo);
                data.putExtra("descripcion", descripcion);
                data.putExtra("stock", stock);
                data.putExtra("ubicacion", ubicacion);
                setResult(RESULT_OK, data);

                finish();
            }
        });

    }
}
