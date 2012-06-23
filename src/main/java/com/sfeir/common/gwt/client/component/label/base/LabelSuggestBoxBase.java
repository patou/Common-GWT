package com.sfeir.common.gwt.client.component.label.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * Classe de base pour les champs de type SuggestBox
 * @author sfeir
 */
public class LabelSuggestBoxBase<K, V> extends LabelListBase<K, SuggestBox, V> implements SelectionHandler<Suggestion> {
    public static final String ITEM_SEPARATOR = ">>";

    /**
     * 
     */
    public LabelSuggestBoxBase() {
    }

    /**
     * @param label
     */
    public LabelSuggestBoxBase(String label) {
        super(label);
    }

    /**
     * @param label
     * @param mandatory
     */
    public LabelSuggestBoxBase(String label, boolean mandatory) {
        super(label, mandatory);
    }

    public LabelSuggestBoxBase(Map<String, Object> config) {
        super(config);
    }

    @Override
    public void setConfig(Map<String, Object> config) {
        super.setConfig(config);
        if (config.containsKey("fullTextSearch")) {
            setFullTextSearch((Boolean) config.get("fullTextSearch"));
        }
        if (config.containsKey("fullTextSearch")) {
            setFullTextSearch((Boolean) config.get("fullTextSearch"));
        }
        if (config.containsKey("suggestLimit")) {
            setSuggestLimit((Integer) config.get("suggestLimit"));
        }
        if (config.containsKey("enabled")) {
            setEnabled((Boolean) config.get("enabled"));
        }
        if (config.containsKey("readOnly")) {
            setReadOnly((Boolean) config.get("readOnly"));
        }
        if (config.containsKey("accessKey")) {
            setAccessKey((Character) config.get("accessKey"));
        }
        if (config.containsKey("title")) {
            setTitle((String) config.get("title"));
        }
        if (config.containsKey("tabIndex")) {
            setTabIndex((Integer) config.get("tabIndex"));
        }
    }

    @Override
    public void clearComponent() {
        getComponent().setText("");
    }

    @Override
    public void removeAll() {
        super.removeAll();
        getSuggestOracle().clear();
    }

    public Integer getItemCount() {
        return getSuggestOracle().count();
    }

    public Integer getItemIndex(String value) {
        return getSuggestOracle().getItemIndex(value);
    }

    @Override
    protected SuggestBox createComponent() {
        SuggestBox suggestBox = new SuggestBox(new SuggestBoxOracle());
        suggestBox.addSelectionHandler(this);
        return suggestBox;
    }

    @Override
    public void onFocus(FocusEvent event) {
        super.onFocus(event);
        getComponent().showSuggestionList();
    }

    @Override
    public void insertItemValue(String item, String value, int index) {
        getSuggestOracle().add(item, value);
    }

    @Override
    public void addDefaultItem(String text) {
        addIndefaultvalueList(text);
        // Remove insert in the proposition list
    }

    /**
     * Selectionne la valeur donnée
     */
    @Override
    protected void setComponentValue(V value, boolean fireEvents) {
        if (value != null) {
            String item = getSuggestOracle().searchItem(value.toString());
            getTextBox().setValue(item, fireEvents);
        }
    }

    /**
     * Retourne la valeur selectionnée
     */

    public Boolean getFullTextSearch() {
        return getSuggestOracle().getFullTextSearch();
    }

    /**
     * Indique que la recherche des mots peut se faire dans tous le mot et non au début de chaque
     * mot
     * @param fullTextSearch
     */
    public void setFullTextSearch(Boolean fullTextSearch) {
        getSuggestOracle().setFullTextSearch(fullTextSearch);
    }

    /**
     * Retourne le SuggestBoxOracle
     * @return
     */
    @SuppressWarnings("unchecked")
    protected SuggestBoxOracle getSuggestOracle() {
        return (SuggestBoxOracle) getComponent().getSuggestOracle();
    }

