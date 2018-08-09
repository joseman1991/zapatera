package modelo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOSE-MA
 */
public class TipomovimientoDAO extends ConexionPSQL {

    public final List<Tipomovimiento> listaTipoMov;

    public TipomovimientoDAO() {
        listaTipoMov = new ArrayList<>();
    }

    public void obtnerListaDetalleMov(String naturaleza) throws SQLException {
        listaTipoMov.clear();
        abrirConexion();
        sentencia= conexion.prepareStatement("select * from tipomovimiento where naturaleza=?");
        sentencia.setString(1, naturaleza);
        resultado= sentencia.executeQuery();
        while (resultado.next()) {            
            Tipomovimiento tm= new Tipomovimiento();
            tm.setCodtipomovimiento(resultado.getString(1));
            tm.setDescripcion(resultado.getString(2));
            tm.setNaturaleza(resultado.getString(3));
            listaTipoMov.add(tm);
        }
        cerrarConexion();
    }

}
