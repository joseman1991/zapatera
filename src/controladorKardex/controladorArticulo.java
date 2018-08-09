/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorKardex;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.AnchoColumna;
import modelo.ArticuloDAO;
import modelo.Articulos;
import modelo.Categorias;
import modelo.CategoriasDAO;
import modelo.DataSet;
import modelo.SubCategoriasDAO;
import modelo.Subcategorias;
import vistaMovimiento.Articulo;

public class controladorArticulo {

    private DataSet dt;    
    private String codArt;

    private List<Categorias> listaCat;

    private final Articulo vista_producto;
    private final ArticuloDAO articuloDAO;
    private Articulos articulo;

    private int codCat;
    private List<Subcategorias> li;

    public controladorArticulo(Articulo vista_producto) {
        this.vista_producto = vista_producto;
        articuloDAO = new ArticuloDAO();
        eventos();
        buscar();
        dt.setCellEditable(false);
        rellenarCombos();
        this.vista_producto.tabla.setRowHeight(25);
        this.vista_producto.tabla.setGridColor(Color.black);
        this.vista_producto.tabla.setShowGrid(true);

         int medida[] = {100, 300, 100, 100,100,100};
        AnchoColumna ac = new AnchoColumna(this.vista_producto.tabla, medida);
    }

    public void IniciarVistaProducto() {
        vista_producto.buscarProducto.requestFocus();
        vista_producto.setVisible(true);
    }

    private void rellenarCombos() {
        try {
            CategoriasDAO catego = new CategoriasDAO();
            catego.obtnerListaCategorias();
            vista_producto.categorias.addItem("SELECCIONA...");
            listaCat = catego.getListaCategorias();
            for (int i = 0; i < listaCat.size(); i++) {
                Categorias get = listaCat.get(i);
                vista_producto.categorias.addItem(get.getDescripcion());
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void eventos() {
        vista_producto.buscarProducto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscar();
            }
        });

        vista_producto.tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int rowSelected = vista_producto.tabla.getSelectedRow();
                codArt = dt.getValueAt(rowSelected, 0).toString();

                vista_producto.nombre.setText(dt.getValueAt(rowSelected, 1).toString());
//                vista_producto.cantidad.setValue(1);

                if (e.getClickCount() == 2) {
                    seleccionar();
                }
            }
        });

        vista_producto.seleciona.addActionListener((ActionEvent e) -> {
            if (vista_producto.tabla.getSelectedRow() >= 0) {
                seleccionar();
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un artículo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        vista_producto.categorias.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                try {
                    int index = vista_producto.categorias.getSelectedIndex() - 1;
                    vista_producto.subcategoria.removeAllItems();
                    SubCategoriasDAO subc = new SubCategoriasDAO();
                    if (index >= 0) {
                        subc.obtnerListaSubCategorias(listaCat.get(index).getCodcategoria());
                    } else {
                        buscar();
                    }
                    li = subc.getListaSubCategorias();
                    for (int i = 0; i < li.size(); i++) {
                        Subcategorias get = li.get(i);
                        vista_producto.subcategoria.addItem(get.getDescripcion());
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            vista_producto.buscarProducto.requestFocus();

        });

        vista_producto.subcategoria.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int index = vista_producto.subcategoria.getSelectedIndex();
                if (index >= 0) {
                    codCat = li.get(index).getCodsubcategoria();
                }
                buscar();
            }
            vista_producto.buscarProducto.requestFocus();

        });
    }

  
    
    private void seleccionar() {
        try {
            articulo=articuloDAO.obtenerArticulo(codArt);
            vista_producto.dispose();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void buscar() {
        try {
            if (vista_producto.subcategoria.getItemCount() > 0) {
                dt = articuloDAO.obtenerArticulos(vista_producto.buscarProducto.getText(), codCat);
            } else {
                dt = articuloDAO.obtenerArticulos(vista_producto.buscarProducto.getText());
            }
            vista_producto.tabla.setModel(dt);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Articulos getArticulo() {
        return articulo;
    }

    

}
