/**
 * PrintFailedException.java
 * edu.harvard.mcz.precapture.exceptions
 * Copyright © 2009 President and Fellows of Harvard College
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Author: Paul J. Morris
 */
package edu.harvard.mcz.precapture.exceptions;

/**
 * PrintFailedException
 *
 * @author Paul J. Morris
 */
public class PrintFailedException extends Exception {

    private static final long serialVersionUID = 9031801441033382725L;

    /**
     * @param message
     */
    public PrintFailedException(String message) {
        super(message);
    }

}
