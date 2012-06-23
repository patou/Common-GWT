package com.sfeir.common.gwt.rebind.requestcontextfactory;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.sfeir.common.gwt.client.mvp.RequestContextFactory;

/**
 * This generator return the implementation of the RequestContextFactory with generation of the createRequestContext method
 * 
 * Generate the method RequestContext createRequestContext(String requestName) :
 * 
 * Search for the all request context from a Request Context, check if there are no problems with it, and generate the factory
 * 
 * Access to the factory by using the ClientFactory.getRequest();
 */
public class RequestContextFactoryGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext generatorContext, String interfaceName) throws UnableToCompleteException {
		RequestContextGeneratorContext context = RequestContextGeneratorContext.create(logger, generatorContext.getTypeOracle(), interfaceName);

		PrintWriter out = generatorContext.tryCreate(logger, context.packageName, context.implName);

		if (out != null) {
			generateOnce(generatorContext, context, out);
		}

		logger.log(TreeLogger.TRACE, context.packageName + "." + context.implName);
		return context.packageName + "." + context.implName;
	}

	private void generateOnce(GeneratorContext generatorContext, RequestContextGeneratorContext context, PrintWriter out) throws UnableToCompleteException {

		TreeLogger logger = context.logger.branch(TreeLogger.DEBUG, String.format("Generating implementation of %s", context.interfaceType.getName()));
		ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(context.packageName, context.implName);

		f.addImplementedInterface(RequestContextFactory.class.getSimpleName());

		f.addImport(RequestContextFactory.class.getCanonicalName());
		f.addImport(GWT.class.getCanonicalName());
		f.addImport(RequestContext.class.getCanonicalName());
		f.addImport(RequestFactory.class.getCanonicalName());
		f.addImport(HashMap.class.getCanonicalName());

		SourceWriter sw = f.createSourceWriter(generatorContext, out);
		sw.println();

		sw.println("HashMap<String,RequestFactory> requestFactory = new HashMap<String,RequestFactory>();");

		sw.println();
		writeCreateRequestFactory(context, sw);
		sw.println();
		writeCreateRequest(context, sw);
		sw.println();

		sw.outdent();
		sw.println("}");
		generatorContext.commit(logger, out);
	}

	/**
	 * Generate the method
	 * 
	 * @param context
	 * @param sw
	 * @throws UnableToCompleteException
	 */
	private void writeCreateRequest(RequestContextGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
		sw.println("public RequestContext createRequestContext(String requestContextName) {");
		sw.indent();
		for (Entry<JClassType, JMethod> requestContextEntries : context.getRequestContextMethod()) {
			JClassType requestContext = requestContextEntries.getKey();
			JMethod method = requestContextEntries.getValue();
			JClassType requestFactory = method.getEnclosingType();
			context.logger.log(TreeLogger.TRACE, requestContext.getName() + "-" + requestFactory.getName());
			sw.println(String.format("if (requestContextName.equals(\"%s\")) {", requestContext.getQualifiedSourceName()));
			sw.indent();

			sw.println(String.format("%1$s rf = (%1$s) requestFactory.get(\"%1$s\");", requestFactory.getQualifiedSourceName()));
			sw.println("if (rf == null) return null;");
			sw.println(String.format("return rf.%s();", method.getName()));

			sw.outdent();
			sw.println("}");
		}

		sw.println("return null;");
		sw.outdent();
		sw.println("}");
	}
	/**
	 * Generate the method
	 * 
	 * @param context
	 * @param sw
	 * @throws UnableToCompleteException
	 */
	private void writeCreateRequestFactory(RequestContextGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
		sw.println("public RequestFactory createRequestFactory(String name) {");
		sw.indent();
		for (JClassType requestFactory : context.getRequestFactoryEntries()) {
			context.logger.log(TreeLogger.TRACE, requestFactory.getName());
			sw.println(String.format("if (name.equals(\"%s\")) {", requestFactory.getQualifiedSourceName()));
			sw.indent();
			
			sw.println(String.format("RequestFactory rf = GWT.create(%s.class);", requestFactory.getQualifiedSourceName()));
			sw.println("requestFactory.put(name,rf);");
			sw.println("return rf;");
			
			sw.outdent();
			sw.println("}");
		}
		
		sw.println("return null;");
		sw.outdent();
		sw.println("}");
	}
}