package controladorUsuarios;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import modelo.AnchoColumna;
import modelo.DataSet;
import modelo.EstadoDAO;
import modelo.PerfilesDAO;
import modelo.Usuarios;
import modelo.UsuariosDAO;
import vistaUsuarios.Vista_Usuario;
import vistaUsuarios.cambiar_clave;

public class ctrl_Usuario {

    private final PerfilesDAO perfilDAO;
    private final Vista_Usuario vista_registro;
    private DataSet dt;

    public int op;

    private boolean est;
    private boolean est1;

    private final Frame frameModal;

    private final EstadoDAO estadoDAO;

    private controladorClave ctrl_clave;
    private cambiar_clave vista_clave;

    private final UsuariosDAO usuariosDAO;
    private String olduser;

    public ctrl_Usuario(Vista_Usuario vista_registro) {
        this.vista_registro = vista_registro;
        perfilDAO = new PerfilesDAO();
        estadoDAO = new EstadoDAO();
        usuariosDAO = new UsuariosDAO();
        frameModal = JOptionPane.getFrameForComponent(this.vista_registro);
        op = 1;
        
         this.vista_registro.tabla.setRowHeight(25);
        this.vista_registro.tabla.setGridColor(Color.black);
        this.vista_registro.tabla.setShowGrid(true);

         int medida[] = {100, 300, 100, 100};
        AnchoColumna ac = new AnchoColumna(this.vista_registro.tabla, medida);
    }

