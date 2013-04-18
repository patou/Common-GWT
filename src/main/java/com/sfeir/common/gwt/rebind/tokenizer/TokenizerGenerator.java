package com.sfeir.common.gwt.rebind.tokenizer;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sfeir.common.gwt.client.place.AbstractPlaceTokenizer;
import com.sfeir.common.gwt.client.place.PlaceProperty;
import com.sfeir.common.gwt.client.place.Tokenizer;
import com.sfeir.common.gwt.client.place.Tokenizer.PropertyType;

/**
 * <p>
 * <span style="color:red">Experimental API: This class is still under rapid development, and is very likely to be deleted. Use it at your own risk. </span>
 * </p>
 * 
 * New implementation for automatically scan all PlaceTokenizer and add it Generates implementations of {@link com.google.gwt.place.shared.PlaceHistoryMapper PlaceHistoryMapper}.
 */
public class TokenizerGenerator extends Generator {
	private TokenizerGeneratorContext context;

	@Override
	public String generate(TreeLogger logger, GeneratorContext generatorContext, String interfaceName) throws UnableToCompleteException {

		context = TokenizerGeneratorContext.create(logger, generatorContext.getTypeOracle(), interfaceName);

		PrintWriter out = generatorContext.tryCreate(logger, context.packageName, context.implName);

		if (out != null) {
			generateOnce(generatorContext, context, out);
		}

		return context.packageName + "." + context.implName;
	}

	private void generateOnce(GeneratorContext generatorContext, TokenizerGeneratorContext context, PrintWriter out) throws UnableToCompleteException {

		TreeLogger logger = context.logger.branch(TreeLogger.DEBUG, String.format("Generating implementation of %s", context.interfaceType.getName()));
		ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(context.packageName, context.implName);

		f.setSuperclass(AbstractPlaceTokenizer.class.getSimpleName() + "<" + context.getPlace().getName() + ">");
		f.addImplementedInterface(context.interfaceType.getName());
		f.addImport(context.getPlace().getQualifiedSourceName());

		f.addImport(Tokenizer.class.getCanonicalName());
		f.addImport(PlaceProperty.class.getCanonicalName());
		f.addImport(PropertyType.class.getCanonicalName());
		f.addImport(context.interfaceType.getQualifiedSourceName());

		f.addImport(AbstractPlaceTokenizer.class.getCanonicalName());

		f.addImport(Place.class.getCanonicalName());

		f.addImport(GWT.class.getCanonicalName());
		f.addImport(Map.class.getCanonicalName());
		f.addImport(HashMap.class.getCanonicalName());
		f.addImport(Map.Entry.class.getCanonicalName());

		SourceWriter sw = f.createSourceWriter(generatorContext, out);
		sw.println();

		writeGetPlaceProperties(context, sw);
		sw.println();

		writeCreatePlaceWithProperties(context, sw);
		sw.println();

		writeGetProperties(context, sw);
		sw.println();

		writeGetPlaceType(context, sw);
		sw.outdent();
		sw.println("}");
		generatorContext.commit(logger, out);
	}

	private void writeGetPlaceProperties(TokenizerGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
		String placeName = context.getPlace().getSimpleSourceName();
		sw.println("public void initPlaceProperties(%s place, Map<String, String> properties) {", placeName);
		sw.indent();
		sw.println("if (parent != null) parent.initPlaceProperties(place, properties);");
		sw.println("%s defaultPlace = createPlace();", placeName);
		for (PlaceProperty property : context.getPlaceProperties()) {
			sw.println(String.format("if (mustAddProperty(place.%1$s, defaultPlace.%1$s))", property.getName()));
			// sw.indent();
			sw.indentln("properties.put(\"%1$s\", %2$s(place.%1$s));", property.getName(), getToStringFunctionName(property.getType()));
			// sw.outdent();
		}
		sw.outdent();
		sw.println("}");
	}

	private void writeGetProperties(TokenizerGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
		sw.println("public void buildProperties(Map<String, PlaceProperty> properties) {");
		sw.indent();
		sw.println("if (parent != null) parent.buildProperties(properties);");
		for (PlaceProperty property : context.getPlaceProperties()) {
			sw.println("properties.put(\"%1$s\", new PlaceProperty(%2$b, %3$b, \"%1$s\", PropertyType.%4$s, \"%5$s\", \"%6$s\", \"%7$s\"));", property.getName(),
					property.isRequired(), property.isDefaultToken(), property.getType().name(), property.getTypeName(), property.getMessage(), property.getAlias());
		}
		sw.outdent();
		sw.println("}");
	}

	private void writeCreatePlaceWithProperties(TokenizerGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
		String placeName = context.getPlace().getSimpleSourceName();
		sw.println("public %s createPlace() {", placeName);
		sw.indentln("return GWT.create(%1$s.class);", placeName);
		sw.println("}");
		sw.println();
		sw.println("public void initPlaceWithProperties(%s place, Map<String, String> properties) {", placeName);
		sw.indent();
		sw.println("if (parent != null) parent.initPlaceWithProperties(place, properties);");
		sw.println("for (Entry<String,String> property : properties.entrySet()) {");
		sw.indent();
		for (PlaceProperty property : context.getPlaceProperties()) {
			if (isNullOrEmpty(property.getAlias())) {
				sw.println("if (property.getKey().equalsIgnoreCase(\"%1$s\"))", property.getName());
			}
			else {
				sw.println("if (property.getKey().equalsIgnoreCase(\"%1$s\") || property.getKey().equalsIgnoreCase(\"%2$s\"))", property.getName(), property.getAlias());
			}
			if (property.getType() == PropertyType.ENUM) {
				sw.indentln("place.%1$s = parseEnum(property.getValue(), %2$s.class);", property.getName(), property.getTypeName());
			}
			else
				sw.indentln("place.%1$s = %2$s(properties.get(\"%1$s\"));", property.getName(), getParseFunctionName(property.getType()));
		}
		sw.outdent();
		sw.println("}");
		sw.outdent();
		sw.println("}");
	}

	private void writeGetPlaceType(TokenizerGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
		JClassType place = context.getPlace();
		String placeName = place.getSimpleSourceName();
		sw.println("public Class<%s> getPlaceType() {", placeName);
		sw.indentln("return %s.class;", placeName);
		sw.println("}");

		sw.println("public Tokenizer<? super %s> getParentTokenizer() {", placeName);
		JClassType superClass = context.getParentPlaceTokenizer();
		if (superClass == null/* || superClass.getSimpleSourceName().equals(Place.class.getName())*/) {
			sw.indentln("return null;");
		}
		else {
			sw.indentln("return GWT.create(%s.class);", superClass.getParameterizedQualifiedSourceName());
		}
		sw.println("}");
		
	}

	private String getParseFunctionName(PropertyType type) {
		switch (type) {
		case INTEGER:
			return "parseInteger";
		case LONG:
			return "parseLong";
		case FLOAT:
			return "parseFloat";
		case DOUBLE:
			return "parseDouble";
		case DATE:
			return "parseDate";
		case BOOLEAN:
			return "parseBoolean";
		case LISTSTRING:
			return "parseListString";
		case STRING:
			return "parseString";
		case ENUM:
			return "parseEnum";
		}
		return "";
	}

	private String getToStringFunctionName(PropertyType type) {
        switch (type) {
            case DATE:
                return "toStringDate";
            case LISTSTRING:
                return "toStringListString";
            default:
                return "toString";
        }
	}
}