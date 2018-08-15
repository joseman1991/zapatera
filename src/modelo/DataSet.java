package modelo;



import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class DataSet extends DefaultTableModel {

    private boolean  cellEditable;
    public DataSet() {
        super();
        cellEditable=true;
    }

    public void setCellEditable(boolean cellEditable) {
        this.cellEditable = cellEditable;
    }

        
    @Override
    public boolean isCellEditable(int row, int column) {
       //all cells false
       return cellEditable;
    }
    
    public void load(ResultSet resultado) throws SQLException {
        List<Object[]> filas = new ArrayList<>();

        ResultSetMetaData metaDatos = resultado.getMetaData();
        int numeroColumnas = metaDatos.getColumnCount();

        String[] etiquetas = new String[numeroColumnas];

        for (int i = 0; i < numeroColumnas; i++) {
            etiquetas[i] = metaDatos.getColumnLabel(i + 1).toUpperCase();
        }
        this.setColumnIdentifiers(etiquetas);

        while (resultado.next()) {
            Object fila[] = new Object[numeroColumnas];
            for (int i = 0; i < numeroColumnas; i++) {
                fila[i] = resultado.getObject(i+1);
            }
            filas.add(fila);
        }
        
        
        for (int i = 0; i < filas.size(); i++) {
            this.addRow(filas.get(i));
        }
        
    }

}
