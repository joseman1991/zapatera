/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.SQLException;

/**
 *
 * @author JOSE
 */
public class EmpresaDAO extends ConexionPSQL{
    
    
    public Empresa obtenerEmpresa() throws SQLException{
        Empresa e= null;
        
        abrirConexion();
        sentencia= conexion.prepareStatement("select * from Empresa");
        resultado=sentencia.executeQuery();
        if(resultado.next()){
            e= new Empresa();
            e.setIdentificacion(resultado.getString(1));
            e.setRazonsocial(resultado.getString(2));      
            System.out.println(resultado.getString(2));
            e.setCodciudad (resultado.getString(3)); 
            Ciudades c= new CiudadesDAO().obtenerCuidad(e.getCodciudad());
            e.setCiudad(c);
            e.setDireccion(resultado.getString(4));
            e.setTelefono(resultado.getString(5));
            e.setIva(resultado.getDouble(6));
        }
        cerrarConexion();
        
        return  e;
    }
    
}
