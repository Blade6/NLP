import java.io.FileNotFoundException;


public class clickThis {

	public static void main(String[] args) {
		fenci fenci_demo = new fenci();
		try {
			fenci_demo.init("��ϳɷ���ʱ");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
