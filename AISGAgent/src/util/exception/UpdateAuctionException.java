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
public class UpdateAuctionException extends Exception {

    /**
     * Creates a new instance of <code>UpdateAuctionException</code> without
     * detail message.
     */
    public UpdateAuctionException() {
    }

    /**
     * Constructs an instance of <code>UpdateAuctionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateAuctionException(String msg) {
        super(msg);
    }
}
