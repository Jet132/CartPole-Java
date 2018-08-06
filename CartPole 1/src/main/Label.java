package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class Label extends JLabel {

	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		update(g);

		repaint();
	}

	public void update(Graphics g) {
		Dimension d = getSize();
		Color cartColor = new Color(0, 20, 255);
		Color arrowColor = new Color(255, 255, 0);
		Color trackColor = new Color(100, 100, 50);

		// Draw Track.
		g.setColor(trackColor);
		g.drawLine(0, pixY(d, 0)+15, getWidth(),pixY(d, 0)+15);

		// Draw message
		String msg1 = "Position = " + main.pos + " ";
		String msg2 = "Angle = " + main.angle + " ";
		String msg3 = "angleDot = " + main.angleDot;
		g.drawString(msg1, 20, d.height - 20);
		g.drawString(msg2, 20, d.height - 40);
		g.drawString(msg3, 20, d.height - 60);

		// Draw cart.
		g.setColor(cartColor);
		g.fillRect(pixX(d, main.pos - 0.2), pixY(d, 0), pixDX(d, 0.4), pixDY(d, -0.2));

		// Draw pole
		g.drawLine(pixX(d, main.pos), pixY(d, 0), pixX(d, main.pos + Math.sin(main.angle) * main.poleLength),
				pixY(d, main.poleLength * Math.cos(main.angle)));

		// Draw action arrow.
		if (main.action != 0) {
			int signAction = (main.action > 0 ? 1 : (main.action < 0) ? -1 : 0);
			int tipx = pixX(d, main.pos + 0.2 * signAction);
			int tipy = pixY(d, -0.1);
			g.setColor(arrowColor);
			g.drawLine(pixX(d, main.pos), pixY(d, -0.1), tipx, tipy);
			g.drawLine(tipx, tipy, tipx - 4 * signAction, tipy + 4);
			g.drawLine(tipx, tipy, tipx - 4 * signAction, tipy - 4);
		}

	}

	public int pixX(Dimension d, double v) {
		return (int) Math.round((v + 2.5) / 5.0 * d.width);
	}

	public int pixY(Dimension d, double v) {
		return (int) Math.round(d.height - (v + 2.5) / 5.0 * d.height);
	}

	public int pixDX(Dimension d, double v) {
		return (int) Math.round(v / 5.0 * d.width);
	}

	public int pixDY(Dimension d, double v) {
		return (int) Math.round(-v / 5.0 * d.height);
	}

}
