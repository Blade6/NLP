package NLP;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import CLAWS_improved.Claws;
import MaxMatchingMethod.MM_segment;
import MaxProbability.MP_segment;
import viterbi.viterbi;

public class Layout {
	private JTextArea inputArea;
	private JLabel label;
	private JTextField para;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	private JButton button6;
	private JButton button7;
	private JButton button8;
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
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public void begin(){
		inputArea = new JTextArea(12, 50);
		inputArea.setLineWrap(true);
		label = new JLabel("maxLen:");
		para = new JTextField("3", 5);
		button1 = new JButton("正向匹配");
		button2 = new JButton("逆向匹配");
		button3 = new JButton("双向匹配");
		button4 = new JButton("概率最大分词");
		button5 = new JButton("MM+CLAWS");
		button6 = new JButton("CLAWS");
		button7 = new JButton("MM+viterbi");
		button8 = new JButton("viterbi");
		outputArea = new JTextArea(12, 50);
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
		panel_2.add(button7);
		panel_2.add(button8);
		panel_3.add(outputArea);
		
		button1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				click_action("MM_segment", 1, false, false);
			}
		});
		button2.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				click_action("MM_segment", -1, false, false);
			}
		});
		button3.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				click_action("MM_segment", 2, false, false);
			}
		});
		button4.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				click_action("MP_segment", 0, false, false);				
			}
		});
		button5.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				click_action("CLAWS", 0, true, false);				
			}
		});
		button6.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				click_action("CLAWS", 0, false, false);				
			}
		});
		button7.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				click_action("viterbi", 0, false, true);				
			}
		});
		button8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				click_action("vierbi", 0, false, false);				
			}
		});
	}
	
	public void click_action(String button, int MM_choice, boolean Claws_flag, boolean viterbi_flag) {
		String s = inputArea.getText();
		String result = null;
		long startTime = System.currentTimeMillis();
		
		if(!s.equals("")) {
			switch (button) {
			case "MM_segment":
				result = MM_segment_action(s, MM_choice);
				break;
			case "MP_segment":
				result = MP_segment_action(s);
				break;
			case "CLAWS":
				result = Claws_action(s, Claws_flag);
				break;
			case "viterbi":
				result = viterbi_action(s, viterbi_flag);
				break;
			}
		} else result = "No input!";
		
		long stopTime = System.currentTimeMillis();
		String timeLog = "Running Time:" + (stopTime-startTime) + "ms";
		
		outputArea.setText(result+"\n"+timeLog);
	}
	
	public String MM_segment_action(String s, int MM_choice){
		MAXLEN = Integer.parseInt(para.getText());
		
		try {
			MM_segment fenci_demo = MM_segment.getInstance();
			if (MM_choice == 1) return fenci_demo.MM(s, MAXLEN, true);
			else if (MM_choice == -1) return fenci_demo.MM(s, MAXLEN, false);
			else return fenci_demo.twoWay(s, MAXLEN);
		} catch (FileNotFoundException e) {
			return "File not found!";
		} catch (IOException e) {
			return "Error happens!";
		}		

	}
	
	public String MP_segment_action(String s) {
		try {
			MP_segment fenci_demo = MP_segment.getInstance();
			return fenci_demo.init(s);
		} catch (FileNotFoundException e) {
			return "File not found!";
		} catch (NumberFormatException e) {
			return "Error happens!";
		} catch (IOException e) {
			return "Error happens!";
		}				
	}
	
	public String Claws_action(String s, boolean Claws_flag) {
		try {
			if (Claws_flag) {
				MM_segment fenci_demo = MM_segment.getInstance();
				s = fenci_demo.twoWay(s, MAXLEN);
			}
			
			Claws biaozhu_demo = Claws.getInstance();
			return biaozhu_demo.init(s);
		} catch (IOException e) {
			return "Error happens!";
		}	
	}
	
	public String viterbi_action(String s, boolean viterbi_flag) {
		try {
			if (viterbi_flag) {
				MM_segment fenci_demo = MM_segment.getInstance();
				s = fenci_demo.twoWay(s, MAXLEN);				
			}
			
			viterbi biaozhu_demo = viterbi.getInstance();
			return biaozhu_demo.init(s);
		} catch (NumberFormatException e) {
			return "Error happens!";
		} catch (IOException e) {
			return "Error happens!";
		}
	}

}
