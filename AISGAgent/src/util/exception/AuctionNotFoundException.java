/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author jamielee
 */
public class AuctionNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>UnknownPersistenceException</code>
     * without detail message.
     */
    public AuctionNotFoundException() {
    }

    /**
     * Constructs an instance of <code>UnknownPersistenceException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public AuctionNotFoundException(String msg) {
        super(msg);
    }
}