    /**
     * @return
     * @see com.google.gwt.user.client.ui.SuggestBox#getLimit()
     */
    public int getSuggestLimit() {
        return getComponent().getLimit();
    }

    /**
     * @return
     * @see com.google.gwt.user.client.ui.SuggestBox#getTabIndex()
     */
    public int getTabIndex() {
        return getComponent().getTabIndex();
    }

    /**
     * @return
     * @see com.google.gwt.user.client.ui.SuggestBox#getTextBox()
     */
    public TextBoxBase getTextBox() {
        return getComponent().getTextBox();
    }

    /**
     * @return
     * @see com.google.gwt.user.client.ui.UIObject#getTitle()
     */
    public String getTitle() {
        return getComponent().getTitle();
    }

    /**
     * @return
     * @see com.google.gwt.user.client.ui.SuggestBox#isAnimationEnabled()
     */
    public boolean isAnimationEnabled() {
        return ((HasAnimation) getComponent().getSuggestionDisplay()).isAnimationEnabled();
    }

    /**
     * @return
     * @see com.google.gwt.user.client.ui.SuggestBox#isAutoSelectEnabled()
     */
    public boolean isAutoSelectEnabled() {
        return getComponent().isAutoSelectEnabled();
    }

    /**
     * @param enable
     * @see com.google.gwt.user.client.ui.SuggestBox#setAnimationEnabled(boolean)
     */
    public void setAnimationEnabled(boolean enable) {
	((HasAnimation) getComponent().getSuggestionDisplay()).setAnimationEnabled(enable);
    }

    /**
     * @param selectsFirstItem
     * @see com.google.gwt.user.client.ui.SuggestBox#setAutoSelectEnabled(boolean)
     */
    public void setAutoSelectEnabled(boolean selectsFirstItem) {
        getComponent().setAutoSelectEnabled(selectsFirstItem);
    }

    /**
     * @param limit
     * @see com.google.gwt.user.client.ui.SuggestBox#setLimit(int)
     */
    public void setSuggestLimit(int limit) {
        getComponent().setLimit(limit);
    }

    /**
     * @param index
     * @see com.google.gwt.user.client.ui.SuggestBox#setTabIndex(int)
     */
    public void setTabIndex(int index) {
        getComponent().setTabIndex(index);
    }

    // TextBox Function
    /**
     * @see com.google.gwt.user.client.ui.TextBox#isEnabled()
     */
    public boolean isEnabled() {
        return getTextBox().isEnabled();
    }

    /**
     * @see com.google.gwt.user.client.ui.TextBox#isReadOnly()
     */
    public boolean isReadOnly() {
        return getTextBox().isReadOnly();
    }

    /**
     * @see com.google.gwt.user.client.ui.TextBox#setAccessKey()
     */
    public void setAccessKey(char key) {
        getTextBox().setAccessKey(key);
    }

    /**
     * @see com.google.gwt.user.client.ui.TextBox#setEnabled()
     */
    public void setEnabled(boolean enabled) {
        getTextBox().setEnabled(enabled);
        getSuggestOracle().setEnabled(enabled);
    }

    /**
     * @see com.google.gwt.user.client.ui.TextBox#setReadOnly()
     */
    public void setReadOnly(boolean readOnly) {
        getTextBox().setReadOnly(readOnly);
    }

    /**
     * Si activé, le début de la liste est afficher par defaut en cliquant sur le texte
     * @param defaultSuggestionList
     *            defaultSuggestionList à alimenter
     */
    public void setDefaultSuggestionList(Boolean defaultSuggestionList) {
        getSuggestOracle().setDefaultSuggestionList(defaultSuggestionList);
    }

    /**
     * @return defaultSuggestionList
     */
    public Boolean getDefaultSuggestionList() {
        return getSuggestOracle().getDefaultSuggestionList();
    }

