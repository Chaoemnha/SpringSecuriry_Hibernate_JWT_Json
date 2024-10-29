package jmaster.io.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//Đeo bt sao chạy đc nma nếu bài cứ bị báo lỗi thiếu bean thì cứ package nào ta chèn nó vào trong ComponentScan
//ComponentScan để điều hướng cho Spring Data JPA chỗ để quét tìm các bean
public class RestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestapiApplication.class, args);
	}
}
