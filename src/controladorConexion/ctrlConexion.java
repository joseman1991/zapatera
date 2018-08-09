/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorConexion;

import vistasInicio.cambio;
import modelo.confi;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import modelo.ConexionPSQL;

/**
 *
 * @author JOSE-MA
 */
public class ctrlConexion {

    private final ConexionPSQL con;
    private final confi conf;

    private final cambio vista_congi;

    public ctrlConexion(cambio vista_cambio) {
        this.conf = new confi();
        this.con = new ConexionPSQL();
        vista_congi = vista_cambio;

        inicializar();
        eventos();
    }

    public void iniciarVistaConfig() {
        vista_congi.setVisible(true);
    }

    private void inicializar() {
        vista_congi.user.setText(conf.accderPorpiedades("usuario"));
        String clave = conf.desencriptar("clave");
        vista_congi.password.setText(clave);
        vista_congi.server.setText(conf.accderPorpiedades("host"));
        vista_congi.baseDatos.setText(conf.accderPorpiedades("basedatos"));
        vista_congi.puerto.setText(conf.accderPorpiedades("puerto"));
    }

    private void eventos() {
        vista_congi.test.addActionListener((ActionEvent e) -> {
            String estado = con.testConection(vista_congi.baseDatos.getText(), vista_congi.user.getText(), vista_congi.password.getText(), vista_congi.server.getText(), vista_congi.puerto.getText());
            vista_congi.suscess.setText(estado);
            if (estado.equals("Conexión Exitosa")) {
                vista_congi.suscess.setForeground(new Color(17, 101, 42));
            } else {
                vista_congi.suscess.setForeground(Color.red);
                if (vista_congi.suscess.getText().equals("Base de datos no existe en el servidor indicado")) {
                    if (JOptionPane.showConfirmDialog(null, "La base de datos " + vista_congi.baseDatos.getText() + " no existe"
                            + "\n¿Deseas crearla?", "¿Crear base de datos?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
                            == JOptionPane.YES_OPTION) {
                        con.testConection("postgres", vista_congi.password.getText(), vista_congi.password.getText(), vista_congi.server.getText(), vista_congi.puerto.getText());
                        con.crearDataBase(vista_congi.baseDatos.getText());
                        JOptionPane.showMessageDialog(null, "Base de datos " + vista_congi.baseDatos.getText()
                                + " ha sido creada satisfactoriamente", "Correcto", JOptionPane.INFORMATION_MESSAGE);
                    }
                    vista_congi.suscess.setText("");
                }
            }

            if (vista_congi.suscess.getText().equals("Conexión Exitosa")) {
                vista_congi.action.setEnabled(true);
            } else {
                vista_congi.action.setEnabled(false);
            }
        });

        vista_congi.action.addActionListener((ActionEvent e) -> {
            Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
            vista_congi.setCursor(cursor);
            String clave = conf.encriptar(vista_congi.password.getText());
            conf.insertar(vista_congi.server.getText(), vista_congi.puerto.getText(), vista_congi.baseDatos.getText(), vista_congi.user.getText(), clave);
            JOptionPane.showMessageDialog(vista_congi, "Configuración gardada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cursor = new Cursor(Cursor.DEFAULT_CURSOR);
            vista_congi.setCursor(cursor);
            vista_congi.dispose();
        });
        vista_congi.cancelar.addActionListener((ActionEvent e) -> {
            vista_congi.dispose();
        });

        vista_congi.baseDatos.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                passwordKeyReleased(evt);
            }
        });
        vista_congi.server.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                passwordKeyReleased(evt);
            }
        });
        vista_congi.password.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                passwordKeyReleased(evt);
            }
        });
        vista_congi.user.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                passwordKeyReleased(evt);
            }
        });
    }

    private void passwordKeyReleased(java.awt.event.KeyEvent evt) {
        vista_congi.action.setEnabled(false);
        vista_congi.suscess.setText("");
    }

}
