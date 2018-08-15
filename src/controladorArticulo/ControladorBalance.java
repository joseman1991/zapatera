/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorArticulo;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import modelo.AnchoColumna;
import modelo.ArticuloDAO;
import modelo.Articulos;
import modelo.Categorias;
import modelo.LeerXLSX;
import modelo.Subcategorias;
import modelo.Usuarios;
import vistaArticulo.Balance;

/**
 *
 * @author JOSE
 */
public class ControladorBalance {

    private Usuarios u;
    private final Balance balance;
    private DefaultTableModel dtm;
    private FileChooser fc;
    private File archivo;
    private final LeerXLSX lcsv;
    private final List<String[]> filas;
    private final List<Articulos> listaArticulos;

    public ControladorBalance(Balance balance) {
        this.lcsv = new LeerXLSX();
        this.balance = balance;
        filas = lcsv.getFilas();
        listaArticulos = new ArrayList<>();
        balance.setTitle("Exportar artículos al sistema");
        eventos();

    }

    private void eventos() {
        String[] coumnas = {"FILA", "ARTÍCULO", "CATEGORÍA", "SUBCATEGORÍA", "STOCK", "COSTO", "PRECIO", "GRABA IVA"};
        dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        dtm.setColumnIdentifiers(coumnas);        
        balance.tabla.setModel(dtm);
        balance.tabla.setRowHeight(25);
        balance.tabla.setGridColor(Color.black);
        balance.tabla.setShowGrid(true);
        int medida[] = {50, 200, 200, 200, 100, 100, 100, 100};
        AnchoColumna ac = new AnchoColumna(this.balance.tabla, medida);
        eventoBotonBuscar();
        eventoGuardar();
        eventoCerrar();
        eventoActualizar();
        eventoBorrar();
    }

    public void InicializarBalance() {
        balance.setVisible(true);
    }

    private void cursorCargando(JInternalFrame jif) {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorNomal(JInternalFrame jif) {
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jif.setCursor(cursor);
    }

    private void eventoBotonBuscar() {
        balance.boton.addActionListener((ActionEvent e) -> {
            cursorCargando(balance);
            FileFilter filter = new FileNameExtensionFilter("Archivo de Excel| *.xlxs", "xlsx");
            this.fc = new FileChooser();
            fc.setFileFilter(filter);
            fc.setAcceptAllFileFilterUsed(false);
            int status = fc.showOpenDialog(balance);
            if (status == JFileChooser.APPROVE_OPTION) {
                archivo = fc.getSelectedFile();
                if (archivo != null) {
                    balance.nombre.setText(archivo.getAbsolutePath());
                    try {
                        lcsv.leerXLSX(archivo.getAbsolutePath());
                        for (int i = 0; i < filas.size(); i++) {
                            String[] get = filas.get(i);
                            dtm.addRow(get);
                        }
                        llenarListaArticulos();
                        fc.LookAndFeel();
                        balance.actuallizar.setEnabled(true);
                        balance.eliminar.setEnabled(true);
                    } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                        JOptionPane.showMessageDialog(balance, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            cursorNomal(balance);
        });
    }

    private void eventoCerrar() {
        balance.cancelar.addActionListener((ActionEvent e) -> {
            balance.dispose();
        });
    }

    private void eventoGuardar() {

        balance.accion.addActionListener((ActionEvent e) -> {
            cursorCargando(balance);
            if (listaArticulos.size() > 0) {
                Connection con = null;
                ArticuloDAO aO = new ArticuloDAO();
                int estado = 0;
                try {
                    estado = aO.insertarListaDeArticulos(listaArticulos,u);
                    con = aO.getConexion();
                    JOptionPane.showMessageDialog(balance, "Articulos registrados", "Listo", JOptionPane.INFORMATION_MESSAGE);
                    while (dtm.getRowCount() > 0) {
                        dtm.removeRow(0);
                    }
                    con.commit();
                    cursorNomal(balance);

                } catch (SQLException ex) {
                    if (ex.getSQLState().equals("23505")) {
                        String msg = String.format("Subcatgoría %s no corresponde a la categoría seleccionada, %s", aO.getSubcategoria(), aO.getMensaje());
                        JOptionPane.showMessageDialog(balance, msg, "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(balance, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (con != null) {
                        try {
                            con.rollback();
                        } catch (SQLException ex1) {
                            JOptionPane.showMessageDialog(balance, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        cursorNomal(balance);
                    }
                } finally {
                    if (con != null) {
                        try {
                            con.close();
                        } catch (SQLException ex1) {
                            JOptionPane.showMessageDialog(balance, ex1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    cursorNomal(balance);
                }
            } else {
                JOptionPane.showMessageDialog(balance, "Ingresa al menos un producto", "Error", JOptionPane.ERROR_MESSAGE);
                cursorNomal(balance);
            }

        });
    }

    private void llenarListaArticulos() {
        listaArticulos.clear();
        for (int i = 0; i < filas.size(); i++) {
            String[] get = filas.get(i);
            Articulos a = new Articulos();
            int k = 1;
            a.setDescripcion(get[k++]);
            Categorias c = new Categorias();
            c.setDescripcion(get[k++]);
            Subcategorias sc = new Subcategorias();
            sc.setCategorias(c);
            sc.setDescripcion(get[k++]);
            a.setSubcategorias(sc);
            try {
                double stock = Double.parseDouble(get[k++]);
                double costo = Double.parseDouble(get[k++]);
                double precio = Double.parseDouble(get[k++]);
                Character grabaIVA = get[k++].charAt(0);
                a.setGrabaiva(grabaIVA);
                a.setStock(stock);
                a.setCosto(costo);
                a.setPvp(precio);
                listaArticulos.add(a);
            } catch (HeadlessException | NumberFormatException e) {
                JOptionPane.showMessageDialog(balance, "Error de tipo de datos en la fila " + (i + 1), "Error", JOptionPane.ERROR_MESSAGE);
                listaArticulos.clear();
                return;
            }
        }
        System.out.println("ok");
    }

    private void eventoActualizar() {
        balance.actuallizar.addActionListener((ActionEvent e) -> {
            cursorCargando(balance);
            if (archivo != null) {
                try {
                    while (dtm.getRowCount() > 0) {
                        dtm.removeRow(0);
                    }
                    lcsv.leerXLSX(archivo.getAbsolutePath());
                    for (int i = 0; i < filas.size(); i++) {
                        String[] get = filas.get(i);
                        dtm.addRow(get);
                    }
                    llenarListaArticulos();
                    fc.LookAndFeel();
                } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    JOptionPane.showMessageDialog(balance, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            cursorNomal(balance);
        });
    }

    private void eventoBorrar() {

        balance.eliminar.addActionListener((ActionEvent e) -> {
            cursorCargando(balance);

            if (archivo != null) {
                while (dtm.getRowCount() > 0) {
                    dtm.removeRow(0);
                }
                balance.nombre.setText("");
                archivo = null;
                balance.eliminar.setEnabled(false);
                balance.actuallizar.setEnabled(false);
            }
            cursorNomal(balance);
        });
    }

    public void setU(Usuarios u) {
        this.u = u;
    }

    
    
}
