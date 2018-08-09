/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorMovimientos;

import controlador.mainControler;
import controladorResponsable.ctrl_responsable2;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import modelo.DataSet;
import modelo.Responsables;
import modelo.ResponsablesDAO;
import modelo.TipoResponsableDAO;
import vistaMovimiento.Responsable;
import vistaResponsables.vista_responsable2;

public class Ctrl_responsable {

    private DataSet dt;
    private Responsables responsable;

    public char tipo;

    private final Responsable vista_responsable;

    private final TipoResponsableDAO tiporesDAO;
    private final ResponsablesDAO respoDAO;
    private final Frame frameModal;

    public Ctrl_responsable(Responsable vista_responsable) {
        this.vista_responsable = vista_responsable;
        frameModal = JOptionPane.getFrameForComponent(this.vista_responsable);
        tiporesDAO = new TipoResponsableDAO();
        respoDAO = new ResponsablesDAO();
        tipo = 'P';
        eventos();
    }

    public void IniciarVistaResponsable() {
        try {
            tiporesDAO.obtnerListaRespondable(tipo + "");
            for (int i = 0; i < tiporesDAO.listaResponsable.size(); i++) {
                vista_responsable.tipoResponsable.addItem(tiporesDAO.listaResponsable.get(i).getDescripcion());
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        vista_responsable.setVisible(true);
    }

    private void eventos() {
        vista_responsable.cancelar.addActionListener((ActionEvent e) -> {
            cerrar();
        });

        vista_responsable.selecionar.addActionListener((ActionEvent e) -> {

            if (vista_responsable.tabla.getSelectedRow() >= 0) {
                seleccionar();
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un proovedor", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        vista_responsable.buscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscar();
            }
        });

        vista_responsable.tipoResponsable.addItemListener((ItemEvent e) -> {
            buscar();
        });

        vista_responsable.tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    seleccionar();
                }
            }
        });

        vista_responsable.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrar();
            }
        });

        vista_responsable.agre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inicializarResponsable();
                ctrl_responsable.IniciarVistaResponsable();
            }
        });
    }

    private ctrl_responsable2 ctrl_responsable;
    private vista_responsable2 vista;

    private void inicializarResponsable() {
        vista = new vista_responsable2(frameModal, true);
        vista.setLocationRelativeTo(frameModal);
        ctrl_responsable = new ctrl_responsable2(vista);
        ctrl_responsable.tipo = tipo;
        ctrl_responsable.setListaCiudades(mainControler.listaCiudades);
        ctrl_responsable.setListaProvincias(mainControler.listaProvincias);
        vista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                buscar();

            }
        });
    }

    private void cerrar() {
        if (responsable == null) {
            int opcion = JOptionPane.showConfirmDialog(null, "No has seleccionado un proovedor"
                    + "\n¿Estás seguro que deseas salir?", "Salir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opcion == JOptionPane.NO_OPTION) {
                vista_responsable.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            } else {
                vista_responsable.dispose();
            }
        }
    }

    private void buscar() {
        try {
            dt = respoDAO.obtenerResponsables(tipo + "", vista_responsable.buscar.getText());
            vista_responsable.tabla.setModel(dt);
            dt.setCellEditable(false);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void seleccionar() {
        int row = vista_responsable.tabla.getSelectedRow();
        String identificacion = dt.getValueAt(row, 0).toString();
        try {
            responsable = respoDAO.obtenerResponsable(tipo + "", identificacion);
            vista_responsable.dispose();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Responsable getVista_responsable() {
        return vista_responsable;
    }

    public Responsables getResponsable() {
        return responsable;
    }

}
