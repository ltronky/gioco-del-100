package it.sblt.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;



public class MainPanel extends JFrame{

	private MainPanel main;

	private JPanel buttonPanel;
	private JPanel backGridPanel;

	private JTextArea logger;

	private Box verticalStatusBox;
	private Box verticalLogPanel;

	private JButton restartBtn;
	private JButton playBtn;
	private JButton stepBtn;

	private JLabel nextNumLabel;
	private JLabel nextNumFieldLabel;
	private JLabel statusLabel;
	private JLabel statusFieldLabel; 

	//private Graphics graphic;

	private PanelGrid grid;

	JPanel[][] panelsGrid;

	Border selectBorder = BorderFactory.createLineBorder(Color.black);

	class PanelGrid extends JPanel {
		private static final long serialVersionUID = 7093635227461022241L;
		private Map<Integer, JLabel> gridMap = new HashMap<Integer, JLabel>();
		
		public PanelGrid(int width, int length, JPanel panel){
			panel.setLayout(new GridLayout(width,length));
			panelsGrid=new JPanel[width][length];


			for(int y=0; y<length; y++){
				for(int x=0; x<width; x++){
					panelsGrid[x][y] = new JPanel();
					JLabel l = new JLabel("");
					panelsGrid[x][y].add(l);
					gridMap.put(new Integer(x*10+y), l);
					panelsGrid[x][y].setBorder(selectBorder);
					panelsGrid[x][y].setPreferredSize(new Dimension(40,40));
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

		public void writeCell(int x, int y, String s) {
			gridMap.get(new Integer(x*10+y)).setText(s);
		}

	}

	public MainPanel() {
		main = this;
//		EventQueue.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//				} 
//				catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
//				} 
				main.setTitle("Gioco del 100");
				main.setResizable(false);

				generateButtonPanel();

				generateGridPanel();

				generateStatusPanel();

				generateLogPanel();

				pack();
				setVisible(true);
				main.setMaximumSize(new Dimension(800,800));
				main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			
//			} 
//		});
	}

	private void generateLogPanel() {

		JPanel logPanel = new JPanel();
		logPanel.setBackground(Color.gray);
		logPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

		verticalLogPanel = Box.createVerticalBox();
		verticalLogPanel.setPreferredSize(new Dimension(570,200));
		
		JLabel titleLogLabel = new JLabel("Monitoraggio vincoli");
		titleLogLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
		
		logger = new JTextArea();
		logger.setMinimumSize(new Dimension(570,200));
		logger.setEditable(false);

		JScrollPane logSPanel = new JScrollPane(logger);
		logSPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		logSPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		logSPanel.setPreferredSize(new Dimension(570,200));
		
		verticalLogPanel.add(titleLogLabel);
		verticalLogPanel.add(logSPanel);
	
		logPanel.add(verticalLogPanel);
		
		add(logPanel, BorderLayout.SOUTH);
	}


	private void generateStatusPanel() {

		verticalStatusBox = Box.createVerticalBox();
		verticalStatusBox.add(Box.createVerticalGlue());
		verticalStatusBox.setPreferredSize(new Dimension(180,300));
		verticalStatusBox.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 0));

		statusLabel = new JLabel("Stato di gioco");
		statusLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));

		statusFieldLabel = new JLabel("In esecuzione");
		statusFieldLabel.setPreferredSize(new Dimension(160,40));
		statusFieldLabel.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Color.white));

		nextNumLabel = new JLabel("Prossimo numero");
		nextNumLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 5, 0));

		nextNumFieldLabel = new JLabel("100");
		nextNumFieldLabel.setPreferredSize(new Dimension(160,40));
		nextNumFieldLabel.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Color.white));

		verticalStatusBox.add(statusLabel);
		verticalStatusBox.add(statusFieldLabel);
		verticalStatusBox.add(nextNumLabel);
		verticalStatusBox.add(nextNumFieldLabel);

		add(verticalStatusBox, BorderLayout.EAST);
	}


	private void generateGridPanel() {

		backGridPanel = new JPanel();

		grid = new PanelGrid(10,10,backGridPanel);

		backGridPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 0));
		add(backGridPanel, BorderLayout.WEST);
	}


	private void generateButtonPanel(){
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(600,50));	

		restartBtn = new JButton("Ricomincia");
		restartBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		playBtn = new JButton("Play/Pausa");
		playBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		stepBtn = new JButton("Passo");
		stepBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				logger.append("AAA\n");
				verticalLogPanel.scrollRectToVisible(logger.getBounds());
			}
		});

		buttonPanel.add(restartBtn);
		buttonPanel.add(playBtn);
		buttonPanel.add(stepBtn);

		add(buttonPanel, BorderLayout.NORTH);

	}


	public void setMainPanel(MainPanel m) {
		main = m;
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

	public void writeNumCell(int x, int y, String str) {
		grid.writeCell(x, y, str);	
	}
}
