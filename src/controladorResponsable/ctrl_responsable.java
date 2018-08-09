/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorResponsable;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.AnchoColumna;
import modelo.Ciudades;
import modelo.CiudadesDAO;
import modelo.DataSet;
import modelo.Provincia;
import modelo.ProvinciaDAO;
import modelo.Responsables;
import modelo.ResponsablesDAO;
import modelo.TipoResponsableDAO;
import vistaResponsables.vista_responsable;

/**
 *
 * @author JOSE-MA
 */
public class ctrl_responsable {

    private List<Ciudades> listaCiudades;
    private List<Provincia> listaProvincias;

    private DataSet dt;
    private Responsables responsable;

    private final vista_responsable vista_responsable;

    private final TipoResponsableDAO tiporesDAO;
    private final ResponsablesDAO respoDAO;
//
//    private final CiudadesDAO ciudadesDAO;
//    private final ProvinciaDAO provinciaDAO;

    public ctrl_responsable(vista_responsable vista_responsable) {
        this.vista_responsable = vista_responsable;
        tiporesDAO = new TipoResponsableDAO();
        respoDAO = new ResponsablesDAO();
//        ciudadesDAO = new CiudadesDAO();
//        provinciaDAO = new ProvinciaDAO(); 

        this.vista_responsable.tabla.setRowHeight(25);
        this.vista_responsable.tabla.setGridColor(Color.black);
        this.vista_responsable.tabla.setShowGrid(true);
        int medida[] = {100, 100, 300, 100, 100, 100,100};
        AnchoColumna ac = new AnchoColumna(this.vista_responsable.tabla, medida);
    }

    public void IniciarVistaResponsable() {
        vista_responsable.buscar.setVisible(false);
        vista_responsable.jLabel2.setVisible(false);
        try {
            tiporesDAO.obtnerListaRespondable("");
            for (int i = 0; i < tiporesDAO.listaResponsable.size(); i++) {
                vista_responsable.tipo_responsable.addItem(tiporesDAO.listaResponsable.get(i).getDescripcion());
            }
//            provinciaDAO.ObtenerListaProvincia();

            for (int i = 0; i < listaProvincias.size(); i++) {
                vista_responsable.provincia.addItem(listaProvincias.get(i).getDescripcion());
            }

            for (int i = 0; i < listaCiudades.size(); i++) {
                Ciudades c = listaCiudades.get(i);
                if (c.getCodprovincia().equals("01")) {
                    vista_responsable.ciudad.addItem(listaCiudades.get(i).getNombreciudad());
                }
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

                int index = vista_responsable.provincia.getSelectedIndex();
                String codpro = listaProvincias.get(index).getCodprovincia();

                vista_responsable.ciudad.removeAllItems();
                for (int i = 0; i < listaCiudades.size(); i++) {
                    Ciudades c = listaCiudades.get(i);
                    if (c.getCodprovincia().equals(codpro)) {
                        vista_responsable.ciudad.addItem(listaCiudades.get(i).getNombreciudad());
                    }
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
                    String codciudad = listaCiudades.get(vista_responsable.ciudad.getSelectedIndex()).getCodciudad();
                    String codtipores = tiporesDAO.listaResponsable.get(vista_responsable.tipo_responsable.getSelectedIndex()).getCodtiporesponsable() + "";
                    responsable.setCodigociudad(codciudad);
                    responsable.setCodtiporesposanble(codtipores);
                    if (respoDAO.insertarResponsable(responsable) == 1) {
                        JOptionPane.showMessageDialog(vista_responsable, "Responsable insertado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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

    }

    public List<Ciudades> getListaCiudades() {
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudades> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public List<Provincia> getListaProvincias() {
        return listaProvincias;
    }

    public void setListaProvincias(List<Provincia> listaProvincias) {
        this.listaProvincias = listaProvincias;
    }

}
