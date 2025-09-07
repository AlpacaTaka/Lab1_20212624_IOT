package com.example.lab1_20212624;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText inputNombre;
    private Button btnJugar;


    /*
    Al ingresar a la aplicación se muestra una vista similar a la mostrada. Cuando se llena el campo de nombre,
    recién se habilita el botón de Jugar. Luego de ello, se puede escoger la temática del juego.
    Nota: El título “TeleAhorcado” puede cambiar de color (Azul, Verde o Rojo) gracias a un Context Menu.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //el resto se usa para temas de edge to edge, no es obligatorio (recordar para siguietes veces xd)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); //este es el que verdaderamente establece la vista , ppt clase2
        //de igual forma con el primero
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //a partir de aqui va el codigo :D

        textView = findViewById(R.id.textteleahorcado);
        // registrar el textView para el context menu (ppt)
        registerForContextMenu(textView); //update funciono



        inputNombre = findViewById(R.id.editTextText);
        btnJugar = findViewById(R.id.botonjugar);

        //habilitar el boton jugar cuando se ingrese un nombre

        //Bing search: how to activate a button after the imput of a name in java android
        //copilot ai integrado al buscador respondio xd
        /*


        // Add a TextWatcher to monitor changes in the EditText
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable the button if the input is not empty
                submitButton.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
         */
        btnJugar.setEnabled(false);
        inputNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnJugar.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No
            }
        });


        //jugar
        btnJugar.setOnClickListener(v -> {
            String nombre = inputNombre.getText().toString().trim();
            if (!nombre.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                intent.putExtra("currentplayer", nombre);
                startActivity(intent);
            }
        });











    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.textteleahorcado) {
            getMenuInflater().inflate(R.menu.menu_context, menu);
        }
    }

    @Override
    //referencia: https://es.stackoverflow.com/questions/92077/añadir-color-a-un-campo-string-android
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.context_azul) {
            textView.setTextColor(Color.parseColor("#2196F3"));
            return true;
        } else if (item.getItemId() == R.id.context_verde) {
            textView.setTextColor(Color.parseColor("#4CAF50"));
            return true;
        } else if (item.getItemId() == R.id.context_rojo) {
            textView.setTextColor(Color.parseColor("#F44336"));
            return true;
        }
        return super.onContextItemSelected(item);
    }
    //ahora si funciono xd




}