/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author JOSE
 */
public class LeerXLSX {

    private final int count;

    private final List<String[]> filas;

    public LeerXLSX() {
        this.count = 8;

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
    public void leerXLSX(String path) throws IOException {

        // Abro el .csv en buffer de lectura
        FileInputStream fis = new FileInputStream(path);
        //InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");

        XSSFWorkbook worbook = new XSSFWorkbook(fis);

        XSSFSheet sheet = worbook.getSheetAt(0);
        //obtener todas las filas de la hoja excel
        Iterator<Row> rowIterator = sheet.iterator();

        Row row;
        rowIterator.next();
        filas.clear();
        int m = 1;
        while (rowIterator.hasNext()) {
            String[] fila = new String[count];
            int i = 0;

            row = rowIterator.next();
            
            //se obtiene las celdas por fila
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell cell;
            //se recorre cada celda
            fila[i++] = m + "";
            m++;
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                fila[i] = cell.toString();
                i++;
            }
            filas.add(fila);
        }
        for (int i = 0; i < filas.size(); i++) {
            String[] get = filas.get(i);
            int k = 0;
            for (String string : get) {
                if (string == null) {
                    k++;//                    
                } else {
                    if (string.trim().equals("")) {
                        k++;
                    }
                }
            }

            if (k == count-1) {
                filas.subList(i, filas.size()).clear();
            }
        }

        // CIerro el buffer de lectura        
    }

    public List<String[]> getFilas() {
        return filas;
    }

}
