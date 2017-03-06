package com.bbva.kst.uniqueid.instruments.inject;

public interface Injector {

	<T> T inject(Class<T> tClass);

}
