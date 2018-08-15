/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controladorArticulo.ControladorBalance;
import vistasInicio.cambio;
import modelo.confi;
import controladorUsuarios.controladorLogin;
import controladorArticulo.controladorArticulo;
import controladorKardex.controlador_kardex;
import controladorMovimientos.CtrlEgresos;
import controladorMovimientos.CtrlIngreso;
import controladorResponsable.ctrl_busca_pro;
import controladorResponsable.ctrl_responsable;
import controladorResponsable.ctrl_responsableAct;
import controladorResponsable.ctrl_responsableElimi;
import controladorUsuarios.controladorClave;
import controladorUsuarios.ctrl_Usuario;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import modelo.Usuarios;
import modelo.UsuariosDAO;
import vistaArticulo.Articulo;
import vistaArticulo.Vista_articulo;
import vistaMovimiento.Vista_Movimientos;
import vistaResponsables.buscaPorProveedor;
import vistaResponsables.vista_responsable;
import vistaUsuarios.Vista_Usuario;
import vistaUsuarios.cambiar_clave;
import vistasInicio.VentanaInicial;
import vistaUsuarios.login;
import vistas_kardex.kardex;
import controladorArticulo.controladorListaArticulo;
import controladorConexion.ctrlConexion;
import controladorFactura.ControloladorFactura;
import controladorMovimientos.Reportes;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import modelo.ArticuloDAO;
import modelo.Articulos;
import modelo.Ciudades;
import modelo.CiudadesDAO;
import modelo.Empresa;
import modelo.EmpresaDAO;
import modelo.Provincia;
import modelo.ProvinciaDAO;
import vistaArticulo.Balance;
import vistaFacturas.Ventas;
import vistaReportes.vista_reporte;

/**
 *
 * @author
 */
public class mainControler {

    public static List<Ciudades> listaCiudades;
    public static List<Provincia> listaProvincias;
    private final List<Articulos> listaArticulos;

    private Empresa empresa;

    private final VentanaInicial vista_ventanaPrincipal;
    private controladorLogin ctrl_login;
    private login vista_login;

    public Usuarios usuario;

    private Vista_Movimientos vista_movimiento;
    private CtrlIngreso ctrl_ingreso;
    private CtrlEgresos ctrl_Egresos;

    private Balance balance;
    private ControladorBalance cb;

    private Vista_Usuario vista_usuario;
    private ctrl_Usuario ctrl_registro;

    private Ventas vistaVentas;
    private ControloladorFactura controladorFactura;
    
    private ctrl_responsable ctrl_responsable;
    private ctrl_responsableAct ctrl_responsable2;
    private ctrl_responsableElimi ctrl_responsable3;
    private vista_responsable vista_responsable;
    private kardex vista_kardex;
    private controlador_kardex ctrl_kardex;

    private buscaPorProveedor vista_busca_pro;
    private ctrl_busca_pro ctrl_busca_pro;

    private Vista_articulo vista_articulo;
    private controladorArticulo ctrl_aticulo;

    private vista_reporte vista_reporte;
    private Reportes ctrl_reporte;

    private Articulo vista_art;
    private controladorListaArticulo ctrl_list_art;

    private ctrlConexion ctConexion;
    private cambio vista_confi_cambio;

    private final confi conf;

    private boolean confserver;

    final SplashScreen splash;
    //texto que se muestra a medida que se va cargando el screensplah
    final String[] texto = {"provincias", "ciudades", "articulos", ""};

    public mainControler(VentanaInicial ventanaP) {
        splash = SplashScreen.getSplashScreen();
        this.listaArticulos = new ArrayList<>();
        mainControler.listaProvincias = new ArrayList<>();
        mainControler.listaCiudades = new ArrayList<>();
        this.vista_ventanaPrincipal = ventanaP;
        conf = new confi();
        confserver = false;
        InicializarComponentes();
    }

