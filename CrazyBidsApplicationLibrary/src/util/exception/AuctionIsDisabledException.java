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
public class AuctionIsDisabledException extends Exception {

    /**
     * Creates a new instance of <code>AuctionIsDisabledException</code> without
     * detail message.
     */
    public AuctionIsDisabledException() {
    }

    /**
     * Constructs an instance of <code>AuctionIsDisabledException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public AuctionIsDisabledException(String msg) {
        super(msg);
    }
}
