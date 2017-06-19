package Application;

import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;




public class MainReader extends JFrame {
	
	private UMLReader umlReader;
	private Container contents;
	private LayoutManager layout;
	private JButton browseButton, countButton ,clearButton;
	private JTextField inputSource;
	private JTextArea result;
	private JLabel label;
	private JFileChooser filechooser;
	private JPanel panel1;
	private JPanel panel2;
	private File file;

	public MainReader(UMLReader reader) {
		this.umlReader = reader;
		this.setTitle("Main Reader");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents();
		this.pack();
	}

	private void initComponents() {
		contents = new Container();
		layout = new BoxLayout(contents, BoxLayout.Y_AXIS);
		panel1 = new JPanel();
		panel2 = new JPanel();
		contents.setLayout(layout);
		result = new JTextArea(40,40);
		result.setEditable(false);
		browseButton = new JButton("Browse");
		countButton = new JButton("Count");
		clearButton = new JButton("Clear");
		filechooser = new JFileChooser();
		label =new JLabel("File or URL name");
		inputSource = new JTextField(20);
		panel1.add(label);
		panel1.add(inputSource);
		panel1.add(browseButton);
		panel1.add(countButton);
		panel1.add(clearButton);
		panel2.add(result);
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
			result.setText(umlReader.readAllLine(url));
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
