package br.com.viacaoasteroide.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Matheus Alves on 04/04/2018.
 */

public class ViagemAdapter extends ArrayAdapter<Viagem> {

    TextView origem, destino, preco, dtIda, hrSaida;
    ImageView capa;
    boolean pg_principal;
    String IMAGE_URL;

    public ViagemAdapter (Context context, ArrayList<Viagem> lista, boolean pg_principal , String url){
        super(context,0,lista);
        this.pg_principal = pg_principal;
        IMAGE_URL = url;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent ) {
        View v = convertView;

        if( v == null ) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.listview_viagem, null);
        }

        origem = (TextView) v.findViewById(R.id.list_origem);
        destino = (TextView) v.findViewById(R.id.list_destino);
        preco = (TextView) v.findViewById(R.id.list_preco);
        dtIda = (TextView) v.findViewById(R.id.list_dtIda);
        hrSaida = (TextView) v.findViewById(R.id.list_hrSaida);
        capa = (ImageView) v.findViewById(R.id.capa_viagem);

        if( !pg_principal){
            capa.setVisibility(View.GONE);
        }

        Viagem viagem = getItem( position );

        origem.setText( "" +  viagem.getPontoPartida() );
        destino.setText( "" + viagem.getPontoChegada() );
        preco.setText(  "" + viagem.getPreco() );
        dtIda.setText( viagem.getDtPartida() );
        hrSaida.setText( viagem.getHrPartida() );

        Picasso.with(getContext()).load(IMAGE_URL + viagem.getImagem() ).into(capa);

        return v;
    }

}
