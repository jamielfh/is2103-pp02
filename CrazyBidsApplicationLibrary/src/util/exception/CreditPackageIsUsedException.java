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
public class CreditPackageIsUsedException extends Exception {

    /**
     * Creates a new instance of <code>CreditPackageIsUsedException</code>
     * without detail message.
     */
    public CreditPackageIsUsedException() {
    }

    /**
     * Constructs an instance of <code>CreditPackageIsUsedException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreditPackageIsUsedException(String msg) {
        super(msg);
    }
}
