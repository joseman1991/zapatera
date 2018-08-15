/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author JOSE
 */
public class LeerCSV {

    private final char separador;
    private final char comillas;

    private final List<String[]> filas;
    private final List<Articulos> listaArticulos;

    // Constructor
    /**
     * Inicializa el constructor definiendo el separador de los campos y las
     * comillas usadas
     *
     * @param separador
     * @param comillas
     */
    public LeerCSV(char separador, char comillas) {
        this.separador = separador;
        this.comillas = comillas;
        listaArticulos = new ArrayList<>();
        filas = new ArrayList<>();
    }

    // Métodos
    /**
     * Lee un CSV que no contiene el mismo caracter que el separador en su texto
     * y sin comillas que delimiten los campos
     *
     * @param path Ruta donde está el archivo
     * @throws IOException
     */
    public void leerCSVSimple(String path) throws IOException {

        // Abro el .csv en buffer de lectura
        FileInputStream fis = new FileInputStream(path);
        InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
        BufferedReader bufferLectura;
        bufferLectura = new BufferedReader(isr);
        bufferLectura.readLine();
        // Leo una línea del archivo
        String linea = bufferLectura.readLine();

        while (linea != null) {
            // Separa la línea leída con el separador definido previamente
            String[] campos = linea.split(String.valueOf(this.separador));
            filas.add(campos);
            linea = bufferLectura.readLine();
        }

        // CIerro el buffer de lectura        
        bufferLectura.close();

    }

    public List<String[]> getFilas() {
        return filas;
    }

    public List<Articulos> getListaArticulos() {
        return listaArticulos;
    }

}
