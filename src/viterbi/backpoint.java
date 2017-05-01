package viterbi;

public class backpoint {
	
	backpoint forward = null;
	int value;//词性在状态表中的位置
	public backpoint(int value){
		this.value = value;
	}
	
}
