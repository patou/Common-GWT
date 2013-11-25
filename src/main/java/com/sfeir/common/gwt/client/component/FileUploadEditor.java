package com.groupemre.simba2.ihm.client.ui.components.editors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.groupemre.simba2.dto.utilisateur.TypeFonction;
import com.groupemre.socle.ihm.client.ui.components.Loading;
import com.groupemre.socle.ihm.client.ui.components.SocleInfo;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitEvent;
import com.sencha.gxt.widget.core.client.form.FormPanel;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.groupemre.simba2.ihm.client.application.Util.checkRight;

/**
 * Classe qui affiche un bouton de téléchargement d'un fichier et retourne le nom du fichier
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
  FocusPanel dropZone;

  private String fileName;
  private String downloadUrl;
  private String downloadText;


  public FileUploadEditor() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("uploadDrop")
  public void onUploadDropChange(ChangeEvent e) {
    formDrop.submit();
  }

  @UiHandler("formDrop")
  public void onFormDropComplete(SubmitCompleteEvent e) {
    loading.setVisible(false);
    dropZone.setVisible(true);
    formDrop.reset();
    disabledDropStyle();
    String fileName = e.getResults().trim();
    if (fileName.contains("error:")) {
      SocleInfo.display(SocleInfo.MessageType.ERROR, fileName.substring(fileName.indexOf("error:")+6));
    }
    else {
      initDownloadUrl(fileName);
    }
  }

  @UiHandler("formDrop")
  public void onFormDropSubmit(SubmitEvent e) {
    loading.setVisible(true);
    dropZone.setVisible(false);
    initDownloadUrl(null);
  }

  @UiHandler("dropZone")
  public void onDropEnterZone(DragEnterEvent e) {
    enableDropStyle();
  }

  @UiHandler("dropZone")
  public void onDropOverZone(DragOverEvent e) {
    enableDropStyle();
  }

  @UiHandler("dropZone")
  public void onDropEnterZone(DragEndEvent e) {
    disabledDropStyle();
  }

  @UiHandler("dropZone")
  public void onDropLeaveZone(DragLeaveEvent e) {
    disabledDropStyle();
  }

  @UiHandler("dropZone")
  public void onDropZone(DropEvent e) {
    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        formDrop.submit();
        disabledDropStyle();
      }
    });
  }

  private void enableDropStyle() {
    dropZone.getElement().getStyle().setOpacity(1.0);
    dropZone.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
    dropZone.getElement().getStyle().setCursor(Style.Cursor.MOVE);
  }

  private void disabledDropStyle() {
    dropZone.getElement().getStyle().setOpacity(0.5);
    dropZone.getElement().getStyle().setBorderStyle(Style.BorderStyle.DASHED);
    dropZone.getElement().getStyle().setCursor(Style.Cursor.DEFAULT);
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

  public void initRight(TypeFonction uploadRight, TypeFonction downloadRight) {
    setUploadRight(uploadRight);
    setDownloadRight(downloadRight);
  }

  public void setDownloadRight(TypeFonction downloadRight) {
    if (checkRight(downloadRight)) {
      if (downloadDisabledHandler != null) {
        downloadDisabledHandler.removeHandler();
        downloadDisabledHandler = null;
      }
    }
    else if (downloadDisabledHandler == null) {
      downloadDisabledHandler = download.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          event.preventDefault();
          event.stopPropagation();
          SocleInfo.display(SocleInfo.MessageType.WARNING, "Vous n'avez pas les droits pour télécharger ce fichier");
        }
      });
    }
  }

  public void setUploadRight(TypeFonction uploadRight) {
    dropZone.setVisible(checkRight(uploadRight));
  }
}