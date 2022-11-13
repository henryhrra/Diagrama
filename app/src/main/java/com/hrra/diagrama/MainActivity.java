package com.hrra.diagrama;

import static com.hrra.diagrama.R.color.purple_200;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.hrra));
        }
        findViewById(R.id.btn_instrucciones).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intrucciones();
            }

        });
        findViewById(R.id.btn_crear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crear();
            }
        });
        findViewById(R.id.swith).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estado();
            }
        });
    }
    Toast toast;
    public void crear() {
        Intent i = new Intent(this, diagrama.class);
        EditText Etxt_inicio = findViewById(R.id.linea_incio);
        boolean error = false;
        if (Etxt_inicio.getText().toString().isEmpty()) {
            error("Debe insertar datos");
        } else {
            String start = Etxt_inicio.getText().toString().toLowerCase(Locale.ROOT).trim();

            if (start.equals("start")) {
                error("Debe cerrar el flujograma con la palabra end");
            } else if(start.contains("\n")){
                int ultimo;
                String[] subStrings = start.split("\n");
                ultimo = subStrings.length -1;
                String primero = subStrings[0].trim();
                String latest = subStrings[ultimo].trim();
                if(primero.equals("start") && latest.equals("end")){
                    start = "";
                    for(String s : subStrings){
                        s = s.replaceAll(" +"," ");
                        start =  start + s.trim()+"\n";

                    }
                    for(int item = 0 ; item < subStrings.length;item++){
                        error = comprobar(subStrings[item],item,subStrings);
                        if(error) break;
                    }
                    if(!error){
                        i.putExtra("txt", start);
                        startActivity(i);
                    }
                }else if(primero.equals("start") && !latest.equals("end")){
                    error("Debe cerrar el flujograma con la palabra end");
                }else if(!primero.equals("start")){
                    error("Debe iniciar flujograma con la palabra start");
                }


            }else {
                error("Debe iniciar el Diagrama de Flujo con la palabra reservada Start");
            }
        }
    }

    public void error(String s){
        View error = getLayoutInflater().inflate(R.layout.error,null);
        final Dialog dialog = new Dialog(MainActivity.this);

        dialog.setContentView(error);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView text = error.findViewById(R.id.Error);
        Button btn_ok = error.findViewById(R.id.ok);
        text.setText(s);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void intrucciones (){
        View vista = getLayoutInflater().inflate(R.layout.indicaciones,null);
        final Dialog dialog = new Dialog(MainActivity.this);
        EditText txt = findViewById(R.id.linea_incio);
        Button btn_ok = vista.findViewById(R.id.ok);
        Button btn_ejempo = vista.findViewById(R.id.ejemplo);
        Switch estado = findViewById(R.id.swith);
        dialog.setContentView(vista);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_ejempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estado.setChecked(true);
                estado();
                dialog.dismiss();
                txt.setText("Start\nRead a\nIf a = 5\na = a + 5\nelse\na = a - 5\nEndif\nPrint a\nEnd");
            }
        });
        dialog.show();
    }

    public boolean comprobar(String simbolo, int index, String[] subStrings) {
        boolean Error = false;
        if (simbolo.equals("start")) {
            Error = false;
        } else if (simbolo.startsWith("read")) {;
            if (simbolo.contains(" ") && (contEsapcios(simbolo) == 1)) {
                Error = false;
            } else {
                error("Al usar la palabra read debe colocar un espacio para declara la variable");
                Error = true;
            }
        } else if (simbolo.startsWith("print")) {
            if (simbolo.contains(" ") && (contEsapcios(simbolo) == 1)) {
                Error = false;
            } else {
                error("Al usar la palabra print debe colocar un espacio y despues agregar la varibale a imprimir");
                Error = true;
            }
        } else if (simbolo.equals("end")) {
            Error = false;
        } else if (simbolo.startsWith("if")) {
            if (!simbolo.startsWith("if ")) {
                error("Al usar la palabra reserveda if debe dejar un espacio antes de seguir escribiendo");
                Error = true;
            } else if (simbolo.contains(" = ") && (contEsapcios(simbolo) == 3)) {
                int contador = subStrings.length;

                if (contador < (index + 4)) {
                    error("Debe usar la sintaxis correcta del if");
                    Error = true;
                } else if (!subStrings[index + 2].equals("else")) {
                    error("Debe escribir que sucede si entra en la decisión dar un salto de linea y colocar la sentencia else");
                    Error = true;
                } else if (!subStrings[index + 4].equals("endif")) {
                    error("Debe escribir que sucede si no entra en la decisión dar un salto de linea y cerrar el ciclo if");
                    Error = true;
                } else if (contador >= (index + 4) && (subStrings[index + 2].equals("else")) && (subStrings[index + 4].equals("endif"))) {
                    Error = false;
                }
            } else {
                error("Al usar la palabra if debe dejar un espacio poner un = dejar un esapacio y el texto que usted quiera");
                Error = true;
            }

        }
        return Error;
    }

    public int contEsapcios(String cadena) {
        int contador = 0;
        for (int i = 0; i < cadena.length(); i++) {
            // Si el carácter en [i] es un espacio (' ') aumentamos el contador
            if (cadena.charAt(i) == ' ') contador++;
        }
        return contador;
    }
    public void estado(){
        Switch Estado = findViewById(R.id.swith);
        EditText txt = findViewById(R.id.linea_incio);
        if(Estado.isChecked()){
            Estado.setText("Activado");
            Estado.setTextColor(Color.GREEN);
            txt.setEnabled(true);
            txt.setHint("Ingrese el texto aquí");
            toast = Toast.makeText(getApplicationContext(), "Se activo el ingreso de texto", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            Estado.setText("Desactivado");
            Estado.setTextColor(Color.RED);
            toast = Toast.makeText(getApplicationContext(), "Se desactivo el ingreso de texto", Toast.LENGTH_SHORT);
            toast.show();
            txt.setEnabled(false);
            txt.setHint("Active para ingresar texto");
        }
    }
}