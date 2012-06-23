package com.sfeir.common.gwt.rebind.layout;

import java.io.PrintWriter;

import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sfeir.common.gwt.client.layout.AbstractLayoutPlace;
import com.sfeir.common.gwt.client.layout.NoLayout;
import com.sfeir.common.gwt.client.layout.UseLayout;

/**
 * Generate the filter for the LayoutActivityManager. The filter will transform a application Place to the Layout Place to display If the place has the annotation @UseLayout with
 * the layout Place to use, the filter return the given layout place Or if the place has the annotation @NoLayout, return directly the application Place. Otherwise return the
 * default Layout Place given with the configuration property "layout.defaultlayoutplace" :
 * 
 * in the gwt.xml, add the line : <set-configuration-property name="layout.defaultlayoutplace" value="com.sfeir.common.gwt.sample.protogwt.client.layout.LayoutPlace" />
 */
public class LayoutFilterGenerator extends Generator {
	/**
	 * The {@link TreeLogger} used to log messages.
	 */
	private TreeLogger logger = null;

	private TypeOracle typeOracle;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		this.logger = logger;
		typeOracle = context.getTypeOracle();
		try {
			JClassType abstractLayoutType = typeOracle.getType(AbstractLayoutPlace.class.getName());
			JClassType placeType = typeOracle.getType(Place.class.getName());

			String defaultlayoutplace = null;
			try {
				ConfigurationProperty property = context.getPropertyOracle().getConfigurationProperty("layout.defaultlayoutplace");
				defaultlayoutplace = property.getValues().get(0);
				JClassType defaultlayoutplaceType = typeOracle.findType(defaultlayoutplace);
				if (defaultlayoutplaceType == null) {
					logger.log(TreeLogger.WARN, "The layout default place '" + defaultlayoutplace + "' didn't exist");
					defaultlayoutplace = null;
				} else if (!defaultlayoutplaceType.isAssignableTo(abstractLayoutType)) {
					logger.log(TreeLogger.WARN, "The layout default place '" + defaultlayoutplace + "' didn't extend the abstract layout place " + abstractLayoutType.getName());
					defaultlayoutplace = null;
				} else {
					try {
						defaultlayoutplaceType.getConstructor(new JType[] { placeType });
					} catch (NotFoundException e) {
						logger.log(TreeLogger.WARN,
								"The layout default place '" + defaultlayoutplace + "' didn't have a constructor which take on parameter a place " + placeType.getName());
						defaultlayoutplace = null;
					}
				}
			} catch (Exception e) {
				defaultlayoutplace = null;
				logger.log(TreeLogger.WARN, "Exception occured when get the configuration property 'layout.defaultlayoutplace'", e);
			}
			JClassType type = typeOracle.getType(typeName);

			String packageName = type.getPackage().getName();
			String simpleName = type.getSimpleSourceName() + "_Generated";
			ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);
			PrintWriter printWriter = context.tryCreate(logger, composer.getCreatedPackage(), composer.getCreatedClassShortName());
			if (printWriter == null)
				return composer.getCreatedClassName();
			composer.addImplementedInterface(typeName);
			composer.setSuperclass("java.lang.Object");
			composer.addImport(Place.class.getName());

			SourceWriter writer = composer.createSourceWriter(context, printWriter);
			writer.indent();
			generateConstructor(writer, simpleName);
			writer.println("public Place filter(Place place) {");
			writer.indent();

			generateFactory(writer, type, defaultlayoutplace);
			writer.outdent();
			writer.println("}");
			writer.outdent();
			writer.commit(logger);

			logger.log(TreeLogger.TRACE, composer.getCreatedClassName());
			return composer.getCreatedClassName();
		} catch (Exception e) {
			logger.log(TreeLogger.ERROR, "Unable to generate the Layout Filter ", e);
			throw new UnableToCompleteException();
		}
		// Get the Showcase ContentWidget subtypes to examine

	}

	/**
	 * Generate source code for the default constructor. Create default constructor, call super(), and insert all key/value pairs from the resoruce bundle.
	 * 
	 * @param sourceWriter
	 *            Source writer to output source code
	 * @param className
	 */
	private void generateConstructor(SourceWriter sourceWriter, String className) {
		// init resource bundle
		// start constructor source generation
		sourceWriter.println("public " + className + "() { ");
		sourceWriter.indent();
		sourceWriter.println("super();");
		// end constructor source generation
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	/**
	 * Generate the factory
	 * 
	 * @param writer
	 * @throws UnableToCompleteException
	 */
	private void generateFactory(SourceWriter writer, JClassType interfaceType, String defaultlayoutplace) throws UnableToCompleteException {
		JClassType cwType = null;
		try {
			cwType = typeOracle.getType(Place.class.getName());

		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Cannot find Place classes", e);
			throw new UnableToCompleteException();
		}
		JClassType[] types = cwType.getSubtypes();
		writer.println("if (place != null) {");
		writer.indent();
		// Generate the source and raw source files
		for (JClassType type : types) {
			if (!type.isAbstract()) {
				writer.print("if (place instanceof ");
				writer.print(type.getParameterizedQualifiedSourceName());
				writer.println(") {");
				writer.indent();
				NoLayout noLayoutAnnotation = type.findAnnotationInTypeHierarchy(NoLayout.class);
				UseLayout annotation = type.findAnnotationInTypeHierarchy(UseLayout.class);
				if (annotation != null) {
					Class<? extends AbstractLayoutPlace> layoutClass = annotation.value();
					try {
						layoutClass.getDeclaredConstructor(Place.class);
					} catch (Exception e) {
						logger.log(TreeLogger.WARN, "The layout place '" + layoutClass.getName() + "' didn't have a constructor which take on parameter the place");
						writer.outdent();
						writer.println("}");
						continue;
					}
					writer.print("return new ");
					writer.print(layoutClass.getName());
					writer.println("(place);");
				} else if (noLayoutAnnotation != null) {
					writer.println("return place;");
				}
				writer.outdent();
				writer.println("}");
			} else {
				logger.log(TreeLogger.TRACE, "The place '" + type.getName() + "' is abstract, no added to the layout filter");
			}
		}
		writer.outdent();
		writer.println("}");
		if (interfaceType.isAnnotationPresent(UseLayout.class)) {
			UseLayout annotation = interfaceType.getAnnotation(UseLayout.class);
			writer.print("return new ");
			writer.print(annotation.value().getName());
			writer.println("(place);");
		} else if (defaultlayoutplace != null) {
			writer.print("return new ");
			writer.print(defaultlayoutplace);
			writer.println("(place);");
		} else {
			writer.println("return place;");
		}
	}

}