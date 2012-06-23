package com.sfeir.common.gwt.client.mvp;

public interface MvpFactoryHandler {

	public abstract void startLoadsplitPt(String splitPt);

	public abstract void endLoadsplitPt(String splitPt);

	public abstract void errorLoadsplitPt(Throwable e, String splitPt);

	public abstract void errorNoFactory(String className);

}