package com.bbva.kst.uniqueid.domains.items;

import java.util.ArrayList;
import java.util.List;

public class FormItemPresentationData {

	public ItemSubtypeForm formInput;
	public Integer masterItem;
	public List<Integer> slaveItems;
	public int item_type;

	public FormItemPresentationData(ItemSubtypeForm itemSubtypeForm) {
		formInput = itemSubtypeForm;
		masterItem = null;
		slaveItems = new ArrayList<>();
	}
}
