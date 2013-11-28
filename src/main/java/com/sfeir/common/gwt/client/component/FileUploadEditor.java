package com.sfeir.common.gwt.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Classe qui affiche un bouton de téléchargement d'un fichier et retourne le nom du fichier
 *
 *
 * Pour déactiver le drop d'un fichier dans un autre endroit que le fileupload, ajouter cela dans le EntryPoint :
 *
 * <pre>
 *   public native void cancelDropWindow() /*-{
 * $doc.body.addEventListener("drop",function(e){
 * e = e || event;
 * if (e.target.name != "file")
 * e.preventDefault();
 * },false);
 * $doc.body.addEventListener("dragover",function(e){
 * e = e || event;
 * if (e.target.name != "file")
 * e.preventDefault();
 * },false);
 * }-*&#47;;
 * </pre>
 * Attention à bien définir la condition pour faire le preventDefault ou pas ! (ici on considère que le name de l'input file est "file"
 * User: psaintsteban
 * Date: 06/11/13
 */
public class FileUploadEditor extends Composite implements LeafValueEditor<String> {

  private HandlerRegistration downloadDisabledHandler;

  interface Binder extends UiBinder<Widget, FileUploadEditor> {}

  private static Binder uiBinder = GWT.create(Binder.class);
  @UiField
  FormPanel formDrop;
  @UiField
  FileUpload uploadDrop;
  @UiField
  Loading loading;
  @UiField
  Anchor download;
  @UiField
  DivElement dropZone;

  private String fileName;
  private String downloadUrl;
  private String downloadText;


  public FileUploadEditor() {
    initWidget(uiBinder.createAndBindUi(this));
    uploadDrop.addBitlessDomHandler(new DropHandler() {
      @Override
      public void onDrop(DropEvent event) {
        disabledDropStyle();
      }
    }, DropEvent.getType()
    );
    uploadDrop.addBitlessDomHandler(new DragLeaveHandler() {
      @Override
      public void onDragLeave(DragLeaveEvent event) {
        disabledDropStyle();
      }
    }, DragLeaveEvent.getType()
    );
    uploadDrop.addBitlessDomHandler(new DragEnterHandler() {
      @Override
      public void onDragEnter(DragEnterEvent event) {
        enableDropStyle();
      }
    }, DragEnterEvent.getType()
    );
  }

  @UiHandler("uploadDrop")
  public void onUploadDropChange(ChangeEvent e) {
    formDrop.submit();
  }

  @UiHandler("formDrop")
  public void onFormDropComplete(FormPanel.SubmitCompleteEvent e) {
    loading.setVisible(false);
    UIObject.setVisible(dropZone, true);
    formDrop.reset();
    disabledDropStyle();
    String fileName = e.getResults().trim();
    if (fileName.contains("error:")) {
      Info.display(fileName.substring(fileName.indexOf("error:") + 6));
    } else if (fileName.contains("413 Request Entity Too Large")) { //
      Info.display("Le fichier est trop gros, selectionez un fichier de taille plus petite");
    } else if (fileName.contains("</")) { //La page contiens de l'HTML
      Info.display("Unexpected error");
    } else {
      initDownloadUrl(fileName);
    }
  }

  @UiHandler("formDrop")
  public void onFormDropSubmit(FormPanel.SubmitEvent e) {
    loading.setVisible(true);
    UIObject.setVisible(dropZone, false);
    initDownloadUrl(null);
  }

  private void enableDropStyle() {
    dropZone.getStyle().setOpacity(1.0);
    dropZone.getStyle().setBorderStyle(Style.BorderStyle.SOLID);
    dropZone.getStyle().setCursor(Style.Cursor.MOVE);
  }

  private void disabledDropStyle() {
    dropZone.getStyle().setOpacity(0.5);
    dropZone.getStyle().setBorderStyle(Style.BorderStyle.DASHED);
    dropZone.getStyle().setCursor(Style.Cursor.DEFAULT);
  }

  @Override
  public void setValue(String value) {
    fileName = isNullOrEmpty(value) ? null : value;
    initDownloadUrl(fileName);
  }

  private void initDownloadUrl(String fileName) {
    if (!isNullOrEmpty(fileName)) {
      download.setText(downloadText);
      download.setHref(downloadUrl + fileName);
      download.setVisible(true);
    } else {
      download.setText("");
      download.setHref("");
      download.setVisible(false);
    }
    formDrop.reset();
    this.fileName = fileName;
  }

  @Override
  public String getValue() {
    return fileName;
  }

  public void setUploadUrl(String action) {
    formDrop.setAction(action);
  }

  public String getUploadUrl() {
    return formDrop.getAction();
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getDownloadText() {
    return downloadText;
  }

  public void setDownloadText(String downloadText) {
    this.downloadText = downloadText;
    if (!isNullOrEmpty(download.getText())) {
      download.setText(downloadText);
    }
  }

  public void setReadOnly(Boolean readOnly) {
    UIObject.setVisible(dropZone, !readOnly);
  }
}