    /**
     * @param title
     * @see com.google.gwt.user.client.ui.UIObject#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
        getComponent().setTitle(title);
    }

    protected class SuggestBoxOracle extends MultiWordSuggestOracle {
        private Map<String, String> values = new LinkedHashMap<String, String>();

        private Boolean fullTextSearch = true;
        private Boolean enabled = true;
        private Boolean defaultSuggestionList = false;

        public SuggestBoxOracle() {
            super();
        }

        public void add(String item, String value) {
            values.put(item, value);
            add(item);
        }

        public Integer count() {
            return values.size();
        }

        public Integer getItemIndex(String value) {
            if (value != null & values.containsValue(value)) {
                Integer i = 0;
                Collection<String> itemValues = values.values();
                for (Iterator<String> iterator = itemValues.iterator(); iterator.hasNext(); ++i) {
                    String val = iterator.next();
                    if (value.equals(val)) {
                        return i;
                    }

                }
            }
            return 0;
        }

        public String getItemValue(String item) {
            return values.get(item);
        }

        public String searchItem(String value) {
            if (value != null) {
                for (Entry<String, String> vv : values.entrySet()) {
                    if (value.equals(vv.getValue())) {
                        return vv.getKey();
                    }
                }
            }
            return null;
        }

        public SuggestBoxOracle(Boolean fullTextSearch) {
            super();
            this.fullTextSearch = fullTextSearch;
        }

        @Override
        public void requestDefaultSuggestions(Request request, Callback callback) {
            Response response;
            if (!defaultSuggestionList) {
                Collection<Suggestion> listResponse = new ArrayList<Suggestion>();
                for (String item : getDefaultValueList()) {
                    listResponse.add(createSuggestion("", item));
                }
                response = new Response(listResponse);
            } else {
                response = findSuggestions(null, request.getLimit());
            }

            callback.onSuggestionsReady(request, response);
        }

        @Override
        public void requestSuggestions(Request request, Callback callback) {
            if (fullTextSearch) {
                Response response = findSuggestions(request.getQuery(), request.getLimit());
                callback.onSuggestionsReady(request, response);
            } else {
                super.requestSuggestions(request, callback);
            }
        }

        /**
         * Reinitialise tous le composant
         */
        public void clear() {
            super.clear();
        }

        public Boolean getFullTextSearch() {
            return fullTextSearch;
        }

        /**
         * Indique que la recherche des mots peut se faire dans tous le mot et non au début de
         * chaque mot
         * @param fullTextSearch
         */
        public void setFullTextSearch(Boolean fullTextSearch) {
            this.fullTextSearch = fullTextSearch;
        }

        /**
         * Recherche toutes les suggestions
         * @param query
         *            La requete
         * @param limit
         *            Le nombre de resultats
         * @return
         */
        public Response findSuggestions(String query, int limit) {
            Collection<Suggestion> listResponse = new ArrayList<Suggestion>();
            if (query != null)
                query = query.trim().toLowerCase();
            for (String item : values.keySet()) {
                if (query == null || isFiltered(query, item)) {
                    listResponse.add(createSuggestion(item, highlightWord(query, item)));
                    if (--limit == 0)
                        break;
                }
            }
            return new Response(listResponse);
        }

        /**
         * Retourne si l'element match la recherche
         * @param query
         *            La recherche
         * @param item
         *            l'element
         * @return
         */
        private boolean isFiltered(String query, String item) {
            if (item == null)
                return false;
            String filtered = item.trim().toLowerCase();
            return filtered.contains(query);
        }

        private String highlightWord(String query, String item) {
            return item.replaceAll("(" + query + ")", "<strong>$1</strong>");
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
            clear();
        }

        public Boolean getEnabled() {
            return enabled;
        }

        /**
         * @param defaultSuggestionList
         *            defaultSuggestionList à alimenter
         */
        public void setDefaultSuggestionList(Boolean defaultSuggestionList) {
            this.defaultSuggestionList = defaultSuggestionList;
        }

        /**
         * @return defaultSuggestionList
         */
        public Boolean getDefaultSuggestionList() {
            return defaultSuggestionList;
        }
    }

    /**
     * 
     */
    public void onSelection(SelectionEvent<Suggestion> event) {
        onChange();
    }
}
