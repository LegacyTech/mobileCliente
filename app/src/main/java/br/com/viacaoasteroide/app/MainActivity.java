package br.com.viacaoasteroide.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Matheus Alves on 29/03/2018.
 */

public class MainActivity extends Activity {


    private static final String MANTER_CONECTADO = "manter_conectado";
    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;

    private CheckBox manterConectado;
    private EditText txt_usuario, txt_senha;
    private Button btn_login;
    private ProgressBar progressBar;
    public String API_URL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        txt_usuario = findViewById(R.id.txt_usuario);
        txt_senha = findViewById(R.id.txt_senha);
        btn_login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_login);

        API_URL = getString(R.string.API_URL); // Pega a url de string padrão

        //Manter conectado
        manterConectado = (CheckBox) findViewById(R.id.manterConectado);

        //Estancias
        preferencias = getSharedPreferences( getString(R.string.key_preferences) , Context.MODE_PRIVATE);
        editor = preferencias.edit();

        boolean conectado = preferencias.getBoolean(MANTER_CONECTADO, false);
        if( conectado ){
            startActivity(new Intent(this, HomeActivity.class));
        }

    }


    //Task para o login
    private class LoginTask extends AsyncTask<Void,Void,Void>{

        Usuario usuario = new Usuario();
        String _usuario, _senha;
        String retornoApi, msg;
        boolean logado;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            _usuario = txt_usuario.getText().toString();
            _senha = txt_senha.getText().toString();

            //EFEITO BOTAO E PROGRESS
            btn_login.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {


            HashMap<String, String> valores = new HashMap<>();
            valores.put("email", _usuario);
            valores.put("senha", _senha);


            retornoApi = Http.post(API_URL + "/loginUser", valores);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //TIRA EFEITO BOTAO E PROGRESS
            progressBar.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);

            try {

                JSONObject objeto = new JSONObject(retornoApi);
                //Montanto objeto
                logado = objeto.getBoolean("sucesso");
                msg = objeto.getString("msg");

                if( logado ){

                    JSONObject objetoUsuario = new JSONObject( objeto.getString("usuario"));
                    usuario.setIdCliente( objetoUsuario.getInt("idCliente") );
                    usuario.setNome( objetoUsuario.getString("nome") );
                    usuario.setSobrenome( objetoUsuario.getString("sobrenome") );

                    //Altera as preferencias do usuario
                    editor.putBoolean( MANTER_CONECTADO, manterConectado.isChecked() );
                    editor.putInt("idCliente" , usuario.getIdCliente() );
                    editor.putString("nome" , usuario.getNome() );
                    editor.putString("sobrenome" , usuario.getSobrenome() );
                    editor.apply();

                    Intent intent = new Intent( getApplicationContext() , HomeActivity.class );
                    startActivity(intent);



                }else{

                    alert("Login", objeto.getString("msg") );

                }



            } catch (JSONException e) {

                e.printStackTrace();

            }

        }
    }

    private void alert(String titulo, String msg){


        AlertDialog.Builder builder = new AlertDialog.Builder(this );

        builder.setMessage(msg)
                .setTitle(titulo);

        builder.setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();

        //mostrar o alerta
        dialog.show();
    }

    public void logar(View view) {

        new LoginTask().execute();
        //startActivity(new Intent(getApplicationContext(), HomeActivity.class));

    }

    public void cadastrar(View view) {

        Intent intent = new Intent( getApplicationContext(), PerfilDadosActivity.class );
        intent.putExtra("novo" , true );
        startActivity( intent );

    }


}
