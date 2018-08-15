package modelo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author JOSE-MA
 */
public class MovimientoDAO extends ConexionPSQL {

    private long codfactura;

    public MovimientoDAO() {

    }

    public long getNextCodMovimiento() throws SQLException {
        abrirConexion();
        long cod = 1;
        sentencia = conexion.prepareStatement("select max(codmovimiento) from movimientos");
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            cod = resultado.getLong(1) + 1;
        }
        cerrarConexion();
        return (cod);
    }

    public long insertarMov(Movimientos mov) throws SQLException {
        mov.setCodmovimiento(getNextCodMovimiento());
        abrirConexion();
        procedimiento = conexion.prepareCall("{call agg_movimiento(?,?,?,?,?,?,?,?,?)}");
        int i = 1;
        procedimiento.setString(i++, mov.getTipomovimiento().getCodtipomovimiento());
        procedimiento.setString(i++, mov.getNaturaleza() + "");
        procedimiento.setString(i++, mov.getCodusuario());
        procedimiento.setString(i++, mov.getResponsables().getCodtiporesposanble());
        procedimiento.setString(i++, mov.getResponsables().getIdentificacion());
        procedimiento.setLong(i++, mov.getCodmovimiento());
        procedimiento.setString(i++, mov.getConcepto());
        procedimiento.setDate(i++, mov.getFechamovimiento());
        procedimiento.setString(i++, mov.getNfactura());
        resultado = procedimiento.executeQuery();
        if (resultado.next()) {
            codfactura = resultado.getLong(1);
        }
        cerrarConexion();
        return mov.getCodmovimiento();
    }

    public long insertarMov(Movimientos mov, List<Detallemovimiento> dtm) throws SQLException {
        mov.setCodmovimiento(getNextCodMovimiento());
        abrirConexion();
        conexion.setAutoCommit(false);
        procedimiento = conexion.prepareCall("{call agg_movimiento(?,?,?,?,?,?,?,?,?)}");
        int i = 1;
        procedimiento.setString(i++, mov.getTipomovimiento().getCodtipomovimiento());
        procedimiento.setString(i++, mov.getNaturaleza() + "");
        procedimiento.setString(i++, mov.getCodusuario());
        procedimiento.setString(i++, mov.getResponsables().getCodtiporesposanble());
        procedimiento.setString(i++, mov.getResponsables().getIdentificacion());
        procedimiento.setLong(i++, mov.getCodmovimiento());
        procedimiento.setString(i++, mov.getConcepto());
        procedimiento.setDate(i++, mov.getFechamovimiento());
        procedimiento.setString(i++, mov.getNfactura());
        resultado = procedimiento.executeQuery();
        if (resultado.next()) {
            codfactura = resultado.getLong(1);
        }
        actualizarDetalles(dtm, mov.getCodmovimiento());
        DetalleMovimientoDAO dmdao= new DetalleMovimientoDAO();
        dmdao.InsertarDetalle(conexion,dtm);       
        return mov.getCodmovimiento();
    }

    
        private void actualizarDetalles(List<Detallemovimiento> listaDetelle,long codmov) throws SQLException {
        for (int i = 0; i < listaDetelle.size(); i++) {            
                listaDetelle.get(i).setOrden(i+1);
                listaDetelle.get(i).setCodmovimiento(codmov);
        }
    }
    
    public Movimientos obtenerMovimiento(long codmivimiento) throws SQLException {
        Movimientos mov = null;
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from movimientos where codmovimiento=?");
        sentencia.setLong(1, codmivimiento);
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            mov = new Movimientos();
            int i = 1;
            mov.setCodtipomovimiento(resultado.getString(i++));
            mov.setNaturaleza(resultado.getString(i++).charAt(0));
            mov.setCodusuario(resultado.getString(i++));
            mov.setTiporesponsable(resultado.getString(i++));
            mov.setIdentificacion(resultado.getString(i++));
            mov.setCodmovimiento(resultado.getLong(i++));
            mov.setConcepto(resultado.getString(i++));
            mov.setFechamovimiento(resultado.getDate(i++));
            mov.setTotalmovimiento(resultado.getDouble(i++));
        }
        cerrarConexion();
        return mov;
    }

    public DataSet obtenerMovimientos(String busca) throws SQLException {
        DataSet dt = new DataSet();
        abrirConexion();
        sentencia = conexion.prepareStatement("select codmovimiento, to_char(fechamovimiento,'dd/mm/yyyy'),concepto,estado from movimientos "
                + "where concepto ilike ?");
        sentencia.setString(1, busca + "%");
        resultado = sentencia.executeQuery();
        dt.load(resultado);
        cerrarConexion();
        return dt;
    }

    public long getCodfactura() {
        return codfactura;
    }

    @Override
    public Connection getConexion() {
        return conexion;
    }
    
    
    

}
