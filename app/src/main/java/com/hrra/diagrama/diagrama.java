package com.hrra.diagrama;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class diagrama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagrama);

        Flujograma(getIntent().getStringExtra("txt"));

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.hrra));
        }

    }

    LinearLayout contenedor;
    Toast toast;

    //Codigo de inicio y mandar a llanar al contructor al validar el start
    public void Flujograma(String items) {
        if (items.equals("start")) {
            simbolos(R.drawable.start_img, items);
        } else if (items.startsWith("start\n")) {
            String[] subStrings = items.split("\n");
            int contador = 0;
            for (String s : subStrings) {
                contador++;
                constructor(s, contador,subStrings);
            }
        }
    }

    public void constructor(String simbolo, int index, String[] subStrings) {
        if (simbolo.equals("start")) {
            simbolos(R.drawable.start_img, simbolo);
        } else if (simbolo.startsWith("read ")) {
            simbolos(R.drawable.read_img, simbolo);
        } else if (simbolo.startsWith("print" )) {
            simbolos(R.drawable.print_img, simbolo);
        } else if (simbolo.equals("end")) {
            simbolos(R.drawable.end_img, simbolo);
        } else if (simbolo.startsWith("if ")) {
            decision(simbolo, subStrings[index], subStrings[index + 2]);
        }
    }
    public void simbolos(int Background, String simbolo) {
        //Codigo para agregar el molde XML
        Integer dp = dpToPx(150);
        contenedor = findViewById(R.id.flujogrma);
        TextView txt = new TextView(this);
        txt.setText(simbolo);
        txt.setGravity(Gravity.CENTER);
        txt.setBackgroundResource(Background);
        contenedor.addView(txt, dp, dp);
    }

    public void decision(String start, String no, String si) {
        contenedor = findViewById(R.id.flujogrma);
        Integer alto = dpToPx(125);
        Integer ancho1 = dpToPx(360);
        Integer ancho2 = dpToPx(150);
        Integer ancho3 = dpToPx(260);
        Integer Margen = dpToPx(25);

        TextView start_if = new TextView(this);
        start_if.setText(start);
        start_if.setGravity(Gravity.CENTER);
        start_if.setBackgroundResource(R.drawable.if_img);
        contenedor.addView(start_if, ancho1, alto);

        LinearLayout padre = new LinearLayout(this);
        padre.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        padre.setGravity(Gravity.CENTER);
        padre.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho2, alto);

        params.setMargins(Margen, 0, Margen, 0);

        TextView ifSi = new TextView(this);
        ifSi.setText(si);
        ifSi.setGravity(Gravity.CENTER);
        ifSi.setBackgroundResource(R.drawable.yes_img);
        padre.addView(ifSi, params);

        TextView ifNo = new TextView(this);
        ifNo.setText(no);
        ifNo.setGravity(Gravity.CENTER);
        ifNo.setBackgroundResource(R.drawable.no_img);
        padre.addView(ifNo, params);

        contenedor.addView(padre);

        TextView end_if = new TextView(this);
        end_if.setGravity(Gravity.CENTER);
        end_if.setBackgroundResource(R.drawable.endif_img);
        contenedor.addView(end_if, ancho3, alto);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}