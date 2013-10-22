package com.sfeir.common.gwt.client.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;



public class AsciiUtilsTest {

	@Test
	public void testConvertNonAscii()
	{
		       String s =
		         "The result : È,É,Ê,Ë,Û,Ù,Ï,Î,À,Â,Ô,è,é,ê,ë,û,ù,ï,î,à,â,ô,ç";
		       String result = AsciiUtils.convertNonAscii(s);
		       // output :
		       assertEquals("The result : E,E,E,E,U,U,I,I,A,A,O,e,e,e,e,u,u,i,i,a,a,o,c", result);
		   
	}
	
	@Test
	public void testClean() {
		String s = "The result : È,É,Ê,Ë,Û,Ù,Ï,Î,À,Â,Ô,è,é,ê,ë,û,ù,ï,î,à,â,ô,ç";
		String result = AsciiUtils.clean(s);
	       // output :
	    assertEquals("The-result-E-E-E-E-U-U-I-I-A-A-O-e-e-e-e-u-u-i-i-a-a-o-c", result);
	}

}
