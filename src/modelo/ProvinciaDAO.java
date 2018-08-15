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
public class ProvinciaDAO extends ConexionPSQL {

    public List<Provincia> listaProvincia;

    public ProvinciaDAO() {
        listaProvincia=new ArrayList<>();
    }

    public ProvinciaDAO(List<Provincia> listaProvincia) {
        this.listaProvincia = listaProvincia;
    }
    
    
    
    
    public void ObtenerListaProvincia() throws SQLException{
        abrirConexion();
        listaProvincia.clear();
        sentencia=conexion.prepareStatement("Select * from provincia order by codprovincia");
        resultado=sentencia.executeQuery();
        while (resultado.next()) {            
            Provincia provincia= new Provincia();
            provincia.setCodprovincia(resultado.getString(1));
            provincia.setDescripcion(resultado.getString(2));
            listaProvincia.add(provincia);
        }
        cerrarConexion();
    }
    
    public Provincia obtenerProvincia(String codprovincia) throws SQLException{
        Provincia provincia=null;
        abrirConexion();
         sentencia=conexion.prepareStatement("Select * from provincia where codprovincia=?");
         sentencia.setString(1, codprovincia);
        resultado=sentencia.executeQuery();
        if (resultado.next()) {            
            provincia= new Provincia();
            provincia.setCodprovincia(resultado.getString(1));
            provincia.setDescripcion(resultado.getString(2));
            
        }
        cerrarConexion();
        return provincia;
    }

}
