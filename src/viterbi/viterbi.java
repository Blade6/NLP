package viterbi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class viterbi {

	private static viterbi singleton;
	private static HashMap<String, Double> transMatrix = new HashMap<String, Double>();
	private static HashMap<String, Double> launchMatrix = new HashMap<String, Double>();
	private static String[] state;
	
	private viterbi() throws NumberFormatException, IOException {
		initState();
		initProbability();
		initMatrix("resources/viterbi/trans.txt", transMatrix);
		initMatrix("resources/viterbi/launch.txt", launchMatrix);
	}
	
	public static viterbi getInstance() throws NumberFormatException, IOException {
		if (singleton == null)
			singleton = new viterbi();
		return singleton;
	}
	
	public String init(String observations) {
		String[] step = observations.split("/");
		String biaozhu_init = Main(step);
		return process(step, biaozhu_init.split(" "));
	}
	
	/*
	 * 一一把词和词性对应 
	 */
	public String process(String[] str, String[] biaozhu) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length; i++) result.append(str[i]+"/"+biaozhu[i]+"  ");
		return result.toString();
	}
	
	public String Main(String[] step){
		int T = step.length, N = state.length;
		double[][] vi = new double[N][T];
		backpoint[][] bp = new backpoint[N][T];
		for(int i=0;i<N;i++){
			if (launchMatrix.containsKey(step[0]+"|"+state[i])) {
				vi[i][0] = transMatrix.get(state[i]) * launchMatrix.get(step[0]+"|"+state[i]);
			} else {
				vi[i][0] = 0;
			}
			bp[i][0] = new backpoint(i);
		}
		for(int j=1;j<T;j++){
			for(int i=0;i<N;i++){
				double max = -1;
				for(int k=0;k<N;k++){
					if (transMatrix.containsKey(state[k]+"|"+state[i])) {
						if(vi[k][j-1] * transMatrix.get(state[k]+"|"+state[i])>max){
							max = vi[k][j-1] * transMatrix.get(state[k]+"|"+state[i]);
							bp[i][j] = new backpoint(k);
							bp[i][j].forward = bp[k][j-1];
						}
					}
				}
				if (launchMatrix.containsKey(step[j]+"|"+state[i])) {
					vi[i][j] = max * launchMatrix.get(step[j]+"|"+state[i]);
				} else {
					vi[i][j] = 0;
				}
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
		while(iterator.forward!=null){
			result.insert(0, state[iterator.value]+" ");
			iterator = iterator.forward;
		}
		return result.toString();
	}
	
	/*
	 * 读入词性，构建隐序列 
	 */
	public void initState() throws IOException {
		BufferedReader input = new BufferedReader(
				new FileReader("resources/viterbi/cixing.txt"));
		String cixing = null;
		HashSet<String> cx_Set = new HashSet<>();
		
		while ((cixing = input.readLine()) != null) {
			cx_Set.add(cixing);
		}
		
		state = new String[cx_Set.size()];
		cx_Set.toArray(state);
		
		input.close();
	}
	
	/*
	 *  转移矩阵的初始概率赋值
	 *  所有词性的初始概率之和为1
	 */
	public void initProbability() throws NumberFormatException, IOException {
		BufferedReader input = new BufferedReader(new FileReader(
				"resources/viterbi/trans_init.txt"));
		String line = null;	
		
		while((line = input.readLine()) != null) {
			String[] words = line.split("\\s+");
			transMatrix.put(words[0], Double.parseDouble(words[1]));
		}
		
		input.close();		
	}
	
	/*
	 * 初始化矩阵
	 */
	public void initMatrix(String filename, HashMap<String, Double> hashMap)
			throws NumberFormatException, IOException{
		BufferedReader input = new BufferedReader(new FileReader(filename));	
		String line = null;
		
		while((line = input.readLine()) != null) {
			String[] words = line.split("\\s+");
			hashMap.put(words[0]+"|"+words[1], Double.parseDouble(words[2]));
		}
		
		input.close();
	}
	
	
}
