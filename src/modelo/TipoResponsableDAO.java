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
public class TipoResponsableDAO extends ConexionPSQL {

    public final List<Tiporesponsable> listaResponsable;

    public TipoResponsableDAO() {
        listaResponsable = new ArrayList<>();
    }

    public void obtnerListaRespondable(String tipores) throws SQLException {
        listaResponsable.clear();
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from tiporesponsable where codtiporesponsable like ? and codtiporesponsable<> 'E'");
        sentencia.setString(1, tipores + "%");
        resultado = sentencia.executeQuery();
        while (resultado.next()) {
            Tiporesponsable tr = new Tiporesponsable();
            tr.setCodtiporesponsable(resultado.getString(1).charAt(0));
            tr.setDescripcion(resultado.getString(2));
            listaResponsable.add(tr);
        }
        cerrarConexion();
    }

    public Tiporesponsable obtenerTiporesponsable(String codtipores) throws SQLException {
        Tiporesponsable tiporesponsable = null;
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from tiporesponsable where codtiporesponsable =?");
        sentencia.setString(1, codtipores);
        resultado=sentencia.executeQuery() ;
       while (resultado.next()) {
            tiporesponsable = new Tiporesponsable();
            tiporesponsable.setCodtiporesponsable(resultado.getString(1).charAt(0));
            tiporesponsable.setDescripcion(resultado.getString(2));
        }
        cerrarConexion();
        return tiporesponsable;
    }

}
