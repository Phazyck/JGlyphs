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
	
	public GlyphShower()
	{
		setLayout(new BorderLayout());
		
		JPanel panelCenter = new JPanel();
		add(panelCenter, BorderLayout.CENTER);
		
//		JPanel panelSouth = new JPanel();
//		add(panelSouth, BorderLayout.SOUTH);
		
		addGlyphs(panelCenter);
		
		setTitle(FRAME_TITLE);
		
		setSize(MIN_WIDTH, MIN_HEIGHT);
		
		Dimension minimumSize = new Dimension(MIN_WIDTH, MIN_HEIGHT);
		setMinimumSize(minimumSize);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void addGlyphs(Container container)
	{
		GridLayout gridLayout = new GridLayout(2, 0);
		container.setLayout(gridLayout);
		
		glyphPanels = new GlyphPanel[MAX_COUNT];
		
		for (int i = 0; i < glyphPanels.length; i++) {
			glyphPanels[i] = new GlyphPanel();
			container.add(glyphPanels[i]);
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
