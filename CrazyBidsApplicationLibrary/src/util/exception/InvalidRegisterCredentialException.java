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
public class InvalidRegisterCredentialException extends Exception{

    /**
     * Creates a new instance of <code>InvalidRegisterCredentialException</code>
     * without detail message.
     */
    public InvalidRegisterCredentialException() {
    }

    /**
     * Constructs an instance of <code>InvalidRegisterCredentialException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidRegisterCredentialException(String msg) {
        super(msg);
    }
}
