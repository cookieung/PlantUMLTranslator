package View;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import Controller.PlantReader;
import Controller.WriterCSP;
import Model.oop.Diagram;


public class UMLReaderGUI extends JFrame {
	
	private UMLReader umlReader;
	private Container contents,contentResult,contentNameFile,contentResultAll;
	private LayoutManager layout,layoutResult,layoutNameFile,layoutResultAll;
	private JButton browseButton, countButton ,clearButton,saveFileButton,addButton,convertButton;
	private JTextField inputSource;
	private JTextField nameFile,saveNameCSP;
	private JTextArea result,cspFile;
	private JLabel label,labelNameSaveFile;
	private JFileChooser filechooser;
	private JPanel panel1,panel2;
	private JPanel panel3;
	private File file;
	private JScrollPane scroll1,scroll2;
	
	private ArrayList<Diagram> diagrams;
	private PlantReader plantReader;
//	private ArrayList<Map<String, ArrayList<String>>> allUML;

	public UMLReaderGUI(UMLReader reader) {
		this.umlReader = reader;
		this.setTitle("Main Reader");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		initController();
		initComponents();
		initActionListener();
		this.pack();
	}
	
	private void initController(){
		refresh();
		diagrams = new ArrayList<>();
		plantReader = new PlantReader();
	}

	private void initComponents() {
		
		contents = new Container();

		contentResult = new Container();
		contentNameFile = new Container();
		contentResultAll = new Container();
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
		result = new JTextArea();
		result.setFont(new Font("Courier", Font.PLAIN,20));
		cspFile = new JTextArea();
		cspFile.setFont(new Font("Courier", Font.PLAIN,20));
		result.setEditable(false);
		cspFile.setEditable(false);
		browseButton = new JButton("Browse");
		browseButton.setFont(new Font("Courier", Font.PLAIN,15));
		countButton = new JButton("Read");
		countButton.setFont(new Font("Courier", Font.PLAIN,15));
		clearButton = new JButton("Clear");
		clearButton.setFont(new Font("Courier", Font.PLAIN,15));
		saveFileButton = new JButton("Save CSP File");
		saveFileButton.setFont(new Font("Courier", Font.PLAIN,15));
		addButton = new JButton("Add File");
		addButton.setFont(new Font("Courier", Font.PLAIN,15));
		convertButton = new JButton("Convert to CSP");
		convertButton.setFont(new Font("Courier", Font.PLAIN,15));
		filechooser = new JFileChooser();
		label =new JLabel("File or URL name");
		labelNameSaveFile =new JLabel("CSP File Name");
		label.setFont(new Font("Courier", Font.BOLD,15));
		labelNameSaveFile.setFont(new Font("Courier", Font.BOLD,15));
		inputSource = new JTextField(100);
		inputSource.setFont(new Font("Courier", Font.PLAIN,15));
		nameFile = new JTextField(10);
		nameFile.setFont(new Font("Courier", Font.PLAIN,15));
		saveNameCSP = new JTextField(100);
		saveNameCSP.setFont(new Font("Courier", Font.PLAIN,15));
		scroll1 = new JScrollPane(result);
		scroll2 = new JScrollPane(cspFile);
		panel1.add(label);
		panel1.add(inputSource);
		panel1.add(browseButton);
		panel1.add(countButton);
		panel1.add(clearButton);
		panel2.add(labelNameSaveFile);
		panel2.add(saveNameCSP);
		panel2.add(saveFileButton);
		panel1.setPreferredSize(new Dimension(1600,40));
		panel2.setPreferredSize(new Dimension(1600,40));
		contentNameFile.setLayout(new BoxLayout(contentNameFile, BoxLayout.X_AXIS));
		contentNameFile.add(nameFile);
		contentNameFile.add(addButton);
		contentNameFile.add(convertButton);
		contentResultAll.setLayout(new GridLayout(1, 2));
		scroll1.setPreferredSize(new Dimension(800,800));
		scroll2.setPreferredSize(new Dimension(800,800));
		contentResultAll.add(scroll1);
		contentResultAll.add(scroll2);
		contentResult.setLayout(new BoxLayout(contentResult, BoxLayout.Y_AXIS));
		contentResult.add(contentNameFile);
		contentResult.add(contentResultAll);
		panel3.add(contentResult);
		panel3.setPreferredSize(new Dimension(2000,1000));
		contents.add(panel1);
		contents.add(panel2);
		contents.add(panel3);

		this.add(contents);

		
	}
	
	
	public void initActionListener(){
		ActionListener listener = new ReadStateDiagramListener();
		countButton.addActionListener(listener);
		ActionListener listener2 = new ClearButtonListener();
		clearButton.addActionListener(listener2);
		inputSource.addActionListener(listener);
		ActionListener listener3 = new BrowseButtonListener();
		browseButton.addActionListener(listener3);
		ActionListener listener4 = new AddFileListener();
		addButton.addActionListener(listener4);
		ActionListener listener5 = new ConvertToDiagramListener();
		convertButton.addActionListener(listener5);
		nameFile.addActionListener(listener5);
		
		ActionListener saveFileListener = new SaveCSPFileListener();
		saveFileButton.addActionListener(saveFileListener);
		
		
	}

