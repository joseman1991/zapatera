/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jTextFieldAutoCOmplete;

/**
 *
 * @author JOSE
 */
public class PredictorException extends Exception {

    public PredictorException(String message) {
        super(message);
    }

    public static void verificarNull(String[] arrStrings) throws PredictorException {
        for (String arrString : arrStrings) {
            if (arrString == null) {
                throw new PredictorException("Los atributos no son los correctos");
            }
        }
        
        

    }

}
