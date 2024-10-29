package jmaster.io.restapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

public class CartItem {
	private int CIId;
	private Product product;
	private int quantity;
	public CartItem(Product product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}
	
	public CartItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCIId() {
		return CIId;
	}
//	public Cart getCart() {
//		return cart;
//	}
//
//	public void setCart(Cart cart) {
//		this.cart = cart;
//	}

	public void setCIId(int cIId) {
		CIId = cIId;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public double getTotalPrice() {
		return quantity*product.getPrice();
	}
}
