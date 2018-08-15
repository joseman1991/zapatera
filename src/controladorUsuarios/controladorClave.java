/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladorUsuarios;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.Usuarios;
import modelo.UsuariosDAO;
import vistaUsuarios.cambiar_clave;

/**
 *
 * @author JOSE-MA
 */
public class controladorClave {

    private final Usuarios usuario;
    private final cambiar_clave vista_clave;
    private final UsuariosDAO usuarioDAO;

    public controladorClave(Usuarios usuario, cambiar_clave vista_clave) {
        this.usuario = usuario;
        this.vista_clave = vista_clave;
        usuarioDAO = new UsuariosDAO();
        eventos();
    }

    public void iniciar() {
        vista_clave.setLocationRelativeTo(null);
        vista_clave.setVisible(true);
    }

    private void eventos() {
        vista_clave.cambiar.addActionListener((ActionEvent e) -> {

            if (vista_clave.borrarC.isSelected()) {
                if (vista_clave.claven.getText().equals(vista_clave.claver.getText())) {
                    usuario.setClave(vista_clave.claven.getText());
                    try {
                        if (usuarioDAO.actualizarClave(usuario) == 1) {
                            JOptionPane.showMessageDialog(vista_clave, "Contraseña cmbiada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            usuario.setClave("");
                            vista_clave.dispose();
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(vista_clave, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(vista_clave, "La contraseña no coincide con la anterior", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (vista_clave.clavean.getText().equals("")) {
                    JOptionPane.showMessageDialog(vista_clave, "Campo vacíos", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (vista_clave.claven.getText().equals("")) {
                    JOptionPane.showMessageDialog(vista_clave, "Campo vacíos", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (vista_clave.claver.getText().equals("")) {
                    JOptionPane.showMessageDialog(vista_clave, "Campo vacíos", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (vista_clave.clavean.getText().equals(usuario.getClave())) {
                        if (vista_clave.claven.getText().equals(vista_clave.claver.getText())) {
                            usuario.setClave(vista_clave.claven.getText());
                            try {
                                if (usuarioDAO.actualizarClave(usuario) == 1) {
                                    JOptionPane.showMessageDialog(vista_clave, "Contraseña cmbiada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                    vista_clave.dispose();
                                }
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(vista_clave, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(vista_clave, "La contraseña no coincide con la anterior", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(vista_clave, "La contraseña antigua no coincide", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });

        vista_clave.cancelar.addActionListener((ActionEvent e) -> {
            vista_clave.dispose();
        });

        vista_clave.borrarC.addActionListener((ActionEvent e) -> {
            vista_clave.clavean.setEditable(false);
            vista_clave.clavean.setText("");
        });
    }

}
