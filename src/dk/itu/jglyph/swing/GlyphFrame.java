package dk.itu.jglyph.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import dk.itu.jglyph.Evaluator;
import dk.itu.jglyph.Filter;
import dk.itu.jglyph.Glyph;
import dk.itu.jglyph.evolution.GlyphEvolver;
import dk.itu.jglyph.evolution.Subject;
import dk.itu.jglyph.features.FeatureExtractors;
import dk.itu.jglyph.user.Node;
import dk.itu.jglyph.util.Random;

public class GlyphFrame extends JFrame 
{
	/**
	 * Auto-generated serial version UID. 
	 */
	private static final long serialVersionUID = -3005890889450279723L;
	
	private final static String FRAME_TITLE = "JGlyph";
	private final static int MIN_WIDTH = 320;
	private final static int MIN_HEIGHT = 380;
	
	private GlyphPanel glyphPanelLeft;
	private GlyphPanel glyphPanelRight;
	private GlyphShower glyphShower;
	
	private Filter filter;
	
	private HashSet<Glyph> visited = new HashSet<>();
	
	private GlyphEvolver evolver;

	private static final int MAX_TEST_COUNT = 20;
	
	private ArrayList<Glyph> population;
	
	public GlyphFrame()
	{
		filter = new Filter();
		
		evolver = new GlyphEvolver(filter.getEvaluator());
		
		setLayout(new BorderLayout());
		
		JPanel panelCenter = new JPanel();
		add(panelCenter, BorderLayout.CENTER);
		
//		JPanel panelSouth = new JPanel();
//		add(panelSouth, BorderLayout.SOUTH);
		
		addGlyphs(panelCenter);
//		addButtons(panelSouth);
		
		

    	glyphShower = new GlyphShower();
    	glyphShower.setVisible(true);
		
		/*EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	glyphShower = new GlyphShower();
//        		SwingUtilities.updateComponentTreeUI(frame);
            	glyphShower.setVisible(true);
            }
        });*/
		
		setTitle(FRAME_TITLE);
		
		setSize(MIN_WIDTH, MIN_HEIGHT);
		
		Dimension minimumSize = new Dimension(MIN_WIDTH, MIN_HEIGHT);
		setMinimumSize(minimumSize);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Autopick timer
		Timer timer = new Timer(250, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double leftValue = FeatureExtractors.dotCount(glyphPanelLeft.getGlyph());
				double rightValue = FeatureExtractors.dotCount(glyphPanelRight.getGlyph());
				
				if (leftValue >= rightValue) {
					pickLeft();
				}
				else pickRight();
			}
		});
		timer.setRepeats(true);
		
		this.addKeyListener(new KeyListener() {
			
			@Override public void keyTyped(KeyEvent e) { }
			@Override public void keyPressed(KeyEvent e) { }
			@Override public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) 
				{
					case KeyEvent.VK_LEFT: {
						pickLeft();
					} break;
					case KeyEvent.VK_RIGHT: {
						pickRight();
					} break;
					// Events for starting and stopping the autpick function
					case KeyEvent.VK_P: {
						if (timer.isRunning()) timer.stop();
						else timer.start();
					}
					default: 
						break;
				}
			}
		});
		
		population = new ArrayList<Glyph>();
		
		for (int i = 0; i < MAX_TEST_COUNT; i++) {
			population.add(evolver.randomGlyph());
		}
		
		updateGlyphs();
		
		
	}
	
	private void updateGlyphs()
	{
		// TODO Make sure they are not the same
		Glyph first;
		if (visited.size() == 0) {
			// We use a new glyph if there is no old glyphs
			first = findNewGlyph();
		}
		else first = findOldGlyph();
		glyphPanelLeft.setGlyph(first);
		Glyph second = findNewGlyph();
		glyphPanelRight.setGlyph(second);
		
		
	}
	
	private Glyph findOldGlyph()
	{
		// TODO Pick from model to improve new layout
		// TODO somehow pick something that maximises the amount of knowledge gained
		Glyph[] glyphs = visited.toArray(new Glyph[visited.size()]);
		return glyphs[Random.getInt(glyphs.length)];
		
//		Glyph current = null;
//		int currentCount = Integer.MIN_VALUE;
//		
//		// Stupid heuristic: Pick most best node always
//		for (Node node : filter.getModel().getNodes()) {
//			// We want the top most. if it has parents, it's no good
//			if (!node.parents.isEmpty()) continue;
//			
//			if (node.countUniqueChildren() > currentCount) {
//				current = node.glyph;
//				currentCount = node.countUniqueChildren();
//			}
//		} 
//		
//		return current;
	}
	
	private Glyph findNewGlyph()
	{
		Glyph glyph = null;
		if (population.size() == 0) updatePopulation();
		do {
			if (population.size() == 0){
				System.out.println("Ran out of evolved glyphs; new glyph is random...");
				glyph = evolver.randomGlyph();
			}
			else glyph = population.remove(population.size()-1);
		} while (visited.contains(glyph));
		
		visited.add(glyph.clone());
		
		return(glyph);
	}
	
	private void updatePopulation() {
		Evaluator evaluator = filter.getEvaluator(); 
		
		// TODO keep evolution alive
		evolver.init(evaluator);
		//evolver.setEvaluator(filter.getEvaluator());
		evolver.evolve();
		
		population = new ArrayList<Glyph>();
		
		int count = Math.max(MAX_TEST_COUNT, GlyphShower.MAX_COUNT);
		for (Subject subject : evolver.getPopulation()) {
			population.add(subject.glyph);
			if (count-- == 0) break;
		}
		
		glyphShower.setGlyphs(population, evaluator);
		while (population.size() > MAX_TEST_COUNT) population.remove(MAX_TEST_COUNT);
	}
	
