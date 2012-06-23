package com.sfeir.common.gwt.rebind.activity;

import java.io.PrintWriter;
import java.util.Map.Entry;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sfeir.common.gwt.client.mvp.ActivityPresenter;
import com.sfeir.common.gwt.client.mvp.AppActivityMapper;

/**
 * <p>
 * <span style="color:red">Experimental API: This class is still under rapid development, and is very likely to be deleted. Use it at your own risk. </span>
 * </p>
 * 
 * Automatically generate the ActivityMapper with scanning Activity and found the corresponding place
 * 
 * @see ActivityGeneratorContext
 * 
 */
@Deprecated
public class ActivityMapperGenerator extends Generator {
	private ActivityGeneratorContext context;

	@Override
	public String generate(TreeLogger logger, GeneratorContext generatorContext, String interfaceName) throws UnableToCompleteException {

		context = ActivityGeneratorContext.create(logger, generatorContext.getTypeOracle(), interfaceName);
		PrintWriter out = generatorContext.tryCreate(logger, context.packageName, context.implName);

		if (out != null) {
			generateOnce(generatorContext, context, out);
		}

		return context.packageName + "." + context.implName;
	}

	private void generateOnce(GeneratorContext generatorContext, ActivityGeneratorContext context, PrintWriter out) throws UnableToCompleteException {

		TreeLogger logger = context.logger.branch(TreeLogger.DEBUG, String.format("Generating implementation of %s", context.interfaceType.getName()));
		ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(context.packageName, context.implName);

		f.setSuperclass(AppActivityMapper.class.getSimpleName());

		f.addImport(AppActivityMapper.class.getName());
		f.addImport(ActivityPresenter.class.getName());
		f.addImport(Activity.class.getName());

		f.addImport(Place.class.getCanonicalName());

		f.addImport(GWT.class.getCanonicalName());
		
		SourceWriter sw = f.createSourceWriter(generatorContext, out);
		sw.println();

		sw.println();

		writeCreateActivity(context, sw);
		sw.println();

		sw.outdent();
		sw.println("}");
		generatorContext.commit(logger, out);
	}

	private void writeCreateActivity(ActivityGeneratorContext context, SourceWriter sw) throws UnableToCompleteException {
		sw.println("protected Activity createActivity(Place place) {");
		sw.indent();

		for (Entry<JClassType, JClassType> placeActivity : context.getActivities()) {
			JClassType place = placeActivity.getKey();
			JClassType activity = placeActivity.getValue();
			sw.println(String.format("if (place instanceof %s) {", place.getQualifiedSourceName()));
			sw.indent();

			sw.println(String.format("return GWT.create(%s.class);", activity.getQualifiedSourceName()));

			sw.outdent();
			sw.println("}");
		}
		sw.println("return null;");
		sw.outdent();
		sw.println("}");
		sw.outdent();
	}
}