/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorArticulo;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.Categorias;
import modelo.CategoriasDAO;
import modelo.DataSet;
import vistaArticulo.Vista_Categorias;

/**
 *
 * @author JOSE-MA
 */
public class controladorCategoría {

    private final Vista_Categorias vista_Categorias;
    private final CategoriasDAO categoriasDAO;
    private DataSet dt;

    public controladorCategoría(Vista_Categorias vista_Categorias) {
        this.vista_Categorias = vista_Categorias;
        categoriasDAO = new CategoriasDAO();
    }

    public void iniciarVistaCategoria() {
        eventos();
        actualizarTabla();
        vista_Categorias.setLocationRelativeTo(null);
        vista_Categorias.setResizable(false);
        vista_Categorias.setVisible(true);        
    }

    private void eventos() {
       
        vista_Categorias.cerrar.addActionListener((ActionEvent e) -> {
            vista_Categorias.dispose();            
        });
        
        vista_Categorias.agregar.addActionListener((ActionEvent e) -> {
            if (vista_Categorias.categoria.getText().equals("")) {
                JOptionPane.showMessageDialog(vista_Categorias, "Ingresa el nombre de la categoría", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Categorias cat = new Categorias();
                cat.setDescripcion(vista_Categorias.categoria.getText());
                try {
                    if (categoriasDAO.insertarCategoria(cat) == 1) {
                        JOptionPane.showMessageDialog(vista_Categorias, "Categoría insertarda", "Error", JOptionPane.INFORMATION_MESSAGE);
                        actualizarTabla();
                        vista_Categorias.categoria.setText("");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vista_Categorias, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    
    private void actualizarTabla(){
         try {
            dt = categoriasDAO.obtenerCategorias();
            dt.setCellEditable(false);
            vista_Categorias.tabla.setModel(dt);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista_Categorias, "Error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
