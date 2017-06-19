package View;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
		layout = new BoxLayout(contents, BoxLayout.Y_AXIS);
		layoutResult = new BoxLayout(contentResult, BoxLayout.Y_AXIS);
		layoutNameFile = new BoxLayout(contentNameFile, BoxLayout.X_AXIS);
		layoutResultAll = new BoxLayout(contentResultAll, BoxLayout.X_AXIS);
		panel1 = new JPanel();
		panel2 = new JPanel();
		contents.setLayout(layout);
		result = new JTextArea(40,40);
		cspFile = new JTextArea(40,40);
		result.setEditable(false);
		cspFile.setEditable(false);
		browseButton = new JButton("Browse");
		countButton = new JButton("Read");
		clearButton = new JButton("Clear");
		addButton = new JButton("Add File");
		convertButton = new JButton("Convert to CSP");
		filechooser = new JFileChooser();
		label =new JLabel("File or URL name");
		inputSource = new JTextField(20);
		nameFile = new JTextField(10);
		panel1.add(label);
		panel1.add(inputSource);
		panel1.add(browseButton);
		panel1.add(countButton);
		panel1.add(clearButton);
		contentNameFile.setLayout(layoutNameFile);
		contentNameFile.add(nameFile);
		contentNameFile.add(addButton);
		contentNameFile.add(convertButton);
		contentResultAll.setLayout(layoutResultAll);
		contentResultAll.add(result);
		contentResultAll.add(cspFile);
		contentResult.setLayout(layoutResult);
		contentResult.add(contentNameFile);
		contentResult.add(contentResultAll);
		panel2.add(contentResult);
		contents.add(panel1);
		contents.add(panel2);

		this.add(contents);
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
				for (Entry<String, ArrayList<String>> aa : a.get(i).entrySet()) {
					cspFile.append(aa.getKey()+"\n");
					for (int j = 0; j < aa.getValue().size(); j++) {
						cspFile.append(aa.getValue().get(j)+" ");
					}
					cspFile.append("\n");
				}
				
			}

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
