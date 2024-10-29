package jmaster.io.restapi.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table( name = "DBRole")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int role_id;
	private String rolename;
	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<User>();
	
	public Role() {
		super();
		// TODO Auto-generated constructor stub
		this.role_id = 1;
		this.rolename = "user";
	}
	
	public Role(int role_id, String rolename) {
		super();
		this.role_id = role_id;
		this.rolename = rolename;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public String getRole() {
		return rolename;
	}
	public void setRole(String rolename) {
		this.rolename = rolename;
	}
	@Override
	public String toString() {
		return "rolename: "+rolename;
	}
}
