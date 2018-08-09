/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorArticulo;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import modelo.ArticuloDAO;
import modelo.Articulos;
import modelo.Categorias;
import modelo.CategoriasDAO;
import modelo.DataSet;
import modelo.EstadoDAO;
import modelo.SubCategoriasDAO;
import modelo.Subcategorias;
import vistaArticulo.Vista_Categorias;
import vistaArticulo.Vista_Sub_Categorias;
import vistaArticulo.Vista_articulo;

public class controladorArticulo {

    private DataSet dt;

    public int op;
    private int codsubc;

    private Vista_Categorias vista_categorias;
    private Vista_Sub_Categorias vista_subcategorias;

    private List<Categorias> listaCat;

    private final Frame frameModal;

    private final EstadoDAO estadoDAO;

    private final Vista_articulo vista_articulo;
    private final ArticuloDAO articuloDAO;
    private Articulos articulo;
    private List<Subcategorias> li;
    private final SubCategoriasDAO subc;

    private controladorCategoría ctrl_categoria;
    private controladorSubCategoría ctrl_sub_categoria;

    public controladorArticulo(Vista_articulo vista_articulo) {
        this.vista_articulo = vista_articulo;
        op = 1;
        articuloDAO = new ArticuloDAO();
        buscar();
        dt.setCellEditable(false);
        articulo = new Articulos();
        estadoDAO = new EstadoDAO();
        subc = new SubCategoriasDAO();
        spinerModel(this.vista_articulo.costo);
        spinerModel(this.vista_articulo.pvp);
        spinerModel(this.vista_articulo.stock);
        frameModal = JOptionPane.getFrameForComponent(vista_subcategorias);
    }

    public void IniciarVistaArticulo() {
        switch (op) {
            case 1:
                vista_articulo.buscarProducto.setVisible(false);
                vista_articulo.bustext.setVisible(false);
                vista_articulo.buscaT.setVisible(false);
                vista_articulo.stock.setEnabled(false);
                vista_articulo.costo.setEnabled(false);
                break;

            case 2:
                vista_articulo.buscarProducto.setVisible(true);
                vista_articulo.bustext.setVisible(true);
                JSpinner.NumberEditor snm = (JSpinner.NumberEditor) vista_articulo.stock.getEditor();
                JFormattedTextField jf = snm.getTextField();
                jf.setEditable(false);
                SpinnerNumberModel model = new SpinnerNumberModel();
                model.setStepSize(0);
                vista_articulo.stock.setModel(model);
                vista_articulo.buscarProducto.requestFocus();
                vista_articulo.accion.setText("Actualizar");
                vista_articulo.titulo.setText("ACTUALIZAR ARTÍCULO");
                vista_articulo.aggcat.setVisible(false);
                vista_articulo.aggsubcat.setVisible(false);
                break;

        }

        rellenarCombos();
        vista_articulo.grabaiva.setSelectedItem("SI");
        articulo.setGrabaiva(vista_articulo.grabaiva.getSelectedItem().toString().charAt(0));
        eventos();
        vista_articulo.setVisible(true);
    }

