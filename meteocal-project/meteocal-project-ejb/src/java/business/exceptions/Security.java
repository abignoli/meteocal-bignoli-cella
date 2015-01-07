/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.exceptions;

/**
 *
 * @author Andrea Bignoli
 */
public class Security extends BusinessException {

    /**
     * Creates a new instance of <code>Security</code> without detail message.
     */
    public Security() {
    }

    /**
     * Constructs an instance of <code>Security</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public Security(String msg) {
        super(msg);
    }
}
