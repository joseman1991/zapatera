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
import java.awt.Cursor;
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
import javax.mail.MessagingException;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.TableModelEvent;
import modelo.AnchoColumna;
import modelo.ArticuloDAO;
import modelo.Articulos;
import modelo.ConexionPSQL;
import modelo.Detallemovimiento;
import modelo.Empresa;
import modelo.EnviarMensaje;
import modelo.FacturaDAO;
import modelo.ModelCellEditable;
import modelo.MovimientoDAO;
import modelo.Movimientos;
import modelo.Responsables;
import modelo.ResponsablesDAO;
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

public class CtrlEgresos {

    private FacturaDAO fdao;

    private final List<Detallemovimiento> listaDetelle;

    private final ModelCellEditable dt;
    private double totalMovimiento;

    private Empresa empresa;

    private Tipomovimiento tipoMov;

    private Usuarios usuario;
    private final Vista_Movimientos vistaEgreso;

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

    private int fila;
    private boolean selPorducto;

    private final MovimientoDAO moviDAO;
    private final TipomovimientoDAO tipoMoviDAO;

    private final Frame frameModal;

    private List<Articulos> listaArticulos;

    private Predictor predictor;
    private Element elemento;
    private MouseAdapter eventoClick;

    private double IVA;
    private double iva;

    private final ArticuloDAO articuloDAO;

    private JasperViewer jv;

    public CtrlEgresos(Vista_Movimientos vistaEgreso) {
        this.vistaEgreso = vistaEgreso;
        this.vistaEgreso.setResizable(false);

        vistaEgreso.titulo.setText("EGRESO DE INVENTARIO");

        moviDAO = new MovimientoDAO();
        tipoMoviDAO = new TipomovimientoDAO();

        fdao = new FacturaDAO();

        listaDetelle = new ArrayList<>();
        frameModal = JOptionPane.getFrameForComponent(vistaEgreso);

        dt = new ModelCellEditable();
        dt.isCellEditable(0, 0);
        this.vistaEgreso.TablaDetalle.setModel(dt);
        articuloDAO = new ArticuloDAO();

        eventos();
        selPorducto = true;
        this.vistaEgreso.TablaDetalle.setRowHeight(25);
        this.vistaEgreso.TablaDetalle.setGridColor(Color.black);
        this.vistaEgreso.TablaDetalle.setShowGrid(true);

        int medida[] = {10, 10, 300, 40, 40, 40, 40};
        AnchoColumna ac = new AnchoColumna(this.vistaEgreso.TablaDetalle, medida);
        conexionPSQL = new ConexionPSQL();
    }

