package belvey.fs.processor;

import java.util.StringTokenizer;

public class MyTest {

	public static void main(String[] args) {
		StringTokenizer st = new StringTokenizer("this is a test,hello");
		StringTokenizer st2 = new StringTokenizer("this is a test,hello", ",");
		
		while (st.hasMoreTokens()) {
	         System.out.println(st.nextToken());
	     }
		
		while (st2.hasMoreTokens()) {
	         System.out.println(st2.nextToken());
	     }

	}
	
}
