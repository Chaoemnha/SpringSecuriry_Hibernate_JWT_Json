package jmaster.io.restapi.cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import jmaster.io.restapi.converter.CustomUserDetails;
import jmaster.io.restapi.model.Cart;
import jmaster.io.restapi.model.CartItem;
import jmaster.io.restapi.model.User;
import jmaster.io.restapi.service.CartService;
import jmaster.io.restapi.service.ProductService;

@Controller
public class CartController {
	@Autowired
	private CartService cartService;
	@Autowired
	private ProductService productService;
	
	private CustomUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        return null;
    }
	
	@RequestMapping("/listcart")
	public String getListCarts(Model model) {
		CustomUserDetails userDetails = getCurrentUserDetails();
		model.addAttribute("carts", cartService.getAllCarts(userDetails.getUsername()));
		model.addAttribute("username", userDetails.getUsername());
		model.addAttribute("roles", userDetails.getAuthorities());
		return "listCarts";
	}
	
	@PostMapping("/deleteACart")
	public String delACart(HttpServletRequest req) {
		CustomUserDetails userDetails = getCurrentUserDetails();
		cartService.delACart(userDetails.getUsername(), Integer.parseInt(req.getParameter("CId")));
		return "returnCI";
	}
	
	@PostMapping("/addToACart")
	public String addToACart(HttpServletRequest req) throws JsonProcessingException {
		CustomUserDetails userDetails = getCurrentUserDetails();
		cartService.addCIToCart(Integer.parseInt(req.getParameter("CId")),req.getParameter("username"), new CartItem(productService.getProductByPid(Integer.parseInt(req.getParameter("PId"))), Integer.parseInt(req.getParameter("quantity"))));
		return "deleteProduct";
	}
	@RequestMapping("/cart/{id}")
	public String getListCartItems(@PathVariable("id") int id, Model model, HttpServletRequest req) {
		CustomUserDetails userDetails = getCurrentUserDetails();
		model.addAttribute("id", id);
		model.addAttribute("cartItems", cartService.getAllCartItems(userDetails.getUsername(), Integer.parseInt(req.getParameter("CId"))));
		model.addAttribute("username", userDetails.getUsername());
		model.addAttribute("roles", userDetails.getAuthorities());
		return "listCartItems";
	}
	@PostMapping("/deleteFromACart")
	public String deleteFromACart(HttpServletRequest request) {
		CustomUserDetails userDetails = getCurrentUserDetails();
		cartService.removeFromCart(userDetails.getUsername(), Integer.parseInt(request.getParameter("CId")), Integer.parseInt(request.getParameter("CIId")));
		return "returnCI";
	}
	@PostMapping("/updateCartItem")
	public String updateCartItem(HttpServletRequest request) {
		CustomUserDetails userDetails = getCurrentUserDetails();
		cartService.updateCartItem(userDetails.getUsername(), Integer.parseInt(request.getParameter("CId")), Integer.parseInt(request.getParameter("CIId")), Integer.parseInt(request.getParameter("quantity")));
		return "returnCI";
	}
	@RequestMapping("/paymentsuccess")
	public String success() {
		return "home";
	}
}
