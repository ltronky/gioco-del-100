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
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;



public class MainPanel extends JFrame{
	private static final long serialVersionUID = 1L;

	private int DIMENSION;

	public int speed = 0;

	public boolean isChoco = false;

	public GameOf100 recursiveAlgorithm;
	public GD100OL cspAlgorithm;

	private Thread runThread;

	private JPanel buttonPanel;
	private JPanel backGridPanel;
	private JPanel legendPanel;

	private Box horizontalBox3;
	private Box horizontalBtnBox3;

	private JButton restartBtn;
	private JButton playBtn;

	private JLabel nextNumLabel;
	private JLabel nextNumFieldLabel;
	private JLabel statusLabel;
	private JLabel statusFieldLabel; 
	private JLabel backTrackFieldLabel;

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

		setTitle("Gioco del 100");
		setResizable(false);

		generateButtonPanel(); // BOTTONI

		generateGridPanel(); // GRIGLIA

		generateStatusPanel(); // STATO

		pack();
		setVisible(true);
		setMinimumSize(new Dimension(600,450));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void generateStatusPanel() {

		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 4, 4, Color.LIGHT_GRAY));
		statusPanel.setPreferredSize(new Dimension(260,350));


		Box verticalBox1 = Box.createVerticalBox();
		verticalBox1.add(Box.createVerticalStrut(50));


		Box horizontalBox1 = Box.createHorizontalBox();
		horizontalBox1.add(Box.createHorizontalGlue());

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

		nextNumFieldLabel = new JLabel("1");
		nextNumFieldLabel.setPreferredSize(new Dimension(130,50));
		nextNumFieldLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 1, 1, Color.LIGHT_GRAY));

		horizontalBox2.add(nextNumLabel);
		horizontalBox2.add(nextNumFieldLabel);


		horizontalBox3 = Box.createHorizontalBox(); // BACKTRAKING
		horizontalBox3.add(Box.createHorizontalGlue());

		JLabel backTrackLabel = new JLabel("Cicli Backtraking:");
		backTrackLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

		backTrackFieldLabel = new JLabel("0");
		backTrackFieldLabel.setPreferredSize(new Dimension(130,50));
		backTrackFieldLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 1, 1, Color.LIGHT_GRAY));

		horizontalBox3.add(backTrackLabel);
		horizontalBox3.add(backTrackFieldLabel);

		verticalBox1.add(horizontalBox1);
		verticalBox1.add(horizontalBox2);
		verticalBox1.add(horizontalBox3);


		legendPanel = new JPanel();

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
		buttonPanel.setPreferredSize(new Dimension(600,150));


		Box verticalBtnBox = Box.createVerticalBox();


		Box horizontalBtnBox1 = Box.createHorizontalBox(); // ALGORITMO

		JLabel algoLabel = new JLabel("Algoritmo:");
		algoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));

		String[] algorithmStrings = {"Ricorsivo", "Choco" };
		algorithmCB = new JComboBox<String>(algorithmStrings);
		algorithmCB.setSelectedIndex(0);
		algorithmCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if(algorithmCB.getSelectedIndex() == 1){
					DisableLegend();
					DisableNextNumber();
					DisableDelay();
					DisableBackTraking();
				}
				else {
					EnableLegend();
					EnableNextNumber();
					EnableDelay();
					EnableBackTraking();
				}
			}
		});

		horizontalBtnBox1.add(algoLabel);
		horizontalBtnBox1.add(Box.createHorizontalStrut(50));
		horizontalBtnBox1.add(algorithmCB);


		Box horizontalBtnBox2 = Box.createHorizontalBox(); // DIMENSIONE

		JLabel dimLabel = new JLabel("Dimensione griglia:");
		dimLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 45));

		String[] dimensionStrings = {"5", "6", "7", "8", "9", "10" };
		dimensionCB = new JComboBox<String>(dimensionStrings);
		dimensionCB.setSelectedIndex(0);

		horizontalBtnBox2.add(dimLabel);
		horizontalBtnBox2.add(Box.createHorizontalStrut(50));
		horizontalBtnBox2.add(dimensionCB);



		horizontalBtnBox3 = Box.createHorizontalBox(); // TEMPO DELAY

		JLabel delayLabel = new JLabel("Delay:");
		delayLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 130));

		String[] tempStrings = {"NO DELAY", "SMALL DELAY", "LARGE DELAY" };
		tempCB = new JComboBox<String>(tempStrings);
		tempCB.setSelectedIndex(0);

		tempCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				switch (tempCB.getSelectedIndex()) {
				case 0:
					speed = 0;
					break;
				case 1:
					speed = 10;
					break;
				case 2:
					speed = 100;
					break;
				default: 
					speed = 0;
					break;
				}
			}
		});

		horizontalBtnBox3.add(delayLabel);
		horizontalBtnBox3.add(Box.createHorizontalStrut(50));
		horizontalBtnBox3.add(tempCB);


		Box horizontalBtnBox4 = Box.createHorizontalBox(); // PULSANTI

		playBtn = new JButton("Play/Stop");
		playBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (runThread != null) {
					synchronized (runThread) {
						if (isChoco) {
							runThread.stop();
							runThread = null;
							statusFieldLabel.setText("STOP: INTERROTO");
						} 
						else {
							runThread.interrupt();
						}
						restartBtn.setEnabled(true);
					}
				}
				else {
					runThread = new Thread() {
						@Override
						public void run() {
							super.run();
							DIMENSION = new Integer((String)dimensionCB.getSelectedItem());
							generateGridPanel();
							dimensionCB.setEnabled(false);
							algorithmCB.setEnabled(false);
							tempCB.setEnabled(false);
							statusFieldLabel.setText("In esecuzione");
							if (algorithmCB.getSelectedIndex() == 0) {
								isChoco = false;
								try {
									restartBtn.setEnabled(false);
									recursiveAlgorithm.startRecursiveAlgorithm(DIMENSION);
									restartBtn.setEnabled(true);
								} catch (Exception e2) {
									statusFieldLabel.setText("STOP: INTERROTTO");
								}
								restartBtn.setEnabled(true);
							}
							else {
								isChoco = true;
								restartBtn.setEnabled(false);
								cspAlgorithm.startAlgorithm(DIMENSION);
								restartBtn.setEnabled(true);
							}
							runThread = null;
							isChoco = false;
						}
					};
					runThread.start();
				}
			}
		});

		restartBtn = new JButton("Ricomincia");
		restartBtn.setEnabled(false);
		restartBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				algorithmCB.setSelectedIndex(0);
				dimensionCB.setSelectedIndex(0);
				tempCB.setSelectedIndex(0);

				EnableLegend();
				EnableNextNumber();

				statusFieldLabel.setText("Fermo");
				nextNumFieldLabel.setText("1");
				backTrackFieldLabel.setText("0");
				algorithmCB.setEnabled(true);
				dimensionCB.setEnabled(true);
				tempCB.setEnabled(true);
				setDimension(5);
				generateGridPanel();
				invalidate();
				validate();
				repaint();
			}
		});

		horizontalBtnBox4.add(restartBtn);
		horizontalBtnBox4.add(Box.createHorizontalStrut(20));
		horizontalBtnBox4.add(playBtn);


		verticalBtnBox.add(horizontalBtnBox1);
		verticalBtnBox.add(horizontalBtnBox2);
		verticalBtnBox.add(horizontalBtnBox3);
		verticalBtnBox.add(horizontalBtnBox4);

		buttonPanel.add(verticalBtnBox);

		add(buttonPanel, BorderLayout.NORTH);

	}

	public void setStatusFieldLabel(String str) {
		statusFieldLabel.setText(str);
	}

	public void setNextNumFieldLabel(String str) {
		nextNumFieldLabel.setText(str);
	}

	public void setBackgroundCell(int x, int y, Color c) {
		grid.paintCell(x, y, c);
	}

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

	public void EnableDelay() {
		horizontalBtnBox3.setVisible(true);
	}

	public void DisableDelay() {
		horizontalBtnBox3.setVisible(false);
	}

	public void EnableBackTraking() {
		horizontalBox3.setVisible(true);
	}

	public void DisableBackTraking() {
		horizontalBox3.setVisible(false);
	}

	public void setDimension(int d) {
		DIMENSION = d;
	}

	public void setBackTrakingLabel(String str) {
		backTrackFieldLabel.setText(str);
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
}
