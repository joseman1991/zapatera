/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.SQLException;

/**
 *
 * @author JOSE-MA
 */
public class UsuariosDAO extends ConexionPSQL {

    public Usuarios getUserLogin(Usuarios users) throws SQLException {
        Usuarios user = null;
        abrirConexion();
        procedimiento = conexion.prepareCall("{call obtenerUsuario(?,?)}");
        procedimiento.setString(1, users.getCodusuario());
        procedimiento.setString(2, users.getClave());
        resultado = procedimiento.executeQuery();
        if (resultado.next()) {
            int i = 1;
            user = new Usuarios();
            user.setCodperfiles(resultado.getInt(i++));
            user.setCodusuario(resultado.getString(i++));
            user.setNombre(resultado.getString(i++));
            i++;
            user.setCodestado(resultado.getString(i++).charAt(0));
        } else {
            procedimiento = conexion.prepareCall("{call isUser(?)}");
            procedimiento.setString(1, users.getCodusuario());
            resultado = procedimiento.executeQuery();
            if (resultado.next()) {
                if (!resultado.getBoolean(1)) {
                    user = new Usuarios();
                    user.setCodusuario(null);
                }
            }
        }


        cerrarConexion();
        return user;
    }

    public DataSet obtenerUsuaarios(String busca, String perfil, String estado) throws SQLException {
        DataSet dt;
        abrirConexion();
        sentencia = conexion.prepareStatement("select codusuario as codigo, nombre, p.descripcion as perfil, e.descripcion as estado "
                + "from usuarios as u, estados as e, perfiles as p "
                + "where u.codperfil=p.codperfil and u.codestado=e.codestado"
                + " and (nombre ilike ? or codusuario like ?) and (p.descripcion like ? and e.descripcion like ?)");
        sentencia.setString(1, "%" + busca + "%");
        sentencia.setString(2, busca + "%");
        sentencia.setString(3, perfil + "%");
        sentencia.setString(4, estado + "%");
        resultado = sentencia.executeQuery();
        dt = new DataSet();
        dt.load(resultado);
        cerrarConexion();
        return dt;
    }

    public boolean isUsuario(String codusuario) throws SQLException {
        abrirConexion();
        sentencia = conexion.prepareStatement("select codusuario from usuarios "
                + "where codusuario=?");
        sentencia.setString(1, codusuario.toUpperCase());
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            cerrarConexion();
            return true;
        }
        cerrarConexion();
        return false;
    }

    public Usuarios obtenerUser(Usuarios users) throws SQLException {
        Usuarios user = null;
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from usuarios where codusuario=?");
        sentencia.setString(1, users.getCodusuario());
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            int i = 1;
            user = new Usuarios();
            user.setCodperfiles(resultado.getInt(i++));
            user.setCodusuario(resultado.getString(i++));
            user.setNombre(resultado.getString(i++));
            user.setClave(resultado.getString(i++));
            user.setCodestado(resultado.getString(i++).charAt(0));
        }
        cerrarConexion();
        return user;
    }

    public int insertarUsuario(Usuarios user) throws SQLException {
        abrirConexion();
        sentencia = conexion.prepareStatement("INSERT INTO usuarios("
                + "            codperfil, codusuario, nombre, clave, codestado) "
                + "    VALUES (?, ?, ?, ?, ?)");
        int i = 1;
        sentencia.setInt(i++, user.getCodperfiles());
        sentencia.setString(i++, user.getCodusuario());
        sentencia.setString(i++, user.getNombre().toUpperCase());
        sentencia.setString(i++, user.getClave());
        sentencia.setString(i++, user.getCodestado() + "");
        int s = sentencia.executeUpdate();
        System.out.println(s);
        cerrarConexion();
        return s;
    }

    public int actualizarUsuario(Usuarios user) throws SQLException {
        abrirConexion();
        sentencia = conexion.prepareStatement("UPDATE usuarios"
                + "   SET codperfil=?, codusuario=?, nombre=?, codestado=?"
                + " WHERE codusuario=?");
        int i = 1;
        sentencia.setInt(i++, user.getCodperfiles());
        sentencia.setString(i++, user.getCodusuario());
        sentencia.setString(i++, user.getNombre().toUpperCase());
        sentencia.setString(i++, user.getCodestado() + "");
        sentencia.setString(i++, user.getCodusuario_old());
        int s = sentencia.executeUpdate();
        System.out.println(s);
        cerrarConexion();
        return s;
    }

    public int eliminarUsuario(Usuarios user) throws SQLException {
        abrirConexion();
        sentencia = conexion.prepareStatement("DELETE FROM usuarios "
                + " WHERE codusuario=?");
        int i = 1;
        sentencia.setString(i++, user.getCodusuario());
        int s = sentencia.executeUpdate();
        cerrarConexion();
        return s;
    }

    public String obtenrClave(Usuarios user) throws SQLException {
        abrirConexion();
        String clave = "";
        sentencia = conexion.prepareStatement("select clave from usuarios where codusuario=?");
        sentencia.setString(1, user.getCodusuario());
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            clave = resultado.getString(1);
        }
        cerrarConexion();
        return clave;
    }

    public int actualizarClave(Usuarios user) throws SQLException {
        abrirConexion();
        sentencia = conexion.prepareStatement("UPDATE usuarios"
                + "   SET clave=? "
                + " WHERE codusuario=?");
        int i = 1;

        sentencia.setString(i++, user.getClave());
        sentencia.setString(i++, user.getCodusuario());

        int s = sentencia.executeUpdate();
        System.out.println(s);
        cerrarConexion();
        return s;
    }

}
