package modelo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOSE-MA
 */
public class DetalleMovimientoDAO extends ConexionPSQL {

    private float stock;

    public List<Object[]> filas;

    public DetalleMovimientoDAO() {
        filas = new ArrayList<>();
    }

    public void InsertarDetalle(Connection con, List<Detallemovimiento> dev) throws SQLException {
        //abrirConexion();
        conexion = con;
        long codmo = 0;
        for (int i = 0; i < dev.size(); i++) {
            Detallemovimiento get = dev.get(i);
            sentencia = conexion.prepareCall("{call insertarDetalle(?,?,?,?,?,?,?,?)}");
            int j = 1;
            sentencia.setLong(j++, get.getCodmovimiento());
            codmo = get.getCodmovimiento();
            sentencia.setString(j++, get.getCodarticilo());
            sentencia.setInt(j++, get.getOrden());
            sentencia.setDouble(j++, get.getCantidad());
            BigDecimal t= new BigDecimal(get.getCosto()).setScale(2, BigDecimal.ROUND_HALF_UP);           
            sentencia.setDouble(j++, t.doubleValue());
            sentencia.setString(j++, get.getNfactura());
            sentencia.setString(j++, get.getResponsables().getCodtiporesposanble());
            sentencia.setString(j++, get.getResponsables().getIdentificacion());
            sentencia.executeUpdate();
        }
        sentencia = con.prepareCall("{call actualizarMovimiento(?)}");
        sentencia.setLong(1, codmo);
        sentencia.executeUpdate();

//        ArticuloDAO artDao = new ArticuloDAO();
//        artDao.actualizarStock(listArticulos);
    }
    
   
    public void obtenerKardex(String codarticulo) throws SQLException {
        abrirConexion();
        filas.clear();
        sentencia = conexion.prepareStatement("select * from obtemKerdex(?)");
        sentencia.setString(1, codarticulo);
        resultado = sentencia.executeQuery();
        while (resultado.next()) {
           
            String fecha =resultado.getString(1);
            String concepto =resultado.getString(2);
            String icantidad = resultado.getString(3);
            String ivu =resultado.getString(4);
            String ivt = resultado.getString(5);
            String scantidad = resultado.getString(6);
            String svu = resultado.getString(7);
            String svt = resultado.getString(8);
 
            String ecantidad = resultado.getString(9);
            String evu = resultado.getString(10);
            String evt =resultado.getString(11);
            Object[] obj = {fecha, concepto, icantidad, ivu, ivt, scantidad, svu, svt, ecantidad, evu, evt};
            filas.add(obj);
        }
        cerrarConexion();
    }

    public void setStock(float stock) {
        this.stock = stock;
    }

}
