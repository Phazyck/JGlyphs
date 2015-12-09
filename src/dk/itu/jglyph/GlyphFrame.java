package dk.itu.jglyph;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
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
	
	public GlyphFrame()
	{
		setLayout(new BorderLayout());
		
		initGlyph(BorderLayout.CENTER);
		initButtons(BorderLayout.SOUTH);
		
		setTitle(FRAME_TITLE);
		
		setSize(MIN_WIDTH, MIN_HEIGHT);
		
		Dimension minimumSize = new Dimension(MIN_WIDTH, MIN_HEIGHT);
		setMinimumSize(minimumSize);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initGlyph(Object constraints)
	{
		glyphPanel = new GlyphPanel();
		
		add(glyphPanel, constraints);
	}
	
	private void initButtons(Object constraints)
	{
		JPanel buttonPanel = new JPanel();
		
		addRandomizerButton(buttonPanel);
		addMutatorButton(buttonPanel);
		
		add(buttonPanel, constraints);
	}
	
	private void addRandomizerButton(Container container)
	{
		JButton buttonRandomize = new JButton("RANDOMIZE");
		
		buttonRandomize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Randomizing.");
				glyphPanel.randomizeGlyph();
			}
		});
		
		container.add(buttonRandomize);
	}
	
	private void addMutatorButton(Container container)
	{
		JButton buttonMutate = new JButton("MUTATE");
		
		buttonMutate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Mutating.");
				glyphPanel.mutateGlyph();
			}
		});
		
		container.add(buttonMutate);
	}
	
	public static void main(String[] args)
	{
		try {
			String laf = UIManager.getSystemLookAndFeelClassName();
//			laf = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
			System.out.println(laf);
			UIManager.setLookAndFeel(laf);

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GlyphFrame frame = new GlyphFrame();
		SwingUtilities.updateComponentTreeUI(frame);
		frame.setVisible(true);
	}
}
