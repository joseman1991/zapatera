/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorMovimientos;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import modelo.ConexionPSQL;
import modelo.DataSet;
import modelo.MovimientoDAO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import vistaReportes.vista_reporte;

/**
 *
 * @author JOSE-MA
 */
public class Reportes {

    private final vista_reporte reporte;
    private final ConexionPSQL conexionPSQL;
    private DataSet dt;
    private final MovimientoDAO movimientoDAO;
    private JasperViewer jv;

    public Reportes(vista_reporte reporte) {
        this.reporte = reporte;
        movimientoDAO = new MovimientoDAO();
        conexionPSQL = new ConexionPSQL();
        inicializar();
        eventos();
    }

    private void eventos() {
        reporte.jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                reporte.concepto.setText(dt.getValueAt(reporte.jTable1.getSelectedRow(), 2).toString());
            }
        });

        reporte.busca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    dt = movimientoDAO.obtenerMovimientos(reporte.busca.getText());
                    dt.setCellEditable(false);
                    reporte.jTable1.setModel(dt);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        reporte.jButton1.addActionListener((ActionEvent e) -> {
            reporte.dispose();
        });
        reporte.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                try {
                    if (jv != null) {
                        jv.dispose();
                    }

                } catch (Exception ex) {

                }
            }
        });

        reporte.generar.addActionListener((ActionEvent evt) -> {
            Connection con = null;
            try {
                con = conexionPSQL.getConexion();
                if (reporte.jTable1.getSelectedRow() >= 0) {
                    File archivo = new File("recursos/reportes/movimientos.jasper");
                    JasperReport report = (JasperReport) JRLoader.loadObject(archivo);
                    Map parametro = new HashMap();
                    parametro.put("codtipomov", dt.getValueAt(reporte.jTable1.getSelectedRow(), 0));
                    JasperPrint jp = JasperFillManager.fillReport(report, parametro, con);
                    jv = new JasperViewer(jp, false);
                    jv.setTitle("Reportes de transacciones");
                    eventsJasper();
                    jv.setVisible(true);
//                    JRPdfExporter  exporter = new JRPdfExporter();
//                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
//                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream("aqui" + ".pdf")); // your output goes here
//                    exporter.exportReport();
                } else {
                    JOptionPane.showMessageDialog(reporte, "Selecciona una transacción", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (JRException e) {
                JOptionPane.showMessageDialog(reporte, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(reporte, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex1) {
                        Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex1) {
                        JOptionPane.showMessageDialog(reporte, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

    public void inicializarReportes() {
        reporte.setVisible(true);
    }

    private void inicializar() {
        try {
            dt = movimientoDAO.obtenerMovimientos("");
            reporte.jTable1.setModel(dt);
            dt.setCellEditable(false);
            reporte.busca.requestFocus();
            reporte.jTable1.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        } catch (SQLException e) {

        }
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

}
