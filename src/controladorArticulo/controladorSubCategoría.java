/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorArticulo;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Categorias;
import modelo.CategoriasDAO;
import modelo.DataSet;
import modelo.SubCategoriasDAO;
import modelo.Subcategorias;
import vistaArticulo.Vista_Sub_Categorias;

/**
 *
 * @author JOSE-MA
 */
public class controladorSubCategoría {

    private final Vista_Sub_Categorias vista_sub_cateogria;
    private final CategoriasDAO categoriasDAO;
    private final SubCategoriasDAO subCategoriasDAO;
    private List<Categorias> listaCategorias;
    private DataSet dt;
    private int codcategoria;
    
    
    public controladorSubCategoría(Vista_Sub_Categorias vista_sub_cateogiria) {
        this.vista_sub_cateogria = vista_sub_cateogiria;
        categoriasDAO = new CategoriasDAO();
        subCategoriasDAO = new SubCategoriasDAO();
        eventos();
        rellanarCombos();
        actualizarTabla();
    }

    public void iniciarVistaSubCategoria() {
        vista_sub_cateogria.setLocationRelativeTo(null);
        vista_sub_cateogria.setResizable(false);
        vista_sub_cateogria.setVisible(true);
    }

    private void rellanarCombos() {
        try {
            categoriasDAO.obtnerListaCategorias();
            listaCategorias = categoriasDAO.getListaCategorias();
            for (int i = 0; i < listaCategorias.size(); i++) {
                vista_sub_cateogria.combocat.addItem(listaCategorias.get(i).getDescripcion());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista_sub_cateogria, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eventos() {
        vista_sub_cateogria.salir.addActionListener((ActionEvent e) -> {
            vista_sub_cateogria.dispose();
        });

        vista_sub_cateogria.agregar.addActionListener((ActionEvent e) -> {
            if (vista_sub_cateogria.subcategoria.getText().equals("")) {
                JOptionPane.showMessageDialog(vista_sub_cateogria, "Ingresa el nombre de la categoía", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Subcategorias cat = new Subcategorias();
                cat.setDescripcion(vista_sub_cateogria.subcategoria.getText());
                cat.setCodcategorias(codcategoria);
                try {
                    if (subCategoriasDAO.insertarSubCategoria(cat) == 1) {
                        JOptionPane.showMessageDialog(vista_sub_cateogria, "Subcategoria insertarda", "Error", JOptionPane.INFORMATION_MESSAGE);
                        vista_sub_cateogria.subcategoria.setText("");
                        actualizarTabla();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vista_sub_cateogria, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        vista_sub_cateogria.combocat.addItemListener((ItemEvent e) -> {
            if(e.getStateChange()==ItemEvent.SELECTED){
                codcategoria=listaCategorias.get(vista_sub_cateogria.combocat.getSelectedIndex()).getCodcategoria();
            }            
        });

    }

    private void actualizarTabla() {
        try {
            dt = subCategoriasDAO.obtenerSubCategorias();
            dt.setCellEditable(false);
            vista_sub_cateogria.tabla.setModel(dt);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista_sub_cateogria, "Error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
