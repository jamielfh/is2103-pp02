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
public class DeliveryAddressExistException extends Exception{

    /**
     * Creates a new instance of <code>DeliveryAddressExistException</code>
     * without detail message.
     */
    public DeliveryAddressExistException() {
    }

    /**
     * Constructs an instance of <code>DeliveryAddressExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeliveryAddressExistException(String msg) {
        super(msg);
    }
}
