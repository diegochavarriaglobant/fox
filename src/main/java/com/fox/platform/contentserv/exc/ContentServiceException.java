package com.fox.platform.contentserv.exc;

/**
 * Exception to return mapping with field problems.
 * @author diego.chavarria
 */
public class ContentServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ContentServiceException( String message ) {
        super(message);
    }

    public ContentServiceException( String message, Throwable e) {
        super(message,e);
    }


}
