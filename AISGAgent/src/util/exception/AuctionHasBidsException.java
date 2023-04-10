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
public class AuctionHasBidsException extends Exception{

    /**
     * Creates a new instance of <code>AuctionHasBidsException</code> without
     * detail message.
     */
    public AuctionHasBidsException() {
    }

    /**
     * Constructs an instance of <code>AuctionHasBidsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AuctionHasBidsException(String msg) {
        super(msg);
    }
}
