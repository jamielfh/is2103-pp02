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
public class AddressIsDisabledException extends Exception{

    /**
     * Creates a new instance of <code>AddressIsDisabledException</code> without
     * detail message.
     */
    public AddressIsDisabledException() {
    }

    /**
     * Constructs an instance of <code>AddressIsDisabledException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public AddressIsDisabledException(String msg) {
        super(msg);
    }
}
