import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import CLAWS_improved.Claws;
import MaxMatchingMethod.MM_segment;
import MaxProbability.MP_segment;

public class window {
	private JTextArea inputArea;
	private JLabel label;
	private JTextField para;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	private JButton button6;
	private JTextArea outputArea;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	
	private static int MAXLEN = 3;

	public void init() throws FileNotFoundException {		
		begin();
		
		JFrame frame = new JFrame();
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panel_1, BorderLayout.NORTH);
		contentPane.add(panel_2, BorderLayout.CENTER);
		contentPane.add(panel_3, BorderLayout.SOUTH);
		frame.setTitle("I love NLP");
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public void begin(){
		inputArea = new JTextArea(10, 40);
		inputArea.setLineWrap(true);
		label = new JLabel("maxLen:");
		para = new JTextField("3", 5);
		button1 = new JButton("正向匹配");
		button2 = new JButton("逆向匹配");
		button3 = new JButton("双向匹配");
		button4 = new JButton("概率最大分词");
		button5 = new JButton("CLAWS");
		button6 = new JButton("viterbi");
		outputArea = new JTextArea(10, 40);
		outputArea.setLineWrap(true);
		
		panel_1 = new JPanel();
		panel_2 = new JPanel();
		panel_3 = new JPanel();
		panel_1.add(inputArea);
		panel_2.add(label);
		panel_2.add(para);
		panel_2.add(button1);
		panel_2.add(button2);
		panel_2.add(button3);
		panel_2.add(button4);
		panel_2.add(button5);
		panel_2.add(button6);
		panel_3.add(outputArea);
		
		button1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				MM_segment_action(false, true);
			}
		});
		button2.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				MM_segment_action(false, false);
			}
		});
		button3.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				MM_segment_action(true, false);
			}
		});
		button4.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				MP_segment_action();				
			}
		});
		button5.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				Claws_action();				
			}
		});
	}
	
	public void MM_segment_action(boolean twoWay, boolean flag){
		MAXLEN = Integer.parseInt(para.getText());
		
		String s = inputArea.getText();
		String result;
		long startTime = System.currentTimeMillis();
		
		if(s.equals("")) result = "无输入";
		else{
			MM_segment fenci_demo;
			try {
				fenci_demo = new MM_segment();
				if(!twoWay) result = fenci_demo.MM(s, MAXLEN, flag);
				else result = fenci_demo.twoWay(s, MAXLEN);
			} catch (FileNotFoundException e) {
				result = "Error happens!";
			}	
		}
		long stopTime = System.currentTimeMillis();
		String timeLog = "分词时间：" + (stopTime-startTime) + "ms";
		
		outputArea.setText(result+"\n"+timeLog);
	}
	
	public void MP_segment_action() {
		String s = inputArea.getText();
		String result;
		long startTime = System.currentTimeMillis();
		
		if(s.equals("")) result = "无输入";
		else{
			MP_segment fenci_demo;
			try {
				fenci_demo = new MP_segment();
				result = fenci_demo.init(s);
			} catch (FileNotFoundException e) {
				result = "Error happens!";
			}			
		}
		long stopTime = System.currentTimeMillis();
		String timeLog = "分词时间：" + (stopTime-startTime) + "ms";
		
		outputArea.setText(result+"\n"+timeLog);
	}
	
	public void Claws_action() {
		String s = inputArea.getText();
		String result;
		long startTime = 0;
		
		if(s.equals("")) result = "无输入";
		else{
			try {
				MM_segment fenci_demo = new MM_segment();
				String fenci_result = fenci_demo.twoWay(s, MAXLEN);
				startTime = System.currentTimeMillis();
				Claws biaozhu_demo = new Claws();
				result = biaozhu_demo.init(fenci_result);
			} catch (FileNotFoundException e) {
				result = "File Not Found!";
			}
		}
		long stopTime = System.currentTimeMillis();
		String timeLog = "标注时间：" + (stopTime-startTime) + "ms";
		
		outputArea.setText(result+"\n"+timeLog);
	}

}
