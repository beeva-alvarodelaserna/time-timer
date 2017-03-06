package com.bbva.kst.uniqueid.domains;

import com.bbva.kst.uniqueid.domains.items.Link;
import java.util.ArrayList;
import java.util.List;

public class SharedItem {

	public int id;
	public String nickName;
	public List<Link> links = new ArrayList<>();
	public String expires;
	public int editable;
	public String[] sharedWith;
	public String sharedBy;

}
