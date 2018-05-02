package br.com.viacaoasteroide.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 16255204 on 04/04/2018.
 */

public class PerfilDadosActivity extends AppCompatActivity {

    Spinner dia, mes, ano;
    Button btn_cad_usuario; //Botão de salvar
    ArrayList<String> array_dias = new ArrayList<>();
    ArrayList<String> array_meses = new ArrayList<>();
    ArrayList<String> array_anos = new ArrayList<>();
    boolean novo;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfildados_activity);

        ArrayAdapter<String> adapterDia, adapterMes, adapterAno;

        //Parte da data

        for( int i = 1 ; i <= 30; i++ ){
            array_dias.add("" + i);
        }

        for( int i = 1 ; i <= 12; i++ ){
            array_meses.add("" + i);
        }

        for( int i = 2018 ; i >= 1920; i-- ){
            array_anos.add("" + i);
        }

        dia = findViewById(R.id.dia_perfildados);
        mes = findViewById(R.id.mes_perfildados);
        ano = findViewById(R.id.ano_perfildados);

        adapterDia = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, array_dias);
        adapterMes = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, array_meses);
        adapterAno = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, array_anos);

        dia.setAdapter(adapterDia);
        mes.setAdapter(adapterMes);
        ano.setAdapter(adapterAno);

        //Verificação
        btn_cad_usuario = findViewById(R.id.btn_cad_usuario);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de voltar

        novo = getIntent().getBooleanExtra( "novo" , false ); //Fala se é cadastro novo ou atualização

        if( novo ){

            btn_cad_usuario.setText("Cadastrar");

        }

    }

    //Metodos Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar

                if( novo )
                   finish();
                else
                    startActivity(new Intent(this, PerfilActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)

                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem

                break;
            default:break;
        }
        return true;
    }


}
