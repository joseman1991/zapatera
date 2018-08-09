/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorMovimientos;

import jTextFieldAutoCOmplete.Element;
import jTextFieldAutoCOmplete.Predictor;
import jTextFieldAutoCOmplete.PredictorException;
import java.awt.Color;
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
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import modelo.AnchoColumna;
import modelo.ArticuloDAO;
import modelo.Articulos;
import modelo.ConexionPSQL;
import modelo.Detallemovimiento;
import modelo.Empresa;
import modelo.FacturaDAO;
import modelo.ModelCellEditable;
import modelo.MovimientoDAO;
import modelo.Movimientos;
import modelo.Responsables;
import modelo.Tipomovimiento;
import modelo.TipomovimientoDAO;
import modelo.Usuarios;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import vistaMovimiento.Vista_Movimientos;
import vistaMovimiento.Articulo;
import vistaMovimiento.Responsable;

public class CtrlIngreso {

    private final List<Detallemovimiento> listaDetelle;

    private final ModelCellEditable dt;
    private double totalMovimiento;

    private Tipomovimiento tipoMov;

    private Usuarios usuario;
    private final Vista_Movimientos vistaIngreso;

    private Responsable vista_resposable;
    private Ctrl_responsable controlador_responsable;
    private Responsables responsable;

    private Articulo vista_producto;
    private ControladorArticulo ctrl_producto;
    private Articulos producto;
    private Articulos producto2;
    private Articulos producto3;
    public List<Tipomovimiento> listaTipoMov;
    private Date fechaMovimiento;

    private boolean selPorducto;
    private int fila;

    private final Frame frameModal;

    private final MovimientoDAO moviDAO;
    private final TipomovimientoDAO tipoMoviDAO;

    private Predictor predictor;
    private Element elemento;
    private MouseAdapter eventoClick;
       private JasperViewer jv;
    private List<Articulos> listaArticulos;
 private final ConexionPSQL conexionPSQL;
    private float IVA;
    private double iva;

    private final ArticuloDAO articuloDAO;

    public CtrlIngreso(Vista_Movimientos vistaIngreso) {
        this.vistaIngreso = vistaIngreso;
        this.vistaIngreso.setResizable(false);

        moviDAO = new MovimientoDAO();
        tipoMoviDAO = new TipomovimientoDAO();

        listaDetelle = new ArrayList<>();
        frameModal = JOptionPane.getFrameForComponent(this.vistaIngreso);

        dt = new ModelCellEditable();
        dt.isCellEditable(0, 0);
        this.vistaIngreso.TablaDetalle.setModel(dt);
        articuloDAO = new ArticuloDAO();

        eventos();
        selPorducto = true;
        this.vistaIngreso.TablaDetalle.setRowHeight(25);
        this.vistaIngreso.TablaDetalle.setGridColor(Color.black);
        this.vistaIngreso.TablaDetalle.setShowGrid(true);
         conexionPSQL = new ConexionPSQL();
        int medida[] = {10, 10, 300, 40, 40, 40, 40};
        AnchoColumna ac = new AnchoColumna(this.vistaIngreso.TablaDetalle, medida);
    }

