package dk.itu.jglyph.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dk.itu.jglyph.Evaluator;
import dk.itu.jglyph.Glyph;

public class GlyphShower extends JFrame 
{
	/**
	 * Auto-generated serial version UID. 
	 */
	private static final long serialVersionUID = -3005890889450279723L;
	
	private final static String FRAME_TITLE = "JGlyph overview";
	private final static int MIN_WIDTH = 800;
	private final static int MIN_HEIGHT = 256;
	public final static int MAX_COUNT = 20;
	
	private GlyphPanel[] glyphPanels;
	
	private Evaluator evaluator;
	
	public GlyphShower()
	{
		setLayout(new BorderLayout());
		
		JPanel panelCenter = new JPanel();
		add(panelCenter, BorderLayout.CENTER);
		
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
						Glyph g = glyphPanels[t].getGlyph().clone();
						g.mutate();
						glyphPanels[t].setGlyph(g);
					} else if (e.getButton() == MouseEvent.BUTTON2) {
						System.out.println("Random");
						Glyph g = glyphPanels[t].getGlyph().clone();
						g.randomizeEdges();
						glyphPanels[t].setGlyph(g);
					}
					System.out.println("Panel " + t + " has fitness: " + evaluator.evaluate(glyphPanels[t].getGlyph()));
				}
			});
		}
	}
	
	private int iteration = 0;
	
	public void setGlyphs(List<Glyph> glyphs, Evaluator evaluator)
	{
		setTitle("JGlyph overview population #" + (++iteration));
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Fitness for this set of glyphs:\t" );
		
		for (int i = 0; i < glyphPanels.length; i++) {
			Glyph g = null;
			if (i < glyphs.size()) {
				g = glyphs.get(i);
				sb.append(evaluator.evaluate(glyphs.get(i)) + "\t");
			}
			glyphPanels[i].setGlyph(g);
		}
		
		this.evaluator = evaluator;
		
		System.out.println(sb.toString());
	}
}
