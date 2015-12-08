package dk.itu.jglyph;

import java.awt.BorderLayout;
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
	 * Auto-Generated serial version.s 
	 */
	private static final long serialVersionUID = -3005890889450279723L;
	
	private final static String FRAME_TITLE = "JGlyph";
	
	public GlyphFrame()
	{
		init();
	}
	
	private void initGlyph(Object constraints)
	{
		final GlyphPanel glyphPanel = new GlyphPanel();
		
		add(glyphPanel, constraints);
	}
	
	private void initButtons(Object constraints)
	{
		JPanel buttonPanel = new JPanel();
		
		JButton button1 = new JButton("BUTTON 1");
		JButton button2 = new JButton("BUTTON 2");
		JButton button3 = new JButton("BUTTON 3");
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("BUTTON 1");
			}
		});
		
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("BUTTON 2");
			}
		});
		
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("BUTTON 3");
			}
		});
		
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);
		
		add(buttonPanel, constraints);
	}
	
	public void init()
	{
		setLayout(new BorderLayout());
		
		initGlyph(BorderLayout.CENTER);
		initButtons(BorderLayout.SOUTH);
		
		setTitle(FRAME_TITLE);
		
		setSize(512, 512);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args)
	{
		try {
			String laf = UIManager.getSystemLookAndFeelClassName();
			laf = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
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
