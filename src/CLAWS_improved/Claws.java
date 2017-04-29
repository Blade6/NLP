package CLAWS_improved;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;

public class Claws {
	
	private ArrayList<String[]> dict;//词典，用于查找
	private ArrayList<String[]> Word;//记录每个分词及它的所有词性
	private HashMap<String, Integer> cixing_times;//每种词性出现的次数
	private HashMap<String, Integer> word_times;//每个分词出现的次数
	private ArrayList<String> Road;//记录各分词的词性相互组合的路径
	
	public String init(String s) throws FileNotFoundException{
		String[] str = s.split("/");
		readDic();
		readFile();
		if(!first(str)) return "Error";
		Road = new ArrayList<String>();
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
		for(int i=0;i<Road.size();i++){
			String road = Road.get(i);
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
	public void readFile() throws FileNotFoundException{
		java.io.File file = new java.io.File("resources/cixing_times.txt");
		Scanner input = new Scanner(file);
		cixing_times = new HashMap<String, Integer>();
		while(input.hasNext()){
			String line = input.nextLine();
			String[] words = line.split("\\s+");
			cixing_times.put(words[0], Integer.parseInt(words[1]));
		}
		input.close();
		
		file = new java.io.File("resources/word_times.txt");
		input = new Scanner(file);
		word_times = new HashMap<String, Integer>();
		while(input.hasNext()){
			String line = input.nextLine();
			String[] words = line.split("\\s+");
			word_times.put(words[0], Integer.parseInt(words[1]));
		}
		input.close();
	}
	
	/*
	 * 读取字典文件
	 */
	public void readDic() throws FileNotFoundException{
		dict = new ArrayList<String[]>();
		
		java.io.File file = new java.io.File("resources/chineseDic.txt");
		Scanner input = new Scanner(file);
		
		while(input.hasNext()){
			String dict_init = input.next();
			String[] item = dict_init.split(",");
			dict.add(item);
		}
		input.close();
	}
	
	/*
	 * 返回某个词的词性数组 
	 */
	public String[] searchDict(String s){
		for(int i=0;i<dict.size();i++){
			if(s.equals(dict.get(i)[0])) return dict.get(i);
		}
		return new String[]{"Error",s};
	}

}
