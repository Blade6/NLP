import java.io.FileNotFoundException;


public class clickThis {

	public static void main(String[] args) {
		viterbi demo = new viterbi();
		try {
			demo.init();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
