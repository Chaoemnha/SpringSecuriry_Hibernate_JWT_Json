package jmaster.io.restapi.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import jmaster.io.restapi.model.Cart;
import jmaster.io.restapi.model.CartItem;
import jmaster.io.restapi.model.User;
import jmaster.io.restapi.repository.UserRepository;

@Service
public class CartService {
	//EnityM cung cấp merge giúp chống tách rời user với cart
	@Autowired
	private UserRepository userRepository;
	
	public User addCIToCart(int CId, String username, CartItem cartItem) throws JsonProcessingException {
		User user = userRepository.findByUsername(username).orElseThrow(()->new EntityNotFoundException("CS 32"));
		int exitsCIId=user.findCIIdExistPId(CId, cartItem.getProduct().getPId());
		if(exitsCIId>=0) {
			Cart cart = User.JsonToCarts(user.getCartsJson()).get(CId);
			CartItem cartItem2 = Cart.JsonToCartItems(cart.getCartItemsJson()).get(exitsCIId);
			cartItem2.setQuantity(cartItem.getQuantity()+cartItem2.getQuantity());
			cart.setCartItem(exitsCIId, cartItem2);
			user.setCart(CId, cart);
			return userRepository.save(user);
		}
		else {
			return userRepository.save(createNewCart(username, cartItem));
		}
	}
	

	public User createNewCart(String username, CartItem cartItem) throws JsonProcessingException {
		User user = userRepository.findByUsername(username).orElseThrow(()-> new EntityNotFoundException("CS 52"));
		Cart cart = new Cart();
		cart.addCartItem(cartItem);
		user.addCart(cart);
		return userRepository.save(user);
	}
	
	public User removeFromCart(String username, int CId, int CIId){
		User user = userRepository.findByUsername(username).orElseThrow(()-> new EntityNotFoundException("CS 55"));
		try {
			Cart cart = User.JsonToCarts(user.getCartsJson()).get(CId);
			cart.removeCartItem(CIId);
			user.setCart(CId, cart);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userRepository.save(user);
	}
	
	public List<Cart> getAllCarts(String username){
		User user = userRepository.findByUsername(username).orElseThrow(()-> new EntityNotFoundException("CS 62"));
		try {
			return User.JsonToCarts(user.getCartsJson());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<Cart>();
		}
	}
	
	public List<CartItem> getAllCartItems(String username, int CId){
		User user = userRepository.findByUsername(username).orElseThrow(()-> new EntityNotFoundException("CS 67"));
		try {
			Cart cart = User.JsonToCarts(user.getCartsJson()).get(CId);
			return Cart.JsonToCartItems(cart.getCartItemsJson());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<CartItem>();
		}
	}
	
	public User updateCartItem(String username, int CId, int CIId, int quantity){
		User user = userRepository.findByUsername(username).orElseThrow(()-> new EntityNotFoundException("CS 55"));
		try {
			Cart cart = User.JsonToCarts(user.getCartsJson()).get(CId);
			CartItem cartItem = Cart.JsonToCartItems(cart.getCartItemsJson()).get(CIId);
			cartItem.setQuantity(quantity);
			cart.setCartItem(CIId, cartItem);
			user.setCart(CId, cart);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userRepository.save(user);
	}
	
	public User delACart(String username, int CId){
		User user = userRepository.findByUsername(username).orElseThrow(()-> new EntityNotFoundException("CS 55"));
		try {
			user.removeCart(CId);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userRepository.save(user);
	}

//	public int findCIIdExistPId(int CId, int PId) {
//		Cart cart = cartRepository.findById(CId).orElseThrow(()->new EntityNotFoundException());
//		Set<CartItem> cartItems = cart.getCartItems();
//		for(CartItem item : cartItems) {
//			if(item.getPId()==PId) {
//				return item.getCIId();
//			}
//		}
//		return -1;
//	}
	

}
