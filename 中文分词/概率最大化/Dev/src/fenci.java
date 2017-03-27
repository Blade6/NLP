import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class fenci {

	private HashMap<String, Double> words;
	private ArrayList<Word> candidates;
	
	public void init(String s) throws FileNotFoundException {
		readWordFre();
		createCandidate(s);
		build(s);
		for(int i=0;i<candidates.size();i++){
			System.out.println(candidates.get(i).index+" "+candidates.get(i).content
					+" "+candidates.get(i).probability+" "+candidates.get(i).best);
		}
		print();
	}
	
	public void print(){
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
		System.out.println(result);
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
				String leftLetter = s.substring(candidates.get(i).index-1, candidates.get(i).index);
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
		candidates = new ArrayList<Word>();
		for(int i=0;i<s.length();i++){
			for(int j=i+1;j<=s.length();j++){
				String subStr = s.substring(i, j);
				if(!exist(subStr)) continue; 
				Word candidate = new Word();
				candidate.index = i;
				candidate.content = subStr;
				candidates.add(candidate);
			}
		}
	}
	
	/*
	 * ���Ҹ��ʱ����Ƿ���ڸô� 
	 */
	public boolean exist(String s){
		if(words.containsKey(s)) return true;
		return false;
	}
	
	/*
	 * ����ĳ����ѡ�ʵĸ��� 
	 */
	public double Fre(String s){
		return words.get(s);
	}
	
	public void readWordFre() throws FileNotFoundException{
		words = new HashMap<String, Double>();
		
		java.io.File file = new java.io.File("wordFrequency.txt");
		
		Scanner input = new Scanner(file);	
		
		while(input.hasNext()){
			String word_init = input.next();
			String word = word_init.substring(0, word_init.indexOf(','));
			double frequency = Double.parseDouble(
					word_init.substring(word_init.lastIndexOf(',')+1,
							word_init.lastIndexOf('%')));
			words.put(word, frequency);
		}
		input.close();
	}
	
}