    public void iniciarRegistro() {
        vista_registro.cambiarclave.setIcon(getIcono(vista_registro.cambiarclave));
        try {
            perfilDAO.getObtnerPerfiles();
            vista_registro.perfil.addItem("SELECCIONA...");
            for (int i = 0; i < perfilDAO.getListaPerfiles().size(); i++) {
                vista_registro.perfil.addItem(perfilDAO.getListaPerfiles().get(i).getDescripcion());
            }
            estadoDAO.getObtnerEstados();
            vista_registro.estado.addItem("SELECCIONA...");
            for (int i = 0; i < estadoDAO.getListaEstados().size(); i++) {
                vista_registro.estado.addItem(estadoDAO.getListaEstados().get(i).getDescripcion());
            }

            buscar();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        eventos();
        est = est1 = false;
    }

    private void eventos() {
        
        

        switch (op) {
            case 1: //registro
                vista_registro.buscar.setVisible(false);
                vista_registro.cambiarclave.setVisible(false);
                vista_registro.lblbuscar.setVisible(false);

                vista_registro.accion.addActionListener((ActionEvent e) -> {
                    if (vista_registro.perfil.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(vista_registro, "Selecciona el perfil", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (vista_registro.estado.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(vista_registro, "Selecciona un estado", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (vista_registro.nombres.getText().equals("")) {
                        JOptionPane.showMessageDialog(vista_registro, "Escribe el nombre", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            Usuarios usuario = new Usuarios();
                            usuario.setCodusuario(vista_registro.codusuario.getText());
                            usuario.setClave(vista_registro.clave.getText());
                            char codest = estadoDAO.getListaEstados().get(vista_registro.estado.getSelectedIndex() - 1).getCodestado().charAt(0);
                            usuario.setCodestado(codest);
                            usuario.setCodperfiles(vista_registro.perfil.getSelectedIndex());
                            usuario.setNombre(vista_registro.nombres.getText());
                            int opcion = JOptionPane.showConfirmDialog(null, "Esta seguro de registrar a este usuario", "Registrar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (opcion == JOptionPane.YES_OPTION) {
                                if (usuariosDAO.insertarUsuario(usuario) == 1) {
                                    JOptionPane.showMessageDialog(vista_registro, "Usuario registrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                    vista_registro.perfil.setSelectedIndex(0);
                                    vista_registro.estado.setSelectedIndex(0);
                                    est = est1 = false;
                                    vista_registro.nombres.setText("");
                                    vista_registro.clave.setText("");
                                    vista_registro.codusuario.setText("");
                                    vista_registro.accion.setEnabled(false);
                                    buscar();
                                } else {
                                    JOptionPane.showMessageDialog(vista_registro, "Ocurrió un error inesperado", "Errir", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(vista_registro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                });

                vista_registro.codusuario.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {

                        if (vista_registro.codusuario.getText().length() < 5) {
                            vista_registro.est.setForeground(Color.red);
                            vista_registro.codusuario.setForeground(Color.red);
                            vista_registro.est.setText("Muy corto");
                            est = false;
                        } else {
                            try {
                                if (usuariosDAO.isUsuario(vista_registro.codusuario.getText())) {
                                    vista_registro.codusuario.setForeground(Color.red);
                                    vista_registro.est.setForeground(Color.red);
                                    vista_registro.est.setText("No disponible");
                                    est = false;
                                } else {
                                    vista_registro.codusuario.setForeground(new Color(061, 100, 045));
                                    vista_registro.est.setForeground(new Color(061, 100, 045));
                                    vista_registro.est.setText("Disponible");
                                    est = true;
                                }
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(vista_registro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        vista_registro.accion.setEnabled(est && est1);
                    }

                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (vista_registro.codusuario.getText().length() == 10) {
                            e.consume();
                        }
                    }
                });

                vista_registro.clave.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {

                        if (vista_registro.clave.getText().length() < 5) {
                            vista_registro.est1.setForeground(Color.red);
                            vista_registro.est1.setText("contraseña muy corta");
                            est1 = false;
                        } else {
                            vista_registro.est1.setForeground(new Color(061, 100, 045));
                            vista_registro.est1.setText("correcto");
                            est1 = true;
                        }
                        vista_registro.accion.setEnabled(est && est1);
                    }
                });

                vista_registro.codusuario.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {

                        if (vista_registro.codusuario.getText().length() < 5) {
                            vista_registro.est.setForeground(Color.red);
                            vista_registro.codusuario.setForeground(Color.red);
                            vista_registro.est.setText("Muy corto");
                        } else {
                            try {
                                if (usuariosDAO.isUsuario(vista_registro.codusuario.getText())) {
                                    vista_registro.codusuario.setForeground(Color.red);
                                    vista_registro.est.setForeground(Color.red);
                                    vista_registro.est.setText("No disponible");
                                } else {
                                    vista_registro.codusuario.setForeground(new Color(061, 100, 045));
                                    vista_registro.est.setForeground(new Color(061, 100, 045));
                                    vista_registro.est.setText("Disponible");
                                }
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(vista_registro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (vista_registro.codusuario.getText().length() == 10) {
                            e.consume();
                        }
                    }
                });

                break;

            case 2:
                vista_registro.clave.setEditable(false);
                vista_registro.titulo.setText("ACTUALIZAR USUARIO");
                vista_registro.accion.setText("Actualizar");
                vista_registro.cambiarclave.setEnabled(false);
                vista_registro.tabla.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        Usuarios usuario = new Usuarios();
                        int rowSelected = vista_registro.tabla.getSelectedRow();
                        String coduser = dt.getValueAt(rowSelected, 0).toString();
                        usuario.setCodusuario(coduser);
                        olduser = coduser;
                        try {
                            usuario = usuariosDAO.obtenerUser(usuario);
                            if (usuario != null) {
                                inicializarLogin();
                                ctrl_clave = new controladorClave(usuario, vista_clave);
                                vista_registro.codusuario.setText(usuario.getCodusuario());
                                vista_registro.nombres.setText(usuario.getNombre());
                                vista_registro.perfil.setSelectedItem(usuario.getPerfiles().getDescripcion());
                                vista_registro.estado.setSelectedItem(usuario.getEstado().getDescripcion());
                                vista_registro.accion.setEnabled(true);
                                vista_registro.cambiarclave.setEnabled(true);
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(vista_registro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                vista_registro.buscar.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        try {
                            dt = usuariosDAO.obtenerUsuaarios(vista_registro.buscar.getText(), "", "");
                            vista_registro.tabla.setModel(dt);
                            dt.setCellEditable(false);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(vista_registro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                vista_registro.accion.addActionListener((ActionEvent e) -> {
                    if (vista_registro.perfil.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(vista_registro, "Selecciona el perfil", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (vista_registro.estado.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(vista_registro, "Selecciona un estado", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (vista_registro.nombres.getText().equals("")) {
                        JOptionPane.showMessageDialog(vista_registro, "Escribe el nombre", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            Usuarios usuario = new Usuarios();
                            usuario.setCodusuario(vista_registro.codusuario.getText());
                            usuario.setClave(vista_registro.clave.getText());
                            char codest = estadoDAO.getListaEstados().get(vista_registro.estado.getSelectedIndex() - 1).getCodestado().charAt(0);
                            usuario.setCodestado(codest);
                            usuario.setCodperfiles(vista_registro.perfil.getSelectedIndex());
                            usuario.setNombre(vista_registro.nombres.getText());
                            usuario.setCodusuario_old(olduser);
                            int opcion = JOptionPane.showConfirmDialog(null, "Esta seguro de actualizar a este usuario", "Registrar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (opcion == JOptionPane.YES_OPTION) {
                                if (usuariosDAO.actualizarUsuario(usuario) == 1) {
                                    JOptionPane.showMessageDialog(vista_registro, "Usuario actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                    vista_registro.perfil.setSelectedIndex(0);
                                    vista_registro.estado.setSelectedIndex(0);
                                    est = est1 = false;
                                    vista_registro.nombres.setText("");
                                    vista_registro.clave.setText("");
                                    vista_registro.codusuario.setText("");
                                    vista_registro.accion.setEnabled(false);
                                    buscar();
                                } else {
                                    JOptionPane.showMessageDialog(vista_registro, "Ocurrió un error inesperado", "Errir", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(vista_registro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                });

                vista_registro.codusuario.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {

                        if (vista_registro.codusuario.getText().length() < 5) {
                            vista_registro.est.setForeground(Color.red);
                            vista_registro.codusuario.setForeground(Color.red);
                            vista_registro.est.setText("Muy corto");
                        } else {
                            try {
                                if (usuariosDAO.isUsuario(vista_registro.codusuario.getText())) {
                                    vista_registro.codusuario.setForeground(Color.red);
                                    vista_registro.est.setForeground(Color.red);
                                    vista_registro.est.setText("No disponible");
                                } else {
                                    vista_registro.codusuario.setForeground(new Color(061, 100, 045));
                                    vista_registro.est.setForeground(new Color(061, 100, 045));
                                    vista_registro.est.setText("Disponible");
                                }
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(vista_registro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (vista_registro.codusuario.getText().length() == 10) {
                            e.consume();
                        }
                    }
                });

                break;

            case 3:
                vista_registro.titulo.setText("ELIMINAR USUARIO");
                vista_registro.accion.setText("Eliminar");
                vista_registro.clave.setEditable(false);
                vista_registro.nombres.setEditable(false);
                vista_registro.codusuario.setEditable(false);
                vista_registro.perfil.setEditable(false);
                vista_registro.estado.setEditable(false);
                vista_registro.cambiarclave.setVisible(false);
                vista_registro.tabla.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        Usuarios usuario = new Usuarios();
                        int rowSelected = vista_registro.tabla.getSelectedRow();
                        String coduser = dt.getValueAt(rowSelected, 0).toString();
                        usuario.setCodusuario(coduser);
                        olduser = coduser;
                        try {
                            usuario = usuariosDAO.obtenerUser(usuario);
                            if (usuario != null) {
                                vista_registro.codusuario.setText(usuario.getCodusuario());
                                vista_registro.nombres.setText(usuario.getNombre());
                                vista_registro.perfil.setSelectedItem(usuario.getPerfiles().getDescripcion());
                                vista_registro.estado.setSelectedItem(usuario.getEstado().getDescripcion());
                                vista_registro.accion.setEnabled(true);
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(vista_registro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                vista_registro.buscar.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        try {
                            dt = usuariosDAO.obtenerUsuaarios(vista_registro.buscar.getText(), "", "");
                            vista_registro.tabla.setModel(dt);
                            dt.setCellEditable(false);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(vista_registro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                vista_registro.accion.addActionListener((ActionEvent e) -> {
                    try {
                        Usuarios usuario = new Usuarios();
                        usuario.setCodusuario(vista_registro.codusuario.getText());
                        usuario.setClave(vista_registro.clave.getText());
                        char codest = estadoDAO.getListaEstados().get(vista_registro.estado.getSelectedIndex() - 1).getCodestado().charAt(0);
                        usuario.setCodestado(codest);
                        usuario.setCodperfiles(vista_registro.perfil.getSelectedIndex());
                        usuario.setNombre(vista_registro.nombres.getText());
                        int opcion = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar a este usuario", "Registrar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (opcion == JOptionPane.YES_OPTION) {
                            if (usuariosDAO.eliminarUsuario(usuario) == 1) {
                                JOptionPane.showMessageDialog(vista_registro, "Usuario eliminado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                vista_registro.perfil.setSelectedIndex(0);
                                vista_registro.estado.setSelectedIndex(0);
                                est = est1 = false;
                                vista_registro.nombres.setText("");
                                vista_registro.clave.setText("");
                                vista_registro.codusuario.setText("");
                                vista_registro.accion.setEnabled(false);
                                buscar();
                            } else {
                                JOptionPane.showMessageDialog(vista_registro, "Ocurrió un error inesperado", "Errir", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(vista_registro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                });

                vista_registro.codusuario.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {

                        if (vista_registro.codusuario.getText().length() < 5) {
                            vista_registro.est.setForeground(Color.red);
                            vista_registro.codusuario.setForeground(Color.red);
                            vista_registro.est.setText("Muy corto");
                        } else {
                            try {
                                if (usuariosDAO.isUsuario(vista_registro.codusuario.getText())) {
                                    vista_registro.codusuario.setForeground(Color.red);
                                    vista_registro.est.setForeground(Color.red);
                                    vista_registro.est.setText("No disponible");
                                } else {
                                    vista_registro.codusuario.setForeground(new Color(061, 100, 045));
                                    vista_registro.est.setForeground(new Color(061, 100, 045));
                                    vista_registro.est.setText("Disponible");
                                }
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(vista_registro, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (vista_registro.codusuario.getText().length() == 10) {
                            e.consume();
                        }
                    }
                });

                break;
        }
        vista_registro.cancelar.addActionListener((ActionEvent e) -> {
            vista_registro.dispose();
        });

        vista_registro.cambiarclave.addActionListener((ActionEvent e) -> {
            ctrl_clave.iniciar();
        });
        vista_registro.setVisible(true);

    }

    private void buscar() {
        try {
            if (vista_registro.perfil.getSelectedIndex() == 0) {
                if (vista_registro.estado.getSelectedIndex() == 0) {
                    dt = usuariosDAO.obtenerUsuaarios(vista_registro.buscar.getText(), "", "");
                } else {
                    dt = usuariosDAO.obtenerUsuaarios(vista_registro.buscar.getText(), "", vista_registro.estado.getSelectedItem().toString());
                }
            } else {
                if (vista_registro.estado.getSelectedIndex() == 0) {
                    dt = usuariosDAO.obtenerUsuaarios(vista_registro.buscar.getText(), vista_registro.perfil.getSelectedItem().toString(), "");
                } else {
                    dt = usuariosDAO.obtenerUsuaarios(vista_registro.buscar.getText(), vista_registro.perfil.getSelectedItem().toString(), vista_registro.estado.getSelectedItem().toString());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        vista_registro.tabla.setModel(dt);
        dt.setCellEditable(false);
    }

    private void inicializarLogin() {
        vista_clave = null;
        vista_clave = new cambiar_clave(frameModal, true);
        vista_clave.setLocationRelativeTo(null);
        vista_clave.setResizable(false);

    }

    private Icon getIcono(JButton btn) {
        ImageIcon image = new ImageIcon("recursos/imagenes/" + btn.getName() + ".png");
        Icon icono = new ImageIcon(image.getImage().getScaledInstance(btn.getWidth() - 1, btn.getHeight() - 2, Image.SCALE_REPLICATE));
        return icono;
    }

}
