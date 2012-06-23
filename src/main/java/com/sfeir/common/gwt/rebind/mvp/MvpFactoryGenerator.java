package com.sfeir.common.gwt.rebind.mvp;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.base.Joiner;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.sfeir.common.gwt.client.mvp.ActivityPresenter;
import com.sfeir.common.gwt.client.mvp.AppActivityMapper;
import com.sfeir.common.gwt.client.mvp.MvpFactory;
import com.sfeir.common.gwt.client.mvp.MvpFactoryAbstract;
import com.sfeir.common.gwt.client.mvp.View;

/**
 * This generator return the implementation of the ViewFactory with generation of the createView method
 * 
 * Generate the method View createView(String viewName) : Scan all views, and search the implementation of the view.
 * 
 * Search for the view name suffixed with the given suffix given in the configuration (<set-configuration-property name="clientfactory.suffixviewimpl" value="Impl" />) Fallback
 * check with the suffix Impl, and then get all subclass implemented by the view and take the first value
 * 
 * Access to the factory by using the ClientFactory.getView();
 * 
 */
public class MvpFactoryGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext generatorContext, String interfaceName) throws UnableToCompleteException {
		String implSuffix = getViewImplSuffixConfiguration(generatorContext);
		List<String> sliptPts = getSpliPtsConfiguration(generatorContext);

		TypeOracle typeOracle = generatorContext.getTypeOracle();

		ViewGeneratorContext contextView = ViewGeneratorContext.create(logger, typeOracle, implSuffix);
		ActivityGeneratorContext contextActivity = ActivityGeneratorContext.create(logger, typeOracle);
		MvpFactoryGeneratorContext contextMvp = new MvpFactoryGeneratorContext();
		contextMvp.create(logger, sliptPts, contextActivity.getActivities(), contextView.getViewImpl());

		JClassType interfaceType = typeOracle.findType(interfaceName);
		if (interfaceType == null) {
			logger.log(TreeLogger.ERROR, "Could not find requested typeName: " + interfaceName);
			throw new UnableToCompleteException();
		}
		String implName = interfaceType.getName().replace(".", "_") + "GeneratedImpl";

		String packageName = interfaceType.getPackage().getName();
		PrintWriter out = generatorContext.tryCreate(logger, packageName, implName);

		if (out != null) {
			generateFactory(logger, generatorContext, contextMvp, packageName, implName, interfaceType, sliptPts, out);
		}

