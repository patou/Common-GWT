package com.sfeir.common.gwt.shared.exceptions;

import java.io.Serializable;

/**
 * Throw this exception from a Service GWT-RPC service to say to the GWT client that the user isn't connected
 * 
 */
public class NotLoginException extends Exception implements Serializable {
    private static final long serialVersionUID = -2904744681997800782L;

    public NotLoginException() {
    	super("You must be login for access to this application");
    }
}
