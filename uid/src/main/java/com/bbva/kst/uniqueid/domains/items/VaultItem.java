package com.bbva.kst.uniqueid.domains.items;

public final class VaultItem {

	public final int id;
	public final String name;
	public final String alias;
	public final String url_image;
	private final int visible;
	private final int order_view;
	public final int num_items;
	private final Link[] links;

	public VaultItem(
		int id, String name, String alias, String url_image, int visible, int order_view, int num_items, Link[] links) {
		this.id = id;
		this.name = name;
		this.alias = alias;
		this.url_image = url_image;
		this.visible = visible;
		this.order_view = order_view;
		this.num_items = num_items;
		this.links = links;
	}

}
