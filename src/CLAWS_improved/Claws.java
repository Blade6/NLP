package CLAWS_improved;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/*
 * ���룺�ѷִʵľ��ӣ�һ/��/���
 * ��һ�������ÿ���ʰ����Ĵ��ԣ�һ/s  ��/n/v/l  ���/n
 * �ڶ���������ÿһ��·���ĸ��ʣ�
 * 		P(s,n,n|һ���ѣ����) = P(һ|s)* P(��|n)*P(���|n)
 * 		P(s,v,n|һ���ѣ����) = P(һ|s)* P(��|v)*P(���|n)
 * 		P(s,l,n|һ���ѣ����) = P(һ|s)* P(��|l)*P(���|n)
 * ���У�P(wi|ti) = ѵ��������wi�Ĵ��Ա����Ϊti�Ĵ���/ѵ��������ti���ֵ��ܴ���
 * ��������ȡ������������·��Ϊ�����
 * 
 */

public class Claws {
	
	private static Claws singleton;
	private static HashMap<String, String[]> dict;//�ʵ䣬���ڲ���
	private static HashMap<String, Integer> cixing_times = new HashMap<String, Integer>();//ÿ�ִ��Գ��ֵĴ���
	private static HashMap<String, Integer> word_times = new HashMap<String, Integer>();//ÿ���ִʳ��ֵĴ���
	
	private ArrayList<String[]> Word;//��¼ÿ���ִʼ��������д���
	private HashSet<String> Road;//��¼���ִʵĴ����໥��ϵ�·��
	
	private Claws() throws IOException {
		readDic();
		readFile("resources/CLAWS/cixing_times.txt", cixing_times);
		readFile("resources/CLAWS/word_times.txt", word_times);
	}
	
	public static Claws getInstance() throws IOException {
		if (singleton == null)
			singleton = new Claws();
		return singleton;
	}
	
	public String init(String s) throws FileNotFoundException{
		String[] str = s.split("/");
		if(!first(str)) return "Error";
		Road = new HashSet<>();
		second(0, "");
		String biaozhu_init = third(str);
		return process(str, biaozhu_init.split(" "));
	}
	
	/*
	 * һһ�Ѵʺʹ��Զ�Ӧ 
	 */
	public String process(String[] str, String[] biaozhu) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length; i++) result.append(str[i]+"/"+biaozhu[i+1]+"  ");
		return result.toString();
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
		Iterator<String> iterator = Road.iterator();
		while (iterator.hasNext()) {
			String road = iterator.next();
			String[] roads = road.split("\\s+");
			double pro = 1.0;
			for(int j=0;j<roads.length-1;j++){
				String s1 = str[j] + "/" + roads[j];
				pro *= getProbability(s1, roads[j]);
			}
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
	public double getProbability(String s, String cixing){
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
	 * ��ȡ�ļ� 
	 */
	public void readFile(String filename, HashMap<String, Integer> hashMap) throws IOException{
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
	public void readDic() throws IOException {
		dict = new HashMap<>();
		
		BufferedReader input = new BufferedReader(new FileReader(
				"resources/MM_segment/chineseDic.txt"));
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
