package br.com.nglauber.nextlevelapps;

import java.io.Serializable;

public class Album implements Serializable {
    String capa;
    String titulo;
    int ano;

    public Album(String capa, String titulo, int ano) {
        this.capa = capa;
        this.titulo = titulo;
        this.ano = ano;
    }
}


