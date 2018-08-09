/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author JOSE
 */
public class Factura {

    private long codfactura;
    private long codmovimiento;
    private int coditpofactura;
    private String numerofactura;
    private char codtiporesponsable;
    private String identificacion;
    private float totalfactura;
    private Timestamp fechahoraingreso;

    public long getCodfactura() {
        return codfactura;
    }

    public void setCodfactura(long codfactura) {
        this.codfactura = codfactura;
    }

    public long getCodmovimiento() {
        return codmovimiento;
    }

    public void setCodmovimiento(long codmovimiento) {
        this.codmovimiento = codmovimiento;
    }

    public int getCoditpofactura() {
        return coditpofactura;
    }

    public void setCoditpofactura(int coditpofactura) {
        this.coditpofactura = coditpofactura;
    }

    public String getNumerofactura() {
        return numerofactura;
    }

    public void setNumerofactura(String numerofactura) {
        this.numerofactura = numerofactura;
    }

    public char getCodtiporesponsable() {
        return codtiporesponsable;
    }

    public void setCodtiporesponsable(char codtiporesponsable) {
        this.codtiporesponsable = codtiporesponsable;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public float getTotalfactura() {
        return totalfactura;
    }

    public void setTotalfactura(float totalfactura) {
        this.totalfactura = totalfactura;
    }

    public Timestamp getFechahoraingreso() {
        return fechahoraingreso;
    }

    public void setFechahoraingreso(Timestamp fechahoraingreso) {
        this.fechahoraingreso = fechahoraingreso;
    }
    
    

}
