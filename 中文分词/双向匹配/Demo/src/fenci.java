import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class fenci {
	
	private static ArrayList<String> words;
	
	public fenci() throws FileNotFoundException{
		readData();
	}
	
	public static String twoWay(String s, int maxLen){
		String s1 = MM(s, maxLen, true);
		String s2 = MM(s, maxLen, false);
		if(s1.equals(s2)) return s1;
		else return "该句子有歧义，无法分词！";
	}
	
	/*
	 * flag为真，正向匹配，否则逆向匹配
	 */
	public static String MM(String s, int maxLen, boolean flag){
		int Len = maxLen;
		int begin = 0;
		int end = s.length();
		String str;
		String result = "";
		while(begin < end){
			if(flag){
				if(end-begin>=Len){
					str = s.substring(begin, begin+Len);
				}else{
					str = s.substring(begin, end);
				}
				if(Match(str)){
					begin += Len;
					Len = maxLen;
					result = result + str + "/";
				}else{
					Len -= 1;
					if(Len==0) return "字典中没有"+str+"字！";
				}
			}else{
				if(end-begin>=Len){
					str = s.substring(end-Len, end);
				}else{
					str = s.substring(begin, end);
				}
				if(Match(str)){
					end -= Len;
					Len = maxLen;
					result = str + "/" + result;
				}else{
					Len -= 1;
					if(Len==0) return "字典中没有"+str+"字！";
				}
			}							
		}
		return result;
	}
	
	public static boolean Match(String str){
		for(int i=0;i<words.size();i++){
			if(str.equals(words.get(i))) return true;
		}
		return false;
	}
	
	public static void readData() throws FileNotFoundException{
		words = new ArrayList<String>();
		
		java.io.File file = new java.io.File("chineseDic.txt");
		
		Scanner input = new Scanner(file);	
		
		while(input.hasNext()){
			String word_init = input.next();
			String word = word_init.substring(0, word_init.indexOf(","));
			words.add(word);
		}
		input.close();
	}

}

