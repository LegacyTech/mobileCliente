package br.com.viacaoasteroide.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by 16255204 on 10/04/2018.
 */

public class ViagemActivity extends AppCompatActivity {

    private TextView titulo_viagem , txt_descricao, txt_origem, txt_destino , txt_data;
    private TextView txt_hrSaida, txt_hrChegada, txt_preco, txt_paradas, txt_poltronas;
    private Button btn;
    private LinearLayout linear_poltrona;
    private ImageView capa;

    private int idViagem;
    boolean passagem_comprada;
    public String API_URL;
    public String IMAGE_URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viagem_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de voltar

        capa = findViewById(R.id.capa_viagem);
        titulo_viagem = findViewById(R.id.titulo_viagem);
        txt_descricao = findViewById(R.id.descricao_viagem);
        txt_origem = findViewById(R.id.origem_viagem);
        txt_destino = findViewById(R.id.destino_viagem);
        txt_data = findViewById(R.id.data_viagem);
        txt_hrSaida = findViewById(R.id.hrSaida_viagem);
        txt_hrChegada = findViewById(R.id.hrChegada_viagem);
        txt_preco = findViewById(R.id.preco_viagem);
        txt_paradas = findViewById(R.id.paradas_viagem);
        txt_poltronas = findViewById(R.id.poltrona_viagem);

        linear_poltrona = findViewById(R.id.linear_poltrona);

        btn = findViewById(R.id.btn_viagem);

        idViagem = getIntent().getIntExtra("idViagem" , 0); //Pega o id que vem da HomeActivity ou PerfilActivity
        passagem_comprada = getIntent().getBooleanExtra("passagem_comprada" , false);

        if( passagem_comprada ){
            btn.setText("Gerar QRCode");
            linear_poltrona.setVisibility(View.VISIBLE);
            String poltrona = getIntent().getStringExtra("poltrona");
            txt_poltronas.setText( poltrona );

        }

        API_URL = getString(R.string.API_URL);
        IMAGE_URL = getString( R.string.IMAGE_URL );

        new popularViagem().execute();

    }

    public class popularViagem extends AsyncTask<Void,Void,Void>{

        private String retornoApi;
        private Viagem viagem;

        @Override
        protected Void doInBackground(Void... voids) {

            HashMap<String , String> valores = new HashMap<>();
            valores.put("id" , "" + idViagem);

            retornoApi = Http.post(API_URL + "/Viagem/BuscarViagemPorID" , valores);

            try {

                JSONObject jsonObject = new JSONObject(retornoApi);

                JSONArray jsonArray = jsonObject.getJSONArray("resultado");

                for( int i = 0; i < jsonArray.length(); i++){

                    JSONObject jsonViagem = jsonArray.getJSONObject(i);

                    viagem = new Viagem
                            (
                                jsonViagem.getInt("idViagem"),
                                jsonViagem.getString("origem") ,
                                jsonViagem.getString("destino") ,
                                jsonViagem.getDouble("preco"),
                                jsonViagem.getString("dtIda"),
                                jsonViagem.getString("hrIda")
                            );
                    viagem.setDescricao(jsonViagem.getString("descricao"));
                    viagem.setHrChegada(jsonViagem.getString("hrChegada"));
                    viagem.setImagem( jsonViagem.getString("imagem1"));
                }

                jsonObject = new JSONObject( Http.get(API_URL + "/Viagem/BuscarParadas?idViagem=" + idViagem) );

                jsonArray = jsonObject.getJSONArray("resultado");
                String paradas = "";

                for( int i = 0 ; i < jsonArray.length() ; i++ ){
                    JSONObject parada = jsonArray.getJSONObject( i );
                    if( i != 0)
                        paradas += " , " + parada.getString("nomePonto");
                    else
                        paradas += parada.getString("nomePonto");
                }
                viagem.setParadas( paradas );

            } catch (JSONException e) {

                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            popularActivity(viagem);

        }
    }

    //Metodos Menu
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

    public void comprar( View view ){

        if( passagem_comprada ){
            Toast.makeText( this , "Gerar QR Code" , Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent( this , CompraActivity.class );
            intent.putExtra("idViagem" , idViagem);
            intent.putExtra("nomeViagem" , titulo_viagem.getText().toString() );
            startActivity( intent );
        }

    }

    public void popularActivity( Viagem viagem ){

        Picasso.with(this).load(IMAGE_URL +"/viacao_asteroide/" + viagem.getImagem() ).into(capa);
        titulo_viagem.setText(viagem.getPontoChegada());
        txt_descricao.setText(viagem.getDescricao());
        txt_origem.setText(viagem.getPontoPartida());
        txt_destino.setText(viagem.getPontoChegada());
        txt_data.setText(viagem.getDtPartida());
        txt_hrSaida.setText(viagem.getHrPartida());
        txt_hrChegada.setText(viagem.getHrChegada());
        txt_preco.setText("R$" + viagem.getPreco());
        txt_paradas.setText( viagem.getParadas() );

    }


}
