package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SliderUI;

public class main {

	static JFrame Frame;
	static Label Label;

	// Vars to store current pole and cart position and previous positions
	static float action;
	static float pos, posDot, angle, angleDot;

	// Constants used for physics
	public static float cartMass = 1;
	public static float poleMass = 0.1f;
	public static float poleLength = 1;
	public static final float forceMag = 10;
	public static final float tau = 0.02f;
	public static final float fricCart = 0.00005f;
	public static final float fricPole = 0.005f;
	public static float totalMass = cartMass + poleMass;
	public static float halfPole = 0.5f * poleLength;
	public static float poleMassLength = halfPole * poleMass;
	public static final float fourthirds = 4 / 3;

	public static void main(String[] args) {
		setup();
		while (true) {
			run();
		}
	}

	static void run() {
		long startTime = System.currentTimeMillis();

		// Update the state of the pole;
		// First calc derivatives of state variables
		float force = forceMag * action;
		// double force = action;
		float sinangle = (float) Math.sin(angle);
		float cosangle = (float) Math.cos(angle);
		float angleDotSq = angleDot * angleDot;
		float common = (force + poleMassLength * angleDotSq * sinangle - fricCart * (posDot < 0 ? -1 : 0)) / totalMass;
		float angleDDot = (float) ((9.8 * sinangle - cosangle * common - fricPole * angleDot / poleMassLength)
				/ (halfPole * (fourthirds - poleMass * cosangle * cosangle / totalMass)));
		float posDDot = common - poleMassLength * angleDDot * cosangle / totalMass;

		// Now update current state.
		pos += posDot * tau;
		posDot += posDDot * tau;
		angle += angleDot * tau;
		angleDot += angleDDot * tau;

		if (pos > 2) {
			posDot = -posDot / 3;
			pos = 2;
		}
		if (pos < -2) {
			posDot = -posDot / 3;
			pos = -2;
		}

		try {
			startTime += 70;
			Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void setup() {
		// Initialize pole state.
		pos = 0;
		posDot = 0;
		angle = 0.1f; // Pole starts off at an angle
		angleDot = 0;
		action = 0;

		Frame = new JFrame();
		Frame.setVisible(true);
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame.setTitle("CartPole");
		Frame.setPreferredSize(new Dimension(1100, 850));
		Frame.setLayout(null);
		Frame.pack();
		Frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				action = 0;

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					action = -1;
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					action = 1;
				if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
					action = 0;
					resetPole();
				}

			}
		});

		Label = new Label();
		Label.setVisible(true);
		Label.setSize(new Dimension(800, 800));
		Frame.add(Label);

		Frame.add(Slider("Pole Lenght",800,0,300,40, 300, 100, new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				poleLength = ((JSlider) e.getSource()).getValue() / 100f;
				halfPole = 0.5f * poleLength;
				if (!((JSlider) e.getSource()).getValueIsAdjusting()) {
					((JSlider) e.getSource()).setToolTipText((String.valueOf(poleLength)));
				}
				Frame.requestFocus();
			}
		}));
		Frame.add(Slider("Pole mass",800,40,300,40, 100, 10, new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				poleMass = ((JSlider) e.getSource()).getValue() / 100f;
				poleMassLength = halfPole * poleMass;
				totalMass = cartMass + poleMass;
				if (!((JSlider) e.getSource()).getValueIsAdjusting()) {
					((JSlider) e.getSource()).setToolTipText((String.valueOf(poleMass)));
				}
				Frame.requestFocus();
			}
		}));
		Frame.add(Slider("Cart mass",800,80,300,40, 30, 10, new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				cartMass = ((JSlider) e.getSource()).getValue() / 10f;
				totalMass = cartMass + poleMass;
				if (!((JSlider) e.getSource()).getValueIsAdjusting()) {
					((JSlider) e.getSource()).setToolTipText((String.valueOf(cartMass)));
				}
				Frame.requestFocus();
			}
		}));
		Frame.requestFocus();
	}

	static JSlider Slider(String Name,int posx, int posy, int sizex, int sizey, int max, int value, ChangeListener Changelister) {
		JSlider slider = new JSlider(1, max, value);
		slider.addChangeListener(Changelister);
		Hashtable labelTable = new Hashtable();
		labelTable.put(1, new JLabel(Name));
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
		slider.setBounds(posx, posy, sizex, sizey);
		return slider;

	}

	static void resetPole() {
		pos = 0;
		posDot = 0;
		angle = 0.1f;
		angleDot = 0;
		((JSlider) Frame.getContentPane().getComponent(1)).setValue(100);
		((JSlider) Frame.getContentPane().getComponent(2)).setValue(10);
		((JSlider) Frame.getContentPane().getComponent(3)).setValue(10);
	}

}
