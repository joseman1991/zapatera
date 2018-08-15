/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorResponsable;

import controladorMovimientos.Reportes;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import modelo.AnchoColumna;
import modelo.ArticuloDAO;
import modelo.CategoriasDAO;
import modelo.ConexionPSQL;
import modelo.DataSet;
import modelo.Empresa;
import modelo.FacturaDAO;
import modelo.Responsables;
import modelo.ResponsablesDAO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import vistaResponsables.buscaPorProveedor;

/**
 *
 * @author JOSE-MA
 */
public class ctrl_busca_pro {

    private final buscaPorProveedor vista_busca_pro;

    private DataSet dt;
    private Responsables responsable;

    private char tipo;

    private final ResponsablesDAO respoDAO;

    public ctrl_busca_pro(buscaPorProveedor vista_busca_pro) {
        this.vista_busca_pro = vista_busca_pro;
        respoDAO = new ResponsablesDAO();

        vista_busca_pro.btnReporte.setEnabled(false);
        this.vista_busca_pro.tabla.setRowHeight(25);
        this.vista_busca_pro.tabla.setGridColor(Color.black);
        this.vista_busca_pro.tabla.setShowGrid(true);
        int medida[] = {20, 300, 20, 20, 20, 20};
        AnchoColumna ac = new AnchoColumna(this.vista_busca_pro.tabla, medida);
        conexionPSQL = new CategoriasDAO();
    }

    private void eventos() {
        buscar();

        DataSet dt2;
        try {
            dt2 = new FacturaDAO().obtnerFactura(new Responsables());
            vista_busca_pro.tabla2.setModel(dt2);
        } catch (SQLException ex) {
            Logger.getLogger(ctrl_busca_pro.class.getName()).log(Level.SEVERE, null, ex);
        }

        vista_busca_pro.salir.addActionListener((ActionEvent e) -> {
            vista_busca_pro.dispose();
        });

        vista_busca_pro.buscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscar();
            }
        });

        vista_busca_pro.tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int row = vista_busca_pro.tabla.getSelectedRow();
                    vista_busca_pro.btnReporte.setEnabled(false);
                    String identificacion = dt.getValueAt(row, 0).toString();
                    responsable = respoDAO.obtenerResponsable(tipo + "", identificacion);
                    DataSet dt2 = new FacturaDAO().obtnerFactura(responsable);
                    vista_busca_pro.tabla2.setModel(dt2);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vista_busca_pro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        vista_busca_pro.tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vista_busca_pro.btnReporte.setEnabled(true);
            }
        });

        eventoBotonReporte();
    }

    public void iniciarVistabusca() {
        eventos();
        vista_busca_pro.setVisible(true);
    }

    private void buscar() {
        try {
            dt = respoDAO.obtenerResponsables(tipo + "", vista_busca_pro.buscar.getText());
            vista_busca_pro.tabla.setModel(dt);
            dt.setCellEditable(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista_busca_pro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eventoBotonReporte() {
        vista_busca_pro.btnReporte.addActionListener((ActionEvent e) -> {
            long id;
            int fila= vista_busca_pro.tabla2.getSelectedRow();
            id=Long.parseLong(vista_busca_pro.tabla2.getValueAt(fila, 0).toString());
            generarReporte(id);
        });
    }

    public void setTipo(char tipo) {
        if (tipo == 'P') {
            vista_busca_pro.setTitle("BUSCAR PROVEEDOR");
        } else if (tipo == 'C') {
            vista_busca_pro.setTitle("BUSCAR CLIENTE");
        }
        this.tipo = tipo;
    }

    private final ConexionPSQL conexionPSQL;
    private JasperViewer jv;
    private Empresa empresa;

    private void generarReporte(long codf) {
        Connection con = null;
        try {
            con = conexionPSQL.getConexion();

            File archivo;
            if (tipo == 'C') {
                archivo = new File("recursos/reportes/factura.jasper");
            } else {
                archivo = new File("recursos/reportes/factura_1.jasper");
            }

            JasperReport report = (JasperReport) JRLoader.loadObject(archivo);
            Map parametro = new HashMap();
            parametro.put("codfact", codf);
            parametro.put("razonSocial", empresa.getRazonsocial());
            parametro.put("ruc", empresa.getIdentificacion());
            parametro.put("Direccion", empresa.getDireccion());
            JasperPrint jp = JasperFillManager.fillReport(report, parametro, con);
            jv = new JasperViewer(jp, false);
            jv.setTitle("Reportes de factura");
            jv.setVisible(true);
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream("recursos/facturas/" + codf + ".pdf")); // your output goes here
            exporter.exportReport();

        } catch (JRException e) {
            JOptionPane.showMessageDialog(vista_busca_pro, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista_busca_pro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } catch (java.io.FileNotFoundException e) {
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    JOptionPane.showMessageDialog(vista_busca_pro, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    
    

}
