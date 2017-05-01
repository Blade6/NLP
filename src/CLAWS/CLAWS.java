package CLAWS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 *   P(s,v,n|һ���ѣ���ˣ�
 * = P(һ���ѣ����|s,v,n)*P(s,v,n)      ��Ҷ˹����ĸʡ��
 * = P(һ|s)*P(��|v)*P(���|n)*P(s|<BOS>)*P(v|s)*P(n|v)*P(n|<EOS>)   ��Ԫ�﷨ģ��
 * 
 * P(v|s)=N(s,v)/N(s *)
 * P(��|v)=N(��,v)/N(v) 
 * 
 */

public class CLAWS {
	
	private static CLAWS singleton;
	private HashMap<String, String[]> dict;//�ʵ䣬���ڲ���
	private HashMap<String, Integer> cixing_times = new HashMap<String, Integer>();//ÿ�ִ��Գ��ֵĴ���
	private HashMap<String, Integer> word_times = new HashMap<String, Integer>();//ÿ���ִʳ��ֵĴ���
	private HashMap<String, Integer> cixing_sequence = new HashMap<String, Integer>();//�������Եĳ��ִ���
	
	private ArrayList<String[]> Word;//��¼ÿ���ִʼ��������д���
	private ArrayList<String> Road;//��¼���ִʵĴ����໥��ϵ�·��
	
	private CLAWS() throws IOException {
		readDic();
		readFile("resources/CLAWS/cixing_times.txt", cixing_times);
		readFile("resources/CLAWS/word_times.txt", word_times);
		readFile("resources/CLAWS/cixing_sequence_times.txt", cixing_sequence);
	}
	
	public static CLAWS getInstance() throws IOException {
		if (singleton == null)
			singleton = new CLAWS();
		return singleton;
	}
	
	/*
	 * ���ִܷʺ�ľ��ӣ�ע��ִʷ���б��
	 */
	public String init(String s) throws IOException{
		String[] str = s.split("/");
		if(!first(str)) return "Error";
		Road = new ArrayList<String>();
		second(0, "");
		return third(str);
	}
	
	/*
	 * ���Ѿ��ֺôʵ��ַ�����ÿ����ȡ���������ж����ִ���
	 */
	public boolean first(String[] str){
		Word = new ArrayList<String[]>();
		for(int i=0;i<str.length;i++){
			String[] sub_str = searchDict(str[i]);
			if(!"Error".equals(sub_str[0])){
				String[] word = sub_str;
				Word.add(word);
			}else{
				System.out.println("�ֵ��в�����"+sub_str[1]+"!");
				return false;
			}
		}
		return true;
	}
	
	/*
	 * ����·��
	 */
	public void second(int t, String w){	
		if(t==Word.size()){
			Road.add(w);
			return ;
		}
		String ww = null;
		for(int i=1;i<Word.get(t).length;i++){
			ww = Word.get(t)[i];
			second(t+1, w+" "+ww);
		}
	}
	
	/*
	 * �����������·�� 
	 */
	public String third(String[] str){
		double max = -1;
		String Result = null;
		for(int i=0;i<Road.size();i++){
			String road = Road.get(i);
			String[] roads = road.split("\\s+");
			double pro = 1.0;
			for(int j=0;j<roads.length-1;j++){
				String s1 = str[j] + "/" + roads[j];
				pro *= getProbability_1(s1, roads[j]);
			}
			pro *= getProbability_2("<BOS>", roads[0]);
			for(int k=1;k<roads.length-1;k++){
				pro *= getProbability_2(roads[k-1], roads[k]);
			}
			pro *= getProbability_2(roads[roads.length-2], "<EOS>");
			if(pro > max){
				max = pro;
				Result = road;
			}
		}
		return Result;
	}
	
	/*
	 * ��������"��/v"�ĸ��� 
	 */
	public double getProbability_1(String s, String cixing){
		Double child = 0.0, parent = 1.0;
		if(word_times.containsKey(s)){
			child = (double) word_times.get(s);
		}
		
		if(cixing_times.containsKey(cixing)){
			parent = (double) cixing_times.get(cixing);
		}
		
		return child / parent;
	}
	
	/*
	 * ��������"s|<BOS>"�ĸ��� 
	 */
	public double getProbability_2(String leftword, String rightword){
		Double child = 0.0, parent = 1.0;
		String str = leftword + "," + rightword;
		
		if(cixing_sequence.containsKey(str)){
			child = (double) cixing_sequence.get(str);
		}
		
		if(cixing_sequence.containsKey(leftword)){
			parent = (double) cixing_sequence.get(leftword);
		}
		
		return child / parent;
	}
	
	/*
	 * ��ȡ�ļ� 
	 */
	public void readFile(String filename, HashMap<String, Integer> hashMap) throws NumberFormatException, IOException{
		BufferedReader input = new BufferedReader(new FileReader(filename));
		String line = null;
		while((line = input.readLine()) != null){
			String[] words = line.split("\\s+");
			hashMap.put(words[0], Integer.parseInt(words[1]));
		}
		input.close();
	}
	
	/*
	 * ��ȡ�ֵ��ļ�
	 */
	public void readDic() throws IOException{
		dict = new HashMap<>();
		
		BufferedReader input = new BufferedReader(new FileReader("resources/MM_segment/chineseDic.txt"));
		String dict_init = null;
		
		while((dict_init = input.readLine()) != null){
			String[] item = dict_init.split(",");
			dict.put(item[0], item);
		}
		input.close();
	}
	
	/*
	 * ����ĳ���ʵĴ������� 
	 */
	public String[] searchDict(String s){
		if (dict.containsKey(s)) return dict.get(s);
		return new String[]{"Error",s};
	}

}
