package com.sfeir.common.gwt.rebind.checker;

import java.io.PrintWriter;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Generate the filter for the LayoutActivityManager. The filter will transform a application Place to the Layout Place to display If the place has the annotation @UseLayout with
 * the layout Place to use, the filter return the given layout place Or if the place has the annotation @NoLayout, return directly the application Place. Otherwise return the
 * default Layout Place given with the configuration property "layout.defaultlayoutplace" :
 * 
 * in the gwt.xml, add the line : <set-configuration-property name="layout.defaultlayoutplace" value="com.sfeir.common.gwt.sample.protogwt.client.layout.LayoutPlace" />
 */
public class CheckerGenerator extends Generator {
	/**
	 * The {@link TreeLogger} used to log messages.
	 */
	private TypeOracle typeOracle;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		typeOracle = context.getTypeOracle();
		try {
			JClassType type = typeOracle.getType(typeName);
			
			String packageName = type.getPackage().getName();
			String simpleName = type.getSimpleSourceName() + "_Generated";
			ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);
			PrintWriter printWriter = context.tryCreate(logger, composer.getCreatedPackage(), composer.getCreatedClassShortName());
			if (printWriter == null)
				return composer.getCreatedClassName();
			composer.setSuperclass(typeName);
			
			SourceWriter writer = composer.createSourceWriter(context, printWriter);
			writer.indent();

			List<String> searchpackages = null;
			try {
				ConfigurationProperty property = context.getPropertyOracle().getConfigurationProperty("checker.searchpackage");
				searchpackages = property.getValues();
			} catch (Exception e) {
				searchpackages = null;
				logger.log(TreeLogger.WARN, "Exception occured when get the configuration property 'checker.searchpackage'", e);
			}
				int i = 0;
				if (searchpackages != null) {
					for (String searchpackage : searchpackages) {
						ClassFinder finder = new ClassFinder(typeOracle);
						StringTokenizer st = new StringTokenizer(searchpackage, ";");
						while (st.hasMoreTokens()) {
							String path = st.nextToken();
							Vector<String> subClasses = finder.findNotCompileGWTclass(path);
							for (String class1 : subClasses) {
								if (!class1.endsWith("Test")) {
									logger.log(Type.ERROR, class1 + " not found in GWT context, maybe this class has GWT compile error");
									writer.print(class1);
									writer.print(" a");
									writer.print(Integer.toString(i++));
									writer.println(";");
								}
							}
						}
					}
				}
				else {
					logger.log(Type.INFO, "Define the property checker.searchpackage in your .gwt.xml for check error in your project");
				}
				boolean hasError = i > 0;
				writer.outdent();
				writer.println("public boolean hasErrors() {");
				writer.indentln("return " + hasError + ";");
				writer.println("}");
				writer.commit(logger);
				
				logger.log(TreeLogger.TRACE, composer.getCreatedClassName());
			
			return composer.getCreatedClassName();
		} catch (Exception e) {
			logger.log(TreeLogger.ERROR, "Unable to generate the checker file ", e);
			throw new UnableToCompleteException();
		}
		// Get the Showcase ContentWidget subtypes to examine

	}
}