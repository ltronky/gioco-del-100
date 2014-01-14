package it.sblt.ui;

import it.sblt.GD100OL;
import it.sblt.GameOf100;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.antlr.v4.codegen.model.Sync;



public class MainPanel extends JFrame{
	private static final long serialVersionUID = 1L;

	private int DIMENSION;

	public boolean delay = true; 
	public int speed = 10;
	
	public GameOf100 recursiveAlgorithm;
	public GD100OL cspAlgorithm;

	private Thread runThread;

	private JPanel buttonPanel;
	private JPanel backGridPanel;
	private JPanel legendPanel;

	private JTextArea logger;

	private Box verticalLogPanel;

	private JButton restartBtn;
	private JButton playBtn;
	private JButton stepBtn;

	private JLabel nextNumLabel;
	private JLabel nextNumFieldLabel;
	private JLabel statusLabel;
	private JLabel statusFieldLabel; 

	private JComboBox<String> algorithmCB;
	private JComboBox<String> dimensionCB;
	private JComboBox<String> tempCB;

	private PanelGrid grid;

	JPanel[][] panelsGrid;

	Border selectBorder = BorderFactory.createLineBorder(Color.black);

	class PanelGrid extends JPanel {
		private static final long serialVersionUID = 7093635227461022241L;
		private List<JLabel> gridList = new ArrayList<JLabel>();

		public PanelGrid(int width, int length, JPanel panel){
			panel.setLayout(new GridLayout(width,length));
			panelsGrid=new JPanel[width][length];

			for(int y=0; y<length; y++){
				for(int x=0; x<width; x++){
					panelsGrid[x][y] = new JPanel();
					panelsGrid[x][y].setBackground(Color.LIGHT_GRAY);
					JLabel l = new JLabel("");
					panelsGrid[x][y].add(l);
					gridList.add(y*DIMENSION+x, l);
					panelsGrid[x][y].setBorder(selectBorder);
					panelsGrid[x][y].setPreferredSize(new Dimension(30,30));
					panel.add(panelsGrid[x][y]);
				}
			}
		}

		public JPanel[][] getPanelsGrid() {
			return panelsGrid;
		}

		public void paintCell(int x, int y, Color c) {
			panelsGrid[x][y].setBackground(c);
		}

		public void writeCell(int x, String s) {
			gridList.get(x).setText(s);
		}

	}

	public MainPanel() {
		DIMENSION = 5; // DEFAULT 
		//		EventQueue.invokeLater(new Runnable() {
		//			@Override
		//			public void run() {
		//				try {
		//					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		//				} 
		//				catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
		//				} 
		setTitle("Gioco del 100");
		setResizable(false);

		generateButtonPanel(); // BOTTONI

		generateGridPanel(); // GRIGLIA

		generateStatusPanel(); // STATO

		//generateLogPanel();

		pack();
		setVisible(true);
		setMinimumSize(new Dimension(500,400));
		//		setMaximumSize(new Dimension(1000,800));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//			} 
		//		});
	}

	//	private void generateLogPanel() {
	//
	//		JPanel logPanel = new JPanel();
	//		logPanel.setBackground(Color.gray);
	//		logPanel.setBorder(new EmptyBorder(0, 0, 30, 0));
	//
	//		verticalLogPanel = Box.createVerticalBox();
	//		verticalLogPanel.setPreferredSize(new Dimension(570,200));
	//
	//		JLabel titleLogLabel = new JLabel("Monitoraggio vincoli");
	//		titleLogLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
	//
	//		logger = new JTextArea();
	//		logger.setMinimumSize(new Dimension(570,200));
	//		logger.setEditable(false);
	//
	//		JScrollPane logSPanel = new JScrollPane(logger);
	//		logSPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	//		logSPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	//		logSPanel.setPreferredSize(new Dimension(570,200));
	//
	//		verticalLogPanel.add(titleLogLabel);
	//		verticalLogPanel.add(logSPanel);
	//
	//		logPanel.add(verticalLogPanel);
	//
	//		add(logPanel, BorderLayout.SOUTH);
	//	}