    public void animar() {
        if (splash != null) {
            Graphics2D g = splash.createGraphics();
            try {
                int i = 1;
                LoadSplash(i, g);
                i++;
                ProvinciaDAO pdao = new ProvinciaDAO(listaProvincias);
                pdao.ObtenerListaProvincia();

                LoadSplash(i, g);
                i++;
                CiudadesDAO cdao = new CiudadesDAO(listaCiudades);
                cdao.ObtenerListaCiudades();

                LoadSplash(i, g);
                i++;
                ArticuloDAO aO = new ArticuloDAO(listaArticulos);
                aO.obtenerArticulos();

                empresa = new EmpresaDAO().obtenerEmpresa();

            } catch (SQLException ex) {
                Logger.getLogger(mainControler.class.getName()).log(Level.SEVERE, null, ex);
            }
            splash.close();
        }
    }

    private void LoadSplash(int i, Graphics2D g) {
        g.setColor(new Color(4, 52, 101));//color de fondo
        g.fillRect(203, 328, 280, 12);//para tapar texto anterior
        g.setColor(Color.white);//color de texto	        
        g.drawString("Cargando " + texto[i - 1] + "...", 203, 338);
        g.setColor(Color.green);//color de barra de progeso
        g.fillRect(204, 308, (i * 307 / texto.length), 12);//la barra de progreso
        //se pinta una linea segmentada encima de la barra verde
        float dash1[] = {2.0f};
        BasicStroke dashed = new BasicStroke(9.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash1, 0.0f);
        g.setStroke(dashed);
        g.setColor(Color.BLUE);//color de barra de progeso
        g.setColor(new Color(4, 52, 101));
        g.drawLine(205, 314, 510, 314);
        //se actualiza todo
        splash.update();
    }

    private void InicializarComponentes() {
        Font font = vista_ventanaPrincipal.estadoLogin.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        vista_ventanaPrincipal.estadoLogin.setFont(font.deriveFont(attributes));
        vista_ventanaPrincipal.Usuario.setIcon(getIcono(vista_ventanaPrincipal.Usuario));
        vista_ventanaPrincipal.articulo.setIcon(getIcono(vista_ventanaPrincipal.articulo));
        vista_ventanaPrincipal.responsable.setIcon(getIcono(vista_ventanaPrincipal.responsable));
        vista_ventanaPrincipal.ingreso.setIcon(getIcono(vista_ventanaPrincipal.ingreso));
        vista_ventanaPrincipal.egreso.setIcon(getIcono(vista_ventanaPrincipal.egreso));
        vista_ventanaPrincipal.listaart.setIcon(getIcono(vista_ventanaPrincipal.listaart));
        vista_ventanaPrincipal.kardex.setIcon(getIcono(vista_ventanaPrincipal.kardex));
        vista_ventanaPrincipal.reportes.setIcon(getIcono(vista_ventanaPrincipal.reportes));
        vista_ventanaPrincipal.perfil1.setVisible(false);
        inicializarLogin();
        usuario = null;
        mainEventos();
        bloquear();
    }

