package com.sfeir.common.gwt.shared.exceptions;

import java.io.Serializable;

/**
 * Throw this exception from a Service GWT-RPC service to say to the GWT client that the user hasn't the right to access to these datas
 * 
 */
@SuppressWarnings("serial")
public class NotAccessAllowedException extends Exception implements Serializable {
    //private static final long serialVersionUID = -2904744681997800782L;

    public NotAccessAllowedException() {
    }
}
