/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorArticulo;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import modelo.AnchoColumna;
import modelo.ArticuloDAO;
import modelo.Articulos;
import modelo.Categorias;
import modelo.CategoriasDAO;
import modelo.ConexionPSQL;
import modelo.DataSet;
import modelo.SubCategoriasDAO;
import modelo.Subcategorias;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import vistaArticulo.Articulo;

public class controladorListaArticulo {

    private DataSet dt;

    public char naturaleza;
    private JasperViewer jv;

    private List<Categorias> listaCat;

    private final Articulo vista_producto;
    private final ArticuloDAO articuloDAO;
    private Articulos articulo;

    private int codCat;
    private List<Subcategorias> li;
    private SpinnerNumberModel model;
    private final ConexionPSQL conexionPSQL;

    public controladorListaArticulo(Articulo vista_producto) {
        this.vista_producto = vista_producto;
        articuloDAO = new ArticuloDAO();
        eventos();
        buscar();
        dt.setCellEditable(false);
        rellenarCombos();
        naturaleza = 'I';
        this.vista_producto.tabla.setRowHeight(25);
        this.vista_producto.tabla.setGridColor(Color.black);
        this.vista_producto.tabla.setShowGrid(true);
        conexionPSQL = new ConexionPSQL();
        int medida[] = {100, 300, 100, 100, 100, 100};
        AnchoColumna ac = new AnchoColumna(this.vista_producto.tabla, medida);
    }

    public void IniciarVistaProducto() {
        vista_producto.buscarProducto.requestFocus();
        spinerModel(vista_producto.stock);
        model.setMaximum(0);
        model.setMinimum(0);
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
                try {
                    articulo = articuloDAO.obtenerArticulo(codArt);
                    vista_producto.nombre.setText(dt.getValueAt(rowSelected, 1).toString());
                    vista_producto.stock.setValue(articulo.getStock());
                    model.setMaximum(articulo.getStock());
                    model.setMinimum(articulo.getStock());
                } catch (SQLException ex) {
                }
            }
        });

        vista_producto.seleciona.addActionListener((ActionEvent e) -> {
            vista_producto.dispose();
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
        eventoReporte();
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

    private void eventoReporte() {
        vista_producto.reporte.addActionListener((ActionEvent e) -> {
            try {
                File archivo;
                Map parametro = new HashMap();
                if (vista_producto.subcategoria.getItemCount() > 0) {
                    archivo = new File("recursos/reportes/articulos.jasper");
                     parametro.put("idsubcategor", codCat);
                } else {
                    archivo = new File("recursos/reportes/articulos_1.jasper");
                   

                }
                JasperReport report = (JasperReport) JRLoader.loadObject(archivo);

                parametro.put("codarti", vista_producto.buscarProducto.getText());
                parametro.put("descripcion", vista_producto.buscarProducto.getText() + "%");
                parametro.put("descripcion2", "%" + vista_producto.buscarProducto.getText() + "%");
                JasperPrint jp = JasperFillManager.fillReport(report, parametro, conexionPSQL.getConexion());
                jv = new JasperViewer(jp, false);
                jv.setTitle("Reportes de artículos");
                eventsJasper();
                jv.setVisible(true);

            } catch (JRException ex) {
                cursorNomal(vista_producto);
                JOptionPane.showMessageDialog(vista_producto, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                cursorNomal(vista_producto);

                JOptionPane.showMessageDialog(vista_producto, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void eventsJasper() {
        jv.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    conexionPSQL.cerrarConexione();
                } catch (SQLException ex) {

                }
            }
        });
    }

    private void cursorCargando(JInternalFrame jif) {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorNomal(JInternalFrame jif) {
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jif.setCursor(cursor);
    }

    private void spinerModel(JSpinner js) {
        double min = 0.00;
        double value = 0.00;
        double stepSize = 0.01;
        model = new SpinnerNumberModel();
        model.setValue(value);
        model.setMinimum(min);
        model.setStepSize(stepSize);

        js.setModel(model);

        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) js.getEditor();
        DecimalFormat format = editor.getFormat();

        format.setMinimumFractionDigits(2);
        editor.getTextField().setEditable(false);
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

}
