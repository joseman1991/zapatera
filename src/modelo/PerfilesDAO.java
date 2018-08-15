/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOSE-MA
 */
public class PerfilesDAO extends ConexionPSQL {

    private final List<Perfiles> listaPerfiles;

    public PerfilesDAO() {
        listaPerfiles = new ArrayList<>();
    }

    public Perfiles getObtnerPerfiles() throws SQLException {
        Perfiles perfil = null;
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from perfiles  where codperfil<>3 order by codperfil");
        resultado = sentencia.executeQuery();
        while (resultado.next()) {
            perfil = new Perfiles();
            perfil.setCodperfil(resultado.getInt(1));
            perfil.setDescripcion(resultado.getString(2));
            listaPerfiles.add(perfil);
        }
        cerrarConexion();
        return perfil;
    }

    public Perfiles getObtner(int codperfil) throws SQLException {
        Perfiles perfil = null;
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from perfiles where codperfil=?");
        sentencia.setInt(1, codperfil);
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            perfil = new Perfiles();
            perfil.setCodperfil(resultado.getInt(1));
            perfil.setDescripcion(resultado.getString(2));
        }
        cerrarConexion();
        return perfil;
    }

    public List<Perfiles> getListaPerfiles() {
        return listaPerfiles;
    }

}
