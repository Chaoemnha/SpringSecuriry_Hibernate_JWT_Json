package jmaster.io.restapi.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

//@Entity
//@Table(name = "DBCart")
public class Cart {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartItemsQuatity;
	private String cartItemsJson;
	
    public int getCartItemsQuatity() {
		return cartItemsQuatity;
	}

	public void setCartItemsQuatity(int cartItemsQuatity) {
		this.cartItemsQuatity = cartItemsQuatity;
	}

	public String getCartItemsJson() {
		return cartItemsJson;
	}

	public void setCartItemsJson(String cartItemsJson) {
		this.cartItemsJson = cartItemsJson;
	}
	
	public void addCartItem(CartItem cartItem) throws JsonProcessingException {
		List<CartItem> cartItems = Cart.JsonToCartItems(this.cartItemsJson);
		cartItems.add(cartItem);
		this.cartItemsQuatity+=1;
		this.cartItemsJson = Cart.CartItemsToJson(cartItems);
	}
	
	public void removeCartItem(int CIId) throws JsonProcessingException {
		List<CartItem> cartItems = Cart.JsonToCartItems(this.cartItemsJson);
		cartItems.remove(CIId);
		this.cartItemsQuatity-=1;
		this.cartItemsJson = Cart.CartItemsToJson(cartItems);
	}
	
	public void setCartItem(int CIId, CartItem cartItem) throws JsonProcessingException {
		List<CartItem> cartItems = Cart.JsonToCartItems(this.cartItemsJson);
		cartItems.set(CIId,cartItem);
		this.cartItemsQuatity = cartItems.size();
		this.cartItemsJson = Cart.CartItemsToJson(cartItems);
	}
	
	public static List<CartItem> JsonToCartItems(String json) throws JsonProcessingException{
		if(json == null||json.isEmpty()) {
			return new ArrayList<CartItem>();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, CartItem.class));
	}
	
	public static String CartItemsToJson(List<CartItem> cartItems) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(cartItems);
	}
	
	public Cart(User user) {
		super();
//		this.cartItems = new HashSet<CartItem>();
//		this.user = user;
	}
//	public void setCartItems(Set<CartItem> cartItems) {
//		this.cartItems = cartItems;
//	}
//	public void setACartItemByPId(CartItem updatedCartItem) {
//        // Duyệt qua các CartItem trong cartItems
//        for (CartItem cartItem : cartItems) {
//            // Nếu tìm thấy CartItem có PId trùng khớp
//            if (cartItem.getPId() == updatedCartItem.getPId()) {
//                // Xóa CartItem cũ
//                this.cartItems.remove(cartItem);
//                // Thêm CartItem mới đã được cập nhật
//                this.cartItems.add(updatedCartItem);
//                break; // Thoát vòng lặp sau khi tìm thấy
//            }
//        }
//    }
//	public Set<CartItem> getCartItems() {
//		return cartItems;
//	}

	public Cart() {
		super();
		// TODO Auto-generated constructor stub
		this.cartItemsQuatity = 0;
		this.cartItemsJson = "";
	}
//	
//	public void addItem(CartItem item) {
//		this.cartItems.add(item);
//	}
//	
//	public void removeItem(CartItem items) {
//		this.cartItems.add(items);
//	}
}
