package com.sfeir.common.gwt.rebind.i18nfactory;

import java.io.PrintWriter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.i18n.client.LocalizableResource;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sfeir.common.gwt.client.i18n.I18nFactory;

/**
 * This generator return the implementation of the I18nFactory with
 * generation of the createI18n method
 * 
 * Generate the method LocalizableResource createI18n(String i18nClass) : 
 * Find all I18n interface (Messages, Constants, ConstantsWithLookup)
 * 
 * 
 * Access to the factory by using the ClientFactory.getMessage();
 */
public class I18nFactoryGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger,
			GeneratorContext generatorContext, String interfaceName)
			throws UnableToCompleteException {
		I18nGeneratorContext context = I18nGeneratorContext.create(logger,
				generatorContext.getTypeOracle(), interfaceName);

		PrintWriter out = generatorContext.tryCreate(logger,
				context.packageName, context.implName);

		if (out != null) {
			generateOnce(generatorContext, context, out);
		}

		logger.log(TreeLogger.TRACE, context.packageName + "."
				+ context.implName);
		return context.packageName + "." + context.implName;
	}

	private void generateOnce(GeneratorContext generatorContext,
			I18nGeneratorContext context, PrintWriter out)
			throws UnableToCompleteException {

		TreeLogger logger = context.logger.branch(TreeLogger.DEBUG, String
				.format("Generating implementation of %s",
						context.interfaceType.getName()));
		ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(
				context.packageName, context.implName);

		f.addImplementedInterface(I18nFactory.class.getSimpleName());

		f.addImport(I18nFactory.class.getCanonicalName());
		f.addImport(LocalizableResource.class.getCanonicalName());
		f.addImport(GWT.class.getCanonicalName());

		SourceWriter sw = f.createSourceWriter(generatorContext, out);
		sw.println();

		sw.println();

		writeCreateI18n(context, sw);
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
	private void writeCreateI18n(I18nGeneratorContext context, SourceWriter sw)
			throws UnableToCompleteException {
		sw.println("public LocalizableResource createI18n(String i18nClass) {");
		sw.indent();

		for (JClassType localizableResource : context.getlocalizableResources()) {
			context.logger.log(TreeLogger.TRACE, localizableResource.getName());
			sw.println(String.format("if (i18nClass.equals(\"%s\")) {",
					localizableResource.getQualifiedSourceName()));
			sw.indent();

			sw.println(String.format("return GWT.create(%s.class);",
					localizableResource.getQualifiedSourceName()));

			sw.outdent();
			sw.println("}");
		}

		sw.println("return null;");
		sw.outdent();
		sw.println("}");
		sw.outdent();
	}
}