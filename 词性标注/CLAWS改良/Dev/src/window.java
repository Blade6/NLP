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
	private JTextArea inputArea;
	private JLabel label;
	private JTextField para;
	private JButton button1;
	private JButton button2;
	private JTextArea outputArea;
	private JPanel panel_h;
	private JPanel panel_m;
	private JPanel panel_l;
	
	private fenci fenci_demo;
	private CLAWS  claws_demo;
	private int MAXLEN = 3;

	public void init() throws FileNotFoundException {		
		begin();
		
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
	
	public void begin(){
		inputArea = new JTextArea(10, 40);
		inputArea.setLineWrap(true);
		label = new JLabel("maxLen:");
		para = new JTextField("3", 5);
		button1 = new JButton("中文分词");
		button2 = new JButton("词性标注");
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
		panel_l.add(outputArea);
		
		button1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					action(false);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		button2.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					action(true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}				
			}
		});
	}
	
	public void action(boolean flag) throws FileNotFoundException{
		MAXLEN = Integer.parseInt(para.getText());
		
		String s = inputArea.getText();
		String result = null;
		long startTime = System.currentTimeMillis();
		
		if(s.equals("")) result = "无输入";
		else{
			result = fenci_demo.twoWay(s, MAXLEN);
			if(flag && !result.equals("该句子有歧义，无法分词！")){
				claws_demo = new CLAWS();
				result = result +"\n" + claws_demo.init(result);
			}
		}
		long stopTime = System.currentTimeMillis();
		String timeLog = "分词时间：" + (stopTime-startTime) + "ms";
		
		outputArea.setText(result+"\n"+timeLog);
	}

}
