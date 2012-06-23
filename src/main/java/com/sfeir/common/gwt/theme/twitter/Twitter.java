package com.sfeir.common.gwt.theme.twitter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.DataResource;

public interface Twitter extends ClientBundle {
	@NotStrict
	@Source({"defaultstyle.css","style.css"})
	Style style();

	public static final class Instance {
		static Twitter instance = null;
		
		public static Twitter getInstance() {
			if (instance == null) {
				instance = GWT.create(Twitter.class);
				injectNoneStandardCss(instance);
				
			}
			return instance;
		}
		
		private static void injectNoneStandardCss(Twitter style) {
			style.style().ensureInjected();
			style.alert().ensureInjected();
			style.animation().ensureInjected();
			style.form().ensureInjected();
			style.grid().ensureInjected();
			style.icon().ensureInjected();
			style.label().ensureInjected();
			style.navbar().ensureInjected();
			style.last().ensureInjected();
			StringBuilder sb = new StringBuilder();
//			sb.append(style.style().getText());
			sb.append("audio:not([controls]){display: none;}");
			sb.append("button::-moz-focus-inner,input::-moz-focus-inner {padding: 0;border: 0;}");
			sb.append("@-webkit-keyframes progress-bar-stripes{from{background-position:00;}to{background-position: 40px 0;}}");
			sb.append("@-moz-keyframes progress-bar-stripes{from{background-position:00;}to{background-position: 40px 0;}}");
			sb.append("@keyframes progress-bar-stripes{from{background-position:00;}to {background-position: 40px 0;}}");
			sb.append("input[type=\"search\"]::-webkit-search-decoration,input[type=\"search\"]::-webkit-search-cancel-button{-webkit-appearance:none;}");
			sb.append(":-moz-placeholder{color:#999999;}");
			sb.append("::-webkit-input-placeholder{color:#999999;}");
//			sb.append(style.accordion().getText());
//			sb.append(style.alert().getText());
//			sb.append(style.animation().getText());
//			sb.append(style.breadcrumb().getText());
//			sb.append(style.button().getText());
//			sb.append(style.carousel().getText());
//			sb.append(style.form().getText());
//			sb.append(style.grid().getText());
//			sb.append(style.icon().getText());
//			sb.append(style.label().getText());
//			sb.append(style.modal().getText());
//			sb.append(style.navbar().getText());
//			sb.append(style.pager().getText());
//			sb.append(style.progress().getText());
			//
			sb.append('.');
			sb.append(style.style().tableStriped());
			sb.append(" tbody tr:nth-child(odd) td,");
			//
			sb.append('.');
			sb.append(style.style().tableStriped());
			sb.append(" tbody tr:nth-child(odd) th{background-color: #f9f9f9;}");
			//
			sb.append("button");
			sb.append('.');
			sb.append(style.style().btn());
			sb.append("::-moz-focus-inner,input[type=\"submit\"]");
			sb.append('.');
			sb.append(style.style().btn());
			sb.append("::-moz-focus-inner{padding:0;border:0;}");
			//
			sb.append('.');
			sb.append(style.style().navbarSearch());
			sb.append(' ');
			sb.append('.');
			sb.append(style.style().searchQuery());
			sb.append(" :-moz-placeholder{color:#eeeeee;}");
			sb.append('.');
			sb.append(style.style().navbarSearch());
			sb.append(' ');
			sb.append('.');
			sb.append(style.style().searchQuery());
			sb.append("::-webkit-input-placeholder{color:#eeeeee;}");
//			sb.append(style.last().getText());
			//Responsible design : 
			{
				sb.append("@media(max-width:480px){");
				sb.append(style.styleMax480().getText());
				sb.append("}");
			}
			//
			{
				sb.append("@media(max-width:768px){");
				sb.append(style.styleMax768().getText());
				sb.append("}");
			}
			{
				sb.append("@media(max-width:980px){");
				sb.append(style.styleMax980().getText());
				sb.append("}");
			}
			{
				sb.append("@media(min-width:980px){");
				sb.append(style.styleMin980().getText());
				sb.append("}");
			}
			{
				sb.append("@media(min-width:768px) and (max-width:980px){");
				sb.append(style.styleMin768Max980().getText());
				sb.append("}");
			}
			{
				sb.append("@media(min-width:1200px){");
				sb.append(style.styleMin1200().getText());
				sb.append("}");
			}
			String css = sb.toString();
			StyleInjector.inject(css);
		}

		public static Style getStyle() {
			return getInstance().style();
		}
	}

	@NotStrict
	@Source({"defaultstyle.css","grid.css"})
	Grid grid();

//	@NotStrict
//	@Source({"defaultstyle.css","accordion.css"})
//	Accordion accordion();
//
	@NotStrict
	@Source({"defaultstyle.css","alert.css"})
	Alert alert();

	@NotStrict
	@Source({"defaultstyle.css","animation.css"})
	Animation animation();
//
//	@NotStrict
//	@Source({"defaultstyle.css","breadcrumb.css"})
//	Breadcrumb breadcrumb();
//
//	@NotStrict
//	@Source({"defaultstyle.css","button.css"})
//	Button button();
//
//	@NotStrict
//	@Source({"defaultstyle.css","carousel.css"})
//	Carousel carousel();

	@NotStrict
	@Source({"defaultstyle.css","form.css"})
	Form form();

	@NotStrict
	@Source({"defaultstyle.css","icon.css"})
	Icon icon();

	@NotStrict
	@Source({"defaultstyle.css","label.css"})
	Label label();

	@NotStrict
	@Source({"defaultstyle.css","last.css"})
	Last last();

//	@NotStrict
//	@Source({"defaultstyle.css","modal.css"})
//	Modal modal();
//
	@NotStrict
	@Source({"defaultstyle.css","navbar.css"})
	Navbar navbar();

//	@NotStrict
//	@Source({"defaultstyle.css","pager.css"})
//	Pager pager();
//
//	@NotStrict
//	@Source({"defaultstyle.css","progress.css"})
//	Progress progress();
//
	@NotStrict
	@Source({"defaultstyle.css","style-max480.css"})
	StyleMax480 styleMax480();

	@NotStrict
	@Source({"defaultstyle.css","style-max768.css"})
	StyleMax768 styleMax768();

	@NotStrict
	@Source({"defaultstyle.css","style-max980.css"})
	StyleMax980 styleMax980();

	@NotStrict
	@Source({"defaultstyle.css","style-min1200.css"})
	StyleMin1200 styleMin1200();

	@NotStrict
	@Source({"defaultstyle.css","style-min768-max980.css"})
	StyleMin768Max980 styleMin768Max980();

	@NotStrict
	@Source({"defaultstyle.css","style-min980.css"})
	StyleMin980 styleMin980();

	@NotStrict
	@Source({"defaultstyle.css","table.css"})
	Table table();

	@Source("glyphicons-halflings-white.png")
	DataResource glyphiconswhite();

	@Source("glyphicons-halflings.png")
	DataResource glyphicons();
}
