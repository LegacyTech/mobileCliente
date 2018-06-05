package br.com.viacaoasteroide.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 16255204 on 24/05/2018.
 */

public class CompraActivity extends AppCompatActivity {


    SharedPreferences preferencias;

    private int idViagem;
    private  String nomeViagem;
    private int idCliente;
    private String API_URL;

    private Spinner spn_poltronas;
    private Spinner spn_paradas;
    private ArrayAdapter<String> poltronas;
    private ArrayAdapter<String> paradas;
    private  ArrayList<Integer> paradas_id = new ArrayList<>();

    private RadioGroup forma_pgo;

    String poltrona;
    int idPontoParada;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de voltar

        API_URL = getString(R.string.API_URL);

        setContentView(R.layout.compra_activity);

        //Preferencias
        preferencias = getSharedPreferences( getString(R.string.key_preferences) , Context.MODE_PRIVATE);
        idCliente = preferencias.getInt("idCliente" , 0);

        idViagem = getIntent().getIntExtra("idViagem" , 0);
        nomeViagem = getIntent().getStringExtra( "nomeViagem");

        getSupportActionBar().setTitle( nomeViagem + " !" ); //Seta o titulo da acitivty


        spn_paradas = findViewById( R.id.sp_ponto_parada);
        spn_poltronas = findViewById( R.id.sp_poltrona);

        paradas = new ArrayAdapter<String>( this , R.layout.support_simple_spinner_dropdown_item , new ArrayList<String>());
        poltronas  = new ArrayAdapter<String>( this , R.layout.support_simple_spinner_dropdown_item , new ArrayList<String>());

        spn_paradas.setAdapter( paradas );
        spn_poltronas.setAdapter( poltronas );

        new populaParadas().execute();
        new populaPoltronas().execute();

        forma_pgo = findViewById(R.id.rdo_form_pag);



    }

    public class populaParadas extends AsyncTask<Void , Void, Void>{
        String retorno;
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> paradas_interna = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... voids) {

            retorno = Http.get( API_URL + "/Viagem/BuscarParadas?idViagem=" + idViagem );


            try {

                JSONObject obj = new JSONObject( retorno );
                JSONArray array_paradas = obj.getJSONArray("resultado");



                for( int i = 0 ; i < array_paradas.length() ; i++){

                    JSONObject parada = array_paradas.getJSONObject( i );

                    ids.add( parada.getInt("idPonto"));
                    paradas_interna.add( parada.getString("nomePonto"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            paradas.addAll( paradas_interna );
            paradas_id.addAll( ids );

        }

    }

    public class populaPoltronas extends AsyncTask<Void, Void, Void>{

        String retorno;
        ArrayList<String> lugares = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... voids) {

            retorno = Http.get( API_URL + "/Viagem/BuscarPoltronasCompradas?idViagem=" + idViagem );

            try {
                JSONObject obj = new JSONObject( retorno );
                JSONArray lugares_array = obj.getJSONArray("resultado");
                int total = lugares_array.getJSONObject( 0 ).getInt("qntLugares");

                for( int i = 1 ; i <= total ; i++ ){

                    if( notIn( i , lugares_array ) ){
                        lugares.add( i+"" );
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

            poltronas.addAll( lugares );

        }
    }

    public class comprar extends  AsyncTask<Void , Void , Void>{

        String retorno;
        boolean ok;
        @Override
        protected Void doInBackground(Void... voids) {

            HashMap<String , String > valores = new HashMap<>();

            valores.put("acento" , poltrona );
            valores.put("idCliente" , idCliente+"" );
            valores.put("idPontoParada" , idPontoParada +"" );
            valores.put("idViagem" , idViagem+"" );

            retorno = Http.post( API_URL + "/Passagem/Comprar" , valores);

            try {
                JSONObject obj = new JSONObject( retorno );
                ok = obj.getBoolean("sucesso");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if( ok ){
                alert("Passagem Comprada" , "O próximo passo é gerar o QRCode da passagem no seu perfil !" , true);
            }else{
                alert("Atenção" , "Não foi possível realizar sua compra , tente mais tarde." , false);
            }

        }
    }


    public boolean notIn( int lugar , JSONArray lugares_ocupados ){

        for( int i = 0 ; i < lugares_ocupados.length() ; i++ ){
           String lugar_ocupado;
            try {

                lugar_ocupado = lugares_ocupados.getJSONObject(i).getString("acento");

                if( lugar_ocupado.equals(""+lugar) ){
                    return false;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return true;
    }


    public void comprarPassagem(View view){


        RadioButton rdo_forma = findViewById(findViewById( this.forma_pgo.getCheckedRadioButtonId()).getId()); //PEGA O RADIO SELECIONADO
        String forma = rdo_forma.getText().toString(); //Pega o texto do radio

        //Finaliza a compra como boleto
        if( forma.equals("Boleto bancário") ){

            poltrona = spn_poltronas.getSelectedItem().toString();
            idPontoParada = paradas_id.get( spn_paradas.getSelectedItemPosition() );

            new comprar().execute();

        }else{
            Toast.makeText( getApplicationContext() , "Essa forma de pagamento ainda não esta disponível" , Toast.LENGTH_SHORT).show();
        }

    }

    private void alert(String titulo, String msg , final boolean sucesso){


        AlertDialog.Builder builder = new AlertDialog.Builder(this );

        builder.setMessage(msg)
                .setTitle(titulo);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if( sucesso ){
                    finish();
                    startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
                }else{
                    finish();
                }

            }
        });

        AlertDialog dialog = builder.create();

        //mostrar o alerta
        dialog.show();
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
