package CLAWS;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;

public class CLAWS {
	
	private ArrayList<String[]> dict;//�ʵ䣬���ڲ���
	private ArrayList<String[]> Word;//��¼ÿ���ִʼ��������д���
	private HashMap<String, Integer> cixing_times;//ÿ�ִ��Գ��ֵĴ���
	private HashMap<String, Integer> word_times;//ÿ���ִʳ��ֵĴ���
	private HashMap<String, Integer> cixing_sequence;//�������Եĳ��ִ���
	private ArrayList<String> Road;//��¼���ִʵĴ����໥��ϵ�·��
	
	/*
	 * ���ִܷʺ�ľ��ӣ�ע��ִʷ���б��
	 */
	public String init(String s) throws FileNotFoundException{
		String[] str = s.split("/");
		readDic();
		readFile();
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
		
		file = new java.io.File("resources/cixing_sequence_times.txt");
		input = new Scanner(file);
		cixing_sequence = new HashMap<String, Integer>();
		while(input.hasNext()){
			String line = input.nextLine();
			String[] words = line.split("\\s+");
			cixing_sequence.put(words[0], Integer.parseInt(words[1]));
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