/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.jasypt.properties.PropertyValueEncryptionUtils;

/**
 *
 * @author José
 */
public class confi {

    private final Properties propiedades = new Properties();
    private OutputStream salida = null;
    private InputStream entrada = null;
    private String ruta;

    public confi() {
        ruta = "recursos/configuracion/data.config";
    }

    public void insertar(String server, String puerto, String baseDatos, String usuario, String clave) {
        try {
            salida = new FileOutputStream("recursos/configuracion/data.config");
            propiedades.setProperty("basedatos", baseDatos);
            propiedades.setProperty("usuario", usuario);
            propiedades.setProperty("clave", clave);
            propiedades.setProperty("host", server);
            propiedades.setProperty("puerto", puerto);
            try {
                propiedades.store(salida, null);
            } catch (IOException ex) {
                System.out.println(ruta);
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String accderPorpiedades(String propiedad) {
        String valor = null;
        try {
            entrada = new FileInputStream("recursos/configuracion/data.config");
            propiedades.load(entrada);
            valor = (propiedades.getProperty(propiedad));
        } catch (IOException ex) {
            System.out.println("ok " + ex.getMessage());
        }
        return valor;
    }

    public Properties getPropiedades() {
        return propiedades;
    }

    public String getRuta() {
        return ruta;
    }

    private final String VALOR = "clave";

    public String encriptar(String clave) {
        PBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(VALOR);
        String claveEncriptada = PropertyValueEncryptionUtils.encrypt(clave, encryptor);
        return claveEncriptada;
    }

    public String desencriptar( String propiedad) {
        String clave = "";
        try {
            PBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(VALOR);
            Properties props = new EncryptableProperties(encryptor);
            props.load(new FileInputStream(ruta));
            clave = props.getProperty(propiedad);
        } catch (FileNotFoundException ex) {
            System.out.println("ok " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("ok " + ex.getMessage());
        }
        return clave;
    }

}
