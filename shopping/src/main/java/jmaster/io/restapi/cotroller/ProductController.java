package jmaster.io.restapi.cotroller;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import jmaster.io.restapi.converter.CustomUserDetails;
import jmaster.io.restapi.model.Cart;
import jmaster.io.restapi.model.Product;
import jmaster.io.restapi.model.User;
import jmaster.io.restapi.service.CartService;
import jmaster.io.restapi.service.ProductService;
import jmaster.io.restapi.service.UserService;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CartService cartService;
	@Autowired
	private UserService userService;
	
	@RequestMapping("/listproduct")
	public String getAllProduct(Model model) throws JsonProcessingException {
		model.addAttribute("products", productService.getAllProducts());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		model.addAttribute("username", userDetails.getUsername());
		model.addAttribute("roles", userDetails.getAuthorities());
		model.addAttribute("carts", cartService.getAllCarts(userDetails.getUsername()));
		return "listproduct";
	}
	
	@PostMapping("/findAProduct")
	public String findAProduct(HttpServletRequest req, Model model) {
		Product product = new Product();
		String str = req.getParameter("pname");
		String srr = req.getParameter("PId");
		if(str!=null) {
			product = productService.getProductByname(req.getParameter("pname"));
		model.addAttribute("PId", product.getPId());
		model.addAttribute("pname", product.getPname());
		model.addAttribute("description", product.getDescription());
		model.addAttribute("price", product.getPrice());
		return "findAProduct";
		}
		else if(srr!=null) {
			product = productService.getProductByPid(Integer.parseInt(req.getParameter("PId")));
			model.addAttribute("PId", product.getPId());
			model.addAttribute("pname", product.getPname());
			model.addAttribute("description", product.getDescription());
			model.addAttribute("price", product.getPrice());
			return "findAProduct";
		}
		else return "redirect:/findAProduct?error";
	}
	
	@GetMapping("/findAProduct")
	public String findAProductG() {
		return "findAProduct";
	}
	
	@PostMapping("/addProduct")
	public String addProductP(HttpServletRequest req, MultipartHttpServletRequest multipartHttpServletRequest) {
			try {
				if(!productService.exitsByPname(req.getParameter("pname"))) {
				byte[] imageBytes = multipartHttpServletRequest.getFile("image").isEmpty() ? null : multipartHttpServletRequest.getFile("image").getBytes();
				String image = Base64.getEncoder().encodeToString(imageBytes);
				Product product = new Product(req.getParameter("pname"), req.getParameter("description"), Double.parseDouble(req.getParameter("price")),image);
				productService.addAProduct(product);
		return "redirect:/addProduct?success";
		}
		else return "redirect:/addProduct?error";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "redirect:/addProduct?success";
	}
	
	@GetMapping("/addProduct")
	public String addProductG() {
		return "addProduct";
	}
	
	@GetMapping("/deleteProduct")
	public String deleteProductG() {
		return "deleteProduct";
	}
	
	@PostMapping("/deleteProduct")
	public String deleteProductP(HttpServletRequest req) {
			productService.deleteProduct(Integer.parseInt(req.getParameter("PId")));
			return "deleteProduct";
	}
	
	@PostMapping("/updateProduct")
	public String updateProductP(HttpServletRequest req, MultipartHttpServletRequest multipartHttpServletRequest) throws IOException {
		Product currentproduct = productService.findProductById(Integer.parseInt(req.getParameter("PId")));
		currentproduct.setPname(req.getParameter("pname"));
		currentproduct.setDescription(req.getParameter("description"));
		currentproduct.setPrice(Double.parseDouble(req.getParameter("price")));
		if(multipartHttpServletRequest.getFile("image") !=null) {
			currentproduct.setImage(Base64.getEncoder().encodeToString(multipartHttpServletRequest.getFile("image").getBytes()));
		}
//			<=>byte[] imageBytes = multipartHttpServletRequest.getFile("image").getBytes();
//			String image = Base64.getEncoder().encodeToString(imageBytes);
//			currentproduct.setImage(image);
		productService.updateProduct(Integer.parseInt(req.getParameter("PId")),currentproduct);
		return "deleteProduct";
	}
	
	@GetMapping("/updateProduct")
	public String updateProductG(HttpServletRequest req, Model model) throws IOException {
		model.addAttribute("currentimage", req.getParameter("image"));
		model.addAttribute("PId", req.getParameter("PId"));
		model.addAttribute("pname", req.getParameter("pname"));
		model.addAttribute("description", req.getParameter("description"));
		model.addAttribute("price", req.getParameter("price"));
		return "updateProduct";
	}
}
