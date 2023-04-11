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
public class CustomerNotLoginException extends Exception{

    /**
     * Creates a new instance of <code>BidNotFoundException</code> without
     * detail message.
     */
    public CustomerNotLoginException() {
    }

    /**
     * Constructs an instance of <code>BidNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerNotLoginException(String msg) {
        super(msg);
    }
}
