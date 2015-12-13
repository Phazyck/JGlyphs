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
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dk.itu.jglyph.Evaluator;
import dk.itu.jglyph.Filter;
import dk.itu.jglyph.Glyph;
import dk.itu.jglyph.evolution.GlyphEvolver;
import dk.itu.jglyph.evolution.Subject;
import dk.itu.jglyph.util.Random;

public class GlyphShower extends JFrame 
{
	/**
	 * Auto-generated serial version UID. 
	 */
	private static final long serialVersionUID = -3005890889450279723L;
	
	private final static String FRAME_TITLE = "JGlyph overview";
	private final static int MIN_WIDTH = 1500;
	private final static int MIN_HEIGHT = 720;
	public final static int MAX_COUNT = 20;
	
	private GlyphPanel[] glyphPanels;
	
	private Filter filter;
	
	public GlyphShower(Filter filter)
	{
		this.filter = filter;
		setLayout(new BorderLayout());
		
		JPanel panelCenter = new JPanel();
		add(panelCenter, BorderLayout.CENTER);
		
//		JPanel panelSouth = new JPanel();
//		add(panelSouth, BorderLayout.SOUTH);
		
		addGlyphPanels(panelCenter);
		
		setTitle(FRAME_TITLE);
		
		setSize(MIN_WIDTH, MIN_HEIGHT);
		
		Dimension minimumSize = new Dimension(MIN_WIDTH, MIN_HEIGHT);
		setMinimumSize(minimumSize);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void addGlyphPanels(Container container)
	{
		GridLayout gridLayout = new GridLayout(2, 0);
		container.setLayout(gridLayout);
		
		glyphPanels = new GlyphPanel[MAX_COUNT];
		
		for (int i = 0; i < glyphPanels.length; i++) {
			glyphPanels[i] = new GlyphPanel();
			container.add(glyphPanels[i]);
			
			final int t = i;
			glyphPanels[i].addMouseListener(new MouseListener() {
				@Override public void mousePressed(MouseEvent e) { }
				@Override public void mouseExited(MouseEvent e) { }
				@Override public void mouseEntered(MouseEvent e) { }
				@Override public void mouseClicked(MouseEvent e) { }
				@Override public void mouseReleased(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						System.out.println("Mutating");
						glyphPanels[t].getGlyph().mutate();
						glyphPanels[t].repaint();
					} else if (e.getButton() == MouseEvent.BUTTON2) {
						System.out.println("Random");
						glyphPanels[t].getGlyph().randomizeEdges();
						glyphPanels[t].repaint();
					}
					System.out.println("Panel " + t + " has fitness: " + filter.getEvaluator().evaluate(glyphPanels[t].getGlyph()));
				}
			});
		}
	}
	
	public void setGlyphs(List<Glyph> glyphs)
	{
		for (int i = 0; i < glyphPanels.length; i++) {
			Glyph g = null;
			if (i < glyphs.size()) g = glyphs.get(i);
			glyphPanels[i].setGlyph(g);
		}
	}
}