    private void rellenarCombos() {
        try {
            vista_articulo.categorias.removeAllItems();
            vista_articulo.subcategoria.removeAllItems();
            vista_articulo.estados.removeAllItems();
            CategoriasDAO catego = new CategoriasDAO();
            catego.obtnerListaCategorias();
            listaCat = catego.getListaCategorias();
            for (int i = 0; i < listaCat.size(); i++) {
                Categorias get = listaCat.get(i);
                vista_articulo.categorias.addItem(get.getDescripcion());
            }
            vista_articulo.categorias.setSelectedItem(listaCat.get(0).getDescripcion());
            articulo.setCodcategoria(listaCat.get(0).getCodcategoria());
            if (op == 1) {
                vista_articulo.codArt.setText(articuloDAO.nextCodigoArt());
            }

            estadoDAO.getObtnerEstados();
            for (int i = 0; i < estadoDAO.getListaEstados().size(); i++) {
                vista_articulo.estados.addItem(estadoDAO.getListaEstados().get(i).getDescripcion());
            }
            articulo.setCodestado(estadoDAO.getListaEstados().get(0).getCodestado().charAt(0));
            subc.obtnerListaSubCategorias(listaCat.get(0).getCodcategoria());
            li = subc.getListaSubCategorias();
            for (int i = 0; i < li.size(); i++) {
                Subcategorias get = li.get(i);
                vista_articulo.subcategoria.addItem(get.getDescripcion());
            }
            if (li.size() > 0) {
                articulo.setCodsubcategoria(li.get(0).getCodsubcategoria());
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void eventos() {
        vista_articulo.cancelar.addActionListener((ActionEvent e) -> {
            vista_articulo.dispose();
        });

        switch (op) {
            case 1:
                vista_articulo.aggcat.addActionListener((ActionEvent e) -> {
                    inicializarCategorias();

                    vista_categorias.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            rellenarCombos();
                        }
                    });
                    ctrl_categoria.iniciarVistaCategoria();
                });

                vista_articulo.aggsubcat.addActionListener((ActionEvent e) -> {
                    inicializarSubCategorias();

                    vista_subcategorias.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            rellenarCombos();
                        }
                    });
                    ctrl_sub_categoria.iniciarVistaSubCategoria();
                });
                break;

            case 2:
                vista_articulo.tabla.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        int rowSelected = vista_articulo.tabla.getSelectedRow();
                        String codArt = dt.getValueAt(rowSelected, 0).toString();
                        try {
                            articulo = articuloDAO.obtenerArticulo(codArt);
                            vista_articulo.codArt.setText(articulo.getCodarticulo());
                            vista_articulo.descripcion.setText(articulo.getDescripcion());
                            vista_articulo.costo.setValue(articulo.getCosto());
                            vista_articulo.stock.setValue(articulo.getStock());
                            vista_articulo.pvp.setValue(articulo.getPvp());
                            vista_articulo.estados.setSelectedItem(articulo.getEstado().getDescripcion());
                            vista_articulo.categorias.setSelectedItem(articulo.getCategorias().getDescripcion());
                            vista_articulo.subcategoria.setSelectedItem(articulo.getSubcategorias().getDescripcion());
                            if (articulo.getGrabaiva() == 'S') {
                                vista_articulo.grabaiva.setSelectedIndex(0);
                            } else {
                                vista_articulo.grabaiva.setSelectedIndex(1);
                            }
                            vista_articulo.descripcion.requestFocus();
                            vista_articulo.descripcion.selectAll();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(vista_articulo, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                vista_articulo.buscarProducto.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (vista_articulo.buscaT.isSelected()) {
                            op = 1;
                        }
                        buscar();
                        op = 2;
                    }
                });
                vista_articulo.buscarProducto.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (vista_articulo.buscaT.isSelected()) {
                            op = 1;
                        }
                        buscar();
                        op = 2;
                    }
                });

                vista_articulo.buscaT.addActionListener((ActionEvent e) -> {
                    if (vista_articulo.buscaT.isSelected()) {
                        vista_articulo.buscaT.setToolTipText("Busca todo, sin importar categorìa");
                        vista_articulo.buscaT.setText("Buscar todos");
                        op = 1;
                    } else {
                        vista_articulo.buscaT.setToolTipText("Busca por categoría");
                        vista_articulo.buscaT.setText("Buscar por categorías");
                        vista_articulo.categorias.setSelectedIndex(-1);
                        vista_articulo.categorias.setSelectedIndex(0);
                    }
                    buscar();
                    op = 2;
                });
                break;
        }

        vista_articulo.accion.addActionListener((ActionEvent e) -> {
            try {
                if (vista_articulo.descripcion.getText().equals("")) {
                    JOptionPane.showMessageDialog(vista_articulo, "Ingresa el nombre del artículo", "Error", JOptionPane.ERROR_MESSAGE);
                } else if ((double) vista_articulo.pvp.getValue() == 0) {
                    JOptionPane.showMessageDialog(vista_articulo, "Ingresa el precio de venta al público del artículo", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (vista_articulo.subcategoria.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(vista_articulo, "Selecciona una subcategoría", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (vista_articulo.categorias.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(vista_articulo, "Selecciona una categoría", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    articulo.setDescripcion(vista_articulo.descripcion.getText());
                    articulo.setCosto((double) vista_articulo.costo.getValue());
                    articulo.setPvp((double) vista_articulo.pvp.getValue());
                    int exc = 0;
                    String operacion = "guardado";
                    switch (op) {
                        case 1:
                            articulo.setCodarticulo(articuloDAO.nextCodigoArt());
                            articulo.setStock((double) vista_articulo.stock.getValue());
                            System.out.println(articulo.getCodestado());
                            exc = articuloDAO.insertarArticulo(articulo);
                            break;

                        case 2:
                            if (vista_articulo.codArt.getText().equals("")) {
                                JOptionPane.showMessageDialog(vista_articulo, "Selecciona el artículo que deseas actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                            } else {
                                exc = articuloDAO.actualizarArticulo(articulo);
                                operacion = "actualizado";
                            }
                            break;
                    }

                    if (exc == 1) {
                        if (vista_articulo.buscaT.isSelected()) {
                            op = 1;
                        }
                        buscar();
                        op = 2;
                        JOptionPane.showMessageDialog(vista_articulo, "Artículo " + operacion + " exitosamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
                        vista_articulo.codArt.setText(articuloDAO.nextCodigoArt());
                        vista_articulo.descripcion.setText("");
                        vista_articulo.costo.setValue(0);
                        vista_articulo.pvp.setValue(0);
                        vista_articulo.stock.setValue(0);
                    } else {
                        JOptionPane.showMessageDialog(vista_articulo, "Ha ocurrido un error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vista_articulo, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        vista_articulo.categorias.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                try {
                    int index = vista_articulo.categorias.getSelectedIndex();
                    vista_articulo.subcategoria.removeAllItems();
                    articulo.setCodcategoria(listaCat.get(index).getCodcategoria());
                    subc.obtnerListaSubCategorias(listaCat.get(index).getCodcategoria());
                    li = subc.getListaSubCategorias();
                    for (int i = 0; i < li.size(); i++) {
                        Subcategorias get = li.get(i);
                        vista_articulo.subcategoria.addItem(get.getDescripcion());
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            switch (op) {
                case 1:
                    vista_articulo.descripcion.requestFocus();
                    break;

                case 2:
                    vista_articulo.buscarProducto.requestFocus();
                    break;
            }
        });

        vista_articulo.estados.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                try {
                    articulo.setCodestado(e.getItem().toString().charAt(0));
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vista_articulo, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        vista_articulo.subcategoria.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int index = vista_articulo.subcategoria.getSelectedIndex();
                codsubc = li.get(index).getCodsubcategoria();
                try {
                    articulo.setCodsubcategoria(codsubc);
                } catch (SQLException ex) {
                }
                switch (op) {
                    case 1:
                        vista_articulo.descripcion.requestFocus();
                        buscar();
                        break;

                    case 2:
                        vista_articulo.buscarProducto.requestFocus();
                        if (vista_articulo.buscaT.isSelected()) {
                            op = 1;
                            buscar();
                            op = 2;
                        }
                        break;
                }

            }
        });

        vista_articulo.grabaiva.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                articulo.setGrabaiva(e.getItem().toString().charAt(0));
            }
        });

    }

    private void buscar() {
        try {
            switch (op) {
                case 1:
                    dt = articuloDAO.obtenerArticulos(vista_articulo.buscarProducto.getText());
                    break;

                case 2:
                    dt = articuloDAO.obtenerArticulos(vista_articulo.buscarProducto.getText(), codsubc);
                    break;
            }

            vista_articulo.tabla.setModel(dt);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void spinerModel(JSpinner js) {
        double min = 0.00;
        double value = 0.00;
        double stepSize = 0.01;
        SpinnerNumberModel model = new SpinnerNumberModel();
        model.setValue(value);
        model.setMinimum(min);
        model.setStepSize(stepSize);

        js.setModel(model);

        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) js.getEditor();
        DecimalFormat format = editor.getFormat();

        format.setMinimumFractionDigits(2);
        editor.getTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                if (evt.getKeyChar() == '.') {
                    evt.consume();
                    if (editor.getTextField().getText().contains(",")) {
                        evt.consume();
                    } else {
                        editor.getTextField().setText(editor.getTextField().getText() + ",");
                    }
                }

                if (evt.getKeyChar() == ',') {
                    if (editor.getTextField().getText().contains(",")) {
                        evt.consume();
                    } else {
                        editor.getTextField().setText(editor.getTextField().getText() + ",");
                    }
                }

                if (!Character.isDigit(evt.getKeyChar())) {
                    evt.consume();
                }

            }
        });

        JFormattedTextField jf = editor.getTextField();

        jf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JTextField tf = (JTextField) e.getSource();
                    tf.selectAll();
                });
            }
        });
    }

    private void inicializarCategorias() {
        vista_categorias = null;
        vista_categorias = new Vista_Categorias(frameModal, true);
        ctrl_categoria = new controladorCategoría(vista_categorias);
    }

    private void inicializarSubCategorias() {
        vista_subcategorias = null;
        vista_subcategorias = new Vista_Sub_Categorias(frameModal, true);
        ctrl_sub_categoria = new controladorSubCategoría(vista_subcategorias);
    }

}
