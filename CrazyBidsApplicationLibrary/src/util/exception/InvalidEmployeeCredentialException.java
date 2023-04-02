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
public class InvalidEmployeeCredentialException extends Exception {

    /**
     * Creates a new instance of <code>InvalidAddressCredentialException</code>
     * without detail message.
     */
    public InvalidEmployeeCredentialException() {
    }

    /**
     * Constructs an instance of <code>InvalidAddressCredentialException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidEmployeeCredentialException(String msg) {
        super(msg);
    }
}
