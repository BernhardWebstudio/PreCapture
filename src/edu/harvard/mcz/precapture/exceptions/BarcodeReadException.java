/**
 * BarcodeReadException.java
 * edu.harvard.mcz.precapture.exceptions
 * Copyright © 2012 President and Fellows of Harvard College
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
 * Author: mole
 */
package edu.harvard.mcz.precapture.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public class BarcodeReadException extends Exception {
    private static final Log log = LogFactory.getLog(BarcodeReadException.class);

    /**
     * Default no argument constructor, constructs a new BarcodeReadException
     * instance.
     */
    public BarcodeReadException() {
    }

    public BarcodeReadException(String message) {
        super(message);
    }
}
