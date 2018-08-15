/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import javax.swing.JFrame;
import javax.swing.UIManager;
import vistasInicio.VentanaInicial;

/**
 *
 * @author José
 */
public class programa {

    public static void main(String[] args) {       
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            VentanaInicial ventanaP = new VentanaInicial();
            mainControler inicio = new mainControler(ventanaP);
            ventanaP.setLocationRelativeTo(null);
            ventanaP.setExtendedState(JFrame.MAXIMIZED_BOTH);
            ventanaP.pack();
//            inicio.animar();
            inicio.IniciarVentana();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            System.err.println(ex.getMessage());
        }

    }

}