	public class SaveCSPFileListener implements ActionListener {
		/**
		 *  method to perform action when the button is pressed 
		 */
		public void actionPerformed(ActionEvent evt) {
			WriterCSP writer = new WriterCSP();
			writer.writing(cspFile.getText(),saveNameCSP.getText());

		}
	}
	
	public class AddFileListener implements ActionListener {
		/**
		 *  method to perform action when the button is pressed 
		 */
		public void actionPerformed(ActionEvent evt) {
			inputSource.setText("");

		}
	}
	
	public class ReadStateDiagramListener implements ActionListener {
		/**
		 *  method to perform action when the button is pressed 
		 */
		public void actionPerformed(ActionEvent evt) {
			String DICT_URL = inputSource.getText(); 
			URL url = null;
			try {
				url = new URL( DICT_URL );
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			umlReader = new UMLReader();
			if(nameFile.getText().length()==0){
				result.setText(umlReader.readAllLine(url));
				nameFile.setText(umlReader.fileName(url+"").replaceAll("-", ""));
			}
			else {
				result.append(umlReader.readAllLine(url));
				nameFile.setText(nameFile.getText()+","+umlReader.fileName(url+"").replaceAll("-", ""));
			}
		}
	}
	
	public class ConvertToDiagramListener implements ActionListener {

		/**
		 *  method to perform action when the button is pressed 
		 */
		public void actionPerformed(ActionEvent evt) {
			String DICT_URL = inputSource.getText(); 
			URL url = null;
			try {
				url = new URL( DICT_URL );
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			System.out.println("UI");
//			System.out.println(result.getText());
			ArrayList<Map<String, ArrayList<String>>> a=plantReader.readAllInput(result.getText(),nameFile.getText());
			System.out.println("Size A :"+a.size());
			for (int i = 0; i < a.size(); i++) {
				diagrams =  plantReader.translateToDiagram(a.get(i));
				
			}
			
//			cspFile.append(a+"\n");
//			cspFile.append("Diagram:\n");
////			cspFile.append("DIAGRAM :\n");
//			for (int i = 0; i < diagrams.size(); i++) {
//				cspFile.append(diagrams.get(i).getName()+" : \n");
//				for (int j = 0; j < diagrams.get(i).getProcesses().getProcessListOptList().size(); j++) {
//					cspFile.append(diagrams.get(i).getProcesses().getProcessListOptList().get(j).getName()+":");
//					cspFile.append(diagrams.get(i).getProcesses().getProcessListOptList().get(j).getAtomicProcess()+"\n");
//					
//				}
//
//				
//			}


			cspFile.append(plantReader.getCSP());
	
			cspFile.append(plantReader.showAssert());
			
			saveNameCSP.setText(inputSource.getText().replace("\\", "/").replaceAll("file:/", "").replace(".txt", ".csp").replace(".puml",".csp"));


		}
	}
	

	/**
	 * It's a controller of this application on the clearButton.
	 *
	 */
	public class ClearButtonListener implements ActionListener {
		/** method to perform action when the button is pressed */
		public void actionPerformed(ActionEvent evt) {
			inputSource.setText("");
			result.setText("");
			nameFile.setText("");
			cspFile.setText("");
			initController();
		}
	}
	
	public class BrowseButtonListener implements ActionListener {
		/** method to perform action when the button is pressed */
		public void actionPerformed(ActionEvent evt) {
			filechooser.showOpenDialog(contents);
			file= filechooser.getSelectedFile();
			inputSource.setText("file:/" +file.getAbsolutePath().toString());
		}
	}
	
	public void run() {
		this.setVisible(true);

	}
	
	public void refresh() {
		this.revalidate();

	}
	
}
