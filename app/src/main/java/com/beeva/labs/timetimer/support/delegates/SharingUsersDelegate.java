package com.beeva.labs.timetimer.support.delegates;

import com.bbva.kst.uniqueid.domains.users.User;
import java.util.ArrayList;
import java.util.List;

public class SharingUsersDelegate implements SharingUsers {

	private List<User> list = new ArrayList<>();

	@Override
	public List<User> loadSavedUsers() {
		return list;
	}

	@Override
	public void saveUsers(List<User> users) {
		list = users;
	}

}
