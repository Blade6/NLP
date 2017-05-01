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
 * 输入：已分词的句子：一/把/青菜
 * 第一步：求出每个词包含的词性：一/s  把/n/v/l  青菜/n
 * 第二步：计算每一条路径的概率：
 * 		P(s,n,n|一，把，青菜) = P(一|s)* P(把|n)*P(青菜|n)
 * 		P(s,v,n|一，把，青菜) = P(一|s)* P(把|v)*P(青菜|n)
 * 		P(s,l,n|一，把，青菜) = P(一|s)* P(把|l)*P(青菜|n)
 * 其中，P(wi|ti) = 训练语料中wi的词性被标记为ti的次数/训练语料中ti出现的总次数
 * 第三步：取概率最大的那条路径为结果。
 * 
 */

public class Claws {
	
	private static Claws singleton;
	private static HashMap<String, String[]> dict;//词典，用于查找
	private static HashMap<String, Integer> cixing_times = new HashMap<String, Integer>();//每种词性出现的次数
	private static HashMap<String, Integer> word_times = new HashMap<String, Integer>();//每个分词出现的次数
	
	private ArrayList<String[]> Word;//记录每个分词及它的所有词性
	private HashSet<String> Road;//记录各分词的词性相互组合的路径
	
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
	 * 一一把词和词性对应 
	 */
	public String process(String[] str, String[] biaozhu) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length; i++) result.append(str[i]+"/"+biaozhu[i+1]+"  ");
		return result.toString();
	}
	
	/*
	 * 把已经分好词的字符串的每个词取出，查找有多少种词性
	 */
	public boolean first(String[] str){
		Word = new ArrayList<String[]>();
		for(int i=0;i<str.length;i++){
			String[] sub_str = searchDict(str[i]);
			if(!"Error".equals(sub_str[0])){
				String[] word = sub_str;
				Word.add(word);
			}else{
				System.out.println("字典中不存在"+sub_str[1]+"!");
				return false;
			}
		}
		return true;
	}
	
	/*
	 * 生成路径
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
	 * 求出概率最大的路径 
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
	 * 返回形如"把/v"的概率 
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
	 * 读取文件 
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
	 * 读取字典文件
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
	 * 返回某个词的词性数组 
	 */
	public String[] searchDict(String s){
		if (dict.containsKey(s)) return dict.get(s);
		return new String[]{"Error",s};
	}

}
