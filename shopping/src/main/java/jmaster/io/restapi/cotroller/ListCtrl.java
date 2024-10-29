package jmaster.io.restapi.cotroller;

import java.util.Arrays;

public class ListCtrl {
	private String username;
	private String[] rolenames;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String[] getRolenames() {
		return rolenames;
	}
	public void setRolenames(String[] rolenames) {
		this.rolenames = rolenames;
	}
	public ListCtrl(String username, String[] rolenames) {
		super();
		this.username = username;
		this.rolenames = rolenames;
	}
	@Override
	public String toString() {
		return "username: " + username + ", rolenames: " + Arrays.toString(rolenames);
	}
	
}
