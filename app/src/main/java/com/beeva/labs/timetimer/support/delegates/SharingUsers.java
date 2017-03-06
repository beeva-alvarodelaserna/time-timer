package com.beeva.labs.timetimer.support.delegates;

import com.bbva.kst.uniqueid.domains.users.User;
import java.util.List;

public interface SharingUsers {

	List<User> loadSavedUsers();

	void saveUsers(List<User> users);

}