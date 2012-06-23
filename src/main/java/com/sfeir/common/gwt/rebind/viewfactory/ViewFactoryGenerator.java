package com.sfeir.common.gwt.rebind.viewfactory;

import java.io.PrintWriter;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sfeir.common.gwt.client.mvp.View;
import com.sfeir.common.gwt.client.mvp.ViewFactory;

/**
 * This generator return the implementation of the ViewFactory with generation of the createView method
 * 
 * Generate the method View createView(String viewName) :
 * Scan all views, and search the implementation of the view.
 * 
 * Search for the view name suffixed with the given suffix given in the configuration (<set-configuration-property name="clientfactory.suffixviewimpl" value="Impl" />)
 * Fallback check with the suffix Impl, and then get all subclass implemented by the view and take the first value
 * 
 * Access to the factory by using the ClientFactory.getView();
 *
 */
@Deprecated
public class ViewFactoryGenerator extends Generator {

    @Override
    public String generate(TreeLogger logger, GeneratorContext generatorContext, String interfaceName) throws UnableToCompleteException {
	String implSuffix;
	try {
	    ConfigurationProperty property = generatorContext.getPropertyOracle().getConfigurationProperty("viewfactory.suffixviewimpl");
	    implSuffix = property.getValues().get(0);
	} catch (Exception e) {
	    implSuffix = "Impl";
	    //logger.log(TreeLogger.WARN, "Unable to find value for '" + PROPERTY_USER_AGENT_RUNTIME_WARNING + "'", e);
	}

	ViewGeneratorContext context = ViewGeneratorContext.create(logger, generatorContext.getTypeOracle(), implSuffix, interfaceName);

	PrintWriter out = generatorContext.tryCreate(logger, context.packageName, context.implName);

	if (out != null) {
	    generateOnce(generatorContext, context, out);
	}

	logger.log(TreeLogger.TRACE, context.packageName + "." + context.implName);
	return context.packageName + "." + context.implName;
    }

    private void generateOnce(GeneratorContext generatorContext, ViewGeneratorContext context, PrintWriter out) throws UnableToCompleteException {

	TreeLogger logger = context.logger.branch(TreeLogger.DEBUG, String.format("Generating implementation of %s", context.interfaceType.getName()));
	ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(context.packageName, context.implName);

	f.addImplementedInterface(ViewFactory.class.getSimpleName());

	f.addImport(View.class.getName());
	f.addImport(GWT.class.getCanonicalName());

	SourceWriter sw = f.createSourceWriter(generatorContext, out);
	sw.println();

	sw.println();

	writeCreateView(context, sw);
	sw.println();

	sw.outdent();
	sw.println("}");
	generatorContext.commit(logger, out);
    }

    /**
     * Generate the method
     * @param context
     * @param sw
     * @throws UnableToCompleteException
     */
    private void writeCreateView(ViewGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
	sw.println("public View createView(String viewInterface) {");
	sw.indent();

	for (Entry<JClassType, JClassType> placeActivity : context.getViewImpl()) {
	    JClassType viewInterface = placeActivity.getKey();
	    JClassType viewImpl = placeActivity.getValue();
	    context.logger.log(TreeLogger.TRACE, viewInterface.getName() + "-" + viewImpl.getName());
	    sw.println(String.format("if (viewInterface.equals(\"%s\")) {", viewInterface.getQualifiedSourceName()));
	    sw.indent();

	    sw.println(String.format("return GWT.create(%s.class);", viewImpl.getQualifiedSourceName()));

	    sw.outdent();
	    sw.println("}");
	}

	sw.println("return null;");
	sw.outdent();
	sw.println("}");
	sw.outdent();
    }
}