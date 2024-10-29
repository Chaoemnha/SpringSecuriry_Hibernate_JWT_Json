package jmaster.io.restapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jmaster.io.restapi.model.Product;
import jmaster.io.restapi.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository  productRepository;
	
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	public Product getProductByPid(int Pid) {
		return productRepository.findById(Pid).orElseThrow(()->new EntityNotFoundException("22"));
	}
	
	public Product getProductByname(String pname) {
		return productRepository.findByPname(pname).orElseThrow(()-> new EntityNotFoundException("26"));
	}
	
	public Product addAProduct(Product product) {
		return productRepository.save(product);
	}
	
	public void deleteProduct(int PId) {
		productRepository.deleteById(PId);
	}
	
	public boolean exitsByPname(String pname) {
		return productRepository.existsByPname(pname);
	}
	
	public Product updateProduct(int id, Product pro) {
		return productRepository.save(pro);
	}
	
	public Product findProductById(int id) {
		return productRepository.findById(id).orElseThrow(()->new EntityNotFoundException("51"));
	}
}
