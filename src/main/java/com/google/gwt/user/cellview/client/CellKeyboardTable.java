package com.google.gwt.user.cellview.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

/**
 * Extend the default CellTable to add keyboard navigation by default and catch the TAB key to go to the next line (and SHIFT + TAB for the previous)
 * The Enter Key automatically select the row.
 * 
 * It was in the google package because of limitation with package visibility
 * @see CellTable
 * @param <T> The type of the cellTable
 */
public class CellKeyboardTable<T> extends CellTable<T> {
    /**
     * Constructs a table with a default page size of 15.
     */
    public CellKeyboardTable() {
	super();
	setup();
    }

    /**
     * Constructs a table with the given page size.
     * 
     * @param pageSize
     *            the page size
     */
    public CellKeyboardTable(final int pageSize) {
	super(pageSize);
	setup();
    }

    /**
     * Constructs a table with a default page size of 15, and the given {@link ProvidesKey key provider}.
     * 
     * @param keyProvider
     *            an instance of ProvidesKey<T>, or null if the record object should act as its own key
     */
    public CellKeyboardTable(ProvidesKey<T> keyProvider) {
	super(keyProvider);
	setup();
    }

    /**
     * Constructs a table with the given page size with the specified {@link Resources}.
     * 
     * @param pageSize
     *            the page size
     * @param resources
     *            the resources to use for this widget
     */
    public CellKeyboardTable(int pageSize, Resources resources) {
	super(pageSize, resources);
	setup();
    }

    /**
     * Constructs a table with the given page size and the given {@link ProvidesKey key provider}.
     * 
     * @param pageSize
     *            the page size
     * @param keyProvider
     *            an instance of ProvidesKey<T>, or null if the record object should act as its own key
     */
    public CellKeyboardTable(int pageSize, ProvidesKey<T> keyProvider) {
	super(pageSize, keyProvider);
	setup();
    }

    /**
     * Constructs a table with the given page size, the specified {@link Resources}, and the given key provider.
     * 
     * @param pageSize
     *            the page size
     * @param resources
     *            the resources to use for this widget
     * @param keyProvider
     *            an instance of ProvidesKey<T>, or null if the record object should act as its own key
     */
    public CellKeyboardTable(final int pageSize, Resources resources, ProvidesKey<T> keyProvider) {
	super(pageSize, resources, keyProvider);
	setup();
    }

    /**
     * Constructs a table with the specified page size, {@link Resources}, key provider, and loading indicator.
     * 
     * @param pageSize
     *            the page size
     * @param resources
     *            the resources to use for this widget
     * @param keyProvider
     *            an instance of ProvidesKey<T>, or null if the record object should act as its own key
     * @param loadingIndicator
     *            the widget to use as a loading indicator, or null to disable
     */
    public CellKeyboardTable(final int pageSize, Resources resources, ProvidesKey<T> keyProvider, Widget loadingIndicator) {
	super(pageSize, resources, keyProvider, loadingIndicator);
	setup();
    }

    /**
     * Override the onBrowserEvent2 to catch TAB and ENTER keys events.
     * 
     * TODO Cautions, this code can change in next release, check the code in the AbastractCellTable.handleKey() exemple code. 
     */
    @Override
    protected void onBrowserEvent2(Event event) {
	if ("keydown".equals(event.getType()) && !isKeyboardNavigationSuppressed() && KeyboardSelectionPolicy.DISABLED != getKeyboardSelectionPolicy()) {
	    HasDataPresenter<T> presenter = getPresenter();
	    int keyCode = event.getKeyCode();
	    if (keyCode == KeyCodes.KEY_TAB) {

	    }
	    if (keyCode == KeyCodes.KEY_ENTER) {
		T object = getPresenter().getKeyboardSelectedRowValue();
		if (object != null) {
		    getSelectionModel().setSelected(object, true);
		    event.preventDefault();
		    return;
		}
	    }
	}
	super.onBrowserEvent2(event);
    }

    /**
     * Default setUp for the table
     * 
     * Enable by default the keyboard
     */
    private void setup() {
        setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
        setAccessKey('t');
    }
}
