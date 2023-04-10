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
public class CreditPackageIsDisabledException extends Exception {

    /**
     * Creates a new instance of <code>CreditPackageIsDisabledException</code>
     * without detail message.
     */
    public CreditPackageIsDisabledException() {
    }

    /**
     * Constructs an instance of <code>CreditPackageIsDisabledException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreditPackageIsDisabledException(String msg) {
        super(msg);
    }
}
