package CLAWS_improved;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;

public class Claws {
	
	private ArrayList<String[]> dict;//�ʵ䣬���ڲ���
	private ArrayList<String[]> Word;//��¼ÿ���ִʼ��������д���
	private HashMap<String, Integer> cixing_times;//ÿ�ִ��Գ��ֵĴ���
	private HashMap<String, Integer> word_times;//ÿ���ִʳ��ֵĴ���
	private ArrayList<String> Road;//��¼���ִʵĴ����໥��ϵ�·��
	
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
	 * ��ȡ�ֵ��ļ�
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
	 * ����ĳ���ʵĴ������� 
	 */
	public String[] searchDict(String s){
		for(int i=0;i<dict.size();i++){
			if(s.equals(dict.get(i)[0])) return dict.get(i);
		}
		return new String[]{"Error",s};
	}

}