    private void cursorCargando(JDesktopPane jif) {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorNomal(JDesktopPane jif) {
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorCargando(JFrame jif) {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorNomal(JFrame jif) {
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorCargando(JMenuItem jif) {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorNomal(JMenuItem jif) {
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorCargando(JLabel jif) {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorNomal(JLabel jif) {
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorCargando(JButton jif) {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        jif.setCursor(cursor);
    }

    private void cursorNomal(JButton jif) {
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jif.setCursor(cursor);
    }

    private void mainEventos() {
        vista_ventanaPrincipal.estadoLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                cursorCargando(vista_ventanaPrincipal.escritorio);
                cursorCargando(vista_ventanaPrincipal.estadoLogin);
                cursorCargando(vista_ventanaPrincipal);

                if (conf.accderPorpiedades("usuario") == null) {
                    JOptionPane.showMessageDialog(vista_ventanaPrincipal, "Configuracion de conexión no establecida", "Error", JOptionPane.ERROR_MESSAGE);
                    inicializarCambio();
                    ctConexion.iniciarVistaConfig();
                } else {
                    if (usuario == null) {
                        if (vista_ventanaPrincipal.estadoLogin.getText().equals("Cancelar")) {
                            ctrl_login.cerrarLogin();
                        } else {
                            inicializarLogin();
                            ctrl_login.IniciarLgin();
                        }
                    } else {
                        int op = JOptionPane.showConfirmDialog(vista_ventanaPrincipal, "¿Estás seguro que deseas salir?", "Cerrar sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (op == JOptionPane.YES_OPTION) {
                            vista_ventanaPrincipal.estadoLogin.setText("Iniciar Sesión");
                            usuario = null;
                            vista_ventanaPrincipal.perfil1.nombre.setText("");
                            vista_ventanaPrincipal.perfil1.perfil.setText("");
                            vista_ventanaPrincipal.perfil1.setVisible(false);
                            bloquear();
                            cerrarVistas();
                        }
                    }
                }
                cursorNomal(vista_ventanaPrincipal.escritorio);
                cursorNomal(vista_ventanaPrincipal.estadoLogin);
                cursorNomal(vista_ventanaPrincipal);
            }
        });

        vista_ventanaPrincipal.Usuario.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.Usuario);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarRegistro();
            ctrl_registro.iniciarRegistro();
            cursorNomal(vista_ventanaPrincipal.Usuario);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.mFventa.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.mFventa);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarBuscaPro('C');
            ctrl_busca_pro.iniciarVistabusca();

            cursorNomal(vista_ventanaPrincipal.mFventa);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.mFcompra.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.mFcompra);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarBuscaPro('P');
            ctrl_busca_pro.iniciarVistabusca();
            cursorNomal(vista_ventanaPrincipal.mFcompra);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.menRegistrar.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.menRegistrar);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarRegistro();
            ctrl_registro.iniciarRegistro();
            cursorNomal(vista_ventanaPrincipal.menRegistrar);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.responsable.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.responsable);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarResponsable();
            ctrl_responsable.IniciarVistaResponsable();
            cursorNomal(vista_ventanaPrincipal.responsable);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.menActuaRes.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.menActuaRes);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarResponsable2();
            ctrl_responsable2.IniciarVistaResponsable();
            cursorNomal(vista_ventanaPrincipal.menActuaRes);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.ingreso.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.ingreso);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarIngresos();
            ctrl_ingreso.IniciarIngreso();
            cursorNomal(vista_ventanaPrincipal.ingreso);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.egreso.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.egreso);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarEgresos();
            ctrl_Egresos.IniciarEgreso();
            cursorNomal(vista_ventanaPrincipal.egreso);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.menActualizar.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.menActualizar);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarRegistro();
            ctrl_registro.op = 2;
            ctrl_registro.iniciarRegistro();
            cursorNomal(vista_ventanaPrincipal.menActualizar);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.menElimi.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.menElimi);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarRegistro();
            ctrl_registro.op = 3;
            ctrl_registro.iniciarRegistro();
            cursorNomal(vista_ventanaPrincipal.menElimi);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.config.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.config);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            if (conf.accderPorpiedades("usuario") == null) {
                inicializarCambio();
                ctConexion.iniciarVistaConfig();
            } else {
                inicializarLogin();
                
                confserver = true;
                ctrl_login.IniciarLgin();
                confserver = ctrl_login.error;
            }
            cursorNomal(vista_ventanaPrincipal.config);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.menActArt.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.menActArt);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarArticulo();
            ctrl_aticulo.op = 2;
            ctrl_aticulo.IniciarVistaArticulo();
            cursorNomal(vista_ventanaPrincipal.menActArt);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.menElimiRes.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.menElimiRes);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarResponsable3();
            ctrl_responsable3.IniciarVistaResponsable();
            cursorNomal(vista_ventanaPrincipal.menElimiRes);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.menIngreso.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.menIngreso);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarIngresos();
            ctrl_ingreso.IniciarIngreso();
            cursorNomal(vista_ventanaPrincipal.menIngreso);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.menEgreso.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.menEgreso);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarEgresos();
            ctrl_Egresos.IniciarEgreso();
            cursorNomal(vista_ventanaPrincipal.menEgreso);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.menKardex.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.menKardex);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializaKardex();
            ctrl_kardex.iniciarKardex();
            cursorNomal(vista_ventanaPrincipal.menKardex);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.articulo.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.articulo);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarArticulo();
            ctrl_aticulo.IniciarVistaArticulo();
            cursorNomal(vista_ventanaPrincipal.articulo);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);

        });

        vista_ventanaPrincipal.kardex.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.kardex);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializaKardex();
            ctrl_kardex.iniciarKardex();
            cursorNomal(vista_ventanaPrincipal.kardex);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);

        });
        
        vista_ventanaPrincipal.mReventa.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.mReventa);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
           inicializaVentas();
            controladorFactura.inicarVentas();
            cursorNomal(vista_ventanaPrincipal.mReventa);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);

        });
        vista_ventanaPrincipal.reportes.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.reportes);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializareporte();
            ctrl_reporte.inicializarReportes();
            cursorNomal(vista_ventanaPrincipal.reportes);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.menuInsArt.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.menuInsArt);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarArticulo();
            ctrl_aticulo.IniciarVistaArticulo();
            cursorNomal(vista_ventanaPrincipal.menuInsArt);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.listaart.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.listaart);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarListArticulo();
            ctrl_list_art.IniciarVistaProducto();
            cursorNomal(vista_ventanaPrincipal.listaart);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.add_palabra.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.add_palabra);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cerrarVistas();
            inicializarBalance();
            cb.InicializarBalance();
            cursorNomal(vista_ventanaPrincipal.add_palabra);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

        vista_ventanaPrincipal.menCamb.addActionListener((ActionEvent e) -> {
            cursorCargando(vista_ventanaPrincipal.menCamb);
            cursorCargando(vista_ventanaPrincipal.escritorio);
            cursorCargando(vista_ventanaPrincipal);
            cambiar_clave cc = new cambiar_clave(vista_ventanaPrincipal, true);
            try {
                usuario.setClave(new UsuariosDAO().obtenrClave(usuario));
                controladorClave ccc = new controladorClave(usuario, cc);
                cc.borrarC.setVisible(false);
                ccc.iniciar();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vista_login, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            cursorNomal(vista_ventanaPrincipal.menCamb);
            cursorNomal(vista_ventanaPrincipal.escritorio);
            cursorNomal(vista_ventanaPrincipal);
        });

    }

    private void cerrarVistas() {
        if (vista_movimiento != null) {
            if (vista_movimiento.isVisible()) {
                vista_movimiento.dispose();
            }
        }

        if (vista_usuario != null) {
            if (vista_usuario.isVisible()) {
                vista_usuario.dispose();
            }
        }
        if (vista_busca_pro != null) {
            if (vista_busca_pro.isVisible()) {
                vista_busca_pro.dispose();
            }
        }

        if (vista_articulo != null) {
            if (vista_articulo.isVisible()) {
                vista_articulo.dispose();
            }
        }

        if (vista_kardex != null) {
            if (vista_kardex.isVisible()) {
                vista_kardex.dispose();
            }
        }

        if (vista_login != null) {
            if (vista_login.isVisible()) {
                vista_login.dispose();
            }
        }

        if (vista_responsable != null) {
            if (vista_responsable.isVisible()) {
                vista_responsable.dispose();
            }
        }

        if (vista_usuario != null) {
            if (vista_usuario.isVisible()) {
                vista_usuario.dispose();
            }
        }
        if (vista_reporte != null) {
            if (vista_reporte.isVisible()) {
                vista_reporte.dispose();
            }
        }
        if (balance != null) {
            if (balance.isVisible()) {
                balance.dispose();
            }
        }
    }

    private void inicializarLogin() {
        vista_login = new login();
        ctrl_login = new controladorLogin(vista_login);
        ctrl_login.status = vista_ventanaPrincipal.statusBar;
        vista_ventanaPrincipal.escritorio.add(vista_login);
        vista_login.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_login.getWidth() / 2, 30);
        eventosLogin();

    }

    private void eventosLogin() {
        vista_login.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                if (!confserver) {
                    vista_ventanaPrincipal.estadoLogin.setText("Cancelar");
                }
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                usuario = ctrl_login.getUser();
                if (usuario != null) {
                    if (confserver) {
                        if (usuario.getCodperfiles() == 1) {
                            inicializarCambio();
                            ctConexion.iniciarVistaConfig();
                            confserver = false;
                            usuario = null;
                        } else {
                            JOptionPane.showMessageDialog(vista_ventanaPrincipal, "No tienes privilegio suficientes para realizar esta operación"
                                    + "\nContacta a un administrador", "", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        vista_ventanaPrincipal.ingreso.setEnabled(true);
                        vista_ventanaPrincipal.egreso.setEnabled(true);
                        vista_ventanaPrincipal.listaart.setEnabled(true);
                        vista_ventanaPrincipal.estadoLogin.setText("Cerrar Sesión");

                        vista_ventanaPrincipal.menMovimiento.setVisible(true);
                        vista_ventanaPrincipal.menAyuda.setVisible(true);
                        vista_ventanaPrincipal.kardex.setEnabled(true);
                        vista_ventanaPrincipal.reportes.setEnabled(true);
                        vista_ventanaPrincipal.menUsuario.setVisible(true);
                        vista_ventanaPrincipal.add_palabra.setVisible(true);
                        vista_ventanaPrincipal.mFactura.setVisible(true);
                        vista_ventanaPrincipal.mReventa.setVisible(true);
                        switch (usuario.getCodperfiles()) {
                            case 1:
                                vista_ventanaPrincipal.Usuario.setEnabled(true);
                                vista_ventanaPrincipal.articulo.setEnabled(true);
                                vista_ventanaPrincipal.responsable.setEnabled(true);
                                vista_ventanaPrincipal.menArticulo.setVisible(true);
                                vista_ventanaPrincipal.menActualizar.setVisible(true);
                                vista_ventanaPrincipal.menRegistrar.setVisible(true);
                                vista_ventanaPrincipal.menElimi.setVisible(true);
                                vista_ventanaPrincipal.menResponsable.setVisible(true);
                                break;

                            case 2:
                                break;
                        }
                        vista_ventanaPrincipal.perfil1.nombre.setText(usuario.getNombre());
                        vista_ventanaPrincipal.perfil1.perfil.setText(usuario.getPerfiles().getDescripcion());
                        vista_ventanaPrincipal.perfil1.foto.setName(usuario.getCodperfiles() + "");
                        vista_ventanaPrincipal.perfil1.foto.setIcon(getIcono(vista_ventanaPrincipal.perfil1.foto));
                        vista_ventanaPrincipal.perfil1.setVisible(true);
                    }
                } else {
                    vista_ventanaPrincipal.estadoLogin.setText("Iniciar Sesión");
                    vista_ventanaPrincipal.escritorio.remove(vista_login);
                    vista_login = null;
                }

            }
        });

    }

    private void inicializaKardex() {
        vista_kardex = null;
        vista_kardex = new kardex();
        ctrl_kardex = new controlador_kardex(vista_kardex);
        vista_ventanaPrincipal.escritorio.add(vista_kardex);
        vista_kardex.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_kardex.getWidth() / 2, 15);
    }
    private void inicializaVentas() {
        vistaVentas = null;
        vistaVentas = new Ventas();
        controladorFactura = new ControloladorFactura(vistaVentas);
        vista_ventanaPrincipal.escritorio.add(vistaVentas);
        vistaVentas.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vistaVentas.getWidth() / 2, 15);
    }

    private void inicializareporte() {
        vista_reporte = null;
        vista_reporte = new vista_reporte();
        ctrl_reporte = new Reportes(vista_reporte);
        vista_ventanaPrincipal.escritorio.add(vista_reporte);
        vista_reporte.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_reporte.getWidth() / 2, 15);
    }

    private void inicializarRegistro() {
        vista_usuario = null;
        vista_usuario = new Vista_Usuario();
        ctrl_registro = new ctrl_Usuario(vista_usuario);
        vista_ventanaPrincipal.escritorio.add(vista_usuario);
        vista_usuario.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_usuario.getWidth() / 2, 15);
        eventosVistaUsuario();
    }

    private void inicializarCambio() {
        vista_confi_cambio = null;
        vista_confi_cambio = new cambio(vista_ventanaPrincipal, true);
        ctConexion = new ctrlConexion(vista_confi_cambio);
        vista_confi_cambio.setLocationRelativeTo(vista_ventanaPrincipal);
    }

    private void inicializarBuscaPro(char tipo) {
        vista_busca_pro = null;
        vista_busca_pro = new buscaPorProveedor();
        ctrl_busca_pro = new ctrl_busca_pro(vista_busca_pro);
        ctrl_busca_pro.setEmpresa(empresa);
        ctrl_busca_pro.setTipo(tipo);
        vista_ventanaPrincipal.escritorio.add(vista_busca_pro);
        vista_busca_pro.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_busca_pro.getWidth() / 2, 15);
        eventosVistaBuscaPro();
    }

    private void inicializarIngresos() {
        vista_movimiento = null;
        vista_movimiento = new Vista_Movimientos();
        ctrl_ingreso = new CtrlIngreso(vista_movimiento);
        ctrl_ingreso.setEmpresa(empresa);
        ctrl_ingreso.setListaArticulos(listaArticulos);
        ctrl_ingreso.setUsuario(usuario);
        vista_ventanaPrincipal.escritorio.add(vista_movimiento);
        vista_movimiento.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_movimiento.getWidth() / 2, 5);
        eventosMovimientos();
    }

    private void inicializarArticulo() {
        vista_articulo = null;
        vista_articulo = new Vista_articulo();
        ctrl_aticulo = new controladorArticulo(vista_articulo);
        vista_ventanaPrincipal.escritorio.add(vista_articulo);
        vista_articulo.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_articulo.getWidth() / 2, 5);
        eventosAritculo();
    }

    private void inicializarListArticulo() {
        vista_art = null;
        vista_art = new Articulo();
        ctrl_list_art = new controladorListaArticulo(vista_art);
        vista_ventanaPrincipal.escritorio.add(vista_art);
        vista_art.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_art.getWidth() / 2, 5);
        eventosListaAritculo();
    }

    private void inicializarResponsable() {
        vista_responsable = null;
        vista_responsable = new vista_responsable();
        ctrl_responsable = new ctrl_responsable(vista_responsable);
        ctrl_responsable.setListaCiudades(listaCiudades);
        ctrl_responsable.setListaProvincias(listaProvincias);
        vista_ventanaPrincipal.escritorio.add(vista_responsable);
        vista_responsable.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_responsable.getWidth() / 2, 15);
        responsableEventos();
    }

    private void inicializarResponsable2() {
        vista_responsable = null;
        vista_responsable = new vista_responsable();
        ctrl_responsable2 = new ctrl_responsableAct(vista_responsable);
        vista_ventanaPrincipal.escritorio.add(vista_responsable);
        vista_responsable.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_responsable.getWidth() / 2, 15);
        responsableEventos();
    }

    private void inicializarResponsable3() {
        vista_responsable = null;
        vista_responsable = new vista_responsable();
        ctrl_responsable3 = new ctrl_responsableElimi(vista_responsable);
        vista_ventanaPrincipal.escritorio.add(vista_responsable);
        vista_responsable.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_responsable.getWidth() / 2, 15);
        responsableEventos();
    }

    private void inicializarBalance() {
        balance = null;
        balance = new Balance();
        cb = new ControladorBalance(balance);
        vista_ventanaPrincipal.escritorio.add(balance);
        balance.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - balance.getWidth() / 2, 15);
        cb.setU(usuario);
        balanceEventos();

    }

    private void responsableEventos() {
        vista_responsable.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                vista_ventanaPrincipal.escritorio.remove(vista_responsable);
            }
        });
    }

    private void balanceEventos() {
        balance.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                vista_ventanaPrincipal.escritorio.remove(balance);
            }
        });
    }

    private void inicializarEgresos() {
        vista_movimiento = null;
        vista_movimiento = new Vista_Movimientos();
        ctrl_Egresos = new CtrlEgresos(vista_movimiento);
        ctrl_Egresos.setUsuario(usuario);
        ctrl_Egresos.setEmpresa(empresa);
        ctrl_Egresos.setListaArticulos(listaArticulos);;
        vista_ventanaPrincipal.escritorio.add(vista_movimiento);
        vista_movimiento.setLocation(vista_ventanaPrincipal.escritorio.getWidth() / 2 - vista_movimiento.getWidth() / 2, 5);
        eventosMovimientos();
    }

    private void eventosMovimientos() {
        vista_movimiento.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                vista_ventanaPrincipal.escritorio.remove(vista_movimiento);
            }
        });
    }

    private void eventosAritculo() {
        vista_articulo.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                vista_ventanaPrincipal.escritorio.remove(vista_articulo);
            }
        });
    }

    private void eventosListaAritculo() {
        vista_art.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                vista_ventanaPrincipal.escritorio.remove(vista_art);
            }
        });
    }

    private void eventosVistaUsuario() {
        vista_usuario.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                vista_ventanaPrincipal.escritorio.remove(vista_usuario);
            }
        });
    }

    private void eventosVistaBuscaPro() {
        vista_busca_pro.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                vista_ventanaPrincipal.escritorio.remove(vista_busca_pro);
            }
        });
    }

    private void bloquear() {
        vista_ventanaPrincipal.ingreso.setEnabled(false);
        vista_ventanaPrincipal.egreso.setEnabled(false);
        vista_ventanaPrincipal.listaart.setEnabled(false);
        vista_ventanaPrincipal.Usuario.setEnabled(false);
        vista_ventanaPrincipal.articulo.setEnabled(false);
        vista_ventanaPrincipal.responsable.setEnabled(false);
        vista_ventanaPrincipal.kardex.setEnabled(false);
        vista_ventanaPrincipal.reportes.setEnabled(false);
        vista_ventanaPrincipal.menArticulo.setVisible(false);
        vista_ventanaPrincipal.menMovimiento.setVisible(false);
        vista_ventanaPrincipal.menUsuario.setVisible(false);
        vista_ventanaPrincipal.menResponsable.setVisible(false);
        vista_ventanaPrincipal.menAyuda.setVisible(false);
        vista_ventanaPrincipal.menActualizar.setVisible(false);
        vista_ventanaPrincipal.menRegistrar.setVisible(false);
        vista_ventanaPrincipal.menElimi.setVisible(false);
        vista_ventanaPrincipal.add_palabra.setVisible(false);
        vista_ventanaPrincipal.mFactura.setVisible(false);
        vista_ventanaPrincipal.mReventa.setVisible(false);
    }

    public void IniciarVentana() {
        java.awt.EventQueue.invokeLater(() -> {
            vista_ventanaPrincipal.setVisible(true);
        });
    }

    private Icon getIcono(JButton btn) {
        btn.setSize(60, 55);
        ImageIcon image = new ImageIcon("recursos/imagenes/" + btn.getName() + ".png");
        Icon icono = new ImageIcon(image.getImage().getScaledInstance(btn.getWidth() - 1, btn.getHeight() - 2, Image.SCALE_REPLICATE));
        return icono;
    }

    private Icon getIcono(JLabel jlb) {
        jlb.setText("");
        ImageIcon image = new ImageIcon("recursos/imagenes/" + jlb.getName() + ".png");
        Icon icono = new ImageIcon(image.getImage().getScaledInstance(jlb.getWidth(), jlb.getHeight(), Image.SCALE_REPLICATE));
        return icono;
    }
}
