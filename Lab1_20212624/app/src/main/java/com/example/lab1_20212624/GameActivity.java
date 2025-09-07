package com.example.lab1_20212624;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    // Variables del juego
    private String palabraSecreta;
    private String palabraMostrada;
    private String tematica;
    private String nombreJugador;
    private int errores = 0;
    private int maxErrores = 6;
    private long tiempoInicio;
    private boolean juegoTerminado = false;
    private int estrellasDisponibles = 0;
    private int aciertosConsecutivos = 0;
    private int aciertosParaComodin = 4;

    // Views
    private TextView tvPalabra;
    private TextView estadoJuego;
    private TextView contadorEstrella;
    private Button btnNuevoJuego;
    private ImageButton btnEstrella;
    private ImageButton btnEstadisticas;
    private ImageView[] partesAhorcado;
    private Button[] botonesLetras;
    private boolean partidaEnCurso = false;

    // Palabras por temática
    private String[][] palabrasPorTematica = {
        // redes
        {"ROUTER", "SWITCH", "FIREWALL", "PROTOCOLO", "ETHERNET", "WIFI", "TCP", "UDP", "HTTP", "FTP"},
        // ciberseguridad
        {"MALWARE", "PHISHING", "ANTIVIRUS", "ENCRIPTACION", "HACKER", "VULNERABILIDAD", "AUTENTICACION", "BACKUP"},
        // fibra
        {"FIBRA", "LASER", "ATENUACION", "DISPERSION", "CONECTOR", "EMPALME", "MULTIPLEXOR", "AMPLIFICADOR"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener datos
        tematica = getIntent().getStringExtra("tematica");
        nombreJugador = getIntent().getStringExtra("jugador");

        // Inicializar views
        inicializarViews();

        // Configurar listeners
        configurarListeners();

        // Iniciar nuevo juego
        iniciarNuevoJuego();
    }

    private void inicializarViews() {
        tvPalabra = findViewById(R.id.tvPalabra);
        estadoJuego = findViewById(R.id.estadojuego);
        contadorEstrella = findViewById(R.id.contadorestrella);
        btnNuevoJuego = findViewById(R.id.nuevojuego);
        btnEstrella = findViewById(R.id.imageButton);
        btnEstadisticas = findViewById(R.id.imageButton2);

        partesAhorcado = new ImageView[6];
        partesAhorcado[0] = findViewById(R.id.imageView3);
        partesAhorcado[1] = findViewById(R.id.imageView4);
        partesAhorcado[2] = findViewById(R.id.imageView5);
        partesAhorcado[3] = findViewById(R.id.imageView6);
        partesAhorcado[4] = findViewById(R.id.imageView9);
        partesAhorcado[5] = findViewById(R.id.imageView8);

        botonesLetras = new Button[26];
        botonesLetras[0] = findViewById(R.id.btnA);
        botonesLetras[1] = findViewById(R.id.btnB);
        botonesLetras[2] = findViewById(R.id.btnC);
        botonesLetras[3] = findViewById(R.id.btnD);
        botonesLetras[4] = findViewById(R.id.btnE);
        botonesLetras[5] = findViewById(R.id.btnF);
        botonesLetras[6] = findViewById(R.id.btnG);
        botonesLetras[7] = findViewById(R.id.btnH);
        botonesLetras[8] = findViewById(R.id.btnI);
        botonesLetras[9] = findViewById(R.id.btnJ);
        botonesLetras[10] = findViewById(R.id.btnK);
        botonesLetras[11] = findViewById(R.id.btnL);
        botonesLetras[12] = findViewById(R.id.btnM);
        botonesLetras[13] = findViewById(R.id.btnN);
        botonesLetras[14] = findViewById(R.id.btnO);
        botonesLetras[15] = findViewById(R.id.btnP);
        botonesLetras[16] = findViewById(R.id.btnQ);
        botonesLetras[17] = findViewById(R.id.btnR);
        botonesLetras[18] = findViewById(R.id.btnS);
        botonesLetras[19] = findViewById(R.id.btnT);
        botonesLetras[20] = findViewById(R.id.btnU);
        botonesLetras[21] = findViewById(R.id.btnV);
        botonesLetras[22] = findViewById(R.id.btnW);
        botonesLetras[23] = findViewById(R.id.btnX);
        botonesLetras[24] = findViewById(R.id.btnY);
        botonesLetras[25] = findViewById(R.id.btnZ);
    }

    private void configurarListeners() {
        // Configurar listeners para botones de letras
        for (int i = 0; i < botonesLetras.length; i++) {
            final int index = i;
            botonesLetras[i].setOnClickListener(v -> {
                if (!juegoTerminado) {
                    char letra = (char) ('A' + index);
                    procesarLetra(letra, botonesLetras[index]);
                }
            });
        }

        // conf para nyevos juego cuando se acciona
        btnNuevoJuego.setOnClickListener(v -> {
            if (partidaEnCurso && !juegoTerminado) {
                // Partida cancelada
                guardarEstadistica("cancelada");
            }
            iniciarNuevoJuego();
        });

        // escuch acomodin (onclick)
        btnEstrella.setOnClickListener(v -> {
            if (juegoTerminado) {
                Toast.makeText(this, "fin", Toast.LENGTH_SHORT).show();
            } else if (estrellasDisponibles > 0) {
                usarComodin();
            } else {
                int faltantes = aciertosParaComodin - aciertosConsecutivos;
                Toast.makeText(this, "0 comodines ", Toast.LENGTH_LONG).show();
            }
        });

        // stats listener mostrar el menú
        btnEstadisticas.setOnClickListener(v -> mostrarMenuEstadisticas());
    }

    private void iniciarNuevoJuego() {
        // Resetear variables
        errores = 0;
        juegoTerminado = false;
        // NO resetear aciertosConsecutivos ni estrellasDisponibles - son acumulables entre partidas (indicado en el documento)
        tiempoInicio = SystemClock.elapsedRealtime();
        partidaEnCurso = true;

        //sSeleccionar palabra aleatoria segun topic
        seleccionarPalabra();

        // guiones palabra oculta
        inicializarPalabraMostrada();

        // Resetear UI
        resetearUI();

        // Actualizar displays
        actualizarPalabraMostrada();
        actualizarContadorEstrellas();
        estadoJuego.setText("");
    }

    private void seleccionarPalabra() {
        Random random = new Random();
        int indiceTematica = 0;

        // Determinar índice de temática usando if-else
        if (tematica.equals("Redes")) {
            indiceTematica = 0;
        } else if (tematica.equals("Ciberseguridad")) {
            indiceTematica = 1;
        } else if (tematica.equals("Fibra Óptica")) {
            indiceTematica = 2;
        }

        // Seleccionar palabra aleatoria de la temática
        String[] palabras = palabrasPorTematica[indiceTematica];
        palabraSecreta = palabras[random.nextInt(palabras.length)];
    }

    private void inicializarPalabraMostrada() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < palabraSecreta.length(); i++) {
            sb.append("_ ");
        }
        palabraMostrada = sb.toString().trim();
    }

    private void resetearUI() {

        for (Button boton : botonesLetras) {
            boton.setEnabled(true); //resetar letas
        }


        for (ImageView parte : partesAhorcado) {
            parte.setVisibility(View.INVISIBLE); // ocultar cadaver xd
        }
    }

    private void procesarLetra(char letra, Button boton) {
        boton.setEnabled(false);

        boolean letraEncontrada = false;
        StringBuilder nuevaPalabra = new StringBuilder();
        String[] partes = palabraMostrada.split(" ");

        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (palabraSecreta.charAt(i) == letra) {
                nuevaPalabra.append(letra).append(" ");
                letraEncontrada = true;
            } else {
                nuevaPalabra.append(partes[i]).append(" ");
            }
        }

        palabraMostrada = nuevaPalabra.toString().trim();
        actualizarPalabraMostrada();

        if (letraEncontrada) {

            aciertosConsecutivos++;

            // Verificar si +1 comodín
            if (aciertosConsecutivos >= aciertosParaComodin) {
                estrellasDisponibles++;
                aciertosConsecutivos = 0;
                Toast.makeText(this, "+1 comodin", Toast.LENGTH_LONG).show();
            }


            actualizarContadorEstrellas(); // Actualizar contador desp de acierto

            // Verificar si ganó
            if (!palabraMostrada.contains("_")) {
                ganarJuego();
            }
        } else {

            aciertosConsecutivos = 0;
            actualizarContadorEstrellas(); // actualizar contador después de resetear

            // mostrar parte del ahorcado
            mostrarParteAhorcado();
            errores++;

            // verificar si perdió
            if (errores >= maxErrores) {
                perderJuego();
            }
        }
    }

    private void mostrarParteAhorcado() {
        if (errores < partesAhorcado.length) {
            partesAhorcado[errores].setVisibility(View.VISIBLE);
        }
    }

    private void actualizarPalabraMostrada() {
        tvPalabra.setText(palabraMostrada);
    }

    private void actualizarContadorEstrellas() {
        contadorEstrella.setText(estrellasDisponibles + "/" + aciertosConsecutivos);

        actualizarEstadoBotonComodin();
    }

    private void actualizarEstadoBotonComodin() {
        if (estrellasDisponibles > 0 && !juegoTerminado) {
            // Comodín disponible - botón habilitado y más visible
            btnEstrella.setEnabled(true);
            btnEstrella.setAlpha(1.0f);
        } else {
            // Sin comodines o juego terminado - botón deshabilitado y más transparente
            btnEstrella.setEnabled(estrellasDisponibles > 0);
            btnEstrella.setAlpha(estrellasDisponibles > 0 ? 0.7f : 0.4f);
        }
    }

    private void usarComodin() {
        if (estrellasDisponibles <= 0 || juegoTerminado) {
            return;
        }

        List<Character> letrasNoAdivinadas = new ArrayList<>();
        for (int i = 0; i < palabraSecreta.length(); i++) {
            char letra = palabraSecreta.charAt(i);
            if (!palabraMostrada.contains(String.valueOf(letra))) {
                if (!letrasNoAdivinadas.contains(letra)) {
                    letrasNoAdivinadas.add(letra);
                }
            }
        }

        if (!letrasNoAdivinadas.isEmpty()) {

            Random random = new Random();
            char letraComodin = letrasNoAdivinadas.get(random.nextInt(letrasNoAdivinadas.size()));

            // Encontrar el botón correspondiente y deshabilitarlo
            int indiceBoton = letraComodin - 'A';
            if (indiceBoton >= 0 && indiceBoton < botonesLetras.length) {
                // Deshabilitar el botón
                botonesLetras[indiceBoton].setEnabled(false);

                // Procesar la letra manualmente (sin afectar aciertos consecutivos)
                StringBuilder nuevaPalabra = new StringBuilder();
                String[] partes = palabraMostrada.split(" ");

                for (int i = 0; i < palabraSecreta.length(); i++) {
                    if (palabraSecreta.charAt(i) == letraComodin) {
                        nuevaPalabra.append(letraComodin).append(" ");
                    } else {
                        nuevaPalabra.append(partes[i]).append(" ");
                    }
                }

                palabraMostrada = nuevaPalabra.toString().trim();
                actualizarPalabraMostrada();

                // Usar el comodín
                estrellasDisponibles--;
                actualizarContadorEstrellas();

                Toast.makeText(this, "comodin usado: " + letraComodin, Toast.LENGTH_SHORT).show();

                // Verificar si ganó
                if (!palabraMostrada.contains("_")) {
                    ganarJuego();
                }
            }
        }
    }

    private void ganarJuego() {
        juegoTerminado = true;
        partidaEnCurso = false;
        long tiempoTranscurrido = (SystemClock.elapsedRealtime() - tiempoInicio) / 1000;
        estadoJuego.setText("Gano / Tiempo: " + tiempoTranscurrido + " s");

        // Deshabilitar todos los botones de letras
        for (Button boton : botonesLetras) {
            boton.setEnabled(false);
        }

        actualizarEstadoBotonComodin();
        guardarEstadistica("ganada");

        Toast.makeText(this, "¡Felicidades " + nombreJugador + "! Has ganado", Toast.LENGTH_LONG).show();
    }

    private void perderJuego() {
        juegoTerminado = true;
        partidaEnCurso = false;
        long tiempoTranscurrido = (SystemClock.elapsedRealtime() - tiempoInicio) / 1000;
        estadoJuego.setText("Perdio /  Tiempo: " + tiempoTranscurrido + " s");

        // Deshabilitar todos los botones de letras
        for (Button boton : botonesLetras) {
            boton.setEnabled(false);
        }
        actualizarEstadoBotonComodin();
        guardarEstadistica("perdida");

        Toast.makeText(this, "perdio", Toast.LENGTH_LONG).show();
    }

    private void mostrarMenuEstadisticas() {
        PopupMenu popup = new PopupMenu(this, btnEstadisticas);
        popup.getMenuInflater().inflate(R.menu.menu_estadisticas, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_estadisticas) {
                Intent intent = new Intent(this, StatsActivity.class);
                intent.putExtra("jugador", nombreJugador);
                startActivity(intent);
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void guardarEstadistica(String tipoPartida) {
        SharedPreferences prefs = getSharedPreferences("GameStats", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Guardar último jugador
        editor.putString("ultimo_jugador", nombreJugador);

        // Incrementar contadores
        int partidasJugadas = prefs.getInt("partidas_jugadas", 0);
        editor.putInt("partidas_jugadas", partidasJugadas + 1);

        // Calcular tiempo transcurrido
        long tiempoTranscurrido = (SystemClock.elapsedRealtime() - tiempoInicio) / 1000;

        // Guardar entrada del historial
        String historialKey = "historial_" + (partidasJugadas + 1);
        String resultado = "";
        switch (tipoPartida) {
            case "ganada":
                resultado = "GANÓ";
                break;
            case "perdida":
                resultado = "PERDIÓ";
                break;
            case "cancelada":
                resultado = "CANCELÓ";
                break;
        }
        String entradaHistorial = "JUEGO " + (partidasJugadas + 1) + " " + resultado + " / " + tiempoTranscurrido + "s";
        editor.putString(historialKey, entradaHistorial);
        editor.putString(historialKey + "_tipo", tipoPartida);

        switch (tipoPartida) {
            case "ganada":
                int partidasGanadas = prefs.getInt("partidas_ganadas", 0);
                editor.putInt("partidas_ganadas", partidasGanadas + 1);
                break;
            case "perdida":
                int partidasPerdidas = prefs.getInt("partidas_perdidas", 0);
                editor.putInt("partidas_perdidas", partidasPerdidas + 1);
                break;
            case "cancelada":
                int partidasCanceladas = prefs.getInt("partidas_canceladas", 0);
                editor.putInt("partidas_canceladas", partidasCanceladas + 1);
                break;
        }

        editor.apply();
    }

}

