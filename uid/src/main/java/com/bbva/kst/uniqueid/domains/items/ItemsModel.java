package com.bbva.kst.uniqueid.domains.items;

import java.util.ArrayList;
import java.util.List;

public class ItemsModel {

	public int id;
	public int verificationLevel;
	public int itemType;
	public int rootType;
	public int editable;
	public boolean shared;
	public int countShared;
	public List<String> metadata = new ArrayList<>();
	public List<ItemsLink> links = new ArrayList<>();
	public Thumbnail thumbnail;
	public String expires;
	public String dateUpdate;
	public String iconImage;
}
