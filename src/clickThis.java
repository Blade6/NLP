import java.io.FileNotFoundException;


public class clickThis {

	public static void main(String[] args) {
		window demo = new window();		
		try {
			demo.init();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
