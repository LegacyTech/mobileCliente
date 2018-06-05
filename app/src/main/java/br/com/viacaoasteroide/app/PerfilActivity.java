package br.com.viacaoasteroide.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matheus Alves on 04/04/2018.
 */

public class PerfilActivity extends AppCompatActivity{

    SharedPreferences preferencias;
    TextView txt_nome_cliente;
    ListView lista_proximas_viagens, lista_historico_viagens;

    ArrayAdapter<Viagem> adapter_proximas, adapter_historico;
    String API_URL;
    String IMAGE_URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de voltar


        API_URL =  getString(R.string.API_URL);
        IMAGE_URL = getString(R.string.IMAGE_URL);

        //Preferencias
        preferencias = getSharedPreferences( getString(R.string.key_preferences) , Context.MODE_PRIVATE);

        txt_nome_cliente = findViewById(R.id.perfil_nome_cliente);
        txt_nome_cliente.setText( "Bem vindo " + preferencias.getString("nome" , "") + " " + preferencias.getString("sobrenome", "") + " !");

        //CRIAR E POPULAR A LISTA DE PROXIMAS VIAGENS
        lista_proximas_viagens = (ListView) findViewById(R.id.lista_proximas_viagens);

        adapter_proximas = new ViagemAdapter(this, new ArrayList<Viagem>(), false , IMAGE_URL);

        lista_proximas_viagens.setAdapter(adapter_proximas);

        new listarViagensPendentes().execute();

        lista_proximas_viagens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Viagem  viagem = (Viagem) lista_proximas_viagens.getItemAtPosition(i); //Pega a viagem na lista
                Intent intent = new Intent( getApplicationContext() , ViagemActivity.class);
                intent.putExtra("idViagem" , viagem.getIdViagem() );
                intent.putExtra("passagem_comprada" , true);
                intent.putExtra("poltrona" , viagem.getPoltrona() );
                startActivity(intent);

            }
        });
        //FIM LISTA


        //CRIAR E POPULAR A LISTA DE HISTORICO VIAGENS
        lista_historico_viagens = (ListView) findViewById(R.id.lista_historico_viagens);

        adapter_historico = new ViagemAdapter(this, new ArrayList<Viagem>(), false , IMAGE_URL);

        lista_historico_viagens.setAdapter(adapter_historico);

        new listarViagensPassadas().execute();

        //FIM LISTA

    }

    public class listarViagensPendentes extends AsyncTask<Void, Void, Void>{

        ArrayList<Viagem> viagens = new ArrayList<>();
        String retornoApi;

        @Override
        protected Void doInBackground(Void... voids) {

            HashMap<String , String> valores = new HashMap<>();
            valores.put("id" , "" + preferencias.getInt("idCliente" , 0) );
            valores.put("ativo" , ""+0);

            retornoApi = Http.post(API_URL + "/Passagem/PorUser" , valores );

            try {

                JSONObject objetoApi = new JSONObject(retornoApi);
                if( objetoApi.getBoolean("sucesso")){

                    JSONArray arrayViagens = objetoApi.getJSONArray("resultado");

                    viagens = montaViagem(arrayViagens);

                }


            } catch (JSONException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter_proximas.addAll(viagens);

        }
    }

    public class listarViagensPassadas extends AsyncTask<Void, Void, Void>{

        ArrayList<Viagem> viagens = new ArrayList<>();
        String retornoApi;

        @Override
        protected Void doInBackground(Void... voids) {

            HashMap<String , String> valores = new HashMap<>();
            valores.put("id" , "" + preferencias.getInt("idCliente" , 0) );
            valores.put("ativo" , ""+1);

            retornoApi = Http.post(API_URL + "/Passagem/PorUser" , valores );

            try {

                JSONObject objetoApi = new JSONObject(retornoApi);
                if( objetoApi.getBoolean("sucesso")){

                    JSONArray arrayViagens = objetoApi.getJSONArray("resultado");

                    viagens = montaViagem(arrayViagens);

                }


            } catch (JSONException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter_historico.addAll(viagens);

        }
    }

    //Monta as viagens de um array
    public ArrayList<Viagem> montaViagem(JSONArray arrayViagens) throws JSONException {

        ArrayList<Viagem> viagens = new ArrayList<>();

        for( int i = 0 ; i < arrayViagens.length() ; i++ ){

            JSONObject arrayViagem = arrayViagens.getJSONObject(i); //Pega o item do array e transfoma em obj json

            Viagem viagem = new Viagem(
                    arrayViagem.getInt("idViagem"),
                    arrayViagem.getString("origem") ,
                    arrayViagem.getString("destino") ,
                    arrayViagem.getDouble("preco"),
                    arrayViagem.getString("dtIda"),
                    arrayViagem.getString("hrIda"));
            viagem.setPoltrona( arrayViagem.getString("poltrona"));
            viagens.add(viagem);

        }

        return  viagens;
    }

    //Botao DOCUMENTOS
    public void documentosAcitivity(View view) {
        startActivity(new Intent(this, EditarDados.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, HomeActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

}
