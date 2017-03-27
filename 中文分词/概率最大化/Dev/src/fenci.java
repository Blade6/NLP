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
		//获取最后一个候选词的序号，这个序号就是尾词的序号，在所有尾词中，累积概率最大的尾词就是最终尾词
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
		//找到尾词后，从右到左输出每个词的最佳左邻词
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
	 * 构建概率最大分词表 
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
				//获取当前词第一个字在原字符串中的前一个字，当前词的左邻词必然是以那个字结尾的词
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
	 * 生成候选词 
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
	 * 查找概率表中是否存在该词 
	 */
	public boolean exist(String s){
		if(words.containsKey(s)) return true;
		return false;
	}
	
	/*
	 * 返回某个候选词的概率 
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
