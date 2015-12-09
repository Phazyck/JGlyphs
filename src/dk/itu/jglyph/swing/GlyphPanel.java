package dk.itu.jglyph.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

import dk.itu.jglyph.Edge;
import dk.itu.jglyph.JGlyph;
import dk.itu.jglyph.Node;

public class GlyphPanel  extends JComponent
{
	
	/**
	 * Auto-generated serial version UID. 
	 */
	private static final long serialVersionUID = 6967190278448536610L;
	
	private final static int DEFAULT_PADDING = 8;
	private final static String DEFAULT_TITLE = null;
	
	private JGlyph glyph = new JGlyph(3, 3);
	
	public GlyphPanel()
	{
		this(DEFAULT_PADDING, DEFAULT_TITLE);
	}
	
	public GlyphPanel(int padding)
	{
		this(padding, DEFAULT_TITLE);
	}
	
	public GlyphPanel(String title)
	{
		this(DEFAULT_PADDING, title);
	}
	
	public GlyphPanel(int padding, String title)
	{
		Border outerBorder = BorderFactory.createEmptyBorder(padding, padding, padding, padding);
		Border middleBorder = BorderFactory.createTitledBorder(title);
		Border compoundBorder = BorderFactory.createCompoundBorder(outerBorder, middleBorder);
		Border innerBorder = BorderFactory.createEmptyBorder(padding, padding, padding, padding);
		Border border = BorderFactory.createCompoundBorder(compoundBorder, innerBorder);
		
		setBorder(border);
	}
	
	private void draw(Graphics2D g2)
	{
		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = Integer.MIN_VALUE;
		
		for(Node node : glyph.getNodes())
		{
			double x = node.x;
			double y = node.y;
			
			if(x < minX) minX = x;
			if(x > maxX) maxX = x;
			if(y < minY) minY = y;
			if(y > maxY) maxY = y;
		}
		
		double spanX = maxX - minX;
		double spanY = maxY - minY;
		
		double w = xMax;
		double h = yMax;
		
		
		double sx = w / spanX;
		double sy = h / spanY;
		
		double s = 1;
		double x0 = xMin;
		double y0 = yMin;
		
		if(sx < sy)
		{
			s = sx;
			y0 += (h - spanY * s) * 0.5;
		}
		else
		{
			s = sy;
			x0 += (w - spanX * s) * 0.5;
		}
		
		for(Edge edge : glyph.getEdges())
		{
			Node n1 = edge.from;
			Node n2 = edge.to;
			int x1 = (int)(x0 + n1.x * s);
			int y1 = (int)(y0 + n1.y * s);
			int x2 = (int)(x0 + n2.x * s);
			int y2 = (int)(y0 + n2.y * s);
			
			if(n1 == n2)
			{
				int r = 3;
				int d = r * 2;
				g2.drawOval(x1 - r, y1 - r, d, d);
			}
			
			g2.drawLine(x1, y1, x2, y2);
		}
		
//		for(Node node : glyph.getNodes())
//		{
//			int x = (int)(x0 + node.x * s);
//			int y = (int)(y0 + node.y * s);
//			
//			int r = 2;
//			int d = r * 2;
//			g2.drawOval(x - r, y - r, d, d);
//		}
		
//		g2.drawRect(xMin, yMin, xMax, yMax);
      
	}
	
	private int xMin, xMax, yMin, yMax;
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);

		 //create a new graphics2D instance
        Graphics2D g2 = (Graphics2D) graphics.create();
        
        //determine the x, y, width and height
        xMin = getInsets().left;
        yMin = getInsets().top;
        xMax = getWidth() - getInsets().left - getInsets().right;
        yMax = getHeight() - getInsets().top - getInsets().bottom;                             

        //draws the blue circle
        g2.setPaint(Color.BLACK);
        
        Stroke stroke = g2.getStroke();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));
        draw(g2);
        g2.setStroke(stroke);
	}
	
	private void printFeatures()
	{
		System.out.println("--- FEATURES ---");
		
		for(dk.itu.jglyph.features.FeatureExtractor ext : dk.itu.jglyph.features.FeatureExtractor.FEATURE_EXTRACTORS)
		{
			String name = ext.getName();
			double value = ext.getFeature(glyph);
			
			System.out.printf("%s\n\t%f\n", name, value);
		}
	}
	
	public void randomizeGlyph()
	{
		glyph.randomizeEdges();
		
		printFeatures();
		
		repaint();
	}
	
	public void mutateGlyph()
	{
		glyph.mutate();
		
		printFeatures();
		
		repaint();
	}
}
