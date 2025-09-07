package com.example.lab1_20212624;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SelectActivity extends AppCompatActivity {

    private String nombreJugador;
    private TextView nombreusuario;
    private Button btnRedes, btnCiberseguridad, btnFibraOptica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.select_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar la vista antes de usarla
        nombreusuario = findViewById(R.id.nombreusuario);
        
        // Inicializar botones
        btnRedes = findViewById(R.id.button);
        btnCiberseguridad = findViewById(R.id.button2);
        btnFibraOptica = findViewById(R.id.button3);

        nombreJugador = getIntent().getStringExtra("currentplayer");
        nombreusuario.setText(nombreJugador);

        // Configurar listeners para los botones de temática
        btnRedes.setOnClickListener(v -> iniciarJuego("Redes"));
        btnCiberseguridad.setOnClickListener(v -> iniciarJuego("Ciberseguridad"));
        btnFibraOptica.setOnClickListener(v -> iniciarJuego("Fibra Óptica"));
    }
    
    private void iniciarJuego(String tematica) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("tematica", tematica);
        intent.putExtra("jugador", nombreJugador);
        startActivity(intent);
    }
}
