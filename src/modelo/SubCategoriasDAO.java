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
public class SubCategoriasDAO extends ConexionPSQL {

    private final List<Subcategorias> listaSubCategorias;

    public SubCategoriasDAO() {
        this.listaSubCategorias = new ArrayList<>();
    }

    public void obtnerListaSubCategorias(int cod) throws SQLException {
        listaSubCategorias.clear();
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from subcategorias where codcategoria = ? order by descripcion");
        sentencia.setInt(1, cod);
        resultado = sentencia.executeQuery();
        while (resultado.next()) {
            Subcategorias tm = new Subcategorias();
            tm.setCodsubcategoria(resultado.getInt(2));
            tm.setCodcategorias(resultado.getInt(1));
            tm.setDescripcion(resultado.getString(3));
            listaSubCategorias.add(tm);
        }
        cerrarConexion();
    }

    public List<Subcategorias> getListaSubCategorias() {
        return listaSubCategorias;
    }

    public Subcategorias geSubtCategorias(int cod) throws SQLException {
        Subcategorias c = new Subcategorias();
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from subcategorias where codsubcategoria= ?");
        sentencia.setInt(1, cod);
        resultado = sentencia.executeQuery();
        if (resultado.next()) {
            c.setCodsubcategoria(resultado.getInt(1));
            c.setDescripcion(resultado.getString(2));
        }
        cerrarConexion();
        return c;
    }

    public DataSet obtenerSubCategorias() throws SQLException {
        DataSet dt = new DataSet();
        abrirConexion();
        sentencia = conexion.prepareStatement("select * from subcategorias");
        resultado = sentencia.executeQuery();
        dt.load(resultado);
        cerrarConexion();
        return dt;
    }

    public int insertarSubCategoria(Subcategorias cat) throws SQLException {
        abrirConexion();
        sentencia = conexion.prepareStatement("insert into subcategorias values (?,default,?)");
        sentencia.setInt(1, cat.getCodcategorias());
        sentencia.setString(2, cat.getDescripcion().toUpperCase());
        int i = sentencia.executeUpdate();
        cerrarConexion();
        return i;
    }

}
