package br.com.viacaoasteroide.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * Created by 16255204 on 04/04/2018.
 */

public class PerfilDadosActivity extends AppCompatActivity {

    Spinner dia, mes, ano;
    Spinner estado, cidade;
    Button btn_cad_usuario; //Botão de salvar
    ArrayList<String> array_dias = new ArrayList<>();
    ArrayList<String> array_meses = new ArrayList<>();
    ArrayList<String> array_anos = new ArrayList<>();
    ArrayAdapter<String> adapterDia, adapterMes, adapterAno;
    ArrayAdapter<String> adapterEstado , adapterCidade;
    EditText txt_nome, txt_sobrenome, txt_cpf, txt_telefone, txt_celular, txt_email, txt_senha, txt_cep, txt_logradouro, txt_bairro, txt_numero ;
    RadioGroup sexo;

    String API_URL;
    String estadoAtual;
    int idCidadeAtual = 0;
    boolean novo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfildados_activity);

        API_URL = getString(R.string.API_URL);

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

        //Parte estado
        estado = findViewById(R.id.estado_perfildados);
        cidade = findViewById(R.id.cidade_perfildados);

        adapterEstado = new ArrayAdapter<String>(this , R.layout.support_simple_spinner_dropdown_item , new ArrayList<String>());
        adapterCidade = new ArrayAdapter<String>(this , R.layout.support_simple_spinner_dropdown_item , new ArrayList<String>());

        estado.setAdapter(adapterEstado);
        cidade.setAdapter(adapterCidade);

        new populaEstado().execute();
        new populaCidade().execute();

        estadoAtual = "Acre";

        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        //Parte de dados gerais
        txt_nome = findViewById(R.id.nome_perfildados);
        txt_sobrenome = findViewById(R.id.sobrenome_perfildados);
        txt_telefone = findViewById(R.id.telefone_perfildados);
        txt_celular = findViewById(R.id.celular_perfildados);
        txt_email = findViewById(R.id.email_perfildados);
        txt_senha = findViewById(R.id.senha_perfildados);
        txt_cep = findViewById(R.id.cep_perfildados);
        txt_logradouro = findViewById(R.id.logradouro_perfildados);
        txt_bairro = findViewById(R.id.bairro_perfildados);
        txt_numero = findViewById(R.id.numero_perfildados);
        txt_cpf = findViewById(R.id.cpf_perfildados);
        sexo = findViewById(R.id.groupsexo_perfildados);

        txt_cpf.addTextChangedListener(MaskEditUtil.mask(txt_cpf, MaskEditUtil.FORMAT_CPF)); //Mascara
        txt_telefone.addTextChangedListener(MaskEditUtil.mask(txt_telefone, MaskEditUtil.FORMAT_FONE)); //Mascara
        txt_celular.addTextChangedListener(MaskEditUtil.mask(txt_celular, MaskEditUtil.FORMAT_CELULAR)); //Mascara

        //Verificação
        btn_cad_usuario = findViewById(R.id.btn_cad_usuario);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de voltar

        novo = getIntent().getBooleanExtra( "novo" , false ); //Fala se é cadastro novo ou atualização

        if( novo ){

            btn_cad_usuario.setText("Cadastrar");

        }

    }

    //Popula estado
    class populaEstado extends AsyncTask<Void, Void, Void>{

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

        String cidadeSelecionada = adapterCidade.getItem(cidade.getSelectedItemPosition());
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


    //Pega todos os dados referente ao usuario
    public Usuario getUsuario(){

        String dia = adapterDia.getItem(this.dia.getSelectedItemPosition());
        String mes = adapterDia.getItem(this.mes.getSelectedItemPosition());
        String ano = adapterDia.getItem(this.ano.getSelectedItemPosition());

        String sexo = findViewById( this.sexo.getCheckedRadioButtonId() ).toString(); //PEGA O RADIO SELECIONADO

        if( sexo.equals("Masculino") ){
            sexo = "M";
        }else{
            sexo = "F";
        }

        Usuario usuario = new Usuario();

        usuario.setNome( txt_nome.getText().toString() );
        usuario.setSobrenome( txt_sobrenome.getText().toString() );
        usuario.setCpf( txt_cpf.getText().toString() );
        usuario.setDtNasc( ano + "-" +  mes + "-" + dia);
        usuario.setSexo(sexo);
        usuario.setTelefone( txt_telefone.getText().toString() );
        usuario.setCelular( txt_celular.getText().toString() );
        usuario.setEmail( txt_email.getText().toString() );
        usuario.setSenha( txt_senha.getText().toString() );

        return usuario;
    }


    //Pega todos os dados referente a endereço
    public Endereco getEndereco(){

        Endereco endereco = new Endereco();
        new getIdCidade().execute();

        endereco.setIdTipoEndereco( 1 );
        endereco.setCep( txt_cep.getText().toString() );
        endereco.setLogradouro( txt_logradouro.getText().toString() );
        endereco.setBairro( txt_bairro.getText().toString() );
        endereco.setNumero( txt_numero.getText().toString() );
        endereco.setCodCidade( idCidadeAtual );

        return endereco;

    }

    public void salvar(View v){

        Usuario usuario = getUsuario(); //Pega o endereco
        Endereco endereco = getEndereco(); //Pega o usuario

        if(usuario.getNome( ).equals(""));
        usuario.getSobrenome( );
        usuario.getCpf( );
        usuario.getDtNasc( );
        usuario.getSexo( );
        usuario.getTelefone( );
        usuario.getCelular( );
        usuario.getEmail( );
        usuario.getSenha( );

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
