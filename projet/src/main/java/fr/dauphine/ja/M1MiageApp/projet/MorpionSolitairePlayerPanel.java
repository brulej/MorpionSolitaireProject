package fr.dauphine.ja.M1MiageApp.projet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 * This class create a morpion solitaire for solo player
 *
 */
class MorpionSolitairePlayerPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton home_button;
	private Boolean end_of_game = false;
	private Grid grid;
	private String message = "A toi de jouer ...";
	private int score = 0;
	private Font scoreFont;
	private String game_version;
	private String name;

	public MorpionSolitairePlayerPanel(String game_version, String name) {
		setPreferredSize(new Dimension(1000, 750));
		setBackground(Color.white);
		setLayout(null);

		home_button = new JButton("Retour");
		home_button.addActionListener(this);
		home_button.setBounds(500, 20, 80, 30);
		add(home_button);

		setFont(new Font("SansSerif", Font.BOLD, 16));
		scoreFont = new Font("SansSerif", Font.BOLD, 12);

		grid = new Grid(35, 9, game_version);
		grid.newGame();
		
		this.name=name;
		this.game_version=game_version;

		start();
	}

	public final void start() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
					grid.showHints();
				else {
					Grid.Result res = grid.playerMove(e.getX(), e.getY(), score + 1);
					if (res == Grid.Result.OK) {
						score++;
					}

					if (grid.possibleMoves().isEmpty()) {
						end_of_game = true;
						HistoryManager historyMngr = new HistoryManager("history.xlsx");
						try {
							historyMngr.saveScore(name, score, game_version);
					    	} catch (FileNotFoundException exep) {
					    		exep.printStackTrace();
					    	} catch (IOException exep) {
					    		exep.printStackTrace();
					    	}	
						repaint();
					}
				}
				repaint();
			}
		});
		
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					
				}
			}
		}).start();
	}

	@Override
	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		Graphics2D g = (Graphics2D) gg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		grid.draw(g, getWidth(), getHeight());

		if (end_of_game) {
			message = "Fini ... ";
			message += "Et �a fait " + score + " points pour toi.";
		}

		g.setColor(Color.white);
		g.fillRect(0, getHeight() - 50, getWidth(), getHeight() - 50);

		g.setColor(Color.lightGray);
		g.setStroke(new BasicStroke(1));
		g.drawLine(0, getHeight() - 50, getWidth(), getHeight() - 50);

		g.setColor(Color.darkGray);

		g.setFont(getFont());
		g.drawString(message, 20, getHeight() - 18);

		String s2 = "Score " + String.valueOf(score);
		g.drawString(s2, getWidth() - 100, getHeight() - 20);
	}


	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent e) {
		JFrame f = new Home();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		Window w = SwingUtilities.getWindowAncestor(this);
		w.setVisible(false);
	}
}