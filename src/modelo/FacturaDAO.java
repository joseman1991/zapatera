/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;
import java.sql.SQLException;

/**
 *
 * @author JOSE
 */
public class FacturaDAO extends ConexionPSQL {

    public long insertarFactura(Factura factura) throws SQLException {
        long cod = 0;
        abrirConexion();
        sentencia = conexion.prepareStatement("select max(codfactura) from factura");
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            cod = resultado.getLong(1);
        }
        procedimiento = conexion.prepareCall("{call agg_factura(?,?,?,?,?)}");
        procedimiento.setLong(1, cod);
        procedimiento.setLong(2, factura.getCodmovimiento());
        procedimiento.setString(3, factura.getNumerofactura());
        procedimiento.setString(4, factura.getCodtiporesponsable() + "");
        procedimiento.setString(5, factura.getCodtiporesponsable() + "");

        procedimiento.executeUpdate();
        cerrarConexion();
        return cod;
    }

    public Date obtenerDomingo() throws SQLException {
        abrirConexion();
        procedimiento = conexion.prepareCall("{call getDomingo()}");
        Date d = null;
        resultado = procedimiento.executeQuery();
        if (resultado.next()) {
            d = resultado.getDate(1);
        }
        cerrarConexion();
        return d;
    }

    public DataSet getFacturas(Date fechainicio, Date fechafin) throws SQLException {
        DataSet dt ;
        abrirConexion();
        sentencia = conexion.prepareStatement("select codfactura,numerofactura, r.razonsocial,f.concepto,f.totalfactura\n"
                + "from factura f inner join movimientos m on m.codmovimiento=f.codmovimiento \n"
                + "inner join responsables r on r.codtiporesponsable=f.codtiporesponsable and r.identificacion=f.identificacion\n"
                + "where naturaleza = 'E' and fechafactura between ? and ?");
        sentencia.setDate(1, fechainicio);
        sentencia.setDate(2, fechafin);
        resultado= sentencia.executeQuery();
        dt= new DataSet();
        dt.load(resultado);
        cerrarConexion();
        return dt;
    }

    public DataSet obtnerFactura(Responsables re) throws SQLException {
        DataSet dt;
        abrirConexion();
        sentencia = conexion.prepareStatement("select codfactura as código, numerofactura as numero, concepto, totalfactura as totactal, fechafactura as fecha "
                + "from factura "
                + "where codtiporesponsable=? and identificacion=?");
        sentencia.setString(1, re.getCodtiporesposanble());
        sentencia.setString(2, re.getIdentificacion());
        resultado = sentencia.executeQuery();
        dt = new DataSet();
        dt.load(resultado);
        dt.setCellEditable(false);
        cerrarConexion();
        return dt;
    }

    /*
    ;
select numerofactura, concepto, totalfactura, fechafactura from factura where codtiporesponsable='P' and identificacion='1207372002' and
fechafactura between '2018-07-24' and '2018-07-26';
     */
    public DataSet obtnerFactura(Responsables re, Date fecha1, Date fecha2) throws SQLException {
        DataSet dt;
        abrirConexion();
        sentencia = conexion.prepareStatement("select numerofactura, concepto, totalfactura, fechafactura "
                + "from factura where codtiporesponsable=? "
                + "and identificacion=? and "
                + "fechafactura between ? and ?");
        resultado = sentencia.executeQuery();
        dt = new DataSet();
        dt.load(resultado);
        dt = new DataSet();
        dt.load(resultado);

        dt.setCellEditable(false);
        cerrarConexion();
        return dt;
    }

    public long obtenerId(long cod) throws SQLException {
        abrirConexion();
        sentencia = conexion.prepareStatement("select codfactura from factura where codmovimiento=?");
        sentencia.setLong(1, cod);
        resultado = sentencia.executeQuery();
        long id = 0;
        if (resultado.next()) {
            id = resultado.getLong(1);
        }
        cerrarConexion();
        return id;
    }

}
