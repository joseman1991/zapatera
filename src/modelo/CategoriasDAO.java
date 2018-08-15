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
public class CategoriasDAO extends ConexionPSQL {
    
    private final List<Categorias> listaCategorias;
    
    public CategoriasDAO() {
        listaCategorias = new ArrayList<>();
    }
    
    public void obtnerListaCategorias() throws SQLException {
        listaCategorias.clear();
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from categorias order by descripcion");
        resultado = sentencia.executeQuery();
        while (resultado.next()) {
            Categorias tm = new Categorias();
            tm.setCodcategoria(resultado.getInt(1));
            tm.setDescripcion(resultado.getString(2));
            listaCategorias.add(tm);
        }
        cerrarConexion();
    }
    
    public Categorias getCategorias(int cod) throws SQLException {
        Categorias c = new Categorias();
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from categorias where codcategoria= ?");
        sentencia.setInt(1, cod);
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            c.setCodcategoria(resultado.getInt(1));
            c.setDescripcion(resultado.getString(2));
        }
        cerrarConexion();
        return c;
    }
    
    
    public DataSet obtenerCategorias() throws SQLException{
        DataSet dt= new DataSet();
        abrirConexion();
        sentencia=conexion.prepareStatement("select * from categorias");
        resultado=sentencia.executeQuery();
        dt.load(resultado);        
        cerrarConexion();
        return  dt;
    }
    
    public int insertarCategoria(Categorias cat) throws SQLException{
        abrirConexion();
        sentencia=conexion.prepareStatement("insert into categorias values(default,?);");
        sentencia.setString(1, cat.getDescripcion().toUpperCase());
        int i= sentencia.executeUpdate();
        cerrarConexion();
        return i;
    }
    
    
    public List<Categorias> getListaCategorias() {
        return listaCategorias;
    }
    
}
