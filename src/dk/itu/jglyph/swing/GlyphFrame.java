package dk.itu.jglyph.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dk.itu.jglyph.Filter;
import dk.itu.jglyph.Glyph;
import dk.itu.jglyph.evolution.GlyphEvolver;
import dk.itu.jglyph.evolution.Subject;
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
	
	private Filter filter;
	
	private HashSet<Glyph> visited = new HashSet<>();
	
	private GlyphEvolver evolver;

	private final static double PROBABILITY_REVISIT = 0.4;
	
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
		
		setTitle(FRAME_TITLE);
		
		setSize(MIN_WIDTH, MIN_HEIGHT);
		
		Dimension minimumSize = new Dimension(MIN_WIDTH, MIN_HEIGHT);
		setMinimumSize(minimumSize);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
					default: 
						break;
				}
			}
		});
		
		updateGlyphs();
	}
	
	private void updateGlyphs()
	{
		glyphPanelLeft.setGlyph(findNewGlyph());
		glyphPanelRight.setGlyph(findNewGlyph());
	}
	
	private Glyph findNewGlyph()
	{
		boolean revisit = Random.getBoolean(PROBABILITY_REVISIT);
		
		Glyph glyph = null;
		
		if(revisit)
		{
			Glyph[] glyphs = new Glyph[visited.size()];
			glyphs = visited.toArray(glyphs);
			
			int index = Random.getInt(glyphs.length);
			glyph = glyphs[index];
		}
		else
		{
			evolver.init(filter.getEvaluator());
			evolver.evolve();
			
			
			
			TreeSet<Subject> population = evolver.clonePopulation();
			
			ArrayList<Glyph> glyphs = new ArrayList<>();
			
			while(population.size() > 0)
			{
				Subject subject = population.pollFirst();
				
				glyphs.add(glyph);
				if(!visited.contains(glyph))
				{
					glyph = subject.glyph;
					break;
				}
			}
			
			// If we didn't find a glyph on the first traversal, pick a random one.
			if(glyph == null)
			{
				int index = Random.getInt(glyphs.size());
				glyph = glyphs.get(index);
			}
			
			visited.add(glyph.clone());
		}
		
		return(glyph);
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
				System.out.println("Left panel clicked!");
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
				System.out.println("Right panel clicked!");
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
