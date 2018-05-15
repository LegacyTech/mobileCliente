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
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 16255204 on 15/05/2018.
 */

public class EditarDados extends AppCompatActivity {

    private EditText txt_celular , txt_telefone , txt_cep, txt_logradouro, txt_bairro, txt_numero;
    private Spinner sp_estado , sp_cidade;
    private SharedPreferences preferencias;
    private int idClitente;
    private String API_URL;
    private ArrayAdapter<String> adapterEstado , adapterCidade;
    private String estadoAtual;
    private  int idCidadeAtual = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de voltar

        setContentView(R.layout.editar_dados);

        API_URL = getString(R.string.API_URL);
        txt_celular = findViewById(R.id.celular_editdados);
        txt_telefone = findViewById(R.id.telefone_editdados);
        txt_cep = findViewById(R.id.cep_editdados);
        txt_logradouro = findViewById(R.id.logradouro_editdados);
        txt_bairro = findViewById(R.id.bairro_editdados);
        txt_numero = findViewById(R.id.numero_editdados);
        sp_estado = findViewById(R.id.estado_editdados);
        sp_cidade = findViewById(R.id.cidade_editdados);

        txt_telefone.addTextChangedListener(MaskEditUtil.mask(txt_telefone, MaskEditUtil.FORMAT_FONE)); //Mascara
        txt_celular.addTextChangedListener(MaskEditUtil.mask(txt_celular, MaskEditUtil.FORMAT_CELULAR)); //Mascara

        //Preferencias
        preferencias = getSharedPreferences( getString(R.string.key_preferences) , Context.MODE_PRIVATE);
        idClitente = preferencias.getInt("idCliente",0);

        new popular().execute();

        //Parte estado

        adapterEstado = new ArrayAdapter<String>(this , R.layout.support_simple_spinner_dropdown_item , new ArrayList<String>());
        adapterCidade = new ArrayAdapter<String>(this , R.layout.support_simple_spinner_dropdown_item , new ArrayList<String>());

        sp_estado.setAdapter(adapterEstado);
        sp_cidade.setAdapter(adapterCidade);

        new populaEstado().execute();
        new populaCidade().execute();

        estadoAtual = "Acre";

        sp_estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                estadoAtual = adapterEstado.getItem(i);
                adapterCidade.clear();
                new populaCidade().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    //Popula estado
    class populaEstado extends AsyncTask<Void, Void, Void> {

        ArrayList<String> arrayEstados = new ArrayList<>();
        String retornoAPI;
        boolean sucesso;

        @Override
        protected Void doInBackground(Void... voids) {

            retornoAPI = Http.get( API_URL + "/Endereco/Estado" );

            try {

                JSONObject object = new JSONObject(retornoAPI);

                sucesso = object.getBoolean("sucesso");

                if( sucesso ){

                    JSONArray estados = object.getJSONArray("resultado");

                    for( int i = 0 ; i < estados.length() ; i++ ){

                        JSONObject estado = estados.getJSONObject(i);

                        arrayEstados.add( estado.getString("nome"));

                    }

                }


            } catch (JSONException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapterEstado.addAll(arrayEstados);

        }

    }

    //Popula cidade
    class populaCidade extends AsyncTask<Void, Void, Void>{

        ArrayList<String> arrayCidades = new ArrayList<>();
        String retornoAPI;
        boolean sucesso;

        @Override
        protected Void doInBackground(Void... voids) {

            retornoAPI = Http.get( API_URL + "/Endereco/CidadePorEstado?estado=" + estadoAtual);

            try {

                JSONObject object = new JSONObject(retornoAPI);

                sucesso = object.getBoolean("sucesso");

                if( sucesso ){

                    JSONArray estados = object.getJSONArray("resultado");

                    for( int i = 0 ; i < estados.length() ; i++ ){

                        JSONObject estado = estados.getJSONObject(i);

                        arrayCidades.add( estado.getString("nomeCidade"));

                    }

                }


            } catch (JSONException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapterCidade.addAll(arrayCidades);

        }

    }

    //Seta o ID da cidade atual
    class getIdCidade extends AsyncTask<Void, Void, Void>{

        String cidadeSelecionada = adapterCidade.getItem(sp_cidade.getSelectedItemPosition());
        String retornoApi;
        int idCidade;

        @Override
        protected Void doInBackground(Void... voids) {

            retornoApi = Http.get(API_URL + "/Endereco/CidadeID?nomeCidade=" + cidadeSelecionada);

            try{

                JSONObject json = new JSONObject(retornoApi);

                if ( json.getBoolean("sucesso") ){

                    idCidade = json.getInt("resultado");

                }else{

                    idCidade = 1;

                }

            } catch (Exception e ){

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            idCidadeAtual = idCidade;
        }
    }

    //Seta os dados do usuario - Fora o endereco
    class popular extends AsyncTask<Void , Void, Void>{

        String retornoApi;
        @Override
        protected Void doInBackground(Void... voids) {

            retornoApi = Http.get(API_URL + "/Usuario/ListarPorID?id=" + idClitente );

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("teste" , retornoApi + idClitente);
            try{

                JSONObject obj = new JSONObject(retornoApi);

                if( obj.getBoolean("sucesso") ){

                    JSONObject usuario = obj.getJSONObject("resultado");

                    txt_telefone.setText( usuario.getString("telefone"));
                    txt_celular.setText( usuario.getString("celular"));


                }

            } catch( Exception e ) {

            }
        }
    }

    class popularEndereco extends AsyncTask<Integer , Void , Void>{
        @Override
        protected Void doInBackground(Integer... integers) {

            for( int endereco : integers ){

                Log.d("Endereco" , endereco + "");

            }


            return null;
        }
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
