package jmaster.io.restapi.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "DBUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "DBUserrole", //Sau chữ thường là chữ hoa thì jdbc auto dịch là space
               joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Set<Role> roles = new HashSet<>();
    //Giờ ms biết là để set thì sql sẽ ko quét, đỡ p transient HUHUHUHUHUHUUUUUUUUUU
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    Set<Cart> carts = new HashSet<Cart>();
    private String cartsJson;
    
    public String getCartsJson() {
		return cartsJson;
	}

	public void setCartsJson(String cartJson) {
		this.cartsJson = cartJson;
	}
	
	public static List<Cart> JsonToCarts(String json) throws JsonProcessingException{
		if(json == null||json.isEmpty()) {
			return new ArrayList<Cart>();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Cart.class));
	}
	
	public static String CartsToJson(List<Cart> carts) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(carts);
	}

	public void addCart(Cart cart) throws JsonProcessingException {
		List<Cart> carts = User.JsonToCarts(this.cartsJson);
		carts.add(cart);
		this.cartsJson = User.CartsToJson(carts);
	}

	public void removeCart(int CId) throws JsonProcessingException {
		List<Cart> carts = User.JsonToCarts(this.cartsJson);
		carts.remove(CId);
		this.cartsJson = User.CartsToJson(carts);
	}
	
	public void setCart(int CId, Cart cart) throws JsonProcessingException {
		List<Cart> carts = User.JsonToCarts(this.cartsJson);
		carts.set(CId,cart);
		this.cartsJson = User.CartsToJson(carts);
	}
	
	public int findCIIdExistPId(int CId, int PId) throws JsonProcessingException {
		List<Cart> carts = User.JsonToCarts(this.cartsJson);
		if(CId>= 0 && CId <= carts.size()) {
			Cart cart = carts.get(CId);
			List<CartItem> cartItems = Cart.JsonToCartItems(cart.getCartItemsJson());
			for (int i = 0; i < cartItems.size(); i++) {
		        if (cartItems.get(i).getProduct().getPId() == PId) {
		            return i; // Trả về chỉ số của CartItem trong List
		        }
		    }
			return -1;
		}
		else return -1;
	}
	public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User [user_id=" + user_id + ", username=" + username + ", password=" + password + ", roles=" + roles + "]";
    }

    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
    
    public User(String username, String password, Set<Role> roles, String CartJson) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    	this.cartsJson = CartJson;
    }
    
    public User() {
        this.user_id = 0;
        this.username = "username";
        this.password = "password";
        this.roles = new HashSet<Role>();
    }
    
}


