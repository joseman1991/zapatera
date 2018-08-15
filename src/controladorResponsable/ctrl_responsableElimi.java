/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorResponsable;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.Ciudades;
import modelo.CiudadesDAO;
import modelo.DataSet;
import modelo.ProvinciaDAO;
import modelo.Responsables;
import modelo.ResponsablesDAO;
import modelo.TipoResponsableDAO;
import vistaResponsables.vista_responsable;

/**
 *
 * @author JOSE-MA
 */
public class ctrl_responsableElimi {

    private DataSet dt;
    private Responsables responsable;

    private final vista_responsable vista_responsable;

    private final TipoResponsableDAO tiporesDAO;
    private final ResponsablesDAO respoDAO;

    private final CiudadesDAO ciudadesDAO;
    ;
    private final ProvinciaDAO provinciaDAO;
    private String codciudad;

    public ctrl_responsableElimi(vista_responsable vista_responsable) {
        this.vista_responsable = vista_responsable;
        tiporesDAO = new TipoResponsableDAO();
        respoDAO = new ResponsablesDAO();
        ciudadesDAO = new CiudadesDAO();
        provinciaDAO = new ProvinciaDAO();
    }

    public void IniciarVistaResponsable() {
        vista_responsable.identificacion.setEditable(false);
        vista_responsable.tipo_responsable.setEnabled(false);
        vista_responsable.titulo.setText("ELIMINAR RESPONSABLE");
        vista_responsable.accion.setText("Eliminar");
        try {
            tiporesDAO.obtnerListaRespondable("");
            for (int i = 0; i < tiporesDAO.listaResponsable.size(); i++) {
                vista_responsable.tipo_responsable.addItem(tiporesDAO.listaResponsable.get(i).getDescripcion());
            }
            provinciaDAO.ObtenerListaProvincia();

            for (int i = 0; i < provinciaDAO.listaProvincia.size(); i++) {
                vista_responsable.provincia.addItem(provinciaDAO.listaProvincia.get(i).getDescripcion());
            }
            ciudadesDAO.ObtenerListaCiudades(provinciaDAO.listaProvincia.get(0).getCodprovincia());

            for (int i = 0; i < ciudadesDAO.listaCiudades.size(); i++) {
                vista_responsable.ciudad.addItem(ciudadesDAO.listaCiudades.get(i).getNombreciudad());
            }
            dt = respoDAO.obtenerResponsables1("", "");
            vista_responsable.tabla.setModel(dt);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        eventos();
        vista_responsable.setVisible(true);
    }

    private void eventos() {
        vista_responsable.provincia.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                try {
                    int index = vista_responsable.provincia.getSelectedIndex();
                    String codpro = provinciaDAO.listaProvincia.get(index).getCodprovincia();
                    ciudadesDAO.ObtenerListaCiudades(codpro);
                    vista_responsable.ciudad.removeAllItems();
                    for (int i = 0; i < ciudadesDAO.listaCiudades.size(); i++) {
                        vista_responsable.ciudad.addItem(ciudadesDAO.listaCiudades.get(i).getNombreciudad());
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vista_responsable, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        vista_responsable.cancelar.addActionListener((ActionEvent e) -> {
            vista_responsable.dispose();
        });

        vista_responsable.accion.addActionListener((ActionEvent e) -> {
            if (vista_responsable.identificacion.getText().equals("")) {
                JOptionPane.showMessageDialog(vista_responsable, "Ingresa la identificacion", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (vista_responsable.direccion.getText().equals("")) {
                JOptionPane.showMessageDialog(vista_responsable, "Ingresa la direccion", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (vista_responsable.telefono.getText().equals("")) {
                JOptionPane.showMessageDialog(vista_responsable, "Ingresa el teléfono", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (vista_responsable.contacto.getText().equals("")) {
                JOptionPane.showMessageDialog(vista_responsable, "Ingresa un contacto", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (vista_responsable.razon_social.getText().equals("")) {
                JOptionPane.showMessageDialog(vista_responsable, "Ingresa la razon social", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    responsable = new Responsables();
                    responsable.setIdentificacion(vista_responsable.identificacion.getText());
                    responsable.setDireccion(vista_responsable.direccion.getText());
                    responsable.setRazonsocial(vista_responsable.razon_social.getText());
                    responsable.setTelefono(vista_responsable.telefono.getText());
                    responsable.setCorreo(vista_responsable.contacto.getText());
                    String codciudad1 = ciudadesDAO.listaCiudades.get(vista_responsable.ciudad.getSelectedIndex()).getCodciudad();
                    String codtipores = tiporesDAO.listaResponsable.get(vista_responsable.tipo_responsable.getSelectedIndex()).getCodtiporesponsable() + "";
                    responsable.setCodigociudad(codciudad1);
                    responsable.setCodtiporesposanble(codtipores);
                    if (respoDAO.eliminarResponsable(responsable) == 1) {
                        JOptionPane.showMessageDialog(vista_responsable, "Responsable eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        vista_responsable.contacto.setText("");
                        vista_responsable.telefono.setText("");
                        vista_responsable.razon_social.setText("");
                        vista_responsable.telefono.setText("");
                        vista_responsable.identificacion.setText("");
                        vista_responsable.provincia.setSelectedIndex(0);
                        vista_responsable.tipo_responsable.setSelectedIndex(0);
                        dt = respoDAO.obtenerResponsables1("", "");
                        vista_responsable.tabla.setModel(dt);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vista_responsable, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        vista_responsable.identificacion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (vista_responsable.identificacion.getText().length() == 10) {
                    e.consume();
                }
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        vista_responsable.telefono.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (vista_responsable.telefono.getText().length() == 10) {
                    e.consume();
                }
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });

        vista_responsable.buscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    dt = respoDAO.obtenerResponsables("", vista_responsable.buscar.getText());
                    dt = respoDAO.obtenerResponsables1("", vista_responsable.buscar.getText());
                    vista_responsable.tabla.setModel(dt);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vista_responsable, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        vista_responsable.tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = vista_responsable.tabla.getSelectedRow();
                try {
                    dt = respoDAO.obtenerResponsables("", vista_responsable.buscar.getText());
                    codciudad = dt.getValueAt(row, 3).toString();
                    dt = respoDAO.obtenerResponsables1("", vista_responsable.buscar.getText());
                    vista_responsable.tipo_responsable.setSelectedItem(dt.getValueAt(row, 0));
                    vista_responsable.identificacion.setText(dt.getValueAt(row, 1).toString());
                    vista_responsable.razon_social.setText(dt.getValueAt(row, 2).toString());
                    Ciudades ciud = ciudadesDAO.obtenerCuidad(codciudad);
                    vista_responsable.provincia.setSelectedItem(ciud.getProvincia().getDescripcion());
                    vista_responsable.ciudad.setSelectedItem(ciud.getNombreciudad());
                    vista_responsable.direccion.setText(dt.getValueAt(row, 4).toString());
                    vista_responsable.telefono.setText(dt.getValueAt(row, 5).toString());
                    vista_responsable.contacto.setText(dt.getValueAt(row, 6).toString());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vista_responsable, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

//                
//                
            }
        });

    }

}
