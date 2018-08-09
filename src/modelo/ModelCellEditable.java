/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author JOSE
 */
public class ModelCellEditable extends DefaultTableModel {

   public ModelCellEditable() {
        super(new Object[][]{}, new String[]{
            "Código", "Cantidad", "Descripcion", "Precio Unitario", "Subotal","IVA","Total"
        });
        super.addRow(new Object[]{});
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        int lasRow = this.getRowCount() - 1;
        return (row == lasRow && column == 0) || (row < lasRow && column == 1);
    }
}
