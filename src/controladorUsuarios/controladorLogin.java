package controladorUsuarios;

import controladorConexion.ctrlConexion;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import modelo.PerfilesDAO;
import modelo.Usuarios;
import modelo.UsuariosDAO;
import vistaUsuarios.login;
import vistasInicio.cambio;

public class controladorLogin   {

  
    private final login vista_login;
    public JLabel status;
    public Usuarios user;

    private final UsuariosDAO usuarioDAO;
    private final PerfilesDAO perlilesDAO;

    public boolean error;
    private ctrlConexion ctConexion;
    private cambio vista_confi_cambio;

    public controladorLogin(login vista_login) {
        this.vista_login = vista_login;
        usuarioDAO = new UsuariosDAO();
        perlilesDAO = new PerfilesDAO();
        this.vista_login.usuario.setText("ADMIN");
        this.vista_login.clave.setText("ADMIN");

    }

    private void cursorCargando(JInternalFrame jif) {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorNomal(JInternalFrame jif) {
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jif.setCursor(cursor);
    }

    public void IniciarLgin() {
        try {
            EventoLogin();
            status.setText("Iniciar Sesión");
            perlilesDAO.getObtnerPerfiles();
            for (int i = 0; i < perlilesDAO.getListaPerfiles().size(); i++) {
                vista_login.perfil.addItem(perlilesDAO.getListaPerfiles().get(i).getDescripcion());
            }
            error = true;
            vista_login.setVisible(true);
                                    vista_login.foto.setIcon(getIcono(vista_login.foto));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista_login, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            inicializarCambio();
            error = false;
            ctConexion.iniciarVistaConfig();
        }

    }

    public void cerrarLogin() {
        cursorCargando(vista_login);
        vista_login.dispose();
        cursorNomal(vista_login);
    }

    private void EventoLogin() {
        vista_login.aceder.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_login);
            acceder();
            cursorNomal(vista_login);
        });

        vista_login.cancelar.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_login);
            cerrarLogin();
            cursorNomal(vista_login);
        });

        vista_login.usuario.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                cursorCargando(vista_login);
                vista_login.usuario.selectAll();
                cursorNomal(vista_login);
            }
        });

        vista_login.clave.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                cursorCargando(vista_login);
                vista_login.clave.selectAll();
                cursorNomal(vista_login);
            }
        });

        vista_login.usuario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                cursorCargando(vista_login);
                soltarTecla(evt);
                cursorNomal(vista_login);
            }
        });
        vista_login.clave.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                cursorCargando(vista_login);
                soltarTecla(evt);
                cursorNomal(vista_login);
            }
        });
        vista_login.aceder.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                cursorCargando(vista_login);
                soltarTecla(evt);
                cursorNomal(vista_login);
            }
        });
        vista_login.perfil.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                cursorCargando(vista_login);
                soltarTecla(evt);
                cursorNomal(vista_login);
            }
        });

        vista_login.cancelar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                cursorCargando(vista_login);
                vista_login.dispose();
                cursorNomal(vista_login);
            }
        });
    }

    private void inicializarCambio() {
        vista_confi_cambio = null;
        vista_confi_cambio = new cambio(null, true);
        ctConexion = new ctrlConexion(vista_confi_cambio);
        vista_confi_cambio.setLocationRelativeTo(null);
    }

    private void acceder() {
        user = new Usuarios();
        user.setCodusuario(vista_login.usuario.getText());
        user.setClave(vista_login.clave.getText());
        try {
            user = usuarioDAO.getUserLogin(user);
            if (user != null) {
                if (user.getCodusuario() == null) {
                    JOptionPane.showMessageDialog(vista_login, "Usuario no existe", "Error", JOptionPane.ERROR_MESSAGE);
                    vista_login.usuario.requestFocus();
                    user = null;
                } else {
                    int idperdil = vista_login.perfil.getSelectedIndex() + 1;
                    if (user.getCodperfiles() == idperdil) {
                        if (user.getCodestado() == 'A') {
                        
                            cerrarLogin();
                        } else {
                            JOptionPane.showMessageDialog(vista_login, "Usuario inactivo, comunicate con el administrador", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(vista_login, "Perfil no asignado para este usuario", "Error", JOptionPane.ERROR_MESSAGE);
                        user = null;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(vista_login, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
                vista_login.clave.requestFocus();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Usuarios getUser() {
        return user;
    }

    private void soltarTecla(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            acceder();
        }
    }

    private Icon getIcono(JLabel jlb) {
        jlb.setText("");
        ImageIcon image = new ImageIcon("recursos/imagenes/" + jlb.getName() + ".png");
        Icon icono = new ImageIcon(image.getImage().getScaledInstance(jlb.getWidth(), jlb.getHeight(), Image.SCALE_REPLICATE));
        return icono;
    }

   

}
