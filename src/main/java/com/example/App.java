package com.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main (String [] args){

        String provincias = "MENDOZA, SAN JUAN, SAN LUIS, CORDOBA, BUENOS AIRES.";

        mostrarProvinciasSeparadas(provincias);
    }


    static void mostrarProvinciasSeparadas(String provincias){

        List<String> listaDeProvincias = new ArrayList();
        listaDeProvincias = Arrays.asList(provincias.split(", "));

        listaDeProvincias.stream().forEach(provincia-> System.out.println(provincia.replace(".","")) );
    }
}
