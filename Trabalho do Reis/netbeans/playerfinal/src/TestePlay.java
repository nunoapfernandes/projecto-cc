import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class TestePlay {

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
	public static void main(String[] args)
			throws UnsupportedAudioFileException, IOException,
			LineUnavailableException {
                final Musica m = new Musica();
		JFrame frame = new JFrame("Player");
		frame.setLocation(400, 300);
		JPanel cont = new JPanel(new java.awt.BorderLayout());
		frame.add(cont);
		final JButton button = new JButton("Play");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				play = true;
				button.setVisible(false);
				try {
					m.play();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (LineUnavailableException e1) {
					e1.printStackTrace();
				}
			}
		});
		cont.add(button, java.awt.BorderLayout.NORTH);
		final JButton button1 = new JButton("Pause");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (play) {
					button1.setText("Resume");
					m.stop();
					play = false;
				} else {
					button1.setText("Pause");
					play = true;
					try {
						m.play();
					} catch (IOException ee) {
						ee.printStackTrace();
					} catch (LineUnavailableException eee) {
						eee.printStackTrace();
					}
				}
			}
		});
		cont.add(button1, java.awt.BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static boolean play;
}
