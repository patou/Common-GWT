package com.sfeir.common.gwt.rebind.dialogbox;

import java.io.PrintWriter;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sfeir.common.gwt.client.place.Dialog;
import com.sfeir.common.gwt.client.place.DialogBoxPlaceInfo;

/**
 * <p>
 * <span style="color:red">Experimental API: This class is still under rapid development, and is very likely to be deleted. Use it at your own risk. </span>
 * </p>
 * 
 * New implementation for automatically scan all PlaceTokenizer and add it Generates implementations of {@link com.google.gwt.place.shared.PlaceHistoryMapper PlaceHistoryMapper}.
 */
public class DialogBoxFactoryGenerator extends Generator {
	private DialogBoxFactoryGeneratorContext context;

	@Override
	public String generate(TreeLogger logger, GeneratorContext generatorContext, String interfaceName) throws UnableToCompleteException {

		context = DialogBoxFactoryGeneratorContext.create(logger, generatorContext.getTypeOracle(), interfaceName);

		PrintWriter out = generatorContext.tryCreate(logger, context.packageName, context.implName);

		if (out != null) {
			generateOnce(generatorContext, context, out);
		}

		return context.packageName + "." + context.implName;
	}

	private void generateOnce(GeneratorContext generatorContext, DialogBoxFactoryGeneratorContext context, PrintWriter out) throws UnableToCompleteException {

		TreeLogger logger = context.logger.branch(TreeLogger.DEBUG, String.format("Generating implementation of %s", context.interfaceType.getName()));
		ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(context.packageName, context.implName);

		f.addImplementedInterface(context.interfaceType.getName());

		f.addImport(context.interfaceType.getQualifiedSourceName());
		f.addImport(Place.class.getCanonicalName());
		f.addImport(DialogBoxPlaceInfo.class.getCanonicalName());

		f.addImport(GWT.class.getCanonicalName());

		SourceWriter sw = f.createSourceWriter(generatorContext, out);
		sw.println();

		writeGetPrefixAndToken(context, sw);
		sw.println();

		sw.outdent();
		sw.println("}");
		generatorContext.commit(logger, out);
	}

	private void writeGetPrefixAndToken(DialogBoxFactoryGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
		sw.println("public DialogBoxPlaceInfo getDialogBoxInfo(Place place) {");
		sw.indent();
		for (Entry<JClassType, Dialog> info : context.getDialogBoxPlaceInfos().entrySet()) {
			String placeTypeName = info.getKey().getQualifiedSourceName();
			Dialog annotation = info.getValue();

			sw.println("if (newPlace instanceof " + placeTypeName + ") {");
			sw.indent();
			sw.println(String.format("return new DialogBoxPlaceInfo(\"%s\", \"%s\", \"%s\", %b);", escape(annotation.width()), escape(annotation.height()),
					escape(annotation.caption()), annotation.isModal()));
			sw.outdent();
			sw.println("}");
		}

		sw.println("return null;");
		sw.outdent();
		sw.println("}");
	}
}