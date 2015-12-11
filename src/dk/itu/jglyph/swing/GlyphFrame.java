package dk.itu.jglyph.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GlyphFrame extends JFrame 
{
	/**
	 * Auto-generated serial version UID. 
	 */
	private static final long serialVersionUID = -3005890889450279723L;
	
	private final static String FRAME_TITLE = "JGlyph";
	private final static int MIN_WIDTH = 320;
	private final static int MIN_HEIGHT = 380;
	
	private GlyphPanel glyphPanel;
//	private GlyphPanel glyphPanel2;
	
	public GlyphFrame()
	{
		setLayout(new BorderLayout());
		
		JPanel panelCenter = new JPanel();
		add(panelCenter, BorderLayout.CENTER);
		
		JPanel panelSouth = new JPanel();
		add(panelSouth, BorderLayout.SOUTH);
		
		addGlyphs(panelCenter);
		addButtons(panelSouth);
		
		setTitle(FRAME_TITLE);
		
		setSize(MIN_WIDTH, MIN_HEIGHT);
		
		Dimension minimumSize = new Dimension(MIN_WIDTH, MIN_HEIGHT);
		setMinimumSize(minimumSize);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void addGlyphs(Container container)
	{
		GridLayout gridLayout = new GridLayout(1, 0);
		container.setLayout(gridLayout);
		
		glyphPanel = new GlyphPanel();
		container.add(glyphPanel);
		
//		glyphPanel2 = new GlyphPanel();
//		container.add(glyphPanel2);
	}
	
	private void addButtons(Container container)
	{
//		addRandomizerButton(container);
//		addMutatorButton(container);
		addButtonPass(container);
		addButtonFail(container);
//		addCrossButton(container);
	}
	
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
	
	private void addButtonPass(Container container)
	{
		JButton buttonPass = new JButton("PASS");
		
		buttonPass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				glyphPanel.passGlyph();
			}
		});
		
		container.add(buttonPass);
	}
	
	private void addButtonFail(Container container)
	{
		JButton buttonFail = new JButton("FAIL");
		
		buttonFail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				glyphPanel.failGlyph();
			}
		});
		
		container.add(buttonFail);
	}
	
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
		try {
			String laf = UIManager.getSystemLookAndFeelClassName();
//			laf = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
//			System.out.println(laf);
			UIManager.setLookAndFeel(laf);

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	GlyphFrame frame = new GlyphFrame();
        		SwingUtilities.updateComponentTreeUI(frame);
        		frame.setVisible(true);
            }
        });
		
	}
}
