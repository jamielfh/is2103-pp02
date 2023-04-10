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
public class AuctionAlreadyClosedException extends Exception{

    /**
     * Creates a new instance of
     * <code>AddressIsAssociatedWithWinningBidException</code> without detail
     * message.
     */
    public AuctionAlreadyClosedException() {
    }

    /**
     * Constructs an instance of
     * <code>AddressIsAssociatedWithWinningBidException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AuctionAlreadyClosedException(String msg) {
        super(msg);
    }
}
