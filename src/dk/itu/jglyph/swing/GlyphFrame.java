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
		
		addGlyphs(panelCenter);

    	glyphShower = new GlyphShower();
    	glyphShower.setVisible(true);
		
		setTitle(FRAME_TITLE);
		
		setSize(MIN_WIDTH, MIN_HEIGHT);
		
		Dimension minimumSize = new Dimension(MIN_WIDTH, MIN_HEIGHT);
		setMinimumSize(minimumSize);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Auto-pick timer
		Timer timer = new Timer(250, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double leftValue = FeatureExtractors.edgeCount(glyphPanelLeft.getGlyph());
				double rightValue = FeatureExtractors.edgeCount(glyphPanelRight.getGlyph());
				
				int ec = 4;
				leftValue = Math.abs(leftValue - ec);
				rightValue = Math.abs(rightValue - ec);
				
				
				if (leftValue <= rightValue) {
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
					// Events for starting and stopping the auto-pick function
					case KeyEvent.VK_P: {
						if (timer.isRunning()) timer.stop();
						else timer.start();
					}
					default: 
						break;
				}
			}
		});
		
//		population = new ArrayList<Glyph>();
//		
//		for (int i = 0; i < MAX_TEST_COUNT; i++) {
//			population.add(evolver.randomGlyph());
//		}
		population = evolver.getDistributedPopulation(MAX_TEST_COUNT);
		glyphShower.setGlyphs(population, filter.getEvaluator());
//		updatePopulation();
		
		updateGlyphs();
		
		
	}
	
	boolean improveModel = false;
	
	private void updateGlyphs()
	{
		if(!tryImproveModel())
		{
			// TODO Make sure they are not the same
			Glyph first;
			if (visited.size() == 0) {
				// We use a new glyph if there is no old glyphs
				first = findNewGlyph();
			}
			else
			{
				first = findOldGlyph();
			}
			glyphPanelLeft.setGlyph(first);
			Glyph second = findNewGlyph();
			glyphPanelRight.setGlyph(second);
		}
		
		
		improveModel = !improveModel;
	}
	
	private boolean tryImproveModel()
	{
		if(!improveModel)
		{
			return false;
		}
		else
		{
			Node nodeA = null;
			for(Node node : filter.getModel().getNodes())
			{
				if(node.parents.size() == 0)
				{
					nodeA = node;
					break;
				}
			}
			
			if(nodeA == null)
			{
				return false;
			}
			
			Node nodeB = null;
			for(Node node : filter.getModel().getNodes())
			{
				if(node == nodeA)
				{
					continue;
				}
				if(node.hasChild(nodeA))
				{
					continue;
				}
				
				if(nodeA.hasChild(node))
				{
					continue;
				}
				
				nodeB = node;
				break;
				
			}
			
			if(nodeB == null)
			{
				return false;
			}
			
			glyphPanelLeft.setGlyph(nodeA.glyph);
			
			glyphPanelRight.setGlyph(nodeB.glyph);
			
			return true;
		}
	}
	
	private Glyph findOldGlyph()
	{
		// TODO Pick from model to improve new layout
		// TODO somehow pick something that maximises the amount of knowledge gained
		Glyph[] glyphs = visited.toArray(new Glyph[visited.size()]);
		return glyphs[Random.getInt(glyphs.length)];
	}
	
	private Glyph findNewGlyph()
	{
		Glyph glyph = null;
		if (population.size() == 0) updatePopulation();
		do 
		{
			if (population.size() == 0){
				System.out.println("Ran out of evolved glyphs; new glyph is random...");
				glyph = evolver.randomGlyph();
			}
			else glyph = population.remove(population.size()-1);
		} 
		while (visited.contains(glyph));
		
		visited.add(glyph.copy());
		
		return(glyph);
	}
	
	private void updatePopulation() {
		Evaluator evaluator = filter.getEvaluator(); 
		
		// TODO keep evolution alive
		evolver.init(evaluator);
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
				pickRight();
			}
		});
		
		
		container.add(glyphPanelRight);
	}


	public static void main(String[] args)
	{

		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	GlyphFrame frame = new GlyphFrame();
        		frame.setVisible(true);
            }
        });
		
	}
}