//	private void findNewGlyph(GlyphPanel panel)
//	{	
		
		
//		evolver.init(filter);
		
		
		
		
		
//		Glyph backup = glyph.clone();
//		
//		while(visited.contains(glyph))
//		{
//			int attempts = 0;
//			
//			do
//			{
//				glyph = backup.clone();
//				glyph.mutate();
//				
//				if(attempts++ > 100)
//				{
//					attempts = 0;
//					backup.randomizeEdges();
//				}
//								
//			} while(!filter.doesPass(glyph));
//		}
				
//		System.out.println();
		
//		printFeatures();
		
//	}
	
	private void pickLeft()
	{
		Glyph better = glyphPanelLeft.getGlyph();
		Glyph worse = glyphPanelRight.getGlyph();
		filter.update(better, worse);
		updateGlyphs();
	}
	
	private void pickRight()
	{
		Glyph better= glyphPanelRight.getGlyph();
		Glyph worse = glyphPanelLeft.getGlyph();
		filter.update(better, worse);
		updateGlyphs();
	}
	
	private void addGlyphs(Container container)
	{
		GridLayout gridLayout = new GridLayout(1, 0);
		container.setLayout(gridLayout);
		
		glyphPanelLeft = new GlyphPanel();
		container.add(glyphPanelLeft);
		
		glyphPanelLeft.addMouseListener(new MouseListener() {
			@Override public void mousePressed(MouseEvent e) { }
			@Override public void mouseExited(MouseEvent e) { }
			@Override public void mouseEntered(MouseEvent e) { }
			@Override public void mouseClicked(MouseEvent e) { }
			@Override public void mouseReleased(MouseEvent e) {
//				System.out.println("Left panel clicked!");
				pickLeft();
			}
		});
		
		glyphPanelRight = new GlyphPanel();
		glyphPanelRight.addMouseListener(new MouseListener() {
			@Override public void mousePressed(MouseEvent e) { }
			@Override public void mouseExited(MouseEvent e) { }
			@Override public void mouseEntered(MouseEvent e) { }
			@Override public void mouseClicked(MouseEvent e) { }
			@Override public void mouseReleased(MouseEvent e) {
//				System.out.println("Right panel clicked!");
				pickRight();
			}
		});
		
		
		container.add(glyphPanelRight);
	}
//	
//	private void addButtons(Container container)
//	{
////		addRandomizerButton(container);
////		addMutatorButton(container);
//		addButtonPickLeft(container);
//		addButtonPickRight(container);
////		addCrossButton(container);
//	}

//	private void addRandomizerButton(Container container)
//	{
//		JButton buttonRandomize = new JButton("RANDOMIZE");
//		
//		buttonRandomize.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				glyphPanel.randomizeGlyph();
//			}
//		});
//		
//		container.add(buttonRandomize);
//	}
	
//	private void addButtonPass(Container container)
//	{
//		JButton buttonPass = new JButton("PASS");
//		
//		buttonPass.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				glyphPanel.passGlyph();
//			}
//		});
//		
//		container.add(buttonPass);
//	}
//	
//	private void addButtonFail(Container container)
//	{
//		JButton buttonFail = new JButton("FAIL");
//		
//		buttonFail.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				glyphPanel.failGlyph();
//			}
//		});
//		
//		container.add(buttonFail);
//	}
	
//	private void addMutatorButton(Container container)
//	{
//		JButton buttonMutate = new JButton("MUTATE");
//		
//		buttonMutate.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				glyphPanel.mutateGlyph();
//			}
//		});
//		
//		container.add(buttonMutate);
//	}
	
//	private void addCrossButton(Container container)
//	{
//		JButton buttonCross = new JButton("CROSS");
//		
//		buttonCross.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				glyphPanel.crossGlyph(glyphPanel2);
//			}
//		});
//		
//		container.add(buttonCross);
//	}

	public static void main(String[] args)
	{
//		try {
//			String laf = UIManager.getSystemLookAndFeelClassName();
////			laf = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
////			System.out.println(laf);
//			UIManager.setLookAndFeel(laf);
//
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//				| UnsupportedLookAndFeelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	GlyphFrame frame = new GlyphFrame();
//        		SwingUtilities.updateComponentTreeUI(frame);
        		frame.setVisible(true);
            }
        });
		
	}
}
