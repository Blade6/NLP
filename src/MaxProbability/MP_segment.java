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
		//查看过程
//		for(int i=0;i<candidates.size();i++){
//			System.out.println(candidates.get(i).index+" "+candidates.get(i).content
//					+" "+candidates.get(i).probability+" "+candidates.get(i).best);
//		}
		return getResult();
	}
	
	public String getResult(){
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
		return result;
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
	 * 生成候选词 
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
	 * 返回某个候选词的概率 
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
