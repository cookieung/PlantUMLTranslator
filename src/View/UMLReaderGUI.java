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
import Model.Diagram;


public class UMLReaderGUI extends JFrame {
	
	private UMLReader umlReader;
	private Container contents,contentResult,contentNameFile,contentResultAll;
	private LayoutManager layout,layoutResult,layoutNameFile,layoutResultAll;
	private JButton browseButton, countButton ,clearButton,addButton,convertButton;
	private JTextField inputSource;
	private JTextField nameFile;
	private JTextArea result,cspFile;
	private JLabel label;
	private JFileChooser filechooser;
	private JPanel panel1;
	private JPanel panel2;
	private File file;
	private JScrollPane scroll1,scroll2;
	
	private ArrayList<Diagram> diagrams;
	private PlantReader plantReader;
	private ArrayList<Map<String, ArrayList<String>>> allUML;

	public UMLReaderGUI(UMLReader reader) {
		this.umlReader = reader;
		this.setTitle("Main Reader");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		initController();
		initComponents();
		this.pack();
	}
	
	private void initController(){
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
		addButton = new JButton("Add File");
		addButton.setFont(new Font("Courier", Font.PLAIN,15));
		convertButton = new JButton("Convert to CSP");
		convertButton.setFont(new Font("Courier", Font.PLAIN,15));
		filechooser = new JFileChooser();
		label =new JLabel("File or URL name");
		label.setFont(new Font("Courier", Font.BOLD,15));
		inputSource = new JTextField(100);
		inputSource.setFont(new Font("Courier", Font.PLAIN,15));
		nameFile = new JTextField(10);
		nameFile.setFont(new Font("Courier", Font.PLAIN,15));
		scroll1 = new JScrollPane(result);
		scroll2 = new JScrollPane(cspFile);
		panel1.add(label);
		panel1.add(inputSource);
		panel1.add(browseButton);
		panel1.add(countButton);
		panel1.add(clearButton);
		panel1.setPreferredSize(new Dimension(1600,40));
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
		panel2.add(contentResult);
		panel2.setPreferredSize(new Dimension(2000,1000));
		contents.add(panel1);
		contents.add(panel2);

		this.add(contents);
		initActionListener();
		
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
				nameFile.setText(umlReader.fileName(url+""));
			}
			else {
				result.append(umlReader.readAllLine(url));
				nameFile.setText(nameFile.getText()+","+umlReader.fileName(url+""));
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
			for (int i = 0; i < a.size(); i++) {
				diagrams =  plantReader.translateToDiagram(a.get(i));
				
			}
			
			cspFile.append("DIAGRAM :\n");
			for (int i = 0; i < diagrams.size(); i++) {
				cspFile.append(diagrams.get(i).getName()+" : \n");
				cspFile.append(diagrams.get(i).getProcesses().getProcessListByName()+"\n");
				
			}
			
			Set<String> allProcess = plantReader.showAllProcess();
			cspFile.append("channel "+allProcess.toString().substring(1, allProcess.toString().length()-1)+"\n\n");
			
			Set<String> allFrameProcess = plantReader.getFrameChannel();
			cspFile.append("channel "+allFrameProcess.toString().substring(1, allFrameProcess.toString().length()-1)+"\n\n");
			
			Object[] s = plantReader.showAllStateDiagram().toArray();
			for (int i = 0; i < s.length; i++) {
				cspFile.append(s[i]+"\n");
			}
			

			

//			cspFile.append("\nShow Relation of State Diagram :\n");
			cspFile.append(plantReader.showRelationOfStateDiagram()+"\n");
			
//			cspFile.append("\nShow All Trace of Message :\n");
			cspFile.append(plantReader.showAllTraceOfMessage()+"\n");
			
//			cspFile.append("\nShow Relation of All Message :\n");
			cspFile.append(plantReader.showRelationOfAllMessage()+"\n");
			
			cspFile.append(plantReader.showRelationWithSMIAndMSG()+"\n");
			
			cspFile.append(plantReader.showSequenceDiagram()+"\n");
			
//			cspFile.append("Relation between Sequence:\n");
			cspFile.append(plantReader.showTheRelationBetweenSequenceDiagramAndMessage()+"\n");

			cspFile.append(plantReader.showAssert());
			
			cspFile.append(plantReader.getRelationFrameWithSequenceDiagram());
			


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
	
}
