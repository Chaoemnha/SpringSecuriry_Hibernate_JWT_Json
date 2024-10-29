package jmaster.io.restapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="DBProduct")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int PId;
	
	@Column(unique = true)
	private String pname;
	
	private String description;
	private double price;
	private String image;
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getPId() {
		return PId;
	}
	public Product(String pname, String description, double price, String image) {
		super();//Đây là pthuc ktao ko biến của lớp kế thừa, chắc để ngăn null, sau đó ta có thể đè thuộc tính kế thừa hoặc custom thêm biến ms
		this.pname = pname;
		this.description = description;
		this.price = price;
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setPId(int id) {
		this.PId = id;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Product() {
		super();
		// TODO Auto-generated constructor stub
		this.pname = "";
		this.description = "";
		this.price = 0;
		this.image = "";
	}
	
}
