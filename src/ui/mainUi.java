package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class mainUi extends JFrame {

	private JPanel contentPane;
	private JTextField textFilePath;
	private File selectFilePath = new File("c:/");
	private File saveFilePath;
	private JLabel lblJaroWinklerAcceptance;
	private JTextField textAcceptanceCriteria;
	private JLabel lblCountOfWords;
	private JTextField textCountOfWord;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainUi frame = new mainUi();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public mainUi() {
		setResizable(false);
		setTitle("WordCounter with Similarity Coded By \u015EAMAN");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 438, 114);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSourceCsvFile = new JLabel("Source CSV File:");
		lblSourceCsvFile.setBounds(10, 11, 130, 14);
		contentPane.add(lblSourceCsvFile);
		
		textFilePath = new JTextField();
		textFilePath.setEditable(false);
		textFilePath.setBounds(115, 8, 171, 20);
		contentPane.add(textFilePath);
		textFilePath.setColumns(10);
		
		JButton btnSelectFile = new JButton("Select File");
		btnSelectFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				do_btnSelectFile_mousePressed(e);
			}
		});
		btnSelectFile.setBounds(296, 7, 125, 23);
		contentPane.add(btnSelectFile);
		
		JButton btnStart = new JButton("Start");
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				do_btnStart_mousePressed(e);
			}
		});
		btnStart.setBounds(296, 36, 125, 39);
		contentPane.add(btnStart);
		
		lblJaroWinklerAcceptance = new JLabel("Jaro Winkler Acceptance Criteria:");
		lblJaroWinklerAcceptance.setBounds(10, 36, 198, 14);
		contentPane.add(lblJaroWinklerAcceptance);
		
		textAcceptanceCriteria = new JTextField();
		textAcceptanceCriteria.setText("0.9");
		textAcceptanceCriteria.setBounds(207, 33, 79, 20);
		contentPane.add(textAcceptanceCriteria);
		textAcceptanceCriteria.setColumns(10);
		
		lblCountOfWords = new JLabel("Count of words that shown:");
		lblCountOfWords.setBounds(10, 61, 171, 14);
		contentPane.add(lblCountOfWords);
		
		textCountOfWord = new JTextField();
		textCountOfWord.setText("2");
		textCountOfWord.setBounds(207, 58, 79, 20);
		contentPane.add(textCountOfWord);
		textCountOfWord.setColumns(10);
	}
	protected void do_btnSelectFile_mousePressed(MouseEvent e) {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal==0){
			selectFilePath = chooser.getSelectedFile();
			textFilePath.setText(selectFilePath.getName());
		}else{}
		
	}
	protected void do_btnStart_mousePressed(MouseEvent e) {
		if(selectFilePath.getAbsolutePath().equals("c:\\"))
			JOptionPane.showMessageDialog(this, "Word Source CSV file must be selected!");
		else{
			//Main section
			//Variables
			JaroWinklerHashMap<String, Integer> counter = new JaroWinklerHashMap<>();
			int lineNumber=1;
			
			
			
			//Define save file path.
			saveFilePath=new File(selectFilePath.getParent()+"/results.csv");
			
			try {
				InputStream in = new FileInputStream(selectFilePath);
				//Reader reader = new InputStreamReader(in);
				Reader reader = new InputStreamReader(in, Charset.forName("UTF-8"));
				BufferedReader read=new BufferedReader(reader);
				String line;
				while ((line=read.readLine())!=null) {
					if(lineNumber==1)
						line=line.substring(1, line.length());
					
					String[] dividedStrings = line.toLowerCase().split(" ");
					for (String dividedString : dividedStrings) {
						String key = counter.containsKeyWithJaro(dividedString, Double.valueOf(textAcceptanceCriteria.getText()));
						if(key!=null)
							counter.replace(key, (counter.get(key))+1);
						else
							counter.put(dividedString, 1);
						
					}
					lineNumber++;
				}
				
				
				// New reader format has been cancelled.
				//Files.lines(Paths.get(selectFilePath.getAbsolutePath()),Charset.forName("UTF-8")).forEach(line ->{
				BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePath.getAbsolutePath()));
				for (String str : counter.keySet()) {
					if(counter.get(str)>=Integer.valueOf(textCountOfWord.getText()))
						writer.write(str+","+counter.get(str)+"\n");
						//System.out.println(str +" -> " + counter.get(str));
				}
				
				writer.flush();
				writer.close();
				
				
				read.close();
				reader.close();
				in.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(this, "Success!");
		}
	}
}