    public void IniciarEgreso() {
        vistaEgreso.usuario.setText(usuario.getCodusuario());
        vistaEgreso.tipoMovimiento.removeAllItems();
        vistaEgreso.datePicker.getJFormattedTextField().setText(formatearFecha(new java.util.Date()));
        vistaEgreso.Naturaleza.setText("Egresos");
        vistaEgreso.lbltotal.setText("Total");
        vistaEgreso.costo.setEnabled(false);
        vistaEgreso.txtCantidad.setEnabled(false);
        try {
            vistaEgreso.nFactura.setEnabled(false);
            vistaEgreso.nFactura.setText(fdao.obtenerNumF());
            tipoMoviDAO.obtnerListaDetalleMov("E");
            listaTipoMov = tipoMoviDAO.listaTipoMov;
            for (int i = 0; i < tipoMoviDAO.listaTipoMov.size(); i++) {
                vistaEgreso.tipoMovimiento.addItem(tipoMoviDAO.listaTipoMov.get(i).getDescripcion());
            }
            eventoCombo();
            if (listaTipoMov.size() > 0) {
                tipoMov = listaTipoMov.get(0);
            }
            IVA = articuloDAO.obtenerIVA();
            vistaEgreso.codMovimiento.setText(moviDAO.getNextCodMovimiento() + "");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vistaEgreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        vistaEgreso.setVisible(true);
        inicializarPredictor();
        eventosPredictor();
    }

    private void cursorCargando(JInternalFrame jif) {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorNomal(JInternalFrame jif) {
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jif.setCursor(cursor);
    }

    private void inicializarPredictor() {
        try {
            predictor = new Predictor(listaArticulos, this.vistaEgreso.txtBusca);
            eventoClick = new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    elemento = predictor.getElemento();
                    if (elemento != null) {
                        try {
                            producto = articuloDAO.obtenerArticulo(elemento.key);
                            if (producto != null) {
                                if (producto.getStock() > 0) {
                                    Detallemovimiento dtm = new Detallemovimiento();
                                    dtm.setCantidad(1);
                                    dtm.setCodarticilo(producto.getCodarticulo());
                                    seleccionarProducto(dtm);
                                    vistaEgreso.txtBusca.setText("");
                                } else {
                                    JOptionPane.showMessageDialog(vistaEgreso, "Artículo sin stock", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }

                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(vistaEgreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            };
            predictor.setEventoClick(eventoClick);
            predictor.obtener(Articulos.class, "codarticulo", "descripcion");
        } catch (PredictorException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            JOptionPane.showMessageDialog(vistaEgreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarProducto(Detallemovimiento dtm) {
        if (producto != null) {
            if (listaDetelle.isEmpty()) {
                listaDetelle.add(dtm);
            }
            añadirDetalle(dtm);
            selPorducto = true;
            producto = null;
            vistaEgreso.eliminar.setEnabled(true);
            if (tipoMov.getCodtipomovimiento().equals("EPV") || tipoMov.getCodtipomovimiento().equals("PER")) {
                vistaEgreso.costo.setEnabled(false);
            } else {
                vistaEgreso.costo.setEnabled(true);
            }
            vistaEgreso.txtCantidad.setEnabled(true);
            fila = vistaEgreso.TablaDetalle.getSelectedRow();
            seleccionarTdo(vistaEgreso.txtCantidad);
        }
    }

    private void eventosPredictor() {
        vistaEgreso.txtBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        elemento = predictor.getElemento();
                        if (elemento != null) {
                            try {
                                producto = articuloDAO.obtenerArticulo(elemento.key);
                                if (producto != null) {
                                    if (producto.getStock() > 0) {
                                        Detallemovimiento dtm = new Detallemovimiento();
                                        dtm.setCantidad(1);
                                        dtm.setCodarticilo(producto.getCodarticulo());
                                        seleccionarProducto(dtm);
                                        vistaEgreso.txtBusca.setText("");
                                    } else {
                                        JOptionPane.showMessageDialog(vistaEgreso, "Artículo sin stock", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(vistaEgreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;
                }
            }
        });

    }

    private void eventoCombo() {
        vistaEgreso.tipoMovimiento.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                tipoMov = listaTipoMov.get(vistaEgreso.tipoMovimiento.getSelectedIndex());

                if (tipoMov.getCodtipomovimiento().equals("EPV")) {
                    vistaEgreso.bucsResponsable.setEnabled(true);
                    vistaEgreso.nFactura.setEnabled(true);
                    vistaEgreso.costo.setEnabled(false);
                    vistaEgreso.lbcosto.setText("PVP");
                    responsable = null;
                    vistaEgreso.responsable.setText("");
                    vistaEgreso.txtCantidad.setValue(0);
                } else {
                    if (tipoMov.getCodtipomovimiento().equals("PER")) {
                        vistaEgreso.nFactura.setEnabled(false);
                        vistaEgreso.costo.setEnabled(false);
                        try {
                            responsable = new ResponsablesDAO().obtenerResponsable("E", "0999991327413");
                            if (responsable != null) {
                                vistaEgreso.responsable.setText(responsable.getRazonsocial());
                                vistaEgreso.bucsResponsable.setEnabled(false);
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(vistaEgreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        responsable = null;
                        vistaEgreso.responsable.setText("");
                        vistaEgreso.nFactura.setEnabled(true);
                        vistaEgreso.bucsResponsable.setEnabled(true);
                        if (listaDetelle.size() > 0) {
                            vistaEgreso.costo.setEnabled(true);
                            seleccionarTdo(vistaEgreso.txtCantidad);
                        }
                    }
                    vistaEgreso.lbcosto.setText("Costo");
                }
                vistaEgreso.costo.setValue(0);
                vistaEgreso.txtCantidad.setValue(1);
                actualizarDetalles();
                if (dt.getRowCount() > 1) {
                    vistaEgreso.TablaDetalle.setRowSelectionInterval(fila, fila);
                } else {
                    vistaEgreso.eliminar.setEnabled(false);
                    vistaEgreso.txtCantidad.setEnabled(false);
                }
            }
        });
    }

    private void eventos() {
        vistaEgreso.bucsResponsable.addActionListener((ActionEvent e) -> {
            InicializarVistaResponsable();
            controlador_responsable = new Ctrl_responsable(vista_resposable);
            controlador_responsable.tipo = 'C';
            controlador_responsable.IniciarVistaResponsable();
        });

        vistaEgreso.datePanel.addActionListener((ActionEvent e) -> {
            java.util.Date date = (java.util.Date) vistaEgreso.datePicker.getModel().getValue();
            String fechaFormat = formatearFecha(date);
            vistaEgreso.datePicker.getJFormattedTextField().setText(fechaFormat);
            vistaEgreso.datePicker.getModel().setSelected(true);
        });

        vistaEgreso.busc_Art.addActionListener((ActionEvent e) -> {
            inicializarVistaArticulo();
            selPorducto = false;
            ctrl_producto = new ControladorArticulo(vista_producto);
            ctrl_producto.naturaleza = 'E';
            ctrl_producto.setListaDetelle(listaDetelle);
            ctrl_producto.IniciarVistaProducto();
        });

        vistaEgreso.eliminar.addActionListener((ActionEvent e) -> {
            if (vistaEgreso.TablaDetalle.getSelectedRowCount() > 1) {
                int op = JOptionPane.showConfirmDialog(null, "¿Estás seguro que deseas eliminar estos producto?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (op == JOptionPane.YES_OPTION) {
                    int row[] = vistaEgreso.TablaDetalle.getSelectedRows();
                    eliminarPruducto(row);
                    vistaEgreso.eliminar.setEnabled(false);
                    vistaEgreso.costo.setValue(0.00);
                    vistaEgreso.txtCantidad.setValue(0.00);
                }
            } else {
                int op = JOptionPane.showConfirmDialog(null, "¿Estás seguro que deseas eliminar este producto?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (op == JOptionPane.YES_OPTION) {
                    int row = vistaEgreso.TablaDetalle.getSelectedRow();
                    dt.removeRow(row);
                    eliminarPruducto(row);
                    vistaEgreso.eliminar.setEnabled(false);
                    vistaEgreso.costo.setValue(0.00);
                    vistaEgreso.txtCantidad.setValue(0.00);
                }
            }

            vistaEgreso.addInternalFrameListener(new InternalFrameAdapter() {
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

        });

        vistaEgreso.TablaDetalle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                fila = vistaEgreso.TablaDetalle.getSelectedRow();
                if (!(fila == vistaEgreso.TablaDetalle.getRowCount() - 1)) {
                    vistaEgreso.txtCantidad.setValue(vistaEgreso.TablaDetalle.getValueAt(fila, 1));
                    vistaEgreso.costo.setValue(vistaEgreso.TablaDetalle.getValueAt(fila, 3));
                    vistaEgreso.eliminar.setEnabled(true);

                    if (tipoMov.getCodtipomovimiento().equals("EPV") || tipoMov.getCodtipomovimiento().equals("PER")) {
                        vistaEgreso.costo.setEnabled(false);
                    } else {
                        vistaEgreso.costo.setEnabled(true);
                    }

                    vistaEgreso.txtCantidad.setEnabled(true);
                    mostrarStock(vistaEgreso.TablaDetalle.getValueAt(fila, 0).toString(), vistaEgreso.TablaDetalle.getValueAt(fila, 1).toString());
                } else {
                    vistaEgreso.eliminar.setEnabled(false);
                    vistaEgreso.costo.setValue(0.00);
                    vistaEgreso.txtCantidad.setValue(0.00);
                    dt.setValueAt("", fila, 1);
                    dt.setValueAt("", fila, 3);
                    vistaEgreso.costo.setEnabled(false);
                    vistaEgreso.txtCantidad.setEnabled(false);
                    mostrarStock("", "0");
                }
            }
        });

        vistaEgreso.TablaDetalle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (fila == dt.getColumnCount() - 1) {
                        seleccionarTdo(vistaEgreso.txtCantidad);
                    } else {
                        vistaEgreso.txtCantidad.setValue(dt.getValueAt(fila, 1));
                        vistaEgreso.txtBusca.requestFocus();
                    }
                }
            }
        });

        dt.addTableModelListener((TableModelEvent e) -> {//           
            if (e.getType() == TableModelEvent.UPDATE) {
                final int row = e.getFirstRow();
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
                                JOptionPane.showMessageDialog(vistaEgreso, "Código de producto incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                                dt.setValueAt("", row, column);
                                dt.setValueAt("", row, 1);
                                dt.setValueAt("", row, 3);
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(vistaEgreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return;
                }

                if (column == 1) {
                    if (listaDetelle.size() > 0) {
                        if (!(fila == vistaEgreso.TablaDetalle.getRowCount() - 1)) {
                            producto2 = listaDetelle.get(fila).getArticulos();
                            double cantidad = Double.parseDouble(dt.getValueAt(row, column).toString());
                            editarCantidad(cantidad);
                        }
                    }
                }
                if (column == 3 && selPorducto) {
                    if (listaDetelle.size() > 0) {
                        if (!(fila == vistaEgreso.TablaDetalle.getRowCount() - 1)) {
                            producto2 = listaDetelle.get(fila).getArticulos();
                            double precio = Double.parseDouble(dt.getValueAt(fila, 3).toString());
                            editarCosto(precio);
                        }
                    }
                }
                vistaEgreso.TablaDetalle.setRowSelectionInterval(fila, fila);
            }
        });

        vistaEgreso.registrar.addActionListener((ActionEvent e) -> {
            if (vistaEgreso.responsable.getText().equals("")) {
                JOptionPane.showMessageDialog(vistaEgreso, "Selecciona un cliente", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                java.util.Date fa = new java.util.Date();
                fa = new java.util.Date(formatearFecha(fa));
                java.util.Date fs = (java.util.Date) vistaEgreso.datePicker.getModel().getValue();
                fs = new java.util.Date(formatearFecha(fs));
                if (fs.after(fa)) {
                    JOptionPane.showMessageDialog(vistaEgreso, "La fecha no debe ser mayor a la fecha actual", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (listaDetelle.isEmpty()) {
                        JOptionPane.showMessageDialog(vistaEgreso, "Ingresa al menos un producto", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (vistaEgreso.concepto.getText().equals("")) {
                            JOptionPane.showMessageDialog(vistaEgreso, "Escribe el concepto de la transaccion", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            Movimientos mov = new Movimientos();
                            if (!tipoMov.getCodtipomovimiento().equals("PER")) {
                                if (!vistaEgreso.nFactura.getText().equals("")) {
                                    DecimalFormat df = new DecimalFormat("000000000");
                                    String nf = vistaEgreso.nFactura.getText();
                                    nf = df.format(Integer.parseInt(nf));
                                    mov.setNfactura(nf);
                                } else {
                                    JOptionPane.showMessageDialog(vistaEgreso, "Escribe el numero de factura de la transaccion", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                            for (int i = 0; i < listaDetelle.size(); i++) {
                                Detallemovimiento get = listaDetelle.get(i);
                                Articulos art = get.getArticulos();
                                if (tipoMov.getCodtipomovimiento().equals("EPV")) {
                                    if (art.getPvp() == 0) {
                                        JOptionPane.showMessageDialog(vistaEgreso, "El precio de los articulos no debe ser 0", "Error", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                } else {
                                    if (get.getCosto() == 0) {
                                        JOptionPane.showMessageDialog(vistaEgreso, "El costo de los articulos no debe ser 0", "Error", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                }
                            }
                            int op = JOptionPane.showConfirmDialog(vistaEgreso, "Esta seguro de registrar esta transacciòn", "Registrar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (op == JOptionPane.YES_OPTION) {
                                Connection con = null;
                                long id = 0;
                                try {
                                    cursorCargando(vistaEgreso);
                                    mov.setTipomovimiento(tipoMov);
                                    mov.setNaturaleza(vistaEgreso.Naturaleza.getText().charAt(0));
                                    mov.setCodusuario(usuario.getCodusuario());
                                    mov.setResponsables(responsable);
                                    mov.setTotalmovimiento(totalMovimiento);
                                    mov.setConcepto(vistaEgreso.concepto.getText());
                                    mov.setFechamovimiento(fechaMovimiento);
                                    insertarNfactura(vistaEgreso.nFactura.getText(), responsable);
                                    id = moviDAO.insertarMov(mov, listaDetelle);

                                    con = moviDAO.getConexion();
                                    JOptionPane.showMessageDialog(vistaEgreso, "Datos Ingresados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                    while (dt.getRowCount() > 0) {
                                        dt.removeRow(0);
                                    }
                                    listaDetelle.clear();
                                    vistaEgreso.concepto.setText("");
                                    vistaEgreso.responsable.setText("");
                                    vistaEgreso.SubtotalMovimiento.setText("0.00");
                                    vistaEgreso.nFactura.setText("");
                                    vistaEgreso.txtBusca.setText("");
                                    vistaEgreso.total.setText("0.00");
                                    vistaEgreso.txtNuevoStock1.setText("");
                                    vistaEgreso.txtStock.setText("");
                                    vistaEgreso.codMovimiento.setText((id + 1) + "");
                                    vistaEgreso.iva.setText("0.00");
                                    vistaEgreso.txtCantidad.setValue(0.00);
                                    vistaEgreso.costo.setValue(0.00);
                                    vistaEgreso.txtCantidad.setEnabled(false);
                                    vistaEgreso.costo.setEnabled(false);
                                    vistaEgreso.eliminar.setEnabled(false);
                                    dt.addRow(new Object[]{});

                                    con.commit();
                                } catch (SQLException ex) {
                                    JOptionPane.showMessageDialog(vistaEgreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                    if (con != null) {
                                        try {
                                            con.rollback();
                                        } catch (SQLException ex1) {
                                            JOptionPane.showMessageDialog(vistaEgreso, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                                            responsable = null;
                                            vistaEgreso.nFactura.setText(fdao.obtenerNumF());
                                        } catch (SQLException ex1) {
                                            JOptionPane.showMessageDialog(vistaEgreso, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                        cursorNomal(vistaEgreso);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }); 

        vistaEgreso.cancelar.addActionListener((ActionEvent e) -> {
            vistaEgreso.dispose();
        });

        eventoSpinerCantidad();
        eventoSpinerCosto();
        spinerModel(vistaEgreso.txtCantidad);
        spinerModel(vistaEgreso.costo);
        eventoEnterCantidad();
        eventoEnterCosto();
        bloquearNumeros(vistaEgreso.nFactura);
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

    private void mostrarStock(String cod, String ns) {
        try {
            producto3 = articuloDAO.obtenerArticulo(cod);
            if (producto3 != null) {
                vistaEgreso.txtStock.setText(producto3.getStock() + "");
                double n;
                if (tipoMov.getCodtipomovimiento().equals("EPV") || tipoMov.getCodtipomovimiento().equals("PER")) {
                    n = -Double.parseDouble(ns) + producto3.getStock();
                } else {
                    n = Double.parseDouble(ns) + producto3.getStock();
                }
                vistaEgreso.txtNuevoStock1.setText(n + "");
                producto3 = null;
            } else {
                vistaEgreso.txtStock.setText("");
                vistaEgreso.txtNuevoStock1.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vistaEgreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertarNfactura(String nf, Responsables responsables) {
        for (int i = 0; i < listaDetelle.size(); i++) {
            listaDetelle.get(i).setNfactura(nf);
            listaDetelle.get(i).setResponsables(responsables);
        }
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

    private void eventoEnterCantidad() {
        ((JSpinner.DefaultEditor) vistaEgreso.txtCantidad.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!tipoMov.getCodtipomovimiento().equals("EPV")) {
                        seleccionarTdo(vistaEgreso.costo);
                    } else {
                        vistaEgreso.txtBusca.requestFocus();
                    }
                }
            }
        });
    }

    private void eventoEnterCosto() {
        ((JSpinner.DefaultEditor) vistaEgreso.costo.getEditor()).getTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    vistaEgreso.txtBusca.requestFocus();
                }
            }
        });
    }

    private void eventoSpinerCantidad() {
        vistaEgreso.txtCantidad.addChangeListener((ChangeEvent e) -> {
            int row = vistaEgreso.TablaDetalle.getSelectedRow();
            if (row >= 0 && row != vistaEgreso.TablaDetalle.getRowCount() - 1) {
                SpinnerNumberModel mo = (SpinnerNumberModel) vistaEgreso.txtCantidad.getModel();
                vistaEgreso.TablaDetalle.setValueAt(vistaEgreso.txtCantidad.getValue(), row, 1);
                mostrarStock(vistaEgreso.TablaDetalle.getValueAt(fila, 0).toString(), vistaEgreso.TablaDetalle.getValueAt(fila, 1).toString());
            }
        });
    }

    private void eventoSpinerCosto() {
        vistaEgreso.costo.addChangeListener((ChangeEvent e) -> {
            int row = vistaEgreso.TablaDetalle.getSelectedRow();
            if (row >= 0) {
                vistaEgreso.TablaDetalle.setValueAt(vistaEgreso.costo.getValue(), row, 3);
            }
        });
    }

    private void InicializarVistaResponsable() {
        vista_resposable = new Responsable(frameModal, true);
        //vista_resposable = new Responsable(this.vistaEgreso, true);
        vista_resposable.setLocationRelativeTo(frameModal);
        vista_resposable.setResizable(false);
        vista_resposable.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                responsable = controlador_responsable.getResponsable();
                if (responsable != null) {
                    vistaEgreso.responsable.setText(responsable.getRazonsocial());
                } else {
                    System.out.println("nulo");
                    vistaEgreso.responsable.setText("");
                }
            }
        });
    }

    private void inicializarVistaArticulo() {
        vista_producto = new Articulo(frameModal, true);
        vista_producto.setLocationRelativeTo(frameModal);
        vista_producto.setResizable(false);
        vistaEgreso.eliminar.setEnabled(false);
        vista_producto.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                producto = ctrl_producto.getArticulo();
                seleccionarProducto(ctrl_producto.getDetalle());
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

    private double stock;

    private void añadirDetalle(Detallemovimiento dtm) {
        SpinnerNumberModel mo = (SpinnerNumberModel) vistaEgreso.txtCantidad.getModel();
        if (tipoMov.getCodtipomovimiento().equals("EPV")) {
            stock = dtm.getArticulos().getStock();
            mo.setMaximum(stock);
        } else {
            //mo.setMaximum(10000);
        }

        selPorducto = false;
        for (int i = 0; i < listaDetelle.size(); i++) {
            if (listaDetelle.get(i).getCodarticilo().equals(dtm.getCodarticilo())) {
                if (tipoMov.getCodtipomovimiento().equals("EPV")) {
                    vistaEgreso.costo.setValue(listaDetelle.get(i).getArticulos().getPvp());
                } else {
                    vistaEgreso.costo.setValue(listaDetelle.get(i).getCosto());
                }
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
            double costo;
            double uiva = 0;

            switch (tipoMov.getCodtipomovimiento()) {
                case "EPV":
                    costo = listaDetelle.get(i).getArticulos().getPvp();
                    listaDetelle.get(i).setCosto(listaDetelle.get(i).getArticulos().getCosto());
                    break;
                case "PER":
                    costo = listaDetelle.get(i).getArticulos().getCosto();
                    break;
                default:
                    costo = listaDetelle.get(i).getCosto();
                    break;
            }
            double subtotal = cantidad * costo;
            totalMovimiento += subtotal;

            if (!tipoMov.getCodtipomovimiento().equals("PER")) {
                if (listaDetelle.get(i).getArticulos().getGrabaiva() == 'S') {
                    uiva = subtotal * IVA / 100;
                }
            }
            iva += uiva;
            double total = subtotal + uiva;
            BigDecimal t = new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP);
            Object[] filas = {codarticulo, cantidad, descripcion, costo, subtotal, uiva, t};
            dt.addRow(filas);
            if (producto != null) {
                if (listaDetelle.get(i).getArticulos().getCodarticulo().equals(producto.getCodarticulo())) {
                    vistaEgreso.txtCantidad.setValue(listaDetelle.get(i).getCantidad());
                    vistaEgreso.TablaDetalle.setRowSelectionInterval(i, i);
                }
            }
        }

        BigDecimal t = new BigDecimal(totalMovimiento).setScale(2, BigDecimal.ROUND_HALF_UP);
        vistaEgreso.SubtotalMovimiento.setText(t + "");
        vistaEgreso.iva.setText(iva + "");
        BigDecimal i = new BigDecimal(iva).setScale(2, BigDecimal.ROUND_HALF_UP);;
        BigDecimal r = t.add(i);

        vistaEgreso.total.setText(r + "");
        if (listaDetelle.size() > 1) {
            mostrarStock(vistaEgreso.TablaDetalle.getValueAt(fila, 0).toString(), vistaEgreso.TablaDetalle.getValueAt(fila, 1).toString());
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

    private final ConexionPSQL conexionPSQL;

    private void generarReporte(long codf) {
        Connection con = null;
        try {
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
            EnviarMensaje em = new EnviarMensaje();
            em.enviarConGMailAdjunto(responsable.getCorreo(), "Se ha generado una nueva factura", "Calzados Mechita le informa su compra, no responda este mensaje", "recursos/facturas/" + codf + ".pdf");
            jv.setVisible(true);

        } catch (JRException e) {
            JOptionPane.showMessageDialog(vistaEgreso, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vistaEgreso, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } catch (java.io.FileNotFoundException e) {
        } catch (MessagingException ex) {
            Logger.getLogger(CtrlEgresos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    JOptionPane.showMessageDialog(vistaEgreso, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

}
