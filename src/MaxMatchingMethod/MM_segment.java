package MaxMatchingMethod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class MM_segment {
	
	private static MM_segment singleton;
	private static HashSet<String> words;
	
	private MM_segment() throws IOException{
		readData();
	}
	
	// ����ģʽ��һ���Զ����ļ����Ժ����ظ����룬�ӿ������ٶ�
	public static MM_segment getInstance() throws IOException {
		if (singleton == null)
			singleton = new MM_segment();
		return singleton;
	}
	
	public String twoWay(String s, int maxLen){	
		String s1 = MM(s, maxLen, true);
		String s2 = MM(s, maxLen, false);
		if(s1.equals(s2)) return s1;
		else return "�þ��������壬�޷��ִʣ�";
	}
	
	/*
	 * flagΪ�棬����ƥ�䣬��������ƥ��
	 */
	public String MM(String s, int maxLen, boolean flag){
		if (maxLen <= 0) return "MaxLen must larger than 0!";
		
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
					if(Len==0) return "�ֵ���û��"+str+"�֣�";
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
					if(Len==0) return "�ֵ���û��"+str+"�֣�";
				}
			}							
		}
		return result;
	}
	
	public boolean Match(String str){
		if(words.contains(str)) return true;
		return false;
	}
	
	public void readData() throws IOException{
		words = new HashSet<>();
		
		String word_init = null;
		BufferedReader input = new BufferedReader(new FileReader(
				"resources/MM_segment/chineseDic.txt"));
		while ((word_init = input.readLine()) != null) {
			String word = word_init.substring(0, word_init.indexOf(","));
			words.add(word);
		}
		input.close();
		
	}

}

