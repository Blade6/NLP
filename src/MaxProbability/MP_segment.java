package MaxProbability;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MP_segment {

	private static MP_segment singleton;
	private static HashMap<String, Double> words;
	private ArrayList<Word> candidates;
	
	private MP_segment() throws NumberFormatException, IOException {
		readWordFre();
	}
	
	public static MP_segment getInstance() throws NumberFormatException, IOException {
		if (singleton == null)
			singleton = new MP_segment();
		return singleton;
	}
	
	public String init(String s) {
		createCandidate(s);
		build(s);
		//�鿴����
//		for(int i=0;i<candidates.size();i++){
//			System.out.println(candidates.get(i).index+" "+candidates.get(i).content
//					+" "+candidates.get(i).probability+" "+candidates.get(i).best);
//		}
		return getResult();
	}
	
	public String getResult(){
		int c_index = candidates.size()-1;
		//��ȡ���һ����ѡ�ʵ���ţ������ž���β�ʵ���ţ�������β���У��ۻ���������β�ʾ�������β��
		int tailIndex = candidates.get(c_index).index;
		double max = candidates.get(c_index).probability;
		int maxTail = c_index;
		while(true){
			--c_index;
			if(candidates.get(c_index).index!=tailIndex) break;
			if(candidates.get(c_index).probability>max){
				max = candidates.get(c_index).probability;
				maxTail = c_index;
			}
		}
		//�ҵ�β�ʺ󣬴��ҵ������ÿ���ʵ�������ڴ�
		String result = candidates.get(maxTail).content;
		int location = maxTail;
		while(location!=0){
			location = candidates.get(location).best;
			result = candidates.get(location).content + "/" + result;
			if(candidates.get(location).index==0) break;
		}
		return result;
	}
	
	/*
	 * �����������ִʱ� 
	 */
	public void build(String s){
		for(int i=0;i<candidates.size();i++){
			if(candidates.get(i).index==0){
				candidates.get(i).probability = Fre(candidates.get(i).content);
			}else{
				String currentWord = candidates.get(i).content;
				double currentPro = Fre(candidates.get(i).content);
				double maxPro = -1;
				int index = 0;
				//��ȡ��ǰ�ʵ�һ������ԭ�ַ����е�ǰһ���֣���ǰ�ʵ����ڴʱ�Ȼ�����Ǹ��ֽ�β�Ĵ�
				String leftLetter = s.substring(
						candidates.get(i).index-1, candidates.get(i).index);
				for(int j=0;j<i;j++){
					String Aword = candidates.get(j).content;
					if(Aword.substring(Aword.length()-1, Aword.length()).equals(leftLetter)){
						double accumulate = currentPro * candidates.get(j).probability;
						if(accumulate>maxPro){
							maxPro = accumulate;
							index = j;
						}
					}
				}
				candidates.get(i).probability = maxPro;
				candidates.get(i).best = index;
			}
		}
	}
	
	/*
	 * ���ɺ�ѡ�� 
	 */
	public void createCandidate(String s){
		candidates = new ArrayList<>();
		for(int i=0;i<s.length();i++){
			for(int j=i+1;j<=s.length();j++){
				String subStr = s.substring(i, j);
				if(!words.containsKey(subStr)) continue; 
				Word candidate = new Word();
				candidate.index = i;
				candidate.content = subStr;
				candidates.add(candidate);
			}
		}
	}
	
	/*
	 * ����ĳ����ѡ�ʵĸ��� 
	 */
	public double Fre(String s){
		return words.get(s);
	}
	
	public void readWordFre() throws NumberFormatException, IOException{
		words = new HashMap<String, Double>();
		
		BufferedReader input = new BufferedReader(
				new FileReader("resources/MP_segment/wordFrequency.txt"));
		String word_init = null;
		
		while((word_init = input.readLine()) != null){
			String word = word_init.substring(0, word_init.indexOf(','));
			double frequency = Double.parseDouble(
					word_init.substring(word_init.lastIndexOf(',')+1,
							word_init.lastIndexOf('%')));
			words.put(word, frequency);
		}
		input.close();
	}
	
}
