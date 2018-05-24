package br.com.viacaoasteroide.app;

/**
 * Created by Matheus Alves on 04/04/2018.
 */

public class Viagem {

    private int idViagem;
    private String dtPartida;
    private String hrPartida;
    private String hrChegada;
    private String hrChegadaReal;
    private String descricao;
    private double preco;
    private int tamanhoRota;
    private String pontoPartida;
    private String pontoChegada;
    private int idOnibus;
    private int idMotorista;
    private int idEpoca;
    private boolean finalizada;
    private String poltrona;
    private String paradas;

    //CONSTRUCT METHODS
    public Viagem( int idViagem,
                   String pontoPartida,
                   String pontoChegada,
                   double preco, String dtPartida,
                   String hrPartida ) {

        this.idViagem = idViagem;
        this.pontoPartida = pontoPartida;
        this.pontoChegada = pontoChegada;
        this.preco = preco;
        this.dtPartida = dtPartida;
        this.hrPartida = hrPartida;

    }

    public Viagem(){}

    //GETTERS AND SETTERS


    public String getParadas() {
        return paradas;
    }

    public void setParadas(String paradas) {
        this.paradas = paradas;
    }

    public int getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(int idViagem) {
        this.idViagem = idViagem;
    }

    public String getDtPartida() {
        return dtPartida;
    }

    public void setDtPartida(String dtPartida) {
        this.dtPartida = dtPartida;
    }

    public String getHrPartida() {
        return hrPartida;
    }

    public void setHrPartida(String hrPartida) {
        this.hrPartida = hrPartida;
    }

    public String getHrChegada() {
        return hrChegada;
    }

    public void setHrChegada(String hrChegada) {
        this.hrChegada = hrChegada;
    }

    public String getHrChegadaReal() {
        return hrChegadaReal;
    }

    public void setHrChegadaReal(String hrChegadaReal) {
        this.hrChegadaReal = hrChegadaReal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getTamanhoRota() {
        return tamanhoRota;
    }

    public void setTamanhoRota(int tamanhoRota) {
        this.tamanhoRota = tamanhoRota;
    }

    public String getPontoPartida() {
        return pontoPartida;
    }

    public void setPontoPartida(String pontoPartida) {
        this.pontoPartida = pontoPartida;
    }

    public String getPontoChegada() {
        return pontoChegada;
    }

    public void setPontoChegada(String pontoChegada) {
        this.pontoChegada = pontoChegada;
    }

    public int getIdOnibus() {
        return idOnibus;
    }

    public void setIdOnibus(int idOnibus) {
        this.idOnibus = idOnibus;
    }

    public int getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(int idMotorista) {
        this.idMotorista = idMotorista;
    }

    public int getIdEpoca() {
        return idEpoca;
    }

    public void setIdEpoca(int idEpoca) {
        this.idEpoca = idEpoca;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public String getPoltrona() {
        return poltrona;
    }

    public void setPoltrona(String poltrona) {
        this.poltrona = poltrona;
    }
}
