/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorKardex;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import modelo.ArticuloDAO;
import modelo.Articulos;
import modelo.ConexionPSQL;
import modelo.DetalleMovimientoDAO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import vistaMovimiento.Articulo;
import vistas_kardex.kardex;

/**
 *
 * @author JOSE-MA
 */
public class controlador_kardex {

    private final kardex vista_kardex;
    private DefaultTableModel dt;
    private final ArticuloDAO articuloDAO;
    private Articulos articulo;
    private Articulo vista_artculo;
    private controladorArticulo ctrl_articulo;

    private final ConexionPSQL conexionPSQL;
    private JasperViewer jv;

    private final DetalleMovimientoDAO detalleMovimientoDAO;
    private final Frame frameModal;

    public controlador_kardex(kardex vista_kardex) {
        this.vista_kardex = vista_kardex;
        articuloDAO = new ArticuloDAO();
        conexionPSQL = new ConexionPSQL();
        detalleMovimientoDAO = new DetalleMovimientoDAO();
        frameModal = JOptionPane.getFrameForComponent(this.vista_kardex);
        this.vista_kardex.imprimir.setIcon(getIcono(this.vista_kardex.imprimir));
        eventos();
        inicializarTabla();
        this.vista_kardex.tabla.setRowHeight(25);
        this.vista_kardex.tabla.setGridColor(Color.black);
        this.vista_kardex.tabla.setShowGrid(true);
    }

    public void iniciarKardex() {
        vista_kardex.setVisible(true);
    }

    private void cursorCargando(JInternalFrame jif) {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorNomal(JInternalFrame jif) {
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jif.setCursor(cursor);
    }

    private void eventos() {

        vista_kardex.aceptar.addActionListener((ActionEvent e) -> {
            vista_kardex.dispose();
        });
        vista_kardex.buscar.addActionListener((ActionEvent e) -> {
            inicializarArticulo();
            ctrl_articulo.IniciarVistaProducto();
        });

        vista_kardex.imprimir.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_kardex);
            try {
                if (articulo != null) {
                    File archivo = new File("recursos/reportes/kardex.jasper");
                    JasperReport report = (JasperReport) JRLoader.loadObject(archivo);
                    Map parametro = new HashMap();
                    parametro.put("codarticulo", articulo.getCodarticulo());
                    parametro.put("articulo", articulo.getDescripcion());
                    parametro.put("max", articuloDAO.obtenerMaximo(articulo.getCodarticulo()) + "");
                    parametro.put("min", articuloDAO.obtenerMinimo(articulo.getCodarticulo()) + "");
                    JasperPrint jp = JasperFillManager.fillReport(report, parametro, conexionPSQL.getConexion());
                    jv = new JasperViewer(jp, false);
                    jv.setTitle("Reportes de transacciones");
                    
                    eventsJasper();
                    jv.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(vista_kardex, "Selecciona un artículo", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (JRException ex) {
                cursorNomal(vista_kardex);
                JOptionPane.showMessageDialog(vista_kardex, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                cursorNomal(vista_kardex);

                JOptionPane.showMessageDialog(vista_kardex, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            cursorNomal(vista_kardex);
        });
    }

    private void inicializarTabla() {
        dt = (DefaultTableModel) vista_kardex.tabla.getModel();
        TableColumn columna = vista_kardex.tabla.getColumn("Detalle");
        TableColumn columna2 = vista_kardex.tabla.getColumn("Fecha");
        columna.setPreferredWidth(300);
        columna2.setPreferredWidth(100);
        vista_kardex.tabla.setRowHeight(25);
        vista_kardex.tabla.setModel(dt);
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

    private void inicializarArticulo() {
        vista_artculo = null;
        vista_artculo = new Articulo(frameModal, true);
        vista_artculo.setLocationRelativeTo(null);
        vista_artculo.setResizable(false);

        ctrl_articulo = new controladorArticulo(vista_artculo);
        vista_artculo.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                articulo = ctrl_articulo.getArticulo();
                if (articulo != null) {
                    int t = dt.getRowCount();
                    while (t > 0) {
                        dt.removeRow(0);
                        t--;
                    }
                    vista_kardex.articulo.setText(articulo.getDescripcion());
                    try {
                        vista_kardex.exismax.setText(articuloDAO.obtenerMaximo(articulo.getCodarticulo()) + "");
                        vista_kardex.exmin.setText(articuloDAO.obtenerMinimo(articulo.getCodarticulo()) + "");
                      
                        detalleMovimientoDAO.obtenerKardex(articulo.getCodarticulo());
                        for (int i = 0; i < detalleMovimientoDAO.filas.size(); i++) {
                            dt.addRow(detalleMovimientoDAO.filas.get(i));
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
    }

    private Icon getIcono(JButton jlb) {
        jlb.setSize(60, 50);
        ImageIcon image = new ImageIcon("recursos/imagenes/" + jlb.getName() + ".png");
        Icon icono = new ImageIcon(image.getImage().getScaledInstance(jlb.getWidth(), jlb.getHeight(), Image.SCALE_REPLICATE));
        return icono;
    }

}
