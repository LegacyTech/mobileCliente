package br.com.viacaoasteroide.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;

/**
 * Created by 16255204 on 04/04/2018.
 */

public class PerfilDadosActivity extends AppCompatActivity {

    Button btn_cad_usuario; //Botão de salvar
    boolean novo;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfildados_activity);

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
