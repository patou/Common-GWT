package com.sfeir.common.gwt.client.mvp;

import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;


/**
 * Factory generated by the RequestContextFactoryGenerator which instantiate the requestContext from the ResquestFactory
 * 
 */
public interface RequestContextFactory {
    public RequestContext createRequestContext(String requestContextName);

	public RequestFactory createRequestFactory(String name);
}
