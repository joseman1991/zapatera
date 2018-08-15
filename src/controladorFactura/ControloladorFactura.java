/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorFactura;

import controladorMovimientos.CtrlEgresos;
import controladorMovimientos.Reportes;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import modelo.ConexionPSQL;
import modelo.DataSet;
import modelo.Empresa;
import modelo.EmpresaDAO;
import modelo.EnviarMensaje;
import modelo.FacturaDAO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdatepicker.impl.JDatePickerImpl;
import vistaFacturas.Ventas;

/**
 *
 * @author JOSE
 */
public class ControloladorFactura {

    private final Ventas vistaVentas;
    private Date fechaInicio;
    private Date fechaFin;
    private final FacturaDAO fdao;

    private long id;
    
    public ControloladorFactura(Ventas vistaVentas) {
        conexionPSQL= new ConexionPSQL();
        this.fdao = new FacturaDAO();
        this.vistaVentas = vistaVentas;
        inicializarFecha();
        enableDatePicker(vistaVentas.datePicker, false);
        enableDatePicker(vistaVentas.datePicker2, false);
        eventos();
    }

    private void inicializarFecha() {
        vistaVentas.btnBuscar.setEnabled(false);
        vistaVentas.datePicker2.getJFormattedTextField().setText(formatearFecha2(new java.util.Date()));
        try {
            java.util.Date d = new Date(fdao.obtenerDomingo().getTime());
            vistaVentas.datePicker.getJFormattedTextField().setText(formatearFecha(d));
            DataSet dt = fdao.getFacturas(fechaInicio, fechaFin);
            vistaVentas.tabla.setModel(dt);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vistaVentas, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void inicarVentas() {
        vistaVentas.setVisible(true);
    }

    private void eventos() {
        eventoFechaInicio();
        eventoFechaFin();
        eventoRadioSemana();
        eventoRadioFecha();
        eventoClicVista();
        eventoCerrarVista();
        eventoCerrar();
        eventoVerFactura();
        eventoTabla();
    }

    private void eventoFechaInicio() {
        vistaVentas.datePanel.addActionListener((ActionEvent e) -> {
            java.util.Date date = (java.util.Date) vistaVentas.datePicker.getModel().getValue();
            String fechaFormat = formatearFecha(date);
            vistaVentas.datePicker.getJFormattedTextField().setText(fechaFormat);
            buscarFactura();
            vistaVentas.datePicker.getModel().setSelected(true);
        });
    }

    private void eventoFechaFin() {
        vistaVentas.datePanel2.addActionListener((ActionEvent e) -> {
            java.util.Date date = (java.util.Date) vistaVentas.datePicker2.getModel().getValue();
            String fechaFormat = formatearFecha2(date);
            vistaVentas.datePicker2.getJFormattedTextField().setText(fechaFormat);
            buscarFactura();
            vistaVentas.datePicker2.getModel().setSelected(true);
        });
    }

    private void buscarFactura() {
        try {
            vistaVentas.btnBuscar.setEnabled(false);
            DataSet dt = fdao.getFacturas(fechaInicio, fechaFin);
            vistaVentas.tabla.setModel(dt);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vistaVentas, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eventoRadioSemana() {
        vistaVentas.semana.addActionListener((ActionEvent e) -> {
            enableDatePicker(vistaVentas.datePicker, false);
            enableDatePicker(vistaVentas.datePicker2, false);
            inicializarFecha();
        });
    }

    private void eventoCerrar() {
        vistaVentas.cerrar.addActionListener((ActionEvent e) -> {
            vistaVentas.dispose();
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

    private void eventoVerFactura() {
        vistaVentas.btnBuscar.addActionListener((ActionEvent e) -> {
            cursorCargando(vistaVentas);
            generarReporte(id);
            cursorNomal(vistaVentas);
        });
    }
     private final ConexionPSQL conexionPSQL;
     private JasperViewer jv;
        private Empresa empresa;
     private void generarReporte(long codf) {
        Connection con = null;
        try {
            empresa= new EmpresaDAO().obtenerEmpresa();
            con = conexionPSQL.getConexion();
            File archivo = new File("recursos/reportes/factura.jasper");
            JasperReport report = (JasperReport) JRLoader.loadObject(archivo);
            Map parametro = new HashMap();
            parametro.put("codfact", codf);
            parametro.put("razonSocial", empresa.getRazonsocial());
            parametro.put("ruc", empresa.getIdentificacion());
            parametro.put("Direccion", empresa.getDireccion());
            JasperPrint jp = JasperFillManager.fillReport(report, parametro, con);
            jv = new JasperViewer(jp, false);
            jv.setTitle("Reportes de factura");
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream("recursos/facturas/" + codf + ".pdf")); // your output goes here
            exporter.exportReport();           
            jv.setVisible(true);

        } catch (JRException e) {
            JOptionPane.showMessageDialog(vistaVentas, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vistaVentas, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } catch (java.io.FileNotFoundException e) {
        }   finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    JOptionPane.showMessageDialog(vistaVentas, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    private void eventoRadioFecha() {
        vistaVentas.fecha.addActionListener((ActionEvent e) -> {
            enableDatePicker(vistaVentas.datePicker, true);
            enableDatePicker(vistaVentas.datePicker2, true);
        });
    }

    private void eventoTabla() {
        vistaVentas.tabla.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 int fila= vistaVentas.tabla.getSelectedRow();
                 id= (Long) vistaVentas.tabla.getValueAt(fila, 0);
                 vistaVentas.btnBuscar.setEnabled(true);
             }
        });
    }

    private void enableDatePicker(JDatePickerImpl dp, boolean isEnable) {
        dp.getJFormattedTextField().setEnabled(isEnable);
        dp.getButton().setEnabled(isEnable);
        if (!isEnable) {
            dp.compruebePopup();
        }
    }

    private void eventoClicVista() {
        vistaVentas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                vistaVentas.datePicker.compruebePopup();
                vistaVentas.datePicker2.compruebePopup();
            }
        });
    }

    private void eventoCerrarVista() {
        vistaVentas.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                vistaVentas.datePicker.compruebePopup();
                vistaVentas.datePicker2.compruebePopup();
            }
        });
    }

    private String formatearFecha(java.util.Date fecha) {
        String fechaFormat;
        if (fecha != null) {
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormat = formateador.format(fecha);
            this.fechaInicio = new Date(fecha.getTime());
        } else {
            fechaFormat = formatearFecha(fechaInicio);
        }
        return fechaFormat;
    }

    private String formatearFecha2(java.util.Date fecha) {
        String fechaFormat;
        if (fecha != null) {
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormat = formateador.format(fecha);
            this.fechaFin = new Date(fecha.getTime());
        } else {
            fechaFormat = formatearFecha(fechaFin);
        }
        return fechaFormat;
    }

}