	private void generateStatusPanel() {

		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 4, 4, Color.LIGHT_GRAY));
		statusPanel.setPreferredSize(new Dimension(260,300));
		

		Box verticalBox1 = Box.createVerticalBox();
		verticalBox1.add(Box.createVerticalStrut(50));


		Box horizontalBox1 = Box.createHorizontalBox();
		horizontalBox1.add(Box.createHorizontalGlue());
		//		horizontalBox1.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		statusLabel = new JLabel("Stato di gioco");
		statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));

		statusFieldLabel = new JLabel("Fermo");
		statusFieldLabel.setPreferredSize(new Dimension(130,50));
		statusFieldLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 1, 1, Color.LIGHT_GRAY));

		horizontalBox1.add(statusLabel);
		horizontalBox1.add(statusFieldLabel);


		Box horizontalBox2 = Box.createHorizontalBox();
		horizontalBox2.add(Box.createHorizontalGlue());

		nextNumLabel = new JLabel("Prossimo numero");
		nextNumLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

		nextNumFieldLabel = new JLabel("2");
		nextNumFieldLabel.setPreferredSize(new Dimension(130,50));
		nextNumFieldLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 1, 1, Color.LIGHT_GRAY));

		horizontalBox2.add(nextNumLabel);
		horizontalBox2.add(nextNumFieldLabel);

		verticalBox1.add(horizontalBox1);
		verticalBox1.add(horizontalBox2);


		legendPanel = new JPanel();
		//		legendPanel.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));

		JLabel legendLabel = new JLabel("Legenda");
		legendLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

		legendPanel.add(legendLabel);

		Box verticalBox2 = Box.createVerticalBox();
		verticalBox2.add(Box.createVerticalStrut(50));

		Box horizontalBox3 = Box.createHorizontalBox();
		horizontalBox3.add(Box.createHorizontalGlue());

		JPanel greenPanel = new JPanel();
		greenPanel.setBackground(Color.GREEN);
		greenPanel.setMaximumSize(new Dimension(20,20));

		JLabel greenLabel = new JLabel("Caselle possibili");
		greenLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		horizontalBox3.add(greenPanel);
		horizontalBox3.add(greenLabel);

		Box horizontalBox4 = Box.createHorizontalBox();
		horizontalBox4.add(Box.createHorizontalGlue());

		JPanel yellowPanel = new JPanel();
		yellowPanel.setBackground(Color.YELLOW);
		yellowPanel.setMaximumSize(new Dimension(20,20));

		JLabel yellowLabel = new JLabel("Casella scelta");
		yellowLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 28));

		horizontalBox4.add(yellowPanel);
		horizontalBox4.add(yellowLabel);

		verticalBox2.add(horizontalBox3);
		verticalBox2.add(horizontalBox4);

		legendPanel.add(verticalBox2);

		verticalBox1.add(legendPanel);

		statusPanel.add(verticalBox1);

		add(statusPanel, BorderLayout.EAST);
	}


	private void generateGridPanel() {
		if(backGridPanel != null) {
			remove(backGridPanel);
		}
		backGridPanel = new JPanel();

		grid = new PanelGrid(DIMENSION,DIMENSION,backGridPanel);

		backGridPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 10));

		add(backGridPanel, BorderLayout.WEST);

	}


	private void generateButtonPanel(){
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(600,50));	

		restartBtn = new JButton("Ricomincia");
		restartBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				algorithmCB.setSelectedIndex(0);
				dimensionCB.setSelectedIndex(0);

				EnableLegend();
				EnableNextNumber();

				statusFieldLabel.setText("Fermo");
				setDimension(5);
				generateGridPanel();
				invalidate();
				validate();
				repaint();
			}
		});


		String[] algorithmStrings = {"Algoritmo Ricorsivo", "Algoritmo Choco" };

		algorithmCB = new JComboBox(algorithmStrings);
		algorithmCB.setSelectedIndex(0);
		algorithmCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if(algorithmCB.getSelectedIndex() == 1){
					DisableLegend();
					DisableNextNumber();
				}
				else {
					EnableLegend();
					EnableNextNumber();
				}
			}
		});

		String[] dimensionStrings = {"5", "6", "7", "8", "9", "10" };

		dimensionCB = new JComboBox(dimensionStrings);
		dimensionCB.setSelectedIndex(0);


		playBtn = new JButton("Play/Pausa");
		playBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (runThread != null) {
					synchronized (runThread) {

					}
				}
				else {
					runThread = new Thread() {
						@Override
						public void run() {
							super.run();

							DIMENSION = new Integer((String)dimensionCB.getSelectedItem());
							generateGridPanel();
							//						validate();
							//						repaint();
							statusFieldLabel.setText("In esecuzione");
							if (algorithmCB.getSelectedIndex() == 0) {
								recursiveAlgorithm.startRecursiveAlgorithm(DIMENSION);
							}
							else {
								cspAlgorithm.startAlgorithm(DIMENSION);
							}
						}
					};
					runThread.start();
				}
			}
		});
		
		String[] tempStrings = {"NO DELAY", "SMALL DELAY", "LARGE DELAY" };

		tempCB = new JComboBox<String>(tempStrings);
		tempCB.setSelectedIndex(0);
		tempCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				switch (tempCB.getSelectedIndex()) {
				case 0:
					delay = false;
					break;
				case 1:
					speed = 10;
					break;

				case 2:
					speed = 100;
					break;

				default: 
					speed = 100;
					break;
				}
				
				
			}
		});


		buttonPanel.add(restartBtn);
		buttonPanel.add(algorithmCB);
		buttonPanel.add(dimensionCB);
		buttonPanel.add(tempCB);
		buttonPanel.add(playBtn);
		//		buttonPanel.add(stepBtn);

		add(buttonPanel, BorderLayout.NORTH);

	}

	public void setStatusFieldLabel(String str) {
		statusFieldLabel.setText(str);
	}
	// ******** main.setStatusFieldLabel("Stop");

	public void setNextNumFieldLabel(String str) {
		nextNumFieldLabel.setText(str);
	}
	// ******** main.setNextNumFieldLabel("1200");

	public void setBackgroundCell(int x, int y, Color c) {
		grid.paintCell(x, y, c);
	}
	// ******** main.setBackgroundCell(0, 0, Color.yellow);

	public void printNumber(String name, int nr) {
		grid.writeCell(nr, name);
		for (int i = 0; i < DIMENSION; i++) {
			for (int j = 0; j < DIMENSION; j++) {
				setBackgroundCell(i,j, Color.LIGHT_GRAY);
			}
		}
	}

	public void setAvailableCell(int nr) {
		setBackgroundCell(nr%DIMENSION, nr/DIMENSION, Color.GREEN);
	}

	public void setSelectedCell(int nr) {
		setBackgroundCell(nr%DIMENSION, nr/DIMENSION, Color.YELLOW);

	}

	public void setFailCell(int nr) {
		setBackgroundCell(nr%DIMENSION, nr/DIMENSION, Color.RED);

	}

	public void EnableLegend() {
		legendPanel.setVisible(true);
	}

	public void DisableLegend() {
		legendPanel.setVisible(false);
	}

	public void EnableNextNumber() {
		nextNumLabel.setVisible(true);
		nextNumFieldLabel.setVisible(true);
	}

	public void DisableNextNumber() {
		nextNumLabel.setVisible(false);
		nextNumFieldLabel.setVisible(false);
	}

	public void setDimension(int d) {
		DIMENSION = d;
	}

	public Thread getRunThread() {
		synchronized (runThread) {
			return runThread;
		}
	}

	public void resetRunThread() {
		synchronized (runThread) {
			runThread = null;
		}

	}

	//	public void printInLog(String s) {
	//		logger.append(s+"\n");
	//	}
}
