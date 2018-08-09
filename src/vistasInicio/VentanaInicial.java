package vistasInicio;

public class VentanaInicial extends javax.swing.JFrame {

    public VentanaInicial() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        articulo = new javax.swing.JButton();
        Usuario = new javax.swing.JButton();
        estadoLogin = new javax.swing.JLabel();
        listaart = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        perfil1 = new vistasInicio.perfil();
        jPanel2 = new javax.swing.JPanel();
        responsable = new javax.swing.JButton();
        egreso = new javax.swing.JButton();
        ingreso = new javax.swing.JButton();
        kardex = new javax.swing.JButton();
        reportes = new javax.swing.JButton();
        escritorio = new javax.swing.JDesktopPane();
        statusBar = new javax.swing.JLabel();
        menBusPro = new javax.swing.JMenuBar();
        menArchivo = new javax.swing.JMenu();
        config = new javax.swing.JMenuItem();
        salir = new javax.swing.JMenuItem();
        menUsuario = new javax.swing.JMenu();
        menRegistrar = new javax.swing.JMenuItem();
        menActualizar = new javax.swing.JMenuItem();
        menElimi = new javax.swing.JMenuItem();
        menCamb = new javax.swing.JMenuItem();
        menArticulo = new javax.swing.JMenu();
        menuInsArt = new javax.swing.JMenuItem();
        menActArt = new javax.swing.JMenuItem();
        add_palabra = new javax.swing.JMenuItem();
        mFactura = new javax.swing.JMenu();
        mFventa = new javax.swing.JMenuItem();
        mFcompra = new javax.swing.JMenuItem();
        menResponsable = new javax.swing.JMenu();
        menAñadirRes = new javax.swing.JMenuItem();
        menActuaRes = new javax.swing.JMenuItem();
        menElimiRes = new javax.swing.JMenuItem();
        menbuscapro = new javax.swing.JMenuItem();
        menMovimiento = new javax.swing.JMenu();
        menIngreso = new javax.swing.JMenuItem();
        menEgreso = new javax.swing.JMenuItem();
        menKardex = new javax.swing.JMenuItem();
        menAyuda = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inventario Bodega");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        articulo.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        articulo.setText("Añadir Artículo");
        articulo.setToolTipText("Añadir Artículo");
        articulo.setEnabled(false);
        articulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        articulo.setIconTextGap(-3);
        articulo.setName("articulo"); // NOI18N
        articulo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        articulo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        Usuario.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        Usuario.setText("Añadir Usuario");
        Usuario.setToolTipText("Añadir Usuario");
        Usuario.setEnabled(false);
        Usuario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Usuario.setIconTextGap(-3);
        Usuario.setName("Usuario"); // NOI18N
        Usuario.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Usuario.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        estadoLogin.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        estadoLogin.setForeground(new java.awt.Color(51, 51, 255));
        estadoLogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        estadoLogin.setText("Iniciar Sesión");
        estadoLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        listaart.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        listaart.setText("Lista de Artículos");
        listaart.setToolTipText("Lista de Artículos");
        listaart.setEnabled(false);
        listaart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        listaart.setIconTextGap(-3);
        listaart.setName("listaart"); // NOI18N
        listaart.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        listaart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("CALZADOS MECHITA");
        jLabel2.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(listaart, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 681, Short.MAX_VALUE)
                        .addComponent(perfil1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(estadoLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(articulo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                        .addComponent(Usuario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(listaart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(perfil1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(estadoLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        responsable.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        responsable.setText("Responsables");
        responsable.setToolTipText("Responsables");
        responsable.setEnabled(false);
        responsable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        responsable.setIconTextGap(-3);
        responsable.setName("responsable"); // NOI18N
        responsable.setPreferredSize(new java.awt.Dimension(103, 23));
        responsable.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        responsable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        egreso.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        egreso.setText("Egreso de Stock");
        egreso.setToolTipText("Egreso de Stock");
        egreso.setEnabled(false);
        egreso.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        egreso.setIconTextGap(-3);
        egreso.setName("egreso"); // NOI18N
        egreso.setPreferredSize(new java.awt.Dimension(103, 23));
        egreso.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        egreso.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        ingreso.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        ingreso.setText("Ingresar Stock");
        ingreso.setToolTipText("Ingresar Stock");
        ingreso.setEnabled(false);
        ingreso.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ingreso.setIconTextGap(-3);
        ingreso.setName("ingreso"); // NOI18N
        ingreso.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        ingreso.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        kardex.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        kardex.setText("Kardex");
        kardex.setToolTipText("Kardex");
        kardex.setEnabled(false);
        kardex.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kardex.setIconTextGap(-3);
        kardex.setName("kardex"); // NOI18N
        kardex.setPreferredSize(new java.awt.Dimension(103, 23));
        kardex.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        kardex.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        reportes.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        reportes.setText("Reportes");
        reportes.setToolTipText("Reportes");
        reportes.setEnabled(false);
        reportes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        reportes.setIconTextGap(-3);
        reportes.setName("reporte"); // NOI18N
        reportes.setPreferredSize(new java.awt.Dimension(103, 23));
        reportes.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        reportes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(egreso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                    .addComponent(responsable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ingreso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(kardex, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                    .addComponent(reportes, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(responsable, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(egreso, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kardex, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportes, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        escritorio.setBackground(java.awt.SystemColor.inactiveCaption);
        escritorio.setAutoscrolls(true);

        javax.swing.GroupLayout escritorioLayout = new javax.swing.GroupLayout(escritorio);
        escritorio.setLayout(escritorioLayout);
        escritorioLayout.setHorizontalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        escritorioLayout.setVerticalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        statusBar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        statusBar.setText("Iniciado....");
        statusBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        menArchivo.setText("Archivo");

        config.setText("Configurar conexión");
        menArchivo.add(config);

        salir.setText("Salir");
        menArchivo.add(salir);

        menBusPro.add(menArchivo);

        menUsuario.setText("Usuarios");

        menRegistrar.setText("Registrar");
        menUsuario.add(menRegistrar);

        menActualizar.setText("Actualizar");
        menUsuario.add(menActualizar);

        menElimi.setText("Eliminar");
        menUsuario.add(menElimi);

        menCamb.setText("Cambiar clave");
        menUsuario.add(menCamb);

        menBusPro.add(menUsuario);

        menArticulo.setText("Artículos");

        menuInsArt.setText("Insertar Artículo");
        menArticulo.add(menuInsArt);

        menActArt.setText("Actualizar Artículo");
        menArticulo.add(menActArt);

        add_palabra.setText("Agregar Balance Inicial");
        menArticulo.add(add_palabra);

        menBusPro.add(menArticulo);

        mFactura.setText("Factura");

        mFventa.setText("Fectua de Ventas");
        mFactura.add(mFventa);

        mFcompra.setText("Factura de Compras");
        mFactura.add(mFcompra);

        menBusPro.add(mFactura);

        menResponsable.setText("Responsables");

        menAñadirRes.setText("Añadir");
        menResponsable.add(menAñadirRes);

        menActuaRes.setText("Actualizar");
        menResponsable.add(menActuaRes);

        menElimiRes.setText("Eliminar");
        menResponsable.add(menElimiRes);

        menbuscapro.setText("Buscar por proveedor");
        menResponsable.add(menbuscapro);

        menBusPro.add(menResponsable);

        menMovimiento.setText("Movimientos");

        menIngreso.setText("Ingreso");
        menMovimiento.add(menIngreso);

        menEgreso.setText("Egreso");
        menMovimiento.add(menEgreso);

        menKardex.setText("Kardex");
        menMovimiento.add(menKardex);

        menBusPro.add(menMovimiento);

        menAyuda.setText("Ayuda");

        jMenuItem4.setText("Acerca de");
        menAyuda.add(jMenuItem4);

        menBusPro.add(menAyuda);

        setJMenuBar(menBusPro);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(escritorio)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(escritorio)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusBar)
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton Usuario;
    public javax.swing.JMenuItem add_palabra;
    public javax.swing.JButton articulo;
    public javax.swing.JMenuItem config;
    public javax.swing.JButton egreso;
    public javax.swing.JDesktopPane escritorio;
    public javax.swing.JLabel estadoLogin;
    public javax.swing.JButton ingreso;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JButton kardex;
    public javax.swing.JButton listaart;
    public javax.swing.JMenu mFactura;
    public javax.swing.JMenuItem mFcompra;
    public javax.swing.JMenuItem mFventa;
    public javax.swing.JMenuItem menActArt;
    public javax.swing.JMenuItem menActuaRes;
    public javax.swing.JMenuItem menActualizar;
    public javax.swing.JMenu menArchivo;
    public javax.swing.JMenu menArticulo;
    public javax.swing.JMenu menAyuda;
    public javax.swing.JMenuItem menAñadirRes;
    private javax.swing.JMenuBar menBusPro;
    public javax.swing.JMenuItem menCamb;
    public javax.swing.JMenuItem menEgreso;
    public javax.swing.JMenuItem menElimi;
    public javax.swing.JMenuItem menElimiRes;
    public javax.swing.JMenuItem menIngreso;
    public javax.swing.JMenuItem menKardex;
    public javax.swing.JMenu menMovimiento;
    public javax.swing.JMenuItem menRegistrar;
    public javax.swing.JMenu menResponsable;
    public javax.swing.JMenu menUsuario;
    public javax.swing.JMenuItem menbuscapro;
    public javax.swing.JMenuItem menuInsArt;
    public vistasInicio.perfil perfil1;
    public javax.swing.JButton reportes;
    public javax.swing.JButton responsable;
    public javax.swing.JMenuItem salir;
    public javax.swing.JLabel statusBar;
    // End of variables declaration//GEN-END:variables
}
