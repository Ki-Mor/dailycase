package com.demo.junit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Arrays;

@SpringBootApplication
public class JunitApplication {

	public static void main(String[] args) {
		SpringApplication.run(JunitApplication.class, args);
	}

	/**
	 * 在main中的主要类
	 */
	public static class Calculator {

		public int calculate(String expression) {
			String[] ss = expression.split("\\+");
			System.out.println(expression + " => " + Arrays.toString(ss));
			int sum = 0;
			for (String s: ss) {
				sum += Integer.parseInt(s.trim());
			}
			return sum;
		}
	}


}
