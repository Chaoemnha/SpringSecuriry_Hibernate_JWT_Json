package jmaster.io.restapi;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleApplication implements CommandLineRunner{
	@Override
		public void run(String... args){
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
			System.out.println("Hello");
			scanner.close();
	}
}
