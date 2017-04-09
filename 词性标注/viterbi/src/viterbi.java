import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class viterbi {

	private HashMap<String, Double> transMatrix;
	private HashMap<String, Double> launchMatrix;
	private ArrayList<String> result;
	
	public void init() throws FileNotFoundException{
		read();
		String observations = "The bear is on the move .";
		String[] state = new String[]{"AT","BEZ","IN","NN","VB","PERIOD"};
		String result = demo(observations, state);
		System.out.println(observations);
		System.out.println(result);
	}
	
	public String demo(String observations, String[] state){
		String[] step = observations.toLowerCase().split("\\s+");
		int T = step.length, N = state.length;
		double[][] vi = new double[N][T];
		backpoint[][] bp = new backpoint[N][T];
		for(int i=0;i<N;i++){
			vi[i][0] = transMatrix.get(state[i]) * launchMatrix.get(step[0]+"|"+state[i]);
			bp[i][0] = new backpoint(i);
		}
		for(int j=1;j<T;j++){
			for(int i=0;i<N;i++){
				double max = -1;
				for(int k=0;k<N;k++){
					if(vi[k][j-1] * transMatrix.get(state[k]+"|"+state[i])>max){
						max = vi[k][j-1] * transMatrix.get(state[k]+"|"+state[i]);
						bp[i][j] = new backpoint(k);
						bp[i][j].forword = bp[k][j-1];
					}
				}
				vi[i][j] = max * launchMatrix.get(step[j]+"|"+state[i]);	
			}
		}
		
		double max = -1;
		int index = 0;
		for(int i=0;i<N;i++){
			if(vi[i][T-1]>max){
				max = vi[i][T-1];
				index = i;
			}
		}
		
		StringBuilder result = new StringBuilder(state[index]);
		backpoint iterator = bp[index][T-1];
		while(iterator.forword!=null){
			result.insert(0, state[iterator.value]+" ");
			iterator = iterator.forword;
		}
		return result.toString();
	}
	
	public void read() throws FileNotFoundException{
		//初始概率赋值
		java.io.File file = new java.io.File("init.txt");
		Scanner input = new Scanner(file);		
		transMatrix = new HashMap<String, Double>();
		
		while(input.hasNext()) {
			String[] words = input.nextLine().split("\\s+");
			transMatrix.put(words[0], Double.parseDouble(words[1]));
		}
		
		input.close();
		
		//初始化转移矩阵
		file = new java.io.File("trans.txt");
		input = new Scanner(file);	
		
		while(input.hasNext()) {
			String line = input.nextLine();
			String[] words = line.split("\\s+");
			transMatrix.put(words[0]+"|"+words[1], Double.parseDouble(words[2]));
		}
		
		input.close();
		
		//初始化发射矩阵
		file = new java.io.File("launch.txt");
		input = new Scanner(file);
		launchMatrix = new HashMap<String, Double>();
		
		while(input.hasNext()) {
			String line = input.nextLine();
			String[] words = line.split("\\s+");
			launchMatrix.put(words[0]+"|"+words[1], Double.parseDouble(words[2]));
		}
		
		input.close();
	}
	
}
