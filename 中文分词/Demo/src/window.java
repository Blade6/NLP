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

public class window {
	private static JTextArea inputArea;
	private static JLabel label;
	private static JTextField para;
	private static JButton button1;
	private static JButton button2;
	private static JButton button3;
	private static JTextArea outputArea;
	private static JPanel panel_h;
	private static JPanel panel_m;
	private static JPanel panel_l;
	
	private static fenci fenci_demo;
	private static int MAXLEN = 3;

	public static void main(String[] args) throws FileNotFoundException {		
		init();
		
		JFrame frame = new JFrame();
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panel_h, BorderLayout.NORTH);
		contentPane.add(panel_m, BorderLayout.CENTER);
		contentPane.add(panel_l, BorderLayout.SOUTH);
		frame.setTitle("中文分词系统");
		frame.setSize(500, 460);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		fenci_demo = new fenci();
	}
	
	public static void init(){
		inputArea = new JTextArea(10, 40);
		inputArea.setLineWrap(true);
		label = new JLabel("maxLen:");
		para = new JTextField("3", 5);
		button1 = new JButton("正向匹配");
		button2 = new JButton("逆向匹配");
		button3 = new JButton("双向匹配");
		outputArea = new JTextArea(10, 40);
		outputArea.setLineWrap(true);
		
		panel_h = new JPanel();
		panel_m = new JPanel();
		panel_l = new JPanel();
		panel_h.add(inputArea);
		panel_m.add(label);
		panel_m.add(para);
		panel_m.add(button1);
		panel_m.add(button2);
		panel_m.add(button3);
		panel_l.add(outputArea);
		
		button1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				action(false, true);
			}
		});
		button2.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				action(false, false);
			}
		});
		button3.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				action(true, false);
			}
		});
	}
	
	public static void action(boolean twoWay, boolean flag){
		MAXLEN = Integer.parseInt(para.getText());
		
		String s = inputArea.getText();
		String result;
		long startTime = System.currentTimeMillis();
		
		if(s.equals("")) result = "无输入";
		else{
			if(!twoWay) result = fenci_demo.MM(s, MAXLEN, flag);
			else result = fenci_demo.twoWay(s, MAXLEN);
		}
		long stopTime = System.currentTimeMillis();
		String timeLog = "分词时间：" + (stopTime-startTime) + "ms";
		
		outputArea.setText(result+"\n"+timeLog);
	}

}
