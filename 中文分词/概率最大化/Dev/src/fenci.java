import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class fenci {

	private static ArrayList<WordFre> words;
	private static ArrayList<Word> candidates;
	
	public static void main(String[] args) throws FileNotFoundException {
		String s = "��ϳɷ���ʱ";
		readWordFre();
		createCandidate(s);
		build(s);
		for(int i=0;i<candidates.size();i++){
			System.out.println(candidates.get(i).index+" "+candidates.get(i).content
					+" "+candidates.get(i).probability+" "+candidates.get(i).best);
		}
		print();
	}
	
	public static void print(){
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
	public static void build(String s){
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
	public static void createCandidate(String s){
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
	public static boolean exist(String s){
		for(int i=0;i<words.size();i++){
			if(s.equals(words.get(i).word)) return true;
		}
		return false;
	}
	
	/*
	 * ����ĳ����ѡ�ʵĸ��� 
	 */
	public static double Fre(String s){
		int i;
		for(i=0;i<words.size();i++){
			if(s.equals(words.get(i).word)) break;
		}
		return words.get(i).frequency;
	}
	
	public static void readWordFre() throws FileNotFoundException{
		words = new ArrayList<WordFre>();
		
		java.io.File file = new java.io.File("wordFrequency.txt");
		
		Scanner input = new Scanner(file);	
		
		while(input.hasNext()){
			WordFre word_input = new WordFre();
			String word_init = input.next();
			String word = word_init.substring(0, word_init.indexOf(','));
			double frequency = Double.parseDouble(
					word_init.substring(word_init.lastIndexOf(',')+1,
							word_init.lastIndexOf('%')));
			word_input.word = word;
			word_input.frequency = frequency;
			words.add(word_input);
		}
		input.close();
	}
	
}
