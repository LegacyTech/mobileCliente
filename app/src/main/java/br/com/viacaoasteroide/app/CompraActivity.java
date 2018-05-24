package br.com.viacaoasteroide.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by 16255204 on 24/05/2018.
 */

public class CompraActivity extends AppCompatActivity {

    private int idViagem;
    private  String nomeViagem;
    private String API_URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de voltar


        setContentView(R.layout.compra_activity);

        idViagem = getIntent().getIntExtra("idViagem" , 0);
        nomeViagem = getIntent().getStringExtra( "nomeViagem");

        getSupportActionBar().setTitle( nomeViagem + " !" ); //Seta o titulo da acitivty

        API_URL = getString(R.string.API_URL);

    }

    //Metodos Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();
                break;
            default:break;
        }
        return true;
    }
}
