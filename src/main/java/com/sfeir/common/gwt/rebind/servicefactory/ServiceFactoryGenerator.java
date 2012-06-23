package com.sfeir.common.gwt.rebind.servicefactory;

import java.io.PrintWriter;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sfeir.common.gwt.client.mvp.ServiceFactory;

/**
 * This generator return the implementation of the ServiceFactory with generation of the createService method
 * 
 * Generate the method Object createService(String serviceName) :
 * 
 * Search for the all GWT-RPC services, check if there are no problems with it, and generate the factory
 * 
 * Access to the factory by using the ClientFactory.getService();
 */
public class ServiceFactoryGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext generatorContext, String interfaceName) throws UnableToCompleteException {
		ServiceGeneratorContext context = ServiceGeneratorContext.create(logger, generatorContext.getTypeOracle(), interfaceName);

		PrintWriter out = generatorContext.tryCreate(logger, context.packageName, context.implName);

		if (out != null) {
			generateOnce(generatorContext, context, out);
		}

		logger.log(TreeLogger.TRACE, context.packageName + "." + context.implName);
		return context.packageName + "." + context.implName;
	}

	private void generateOnce(GeneratorContext generatorContext, ServiceGeneratorContext context, PrintWriter out) throws UnableToCompleteException {

		TreeLogger logger = context.logger.branch(TreeLogger.DEBUG, String.format("Generating implementation of %s", context.interfaceType.getName()));
		ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(context.packageName, context.implName);

		f.addImplementedInterface(ServiceFactory.class.getSimpleName());

		f.addImport(ServiceFactory.class.getCanonicalName());
		f.addImport(GWT.class.getCanonicalName());

		SourceWriter sw = f.createSourceWriter(generatorContext, out);
		sw.println();

		sw.println();

		writeCreateService(context, sw);
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
	private void writeCreateService(ServiceGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
		sw.println("public Object createService(String serviceName) {");
		sw.indent();

		for (Entry<JClassType, JClassType> services : context.getServicesAsync()) {
			JClassType service = services.getKey();
			JClassType async = services.getValue();
			context.logger.log(TreeLogger.TRACE, async.getName() + "-" + service.getName());
			sw.println(String.format("if (serviceName.equals(\"%s\") || serviceName.equals(\"%s\")) {", async.getQualifiedSourceName(), service.getQualifiedSourceName()));
			sw.indent();

			sw.println(String.format("return GWT.create(%s.class);", service.getQualifiedSourceName()));

			sw.outdent();
			sw.println("}");
		}

		sw.println("return null;");
		sw.outdent();
		sw.println("}");
		sw.outdent();
	}
}