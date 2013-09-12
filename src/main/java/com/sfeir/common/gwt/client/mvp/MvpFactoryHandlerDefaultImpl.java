package com.sfeir.common.gwt.client.mvp;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class MvpFactoryHandlerDefaultImpl implements MvpFactoryHandler {
//	DialogBox dialogBox = getDialogBox();
	/* (non-Javadoc)
	 * @see com.sfeir.common.gwt.client.mvp.MvpFactoryHandler#startLoadsplitPt()
	 */
//	private DialogBox getDialogBox() {
//		if (dialogBox == null) {
//			dialogBox = new DialogBox(false, true);
//			dialogBox.setWidget(new Label("Loading ..."));
//		}
//		return dialogBox;
//	}
	@Override
	public void startLoadsplitPt(String splitPt) {
//		getDialogBox().center();
	}
	
	/* (non-Javadoc)
	 * @see com.sfeir.common.gwt.client.mvp.MvpFactoryHandler#endLoadsplitPt()
	 */
	@Override
	public void endLoadsplitPt(String splitPt) {
//		getDialogBox().hide();
	}
	
	/* (non-Javadoc)
	 * @see com.sfeir.common.gwt.client.mvp.MvpFactoryHandler#errorLoadsplitPt(java.lang.Exception)
	 */
	@Override
	public void errorLoadsplitPt(Throwable e, String splitPt) {
//		getDialogBox().hide();
		GWT.log("Error when loading the slipt pt : "+ splitPt, e);
		Window.alert("Error when loading the slipt pt : "+ splitPt + "\n" + e.getMessage());
	}
	
	/* (non-Javadoc)
	 * @see com.sfeir.common.gwt.client.mvp.MvpFactoryHandler#errorNoFactory(java.lang.String)
	 */
	@Override
	public void errorNoFactory(String className) {
		GWT.log("The class " + className + " can't be loaded because the split point isn't loaded, load an activity before");
		Window.alert("The class " + className + " can't be loaded because the split point isn't loaded, load an activity before");
	}
}