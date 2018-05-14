package br.com.viacaoasteroide.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 16255204 on 03/04/2018.
 */

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;

    ListView lista;
    ArrayAdapter<Viagem> adapter;
    TextView txt_busca;
    String API_URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        preferencias = getSharedPreferences( getString(R.string.key_preferences) , Context.MODE_PRIVATE);
        editor = preferencias.edit();

        //Evento de change
        txt_busca = findViewById(R.id.txt_busca);
        txt_busca.addTextChangedListener( new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                new buscarViagens().execute();
            }

        });



        API_URL = getString(R.string.API_URL);

        //CRIAR E POPULAR A LISTA
        lista = (ListView) findViewById(R.id.list_viagens);

        adapter = new ViagemAdapter(getApplicationContext(), new ArrayList<Viagem>(), true);

        new popularViagens().execute();

        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Viagem  viagem = (Viagem) lista.getItemAtPosition(i); //Pega a viagem na lista
                Intent intent = new Intent( getApplicationContext() , ViagemActivity.class);
                intent.putExtra("idViagem" , viagem.getIdViagem() );
                intent.putExtra("passagem_comprada" , false);
                startActivity(intent);


            }
        });
        //FIM LISTA

    }

    private class popularViagens extends AsyncTask<Void, Void, Void>{

        String retornoApi;
        ArrayList<Viagem> viagens = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... voids) {

            retornoApi = Http.get(API_URL + "/Viagem");

            try {

                JSONObject jsonViagens = new JSONObject(retornoApi); //Transforma a string em JSON

                JSONArray arrayViagens = jsonViagens.getJSONArray("resultado"); //Pega o JSON e transforma em Array

                viagens = montaViagem(arrayViagens);

            } catch (JSONException e) {

                e.printStackTrace();

            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.addAll(viagens); //Adiciona as viagens no adapter
        }

    }

    private class buscarViagens extends AsyncTask<Void, Void, Void>{

        String destino, retornoApi;
        ArrayList<Viagem> viagens = new ArrayList<>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            destino = txt_busca.getText().toString();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            HashMap<String , String> valores = new HashMap<>();
            valores.put("destino", destino);

            retornoApi = Http.post(API_URL + "/Viagem/BuscarViagem" , valores);

            JSONObject jsonViagens = null; //Transforma a string em JSON
            try {

                jsonViagens = new JSONObject(retornoApi);

                JSONArray arrayViagens = jsonViagens.getJSONArray("resultado"); //Pega o JSON e transforma em Array

                viagens = montaViagem(arrayViagens);


            } catch (JSONException e) {
                e.printStackTrace();
            }



            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter.clear();
            adapter.addAll(viagens);

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
            viagens.add(viagem);

        }

        return  viagens;
    }

    //Criar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);//SETA OS ITENS DO MENU
        return true;
    }

    //Popular Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //VERIFICA QUAL OPÇÃO DO MENU FOI CLICADA
        switch ( item.getItemId() ) {
            case R.id.perfil:
                startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                break;
            case R.id.sair:

                //Altera as preferencias do usuario
                editor.clear();
                editor.commit();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}


