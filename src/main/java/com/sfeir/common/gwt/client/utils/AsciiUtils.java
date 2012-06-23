package com.sfeir.common.gwt.client.utils;
public class AsciiUtils {

    // private constructor, can't be instanciated!
    private AsciiUtils() { }

    /**
     * remove accentued from a string and replace with ascii equivalent
     * @param s
     * @return
     */
    public static String convertNonAscii(String s) {
       if (s == null) return null;
       StringBuilder sb = new StringBuilder();
       int n = s.length();
       for (int i = 0; i < n; i++) {
          char c = s.charAt(i);
          int pos = UNICODE.indexOf(c);
          if (pos > -1){
              sb.append(PLAIN_ASCII.charAt(pos));
          }
          else {
              sb.append(c);
          }
       }
       return sb.toString();
    }
    
    /**
     * Clean the string by replace all accent, and replace all non alphanumeric character by the character "-"
     * @param tr
     * @return empty string if the given string is null or empty
     */
    public static String clean(String tr) {
    	if (tr == null || tr.isEmpty())
    		return "";
		return convertNonAscii(tr).replaceAll("[^a-zA-Z0-9]", "-").replaceAll("[-]+", "-");
	}

    
    private static final String PLAIN_ASCII =
    		"AaEeIiOoUu"    // grave
    		+ "AaEeIiOoUuYy"  // acute
    		+ "AaEeIiOoUuYy"  // circumflex
    		+ "AaOoNn"        // tilde
    		+ "AaEeIiOoUuYy"  // umlaut
    		+ "Aa"            // ring
    		+ "Cc"            // cedilla
    		+ "OoUu"          // double acute
    		;
    
    private static final String UNICODE =
    		"\u00C0\u00E0\u00C8\u00E8\u00CC\u00EC\u00D2\u00F2\u00D9\u00F9"
    				+ "\u00C1\u00E1\u00C9\u00E9\u00CD\u00ED\u00D3\u00F3\u00DA\u00FA\u00DD\u00FD"
    				+ "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177"
    				+ "\u00C3\u00E3\u00D5\u00F5\u00D1\u00F1"
    				+ "\u00C4\u00E4\u00CB\u00EB\u00CF\u00EF\u00D6\u00F6\u00DC\u00FC\u0178\u00FF"
    				+ "\u00C5\u00E5"
    				+ "\u00C7\u00E7"
    				+ "\u0150\u0151\u0170\u0171"
    				;
}