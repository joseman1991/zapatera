/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorMovimientos;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.ArticuloDAO;
import modelo.Articulos;
import modelo.Categorias;
import modelo.CategoriasDAO;
import modelo.DataSet;
import modelo.Detallemovimiento;
import modelo.SubCategoriasDAO;
import modelo.Subcategorias;
import vistaMovimiento.Articulo;

public class ControladorArticulo {

    static int orden = 1;
    private DataSet dt;
    private float cantidad;

    public char naturaleza;

    private List<Detallemovimiento> listaDetelle;
    private Detallemovimiento detalle;

    private List<Categorias> listaCat;

    private final Articulo vista_producto;
    private final ArticuloDAO articuloDAO;
    private Articulos articulo;

    private int codCat;
    private List<Subcategorias> li;

    public ControladorArticulo(Articulo vista_producto) {
        this.vista_producto = vista_producto;
        articuloDAO = new ArticuloDAO();
        eventos();
        buscar();
        dt.setCellEditable(false);
        rellenarCombos();
        naturaleza = 'I';
    }

    public void IniciarVistaProducto() {
        vista_producto.buscarProducto.requestFocus();
//        spinerModel(vista_producto.cantidad);
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
                String codArt = dt.getValueAt(rowSelected, 0).toString();
                if (buscarDetalle(codArt)) {
                    vista_producto.nombre.setText(detalle.getArticulos().getDescripcion());

                } else {
                    vista_producto.nombre.setText(dt.getValueAt(rowSelected, 1).toString());
                }

                if (e.getClickCount() == 2) {
                    verficiar();
                }
            }
        });

        vista_producto.seleciona.addActionListener((ActionEvent e) -> {
            if (vista_producto.tabla.getSelectedRow() >= 0) {
                verficiar();
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

    private boolean buscarDetalle(String codigoPro) {
        for (int i = 0; i < listaDetelle.size(); i++) {
            if (listaDetelle.get(i).getCodarticilo().equals(codigoPro)) {
                detalle = listaDetelle.get(i);
                return true;
            }
        }
        return false;
    }

    private void verficiar() {
        if (naturaleza == 'E') {
            int rowSelected = vista_producto.tabla.getSelectedRow();
            float stock = Float.parseFloat(dt.getValueAt(rowSelected, 4).toString());
            if (stock > 0) {
                seleccionar();
            } else {
                JOptionPane.showMessageDialog(null, "Artículo sin stock", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            seleccionar();
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

    private void seleccionar() {
        try {
            int rowSelected = vista_producto.tabla.getSelectedRow();
            String codArt = dt.getValueAt(rowSelected, 0).toString();
            articulo = articuloDAO.obtenerArticulo(codArt);
            detalle = new Detallemovimiento();
            detalle.setCodarticilo(codArt);
            detalle.setCantidad(1);
//            detalle.setCosto(articulo.getCosto());
            vista_producto.dispose();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setListaDetelle(List<Detallemovimiento> listaDetelle) {
        this.listaDetelle = listaDetelle;
    }

    public Detallemovimiento getDetalle() {
        return detalle;
    }

}
