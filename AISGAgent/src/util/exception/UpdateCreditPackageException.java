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
public class UpdateCreditPackageException extends Exception {

    /**
     * Creates a new instance of <code>UpdateCreditPackageException</code>
     * without detail message.
     */
    public UpdateCreditPackageException() {
    }

    /**
     * Constructs an instance of <code>UpdateCreditPackageException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateCreditPackageException(String msg) {
        super(msg);
    }
}