		logger.log(TreeLogger.TRACE, packageName + "." + implName);
		return packageName + "." + implName;
	}

	/**
	 * Get View Impl Suffix Configuration
	 * @param generatorContext
	 * @return
	 */
	private String getViewImplSuffixConfiguration(GeneratorContext generatorContext) {
		String implSuffix;
		try {
			ConfigurationProperty property = generatorContext.getPropertyOracle().getConfigurationProperty("viewfactory.suffixviewimpl");
			implSuffix = property.getValues().get(0);
		} catch (Exception e) {
			implSuffix = "Impl";
			// logger.log(TreeLogger.WARN, "Unable to find value for '" + PROPERTY_USER_AGENT_RUNTIME_WARNING + "'", e);
		}
		return implSuffix;
	}

	/**
	 * Get Java configuration
	 * 
	 * @param generatorContext
	 * @return
	 */
	private List<String> getSpliPtsConfiguration(GeneratorContext generatorContext) {
		List<String> splitPts;
		try {
			ConfigurationProperty property = generatorContext.getPropertyOracle().getConfigurationProperty("mvpfactory.splitpts");
			splitPts = property.getValues();
		} catch (Exception e) {
			splitPts = newArrayList();
		}
		return splitPts;
	}

	/**
	 * Generate the global activity
	 * 
	 * @param logger The Tree logger
	 * @param generatorContext The Generator Context
	 * @param contextMvp The MVP Context
	 * @param packageName Factory Package
	 * @param implName Factory class name
	 * @param interfaceType Interface JClassType
	 * @param sliptPts List of all split pts
	 * @param out The PrintWriter
	 * @throws UnableToCompleteException
	 */
	private void generateFactory(TreeLogger logger, GeneratorContext generatorContext, MvpFactoryGeneratorContext contextMvp, String packageName, String implName,
			JClassType interfaceType, List<String> sliptPts, PrintWriter out) throws UnableToCompleteException {

		TreeLogger log = logger.branch(TreeLogger.DEBUG, String.format("Generating implementation of %s", interfaceType.getName()));
		ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(packageName, implName);

		f.setSuperclass(MvpFactoryAbstract.class.getSimpleName());
		
		f.addImport(View.class.getName());
		f.addImport(MvpFactoryAbstract.class.getName());
		f.addImport(GWT.class.getCanonicalName());
		f.addImport(AppActivityMapper.class.getName());
		f.addImport(ActivityPresenter.class.getName());
		f.addImport(Activity.class.getName());
		f.addImport(Place.class.getCanonicalName());
		f.addImport(RunAsyncCallback.class.getName());
		f.addImport(AsyncCallback.class.getName());

		SourceWriter sw = f.createSourceWriter(generatorContext, out);
		sw.println();
		sw.println("public %s() {", implName);
		sw.indent();
		String listSplitPts = Joiner.on("\",\"").skipNulls().join(sliptPts);
		sw.println("super(new String[]{%s}, new %s());", isNullOrEmpty(listSplitPts) ? "" : "\"" + listSplitPts + "\"",
				generateMvpFactory(log, generatorContext, null, contextMvp.getActivityMap(null), contextMvp.getViewMap(null)));
		sw.outdent();
		sw.println("}");

		sw.println("protected void loadFactory(String splitPt, final AsyncCallback<MvpFactory> callback){");
		{
			sw.indent();
			for (String split : contextMvp.getSplitPts()) {
				String mvpFactory = generateMvpFactory(log, generatorContext, split, contextMvp.getActivityMap(split), contextMvp.getViewMap(split));
				sw.println("if (\"%s\".equals(splitPt)) {", split);
				{
					sw.indent();
					sw.println("GWT.runAsync(%s.class, new RunAsyncCallback() {", mvpFactory);
					{
						sw.indent();
						sw.println("public void onFailure(Throwable caught) {");
						{
							sw.indentln("callback.onFailure(caught);");
						}
						sw.println("}");
						sw.println("public void onSuccess() {");
						{
							sw.indentln("callback.onSuccess(new %s());", mvpFactory);
						}
						sw.println("}");
						sw.outdent();
					}
					sw.println("});");
					sw.outdent();
				}
				sw.println("}");
			}
			sw.outdent();
		}
		sw.println("}");
		sw.outdent();
		sw.println("}");
		generatorContext.commit(log, out);
	}

	/**
	 * Generate the MvpFactory (with the createActivity and createView)
	 * 
	 * @param logger The tree logger
	 * @param context The generator context
	 * @param splitPt The split pt (or Null for the default factory)
	 * @param listActivities List of Activities for the split pt
	 * @param listViews List of view for the split pt
	 * @return
	 */
	private String generateMvpFactory(TreeLogger logger, GeneratorContext context, String splitPt, Collection<Entry<JClassType, JClassType>> listActivities,
			Collection<Entry<JClassType, JClassType>> listViews) {
		String packageName = isNullOrEmpty(splitPt) ? MvpFactory.class.getPackage().getName() : splitPt;
		String implName = "SplitMvpFactoryGenerated";
		PrintWriter out = context.tryCreate(logger, packageName, implName);
		TreeLogger log = logger.branch(TreeLogger.DEBUG, String.format("Generating implementation of %s", packageName + "." + implName));
		if (out != null) {
			ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(packageName, implName);

			f.addImplementedInterface(MvpFactory.class.getSimpleName());

			f.addImport(View.class.getName());
			f.addImport(MvpFactory.class.getName());
			f.addImport(GWT.class.getCanonicalName());
			f.addImport(AppActivityMapper.class.getName());
			f.addImport(ActivityPresenter.class.getName());
			f.addImport(Activity.class.getName());
			f.addImport(Place.class.getCanonicalName());
			f.setJavaDocCommentForClass(String.format("Split Pt : %s", splitPt));
			SourceWriter sw = f.createSourceWriter(context, out);
			sw.println();
			sw.println();
			writeCreateActivity(log, listActivities, sw);
			sw.println();

			writeCreateView(log, listViews, sw);
			sw.println();
			sw.outdent();
			sw.println("}");

			context.commit(log, out);
		}
		return packageName + "." + implName;
	}


	/**
	 * Generate the method createView
	 * 
	 * @param logger The tree logger
	 * @param contextViewImpl list of view
	 * @param sw SourceWriter
	 */
	private void writeCreateView(TreeLogger logger, Collection<Entry<JClassType, JClassType>> contextViewImpl, SourceWriter sw) {
		sw.println("public View createView(String viewInterface) {");
		sw.indent();

		for (Entry<JClassType, JClassType> placeActivity : contextViewImpl) {
			JClassType viewInterface = placeActivity.getKey();
			JClassType viewImpl = placeActivity.getValue();
			logger.log(TreeLogger.TRACE, viewInterface.getName() + "-" + viewImpl.getName());
			sw.println("if (viewInterface.equals(\"%s\")) {", viewInterface.getQualifiedSourceName());
			sw.indent();

			sw.println("return GWT.create(%s.class);", viewImpl.getQualifiedSourceName());

			sw.outdent();
			sw.println("}");
		}

		sw.println("return null;");
		sw.outdent();
		sw.println("}");
	}

	/** Generate the method createActivity
	 * 
	 * @param logger The Tree logger
	 * @param contextActivities The list of activity
	 * @param sw The Source Writer
	 */
	private void writeCreateActivity(TreeLogger logger, Collection<Entry<JClassType, JClassType>> contextActivities, SourceWriter sw) {
		sw.println("public Activity createActivity(Place place) {");
		sw.indent();

		for (Entry<JClassType, JClassType> placeActivity : contextActivities) {
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
	}
}