    public void IniciarIngreso() {
        vistaIngreso.usuario.setText(usuario.getCodusuario());
        vistaIngreso.tipoMovimiento.removeAllItems();
        vistaIngreso.costo.setEnabled(false);
        vistaIngreso.txtCantidad.setEnabled(false);
        vistaIngreso.datePicker.getJFormattedTextField().setText(formatearFecha(new java.util.Date()));
        try {
            tipoMoviDAO.obtnerListaDetalleMov("I");
            listaTipoMov = tipoMoviDAO.listaTipoMov;
            for (int i = 0; i < tipoMoviDAO.listaTipoMov.size(); i++) {
                if (listaTipoMov.get(i).getCodtipomovimiento().equals("BAI")) {
                    continue;
                }
                vistaIngreso.tipoMovimiento.addItem(tipoMoviDAO.listaTipoMov.get(i).getDescripcion());
            }
            vistaIngreso.codMovimiento.setText(moviDAO.getNextCodMovimiento() + "");
            IVA = articuloDAO.obtenerIVA();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vistaIngreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        vistaIngreso.setVisible(true);
        inicializarPredictor();
        eventosPredictor();
    }

    private void inicializarPredictor() {
        try {
            
            predictor = new Predictor(listaArticulos, this.vistaIngreso.txtBusca);
            eventoClick = new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    elemento = predictor.getElemento();
                    if (elemento != null) {
                        try {
                            producto = articuloDAO.obtenerArticulo(elemento.key);
                            Detallemovimiento dtm = new Detallemovimiento();
                            dtm.setCantidad(1);
                            dtm.setCodarticilo(producto.getCodarticulo());
                            seleccionarProducto(dtm);
                            vistaIngreso.txtBusca.setText("");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(vistaIngreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            };
            predictor.setEventoClick(eventoClick);
            predictor.obtener(Articulos.class, "codarticulo", "descripcion");
        } catch (PredictorException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException  ex) {
            JOptionPane.showMessageDialog(vistaIngreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarStock(String cod, String ns) {
        try {
            producto3 = articuloDAO.obtenerArticulo(cod);
            if (producto3 != null) {
                vistaIngreso.txtStock.setText(producto3.getStock() + "");
                double n = Double.parseDouble(ns) + producto3.getStock();
                vistaIngreso.txtNuevoStock1.setText(n + "");
                producto3 = null;
            } else {
                vistaIngreso.txtStock.setText("");
                vistaIngreso.txtNuevoStock1.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vistaIngreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eventos() {
        vistaIngreso.bucsResponsable.addActionListener((ActionEvent e) -> {
            InicializarVistaResponsable();
            controlador_responsable = new Ctrl_responsable(vista_resposable);
            controlador_responsable.IniciarVistaResponsable();
        });

        vistaIngreso.datePanel.addActionListener((ActionEvent e) -> {
            java.util.Date date = (java.util.Date) vistaIngreso.datePicker.getModel().getValue();
            String fechaFormat = formatearFecha(date);
            vistaIngreso.datePicker.getJFormattedTextField().setText(fechaFormat);
            vistaIngreso.datePicker.getModel().setSelected(true);
        });

        vistaIngreso.busc_Art.addActionListener((ActionEvent e) -> {
            selPorducto = false;
            inicializarVistaArticulo();
            ctrl_producto = new ControladorArticulo(vista_producto);
            ctrl_producto.setListaDetelle(listaDetelle);
            ctrl_producto.IniciarVistaProducto();
        });

        vistaIngreso.eliminar.addActionListener((ActionEvent e) -> {
            if (vistaIngreso.TablaDetalle.getSelectedRowCount() > 1) {
                int op = JOptionPane.showConfirmDialog(vistaIngreso, "¿Estás seguro que deseas eliminar estos producto?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (op == JOptionPane.YES_OPTION) {
                    int row[] = vistaIngreso.TablaDetalle.getSelectedRows();
                    eliminarPruducto(row);
                    vistaIngreso.eliminar.setEnabled(false);
                    vistaIngreso.costo.setValue(0.00);
                    vistaIngreso.txtCantidad.setValue(0.00);
                }
            } else {
                int op = JOptionPane.showConfirmDialog(vistaIngreso, "¿Estás seguro que deseas eliminar este producto?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (op == JOptionPane.YES_OPTION) {
                    int row = vistaIngreso.TablaDetalle.getSelectedRow();
                    dt.removeRow(row);
                    eliminarPruducto(row);
                    vistaIngreso.eliminar.setEnabled(false);
                    vistaIngreso.costo.setValue(0.00);
                    vistaIngreso.txtCantidad.setValue(0.00);
                }
            }
        });

        vistaIngreso.TablaDetalle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                fila = vistaIngreso.TablaDetalle.getSelectedRow();
                if (!(fila == vistaIngreso.TablaDetalle.getRowCount() - 1)) {
                    vistaIngreso.txtCantidad.setValue(vistaIngreso.TablaDetalle.getValueAt(fila, 1));
                    vistaIngreso.costo.setValue(vistaIngreso.TablaDetalle.getValueAt(fila, 3));
                    vistaIngreso.eliminar.setEnabled(true);
                    vistaIngreso.costo.setEnabled(true);
                    vistaIngreso.txtCantidad.setEnabled(true);
                    mostrarStock(vistaIngreso.TablaDetalle.getValueAt(fila, 0).toString(), vistaIngreso.TablaDetalle.getValueAt(fila, 1).toString());
                } else {
                    vistaIngreso.eliminar.setEnabled(false);
                    vistaIngreso.costo.setValue(0.00);
                    vistaIngreso.txtCantidad.setValue(0.00);
                    dt.setValueAt("", fila, 1);
                    dt.setValueAt("", fila, 3);
                    vistaIngreso.costo.setEnabled(false);
                    vistaIngreso.txtCantidad.setEnabled(false);
                    mostrarStock("", "0");
                }
            }
        });

        vistaIngreso.TablaDetalle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    seleccionarTdo(vistaIngreso.txtCantidad);
                }

            }
        });

        dt.addTableModelListener((TableModelEvent e) -> {//           
            if (e.getType() == TableModelEvent.UPDATE) {
                final int row = vistaIngreso.TablaDetalle.getSelectedRow();
                final int column = e.getColumn();
                if (column == 0) {
                    try {
                        String codigo = dt.getValueAt(row, column).toString();
                        if (!codigo.equals("")) {
                            selPorducto = false;
                            producto = articuloDAO.obtenerArticulo(codigo);
                            if (producto != null) {
                                Detallemovimiento dtm = new Detallemovimiento();
                                dtm.setCantidad(1);
                                dtm.setCodarticilo(producto.getCodarticulo());
                                seleccionarProducto(dtm);
                            } else {
                                JOptionPane.showMessageDialog(vistaIngreso, "Código de producto incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                                dt.setValueAt("", row, column);
                                dt.setValueAt("", row, 1);
                                dt.setValueAt("", row, 3);
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(vistaIngreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return;
                }

                if (column == 1) {
                    if (listaDetelle.size() > 0) {
                        if (!(fila == vistaIngreso.TablaDetalle.getRowCount() - 1)) {
                            producto2 = listaDetelle.get(fila).getArticulos();
                            double cantidad = Double.parseDouble(dt.getValueAt(row, column).toString());
                            editarCantidad(cantidad);
                        }
                    }
                }
                if (column == 3 && selPorducto) {
                    if (listaDetelle.size() > 0) {
                        if (!(fila == vistaIngreso.TablaDetalle.getRowCount() - 1)) {
                            producto2 = listaDetelle.get(fila).getArticulos();
                            double precio = Double.parseDouble(dt.getValueAt(fila, 3).toString());
                            editarCosto(precio);
                        }

                    }
                }
                vistaIngreso.TablaDetalle.setRowSelectionInterval(fila, fila);
            }
        });

        vistaIngreso.registrar.addActionListener((ActionEvent e) -> {
            if (vistaIngreso.responsable.getText().equals("")) {
                JOptionPane.showMessageDialog(vistaIngreso, "Selecciona un proveedor", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                java.util.Date fa = new java.util.Date();
                fa = new java.util.Date(formatearFecha(fa));
                java.util.Date fs = (java.util.Date) vistaIngreso.datePicker.getModel().getValue();
                fs = new java.util.Date(formatearFecha(fs));
                if (fs.after(fa)) {
                    JOptionPane.showMessageDialog(vistaIngreso, "La fecha no debe ser mayor a la fecha actual", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (listaDetelle.isEmpty()) {
                        JOptionPane.showMessageDialog(vistaIngreso, "Ingresa al menos un producto", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (vistaIngreso.concepto.getText().equals("")) {
                            JOptionPane.showMessageDialog(vistaIngreso, "Escribe el concepto de la transaccion", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            for (int i = 0; i < listaDetelle.size(); i++) {
                                Detallemovimiento get = listaDetelle.get(i);
                                if (get.getCosto() == 0) {
                                    JOptionPane.showMessageDialog(vistaIngreso, "El costo de los articulos no debe ser 0", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                            if (!vistaIngreso.nFactura.getText().equals("")) {
                                int op = JOptionPane.showConfirmDialog(null, "Esta seguro de registrar esta transacciòn", "Registrar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (op == JOptionPane.YES_OPTION) {
                                    Connection con = null;
                                    long id=0;
                                    try {
                                        Movimientos mov = new Movimientos();
                                        mov.setTipomovimiento(tipoMov);
                                        mov.setNaturaleza(vistaIngreso.Naturaleza.getText().charAt(0));
                                        mov.setCodusuario(usuario.getCodusuario());
                                        mov.setResponsables(responsable);
                                        mov.setTotalmovimiento(totalMovimiento);
                                        mov.setConcepto(vistaIngreso.concepto.getText());
                                        mov.setFechamovimiento(fechaMovimiento);
                                        
                                        DecimalFormat df= new DecimalFormat("000000000");
                                        String nf=vistaIngreso.nFactura.getText();
                                        nf=df.format(Integer.parseInt(nf));                                        
                                        mov.setNfactura(nf);
                                        insertarNfactura(nf, responsable);
                                         id = moviDAO.insertarMov(mov, listaDetelle);
                                        con = moviDAO.getConexion();
                                        JOptionPane.showMessageDialog(vistaIngreso, "Datos Ingresados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                        while (dt.getRowCount() > 0) {
                                            dt.removeRow(0);
                                        }
                                        listaDetelle.clear();
                                        vistaIngreso.concepto.setText("");
                                        vistaIngreso.responsable.setText("");
                                        vistaIngreso.SubtotalMovimiento.setText("0.00");
                                        vistaIngreso.nFactura.setText("");
                                        vistaIngreso.txtBusca.setText("");
                                        vistaIngreso.total.setText("0.00");
                                        vistaIngreso.txtNuevoStock1.setText("");
                                        vistaIngreso.txtStock.setText("");
                                        vistaIngreso.codMovimiento.setText((id + 1) + "");
                                        vistaIngreso.iva.setText("0.00");
                                        vistaIngreso.txtCantidad.setValue(0.00);
                                        vistaIngreso.costo.setValue(0.00);
                                        vistaIngreso.txtCantidad.setEnabled(false);
                                        vistaIngreso.costo.setEnabled(false);
                                        vistaIngreso.eliminar.setEnabled(false);
                                        dt.addRow(new Object[]{});
                                        responsable = null;
                                        con.commit();
                                    } catch (SQLException ex) {
                                        JOptionPane.showMessageDialog(vistaIngreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                        if (con != null) {
                                            try {
                                                con.rollback();
                                            } catch (SQLException ex1) {
                                                JOptionPane.showMessageDialog(vistaIngreso, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                    } finally {
                                        if (con != null) {
                                            try {
                                                con.close();
                                                FacturaDAO f = new FacturaDAO();
                                            long codf = f.obtenerId(id);
                                            System.out.println(String.format("codm %d codf%d ", id, codf));
                                            generarReporte(codf);
                                            } catch (SQLException ex1) {
                                                JOptionPane.showMessageDialog(vistaIngreso, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(vistaIngreso, "Escribe el numero de factura de la transaccion", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        vistaIngreso.cancelar.addActionListener((ActionEvent e) -> {
            vistaIngreso.dispose();
        });

        vistaIngreso.tipoMovimiento.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                tipoMov = listaTipoMov.get(vistaIngreso.tipoMovimiento.getSelectedIndex());
            }
        });

        eventoSpinerCantidad();
        eventoSpinerCosto();
        spinerModel(vistaIngreso.txtCantidad);
        spinerModel(vistaIngreso.costo);
        eventoEnterCantidad();
        eventoEnterCosto();
        bloquearNumeros(vistaIngreso.nFactura);
    }
 private Empresa empresa;
      private void generarReporte(long codf) {
        Connection con = null;
        try {
            con = conexionPSQL.getConexion();

            File archivo = new File("recursos/reportes/factura_1.jasper");
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
            JOptionPane.showMessageDialog(vistaIngreso, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vistaIngreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(vistaIngreso, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }
    
    
    private void bloquearNumeros(JTextField jtf) {
        jtf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (jtf.getText().length() > 8) {
                    e.consume();
                } else {
                    if (!Character.isDigit(e.getKeyChar())) {
                        e.consume();
                    }
                }
            }
        });

    }

    private void insertarNfactura(String nf, Responsables responsables) {
        for (int i = 0; i < listaDetelle.size(); i++) {
            listaDetelle.get(i).setNfactura(nf);
            listaDetelle.get(i).setResponsables(responsables);
        }
    }

    private void eventosPredictor() {
        vistaIngreso.txtBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        elemento = predictor.getElemento();
                        if (elemento != null) {
                            try {
                                producto = articuloDAO.obtenerArticulo(elemento.key);
                                Detallemovimiento dtm = new Detallemovimiento();
                                dtm.setCantidad(1);
                                dtm.setCodarticilo(producto.getCodarticulo());
                                seleccionarProducto(dtm);
                                vistaIngreso.txtBusca.setText("");
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(vistaIngreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;
                }
            }
        });

    }

    private void eliminarPruducto(int row) {
        listaDetelle.remove(row);
        for (int i = 0; i < listaDetelle.size(); i++) {
            listaDetelle.get(i).setOrden(i + 1);
        }
        actualizarDetalles();
    }

    private void eliminarPruducto(int row[]) {
        for (int i = 0; i < row.length; i++) {
            int j = row[i];
            dt.removeRow(j);
            listaDetelle.remove(j);
        }

        for (int i = 0; i < listaDetelle.size(); i++) {
            listaDetelle.get(i).setOrden(i + 1);
        }
        actualizarDetalles();
    }

    private void InicializarVistaResponsable() {
        vista_resposable = new Responsable(frameModal, true);
        vista_resposable.setLocationRelativeTo(frameModal);
        vista_resposable.setResizable(false);
        vista_resposable.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                responsable = controlador_responsable.getResponsable();
                if (responsable != null) {
                    vistaIngreso.responsable.setText(responsable.getRazonsocial());
                } else {
                    vistaIngreso.responsable.setText("");
                }
            }
        });
    }

    private void inicializarVistaArticulo() {
        vista_producto = new Articulo(frameModal, true);
        vista_producto.setLocationRelativeTo(frameModal);
        vista_producto.setResizable(false);
        vistaIngreso.eliminar.setEnabled(false);
        vista_producto.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                producto = ctrl_producto.getArticulo();
                seleccionarProducto(ctrl_producto.getDetalle());
            }
        });
    }

    private void seleccionarProducto(Detallemovimiento dtm) {
        if (producto != null) {
            if (listaDetelle.isEmpty()) {
                listaDetelle.add(dtm);
            }
            añadirDetalle(dtm);
            selPorducto = true;
            producto = null;
            vistaIngreso.eliminar.setEnabled(true);
            vistaIngreso.costo.setEnabled(true);
            vistaIngreso.txtCantidad.setEnabled(true);
            fila = vistaIngreso.TablaDetalle.getSelectedRow();
            seleccionarTdo(vistaIngreso.txtCantidad);
        }
    }

    private void eventoSpinerCantidad() {
        vistaIngreso.txtCantidad.addChangeListener((ChangeEvent e) -> {
            int row = vistaIngreso.TablaDetalle.getSelectedRow();
            if (row >= 0 && row != vistaIngreso.TablaDetalle.getRowCount() - 1) {
                vistaIngreso.TablaDetalle.setValueAt(vistaIngreso.txtCantidad.getValue(), row, 1);
                mostrarStock(vistaIngreso.TablaDetalle.getValueAt(fila, 0).toString(), vistaIngreso.TablaDetalle.getValueAt(fila, 1).toString());
            }
        });
    }

    private void eventoSpinerCosto() {
        vistaIngreso.costo.addChangeListener((ChangeEvent e) -> {
            int row = vistaIngreso.TablaDetalle.getSelectedRow();
            if (row >= 0) {
                vistaIngreso.TablaDetalle.setValueAt(vistaIngreso.costo.getValue(), row, 3);
            }
        });
    }

    private void eventoEnterCantidad() {
        ((JSpinner.DefaultEditor) vistaIngreso.txtCantidad.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    seleccionarTdo(vistaIngreso.costo);
                }
            }
        });
    }

    private void eventoEnterCosto() {
        ((JSpinner.DefaultEditor) vistaIngreso.costo.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    vistaIngreso.txtBusca.requestFocus();
                }
            }
        });
    }

    private void editarCantidad(double cantidad) {
        for (int i = 0; i < listaDetelle.size(); i++) {
            if (listaDetelle.get(i).getCodarticilo().equals(producto2.getCodarticulo())) {
                listaDetelle.get(i).setCantidad(cantidad);
            }
        }
        actualizarDetalles();
    }

    private void editarCosto(double costo) {
        for (int i = 0; i < listaDetelle.size(); i++) {
            if (listaDetelle.get(i).getCodarticilo().equals(producto2.getCodarticulo())) {
                listaDetelle.get(i).setCosto(costo);
            }
        }
        actualizarDetalles();
    }

    private String formatearFecha(java.util.Date fecha) {
        String fechaFormat;
        if (fecha != null) {
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormat = formateador.format(fecha);
            this.fechaMovimiento = new Date(fecha.getTime());
        } else {
            fechaFormat = formatearFecha(fechaMovimiento);
        }
        return fechaFormat;
    }

    private void añadirDetalle(Detallemovimiento dtm) {
        selPorducto = false;
        for (int i = 0; i < listaDetelle.size(); i++) {
            if (listaDetelle.get(i).getCodarticilo().equals(dtm.getCodarticilo())) {
                vistaIngreso.costo.setValue(listaDetelle.get(i).getCosto());
                break;
            } else if (i == listaDetelle.size() - 1) {
                listaDetelle.add(dtm);
            }
        }
        selPorducto = true;
        actualizarDetalles();
    }

    private void actualizarDetalles() {
        while (dt.getRowCount() > 0) {
            dt.removeRow(0);
        }
        totalMovimiento = 0;
        iva = 0;
        for (int i = 0; i < listaDetelle.size(); i++) {
            String codarticulo = listaDetelle.get(i).getArticulos().getCodarticulo();
            double cantidad = listaDetelle.get(i).getCantidad();
            String descripcion = listaDetelle.get(i).getArticulos().getDescripcion();
            double costo = listaDetelle.get(i).getCosto();
            double subtotal = cantidad * costo;
            totalMovimiento += subtotal;
            double uiva = 0;
            if (listaDetelle.get(i).getArticulos().getGrabaiva() == 'S') {
                uiva = subtotal * IVA / 100;
            }
            iva += uiva;
            
            double total = subtotal + uiva;
            BigDecimal t = new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP);
            Object[] filas = {codarticulo, cantidad, descripcion, costo, subtotal, uiva, t};
            dt.addRow(filas);
            if (producto != null) {
                if (listaDetelle.get(i).getArticulos().getCodarticulo().equals(producto.getCodarticulo())) {
                    vistaIngreso.txtCantidad.setValue(listaDetelle.get(i).getCantidad());
                    vistaIngreso.TablaDetalle.setRowSelectionInterval(i, i);
                    fila = i;
                }
            }
        }
        BigDecimal t = new BigDecimal(totalMovimiento).setScale(2, BigDecimal.ROUND_HALF_UP); 
        BigDecimal i = new BigDecimal(iva).setScale(2, BigDecimal.ROUND_HALF_UP);;
        vistaIngreso.SubtotalMovimiento.setText(t + "");
        vistaIngreso.iva.setText(i + "");        
       
        BigDecimal r = t.add(i);

        vistaIngreso.total.setText(r + "");
        if (listaDetelle.size() > 1) {
            mostrarStock(vistaIngreso.TablaDetalle.getValueAt(fila, 0).toString(), vistaIngreso.TablaDetalle.getValueAt(fila, 1).toString());
        }
        dt.addRow(new Object[]{});
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    private void spinerModel(JSpinner js) {
        double min = 0.00;
        double value = 0.00;
        double stepSize = 1.00;
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
        jf.setValue(0f);
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

    private void seleccionarTdo(JSpinner js) {
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) js.getEditor();
        JFormattedTextField jf = editor.getTextField();
        jf.requestFocus();
    }

    public void setListaArticulos(List<Articulos> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    
    
    
    
}
