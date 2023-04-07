/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Bransome Tan Yi Hao
 */
public class NotEnoughCreditException extends Exception{

    /**
     * Creates a new instance of <code>NotEnoughCreditException</code> without
     * detail message.
     */
    public NotEnoughCreditException() {
    }

    /**
     * Constructs an instance of <code>NotEnoughCreditException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NotEnoughCreditException(String msg) {
        super(msg);
    }
}
