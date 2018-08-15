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
public class EstadoDAO extends ConexionPSQL {

    private final List<Estado> listaEstados;

    public EstadoDAO() {
        listaEstados = new ArrayList<>();
    }

    public Estado getObtnerEstados() throws SQLException {
        Estado estado = null;
        listaEstados.clear();
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from estados order by codestado");
        resultado = sentencia.executeQuery();
        while (resultado.next()) {
            estado = new Estado();
            estado.setCodestado(resultado.getString(1));
            estado.setDescripcion(resultado.getString(2));
            listaEstados.add(estado);
        }
        cerrarConexion();
        return estado;
    }

    public Estado getObtnerEstado(char codestado) throws SQLException {
        Estado estado = null;
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from estados where codestado=?");
        sentencia.setString(1, codestado+"");
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            estado = new Estado();
            estado.setCodestado(resultado.getString(1));
            estado.setDescripcion(resultado.getString(2));
        }
        cerrarConexion();
        return estado;
    }

    public List<Estado> getListaEstados() {
        return listaEstados;
    }

}
