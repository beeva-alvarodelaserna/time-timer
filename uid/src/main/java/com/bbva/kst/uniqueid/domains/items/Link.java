package com.bbva.kst.uniqueid.domains.items;

public class Link {

	private final String href;
	private final String rel;
	private final String title;
	private final String name;
	private final String method;

	public Link(String href, String rel, String title, String name, String method) {
		this.href = href;
		this.rel = rel;
		this.title = title;
		this.name = name;
		this.method = method;
	}
}
