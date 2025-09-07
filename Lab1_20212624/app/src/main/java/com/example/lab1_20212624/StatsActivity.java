package com.example.lab1_20212624;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StatsActivity extends AppCompatActivity {

    private TextView tvNombreJugador;
    private TextView tvFechaHora;
    private TextView tvPartidasGanadas;
    private TextView tvPartidasPerdidas;
    private TextView tvPartidasCanceladas;
    private LinearLayout layoutHistorial;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.stats_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarViews();
        configurarListeners();
        cargarEstadisticas();
    }

    private void inicializarViews() {
        tvNombreJugador = findViewById(R.id.tvNombreJugador);
        tvFechaHora = findViewById(R.id.tvFechaHora);
        tvPartidasGanadas = findViewById(R.id.tvPartidasGanadas);
        tvPartidasPerdidas = findViewById(R.id.tvPartidasPerdidas);
        tvPartidasCanceladas = findViewById(R.id.tvPartidasCanceladas);
        layoutHistorial = findViewById(R.id.layoutHistorial);
        btnVolver = findViewById(R.id.btnVolver);
    }

    private void configurarListeners() {
        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarEstadisticas() {
        SharedPreferences prefs = getSharedPreferences("GameStats", MODE_PRIVATE);

        // Obtener datos del intent o de las preferencias
        String nombreJugador = getIntent().getStringExtra("jugador");
        if (nombreJugador == null) {
            nombreJugador = prefs.getString("ultimo_jugador", "Jugador");
        }

        // Mostrar nombre del jugador
        tvNombreJugador.setText("Jugador: " + nombreJugador);

        // Mostrar fecha y hora actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String fechaHora = sdf.format(new Date());
        tvFechaHora.setText("Fecha y Hora: " + fechaHora);

        // Cargar estadísticas de resumen
        int partidasJugadas = prefs.getInt("partidas_jugadas", 0);
        int partidasGanadas = prefs.getInt("partidas_ganadas", 0);
        int partidasPerdidas = prefs.getInt("partidas_perdidas", 0);
        int partidasCanceladas = prefs.getInt("partidas_canceladas", 0);

        tvPartidasGanadas.setText("Ganadas: " + partidasGanadas);
        tvPartidasPerdidas.setText("Perdidas: " + partidasPerdidas);
        tvPartidasCanceladas.setText("Canceladas: " + partidasCanceladas);

        // Cargar historial detallado
        cargarHistorial(prefs, partidasJugadas);
    }

    private void cargarHistorial(SharedPreferences prefs, int totalPartidas) {
        // Limpiar historial anterior
        layoutHistorial.removeAllViews();

        if (totalPartidas == 0) {
            TextView sinHistorial = new TextView(this);
            sinHistorial.setText("No hay partidas registradas");
            sinHistorial.setTextSize(16);
            sinHistorial.setTextColor(Color.GRAY);
            sinHistorial.setPadding(16, 16, 16, 16);
            layoutHistorial.addView(sinHistorial);
            return;
        }

        // Cargar cada entrada del historial (desde la más reciente)
        for (int i = totalPartidas; i >= 1; i--) {
            String historialKey = "historial_" + i;
            String tipoKey = historialKey + "_tipo";

            String entradaHistorial = prefs.getString(historialKey, null);
            String tipoPartida = prefs.getString(tipoKey, "ganada");

            if (entradaHistorial != null) {
                TextView entradaTexto = new TextView(this);
                entradaTexto.setText(entradaHistorial);
                entradaTexto.setTextSize(14);
                entradaTexto.setPadding(16, 8, 16, 8);

                // Establecer color según el tipo de partida
                switch (tipoPartida) {
                    case "ganada":
                        entradaTexto.setTextColor(Color.parseColor("#4CAF50")); // Verde
                        break;
                    case "perdida":
                        entradaTexto.setTextColor(Color.parseColor("#F44336")); // Rojo
                        break;
                    case "cancelada":
                        entradaTexto.setTextColor(Color.parseColor("#FF9800")); // Naranja
                        break;
                    default:
                        entradaTexto.setTextColor(Color.BLACK);
                        break;
                }

                layoutHistorial.addView(entradaTexto);
            }
        }
    }
}
