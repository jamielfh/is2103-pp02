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
public class BidNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>BidNotFoundException</code> without
     * detail message.
     */
    public BidNotFoundException() {
    }

    /**
     * Constructs an instance of <code>BidNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BidNotFoundException(String msg) {
        super(msg);
    }
}
