package com.sfeir.common.gwt.rebind.place;

import java.io.PrintWriter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sfeir.common.gwt.client.history.AbstractPlaceHistoryMapperExtend;
import com.sfeir.common.gwt.client.history.AbstractPlaceHistoryMapperExtend.PrefixAndToken;

/**
 * <p>
 * <span style="color:red">Experimental API: This class is still under rapid development, and is very likely to be deleted. Use it at your own risk. </span>
 * </p>
 * 
 * New implementation for automatically scan all PlaceTokenizer and add it Generates implementations of {@link com.google.gwt.place.shared.PlaceHistoryMapper PlaceHistoryMapper}.
 */
public class PlaceHistoryMapperGenerator extends Generator {
    private PlaceHistoryGeneratorContext context;

    @Override
    public String generate(TreeLogger logger, GeneratorContext generatorContext, String interfaceName) throws UnableToCompleteException {

	context = PlaceHistoryGeneratorContext.create(logger, generatorContext.getTypeOracle(), interfaceName);

	PrintWriter out = generatorContext.tryCreate(logger, context.packageName, context.implName);

	if (out != null) {
	    generateOnce(generatorContext, context, out);
	}

	return context.packageName + "." + context.implName;
    }

    private void generateOnce(GeneratorContext generatorContext, PlaceHistoryGeneratorContext context, PrintWriter out) throws UnableToCompleteException {

	TreeLogger logger = context.logger.branch(TreeLogger.DEBUG, String.format("Generating implementation of %s", context.interfaceType.getName()));
	ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(context.packageName, context.implName);

	f.setSuperclass(AbstractPlaceHistoryMapperExtend.class.getSimpleName());
	f.addImplementedInterface(context.interfaceType.getName());

	f.addImport(AbstractPlaceHistoryMapperExtend.class.getName());
	f.addImport(context.interfaceType.getQualifiedSourceName());

	f.addImport(AbstractPlaceHistoryMapperExtend.class.getCanonicalName());
	if (context.factoryType != null) {
	    f.addImport(context.factoryType.getQualifiedSourceName());
	}

	f.addImport(Place.class.getCanonicalName());
	f.addImport(PlaceTokenizer.class.getCanonicalName());
	f.addImport(PrefixAndToken.class.getCanonicalName());

	f.addImport(GWT.class.getCanonicalName());

	SourceWriter sw = f.createSourceWriter(generatorContext, out);
	sw.println();

	writeGetPrefixAndToken(context, sw);
	sw.println();

	writeGetTokenizer(context, sw);
	sw.println();

	sw.outdent();
	sw.println("}");
	generatorContext.commit(logger, out);
    }

    private void writeGetPrefixAndToken(PlaceHistoryGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
	sw.println("protected PrefixAndToken getPrefixAndToken(Place newPlace) {");
	sw.indent();
	for (JClassType placeType : context.getPlaceTypes()) {
	    String placeTypeName = placeType.getQualifiedSourceName();
	    String prefix = context.getPrefix(placeType);

	    sw.println("if (newPlace instanceof " + placeTypeName + ") {");
	    sw.indent();
	    sw.println(placeTypeName + " place = (" + placeTypeName + ") newPlace;");
	    boolean canBeIndexed = false;
	    String newPrefix = prefix;
	    if (prefix.charAt(0) == '!') {
		newPrefix = prefix.substring(1);
		canBeIndexed = true;
	    }
	    JMethod getter = context.getTokenizerGetter(prefix);
	    if (getter != null) {
		sw.println(String.format("return new PrefixAndToken(\"%s\", " + "factory.%s().getToken(place), %b);", escape(newPrefix), getter.getName(), canBeIndexed));
	    } else {
		sw.println(String.format("PlaceTokenizer<%s> t = GWT.create(%s.class);", placeTypeName, context.getTokenizerType(prefix).getQualifiedSourceName()));
		sw.println(String.format("return new PrefixAndToken(\"%s\", " + "t.getToken((%s) place), %b);", escape(newPrefix), placeTypeName, canBeIndexed));
	    }

	    sw.outdent();
	    sw.println("}");
	}

	sw.println("return null;");
	sw.outdent();
	sw.println("}");
    }

    private void writeGetTokenizer(PlaceHistoryGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
	sw.println("protected PlaceTokenizer<?> getTokenizer(String prefix) {");
	sw.indent();

	for (String prefix : context.getPrefixes()) {
	    JMethod getter = context.getTokenizerGetter(prefix);
	    String newPrefix = prefix;
	    if (prefix.charAt(0) == '!') {
		newPrefix = prefix.substring(1);
	    }
	    sw.println("if (\"" + escape(newPrefix) + "\".equals(prefix)) {");
	    sw.indent();

	    if (getter != null) {
		sw.println("return factory." + getter.getName() + "();");
	    } else {
		sw.println(String.format("return GWT.create(%s.class);", context.getTokenizerType(prefix).getQualifiedSourceName()));
	    }

	    sw.outdent();
	    sw.println("}");
	}

	sw.println("return null;");
	sw.outdent();
	sw.println("}");
	sw.outdent();
    }
}