package NLP;
import java.io.FileNotFoundException;


public class Begin {

	public static void main(String[] args) {
		Layout demo = new Layout();		
		try {
			demo.init();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
