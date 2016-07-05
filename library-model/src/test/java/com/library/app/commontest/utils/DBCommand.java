package com.library.app.commontest.utils;

import org.junit.Ignore;

@Ignore
public interface DBCommand<T> {
	
	T execute();
}
