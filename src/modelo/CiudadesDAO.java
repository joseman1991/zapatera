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
public class CiudadesDAO extends ConexionPSQL {

    public List<Ciudades> listaCiudades;

    public CiudadesDAO() {
        listaCiudades = new ArrayList<>();
    }

    public CiudadesDAO(List<Ciudades> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }
    
    

    public void ObtenerListaCiudades(String codprovincia) throws SQLException {
        abrirConexion();
        listaCiudades.clear();
        sentencia = conexion.prepareStatement("Select * from ciudades where codprovincia=?");
        sentencia.setString(1, codprovincia);
        resultado = sentencia.executeQuery();
        while (resultado.next()) {
            Ciudades ciudad = new Ciudades();
            ciudad.setCodciudad(resultado.getString(1));
            ciudad.setCodprovincia(resultado.getString(2));
            ciudad.setNombreciudad(resultado.getString(3));
            listaCiudades.add(ciudad);
        }
        cerrarConexion();
    }
    public void ObtenerListaCiudades() throws SQLException {
        abrirConexion();
        listaCiudades.clear();
        sentencia = conexion.prepareStatement("Select * from ciudades");       
        resultado = sentencia.executeQuery();
        while (resultado.next()) {
            Ciudades ciudad = new Ciudades();
            ciudad.setCodciudad(resultado.getString(1));
            ciudad.setCodprovincia(resultado.getString(2));
            ciudad.setNombreciudad(resultado.getString(3));
            listaCiudades.add(ciudad);
        }
        cerrarConexion();
    }

    public Ciudades obtenerCuidad(String codciudad) throws SQLException {
        Ciudades ciudad = null;
        abrirConexion();
        sentencia = conexion.prepareStatement("Select * from ciudades where codciudad=?");
        sentencia.setString(1, codciudad);
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            ciudad = new Ciudades();
            ciudad.setCodciudad(resultado.getString(1));
            ciudad.setCodprovincia(resultado.getString(2));
            ciudad.setNombreciudad(resultado.getString(3));
        }
        cerrarConexion();
        return ciudad;
    }
    
    
}
