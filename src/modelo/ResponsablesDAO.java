package modelo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.SQLException;

/**
 *
 * @author JOSE-MA
 */
public class ResponsablesDAO extends ConexionPSQL {

    public DataSet obtenerResponsables(String codres, String busca) throws SQLException {
        DataSet dt = new DataSet();
        abrirConexion();
        sentencia = conexion.prepareStatement("select identificacion, razonsocial, nombreciudad as ciudad, direccion,"
                + "  telefono, contacto  from responsables r inner join ciudades c on c.codciudad = r.codciudad"
                + " where (identificacion like ? or razonsocial ilike ? or razonsocial ilike ?) and codtiporesponsable like ?");
        sentencia.setString(1, busca + "%");
        sentencia.setString(2, "%" + busca + "%");
        sentencia.setString(3, busca + "%");
        sentencia.setString(4, codres + "%");
        resultado = sentencia.executeQuery();
        dt.load(resultado);
        cerrarConexion();
        return dt;
    }

    public DataSet obtenerResponsables1(String codres, String busca) throws SQLException {
        DataSet dt = new DataSet();
        abrirConexion();
        sentencia = conexion.prepareStatement("SELECT codtiporesponsable as tipo, identificacion, razonsocial, codciudad as ciudad, direccion, "
                + "  telefono, contacto"
                + "  FROM responsables "
                + " where (identificacion like ? or razonsocial ilike ? or razonsocial ilike ?) and codtiporesponsable like ?");
        sentencia.setString(1, busca + "%");
        sentencia.setString(2, "%" + busca + "%");
        sentencia.setString(3, busca + "%");
        sentencia.setString(4, codres + "%");
        resultado = sentencia.executeQuery();
        dt.load(resultado);
        int row = dt.getRowCount();
        for (int i = 0; i < row; i++) {
            Responsables res = new Responsables();
            res.setCodtiporesposanble(dt.getValueAt(i, 0).toString());
            dt.setValueAt(res.getTiporesponsable().getDescripcion(), i, 0);
            CiudadesDAO ciudad = new CiudadesDAO();
            String nombre = ciudad.obtenerCuidad(dt.getValueAt(i, 3).toString()).getNombreciudad();
            dt.setValueAt(nombre, i, 3);
        }
        dt.setCellEditable(false);
        cerrarConexion();
        return dt;
    }

    public Responsables obtenerResponsable(String codtipo, String identificacion) throws SQLException {
        Responsables responsable = null;
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from responsables "
                + " where codtiporesponsable=? and identificacion=?");
        sentencia.setString(1, codtipo);
        sentencia.setString(2, identificacion);
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            responsable = new Responsables();
            int i = 1;
            responsable.setCodtiporesposanble(resultado.getString(i++));
            responsable.setIdentificacion(resultado.getString(i++));
            responsable.setRazonsocial(resultado.getString(i++));
            responsable.setCodigociudad(resultado.getString(i++));
            responsable.setDireccion(resultado.getString(i++));
            responsable.setTelefono(resultado.getString(i++));
            responsable.setCorreo(resultado.getString(i++));
        }
        cerrarConexion();
        return responsable;
    }

    public int insertarResponsable(Responsables responsable) throws SQLException {
        int res;
        abrirConexion();
        sentencia = conexion.prepareStatement("INSERT INTO responsables(codtiporesponsable, identificacion, razonsocial, codciudad, direccion, telefono, contacto) "
                + "    VALUES (?, ?, ?, ?, ?, ?, ?);");
        int i = 1;
        sentencia.setString(i++, responsable.getCodtiporesposanble());
        sentencia.setString(i++, responsable.getIdentificacion());
        sentencia.setString(i++, responsable.getRazonsocial().toUpperCase());
        sentencia.setString(i++, responsable.getCodigociudad());
        sentencia.setString(i++, responsable.getDireccion().toUpperCase());
        sentencia.setString(i++, responsable.getTelefono());
        sentencia.setString(i++, responsable.getCorreo().toUpperCase());
        res = sentencia.executeUpdate();
        cerrarConexion();
        return res;
    }

    public int actualizarResponsable(Responsables responsable) throws SQLException {
        int res;
        abrirConexion();
        sentencia = conexion.prepareStatement("UPDATE responsables"
                + "   SET codtiporesponsable=?, identificacion=?, razonsocial=?, codciudad=?, "
                + "       direccion=?, telefono=?, contacto=?"
                + " WHERE codtiporesponsable=? and identificacion=?");
        int i = 1;
        sentencia.setString(i++, responsable.getCodtiporesposanble());
        sentencia.setString(i++, responsable.getIdentificacion());
        sentencia.setString(i++, responsable.getRazonsocial().toUpperCase());
        sentencia.setString(i++, responsable.getCodigociudad());
        sentencia.setString(i++, responsable.getDireccion().toUpperCase());
        sentencia.setString(i++, responsable.getTelefono());
        sentencia.setString(i++, responsable.getCorreo().toUpperCase());
        sentencia.setString(i++, responsable.getCodtiporesposanble());
        sentencia.setString(i++, responsable.getIdentificacion());
        res = sentencia.executeUpdate();
        cerrarConexion();
        return res;
    }
    public int eliminarResponsable(Responsables responsable) throws SQLException {
        int res;
        abrirConexion();
        sentencia = conexion.prepareStatement("DELETE FROM responsables "
                + " WHERE codtiporesponsable=? and identificacion=?");
        int i = 1;
        sentencia.setString(i++, responsable.getCodtiporesposanble());
        sentencia.setString(i++, responsable.getIdentificacion());
        
        res = sentencia.executeUpdate();
        cerrarConexion();
        return res;
    }

}
