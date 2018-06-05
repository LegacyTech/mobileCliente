package br.com.viacaoasteroide.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Created by 16255204 on 05/06/2018.
 */

public class QrCodeActivity extends AppCompatActivity {

    int idPassagem;
    ImageView imageView;
    String API_URL;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão de voltar
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão de voltar

        imageView = findViewById(R.id.image_qrcode);

        idPassagem = getIntent().getIntExtra("idPassagem" , 0 );

        API_URL = getString(R.string.API_URL);

        new getPassagem().execute();
    }


    public class getPassagem extends AsyncTask<Void, Void, Void> {
        String retorno;
        @Override
        protected Void doInBackground(Void... voids) {

            retorno = Http.get(API_URL + "/Passagem/PegarPassagem?idPassagem=" + idPassagem );

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Teste" , retorno );

            //Gera o qrcode do retorno
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try{
                BitMatrix bitMatrix = multiFormatWriter.encode( retorno , BarcodeFormat.QR_CODE, 600 , 600 );
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap( bitMatrix );
                imageView.setImageBitmap( bitmap );
            }catch( WriterException e ){}
        }
    }

    public void voltar( View v ){
        finish();
    }

    //Metodos Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar

                startActivity(new Intent(this, PerfilActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)

                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem

                break;
            default:break;
        }
        return true;
    }
}
