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
public class UpdateDeliveryAddressException extends Exception {

    /**
     * Creates a new instance of <code>UpdateDeliveryAddressException</code>
     * without detail message.
     */
    public UpdateDeliveryAddressException() {
    }

    /**
     * Constructs an instance of <code>UpdateDeliveryAddressException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateDeliveryAddressException(String msg) {
        super(msg);
    }
}
