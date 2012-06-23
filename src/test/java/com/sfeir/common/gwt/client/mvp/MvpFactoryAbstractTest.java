package com.sfeir.common.gwt.client.mvp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.user.client.rpc.AsyncCallback;

@RunWith(MockitoJUnitRunner.class)
public class MvpFactoryAbstractTest {
	MvpFactoryAbstract factory;
	
	@Mock
	MvpFactory defaultFactory;
	@Mock
	MvpFactory loadFactory;
	@Mock
	MvpFactoryHandler handler;
	
	@Before
	public void newFactory() {
		factory = new MvpFactoryAbstract() {
			
			@Override
			protected void loadFactory(String splitPt, AsyncCallback<MvpFactory> callback) {
				callback.onSuccess(loadFactory);
			}
		};
		factory.setDefaultFactory(defaultFactory);
		factory.splitPts = new String[]{"a.b", "a", "c"};
		factory.handler = handler;
	}
	
	@Test
	public void testGetSplitPt() {
		assertEquals("a", factory.getSplitPt("a.T"));
		assertEquals("a.b", factory.getSplitPt("a.b.T"));
		assertEquals("a", factory.getSplitPt("a.c.T"));
		assertEquals("c", factory.getSplitPt("c.T"));
		assertEquals(null, factory.getSplitPt("d.T"));
	}

	@Test
	public void testGetFactory() {
		assertEquals(null, factory.getFactory("a.T"));
		assertEquals(defaultFactory, factory.getFactory("d.T"));
		factory.factories.put("a.b", loadFactory);
		assertEquals(loadFactory, factory.getFactory("a.b.T"));
		assertEquals(null, factory.getFactory("a.c.T"));
		assertEquals(null, factory.getFactory("c.T"));
	}

	@Test
	public void testOnLoadFactoryError() {
		Exception error = new Exception();
		factory.onLoadFactoryError(error, "a");
		verify(handler).errorLoadsplitPt(error, "a");
	}
}
