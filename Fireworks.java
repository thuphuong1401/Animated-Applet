/*Phuong Vu, pvu3, Project 3, lab TR 12:30 - 13:45
 I did not copy code from anyone on this proejct
 */

import java.awt.color.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Fireworks extends JFrame implements ActionListener,ChangeListener, ItemListener {
	double x;
	double y;
	double angle;
	double speed;
	double grav = 9.8;
	double time;
	JButton button = new JButton("Launch!");
	JSlider sliderAngle = new JSlider(0,90);
	JSlider sliderSpeed = new JSlider(50,150);
	JSlider sliderTime = new JSlider(0,20);
	JComboBox comboBoxColor = new JComboBox(new String[] {"Black", "Red", "Magenta", "Yellow", "Blue", "Green"});
	JComboBox comboBoxShape = new JComboBox(new String[] {"Snow flake","Blooming snow flake","Surprising Triangles","Bland Rectangles","Bland Circles","Cute Little Chicken", "Basic Flowers"});
	JLabel labelAngle = new JLabel("Angle");
	JLabel labelSpeed = new JLabel("Speed");
	JLabel labelTime = new JLabel("Time");
	JLabel labelColor = new JLabel("Color");
	JLabel labelShape = new JLabel("Shape");
	Color currentColor = Color.BLACK;
	String selectedExplosion = "Snow flake";
	int currentShape = 1;
	Point p1 ;
	Point p2 ;
	Point p3 ;


    //constructor of the Fireworks application
	public Fireworks() {
		setLayout(new FlowLayout());
		add(labelAngle);
		sliderAngle.setToolTipText("Drag the knob to change the value of the angle");
		add(sliderAngle);
		sliderAngle.addChangeListener(this);

		add(labelSpeed);
		sliderSpeed.setToolTipText("Drag the knob to change the value of the speed");
		add(sliderSpeed);
		sliderSpeed.addChangeListener(this);

		add(labelTime);
		sliderTime.setToolTipText("Drag the knob to change the value of the time");
		add(sliderTime);
		sliderTime.addChangeListener(this);

		add(labelColor);
		add(comboBoxColor);
		comboBoxColor.setToolTipText("Chose your favorite color");
		comboBoxColor.addItemListener(this);


		add(labelShape);
		add(comboBoxShape);
		comboBoxShape.setToolTipText("Choose the shape of the explosion");
		comboBoxShape.addItemListener(this);

		add(button);
		button.setToolTipText("Press this and wait for the amazing stuff to happen!");
		button.addActionListener(this);

		angle = sliderAngle.getValue();
		speed = sliderSpeed.getValue();
		time = sliderTime.getValue();
	}


	public static void main(String[] args) { //fireworks is an instance of JFrame as the class Fireworks extends JFrame
		Fireworks fireworks = new Fireworks();
		fireworks.setVisible(true);
		fireworks.setSize(1300,600);
		fireworks.setDefaultCloseOperation(fireworks.EXIT_ON_CLOSE);
	}

	//method to draw the snow flake (appear right away)
	public void drawSnowflake(Graphics g, int x, int y, int size, int level) {
		for (int i = 0; i < 360; i += 60) {
			double rad = i * Math.PI / 180;
			int x2 = (int) (x + Math.cos(rad) * size);
			int y2 = (int) (y + Math.sin(rad) * size);
			g.drawLine(x, y, x2, y2);
			if (level > 0) { //base case is when level = 0 and the method just draw a line
				drawSnowflake(g, x2, y2, size/3, level-1);
			}
		}
	}

	//method to draw the blooming snow flake (appear gradually). The effect is achieved by setting higher recursion level.
	public void drawBloomingSnowflake(Graphics g, int x, int y, int size, int level) {
		for (int i = 0; i < 360; i += 60) {
			double rad = i * Math.PI / 180;
			int x2 = (int) (x + Math.cos(rad) * size);
			int y2 = (int) (y + Math.sin(rad) * size);
			g.drawLine(x, y, x2, y2);
			if (level > 0) { //base case is when level = 0 and the method just draw a line
				drawBloomingSnowflake(g, x2, y2, size/3, level-1);
			}
		}
	}

	//method to draw Triangle explosion
	public static void drawFigure(int level, Graphics g,
			Point p1, Point p2, Point p3) {
		if (level == 1) {
			// base case: simple triangle
			Polygon p = new Polygon();
			p.addPoint(p1.x, p1.y);
			p.addPoint(p2.x, p2.y);
			p.addPoint(p3.x, p3.y);
			g.fillPolygon(p);
		} else {
			// recursive case, split into 3 triangles
			Point p4 = midpoint(p1, p2);
			Point p5 = midpoint(p2, p3);
			Point p6 = midpoint(p1, p3);

			// recurse on 3 triangular areas
			drawFigure(level - 1, g, p1, p4, p6);
			drawFigure(level - 1, g, p4, p2, p5);
			drawFigure(level - 1, g, p6, p5, p3);
		}
	}

	// returns the midpoint of p1 and p2
	public static Point midpoint(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}
	
	//method to draw the flower
	public void drawBasicFlowers(Graphics g, int x1, int y1) {
		Graphics gPetal = getGraphics();
		
		g.setColor(Color.YELLOW);
		g.fillOval(x1, y1, 20, 20);
		
		gPetal.setColor(Color.RED);
		gPetal.fillOval(x1 + 4, y1 + 12, 15, 35);
		gPetal.fillOval(x1 + 4, y1 - 32, 15, 35);
		gPetal.fillOval(x1 + 15, y1 - 3, 35, 15);
		gPetal.fillOval(x1 - 28 , y1 - 3, 35, 15);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) { //the launch button
		Graphics g1 = getGraphics();
		super.paint(g1); //clear the panel
		g1.setColor(currentColor); //set the color for the trajectory 
		
		//trajectory
		for(double i = 0; i<=time; i+=0.01) { //main trajectory
			x = speed*Math.cos(angle)*i;
			y = speed*Math.sin(angle)*i - (1.0/2.0)*grav*Math.pow(i, 2);
			g1.drawOval((int)x,(int)(getHeight() - y),1,1);
			System.out.println(i);
		}

		double splitAngle = Math.atan((speed*Math.sin(angle) - grav*time) / (speed*Math.cos(angle))); 
		
		//the first for loop is used to control the angle that the sub - trajectories splits from the main trajectory
		for(double angle = splitAngle - Math.toRadians(180); angle <= splitAngle + Math.toRadians(180); angle+=Math.toRadians(40)) {
			double x1 = 0;
			double y1 = 0;
			for(double i = 0; i<=1.2; i+=0.01) { //this for loop draws the pathway of sub - trajectories
				x1 = speed*Math.cos(angle)*i;
				y1 = speed*Math.sin(angle)*i - (1.0/2.0)*grav*Math.pow(i, 2);
				g1.drawOval((int)(x+x1),(int)(getHeight() - (y+y1)),1,1);
			}
			if(currentShape == 1) {
				drawSnowflake(g1,(int)(x + x1),(int)(getHeight()-(y + y1)),25,2);
			} else if(currentShape == 2) {
				drawBloomingSnowflake(g1,(int)(x + x1),(int)(getHeight()-(y + y1)),25,6);
			} else if(currentShape == 3) {
				p1 = new Point((int)(x + x1), (int)(getHeight() - (y+y1))-20);
				p2 = new Point((int)(x + x1 - 20),(int)(getHeight() - (y+y1))+ 20);
				p3 = new Point((int)(x + x1 + 20),(int)(getHeight() - (y+y1)) + 20);
				drawFigure(3,g1,p1,p2,p3);
			} else if(currentShape == 4) {
				g1.fillRect((int)(x + x1 - 15),(int)(getHeight() - (y+y1) -15), 30, 30);
			} else if(currentShape == 5) {
				g1.fillOval((int)(x + x1 - 7), (int)(getHeight() - (y+y1) - 7), 20, 20);
			} else if(currentShape == 6) {
				g1.setColor(Color.YELLOW);
				g1.fillOval((int)(x + x1 - 12), (int)(getHeight() - (y+y1) - 12), 40, 40);
				g1.setColor(Color.BLACK);
				g1.fillOval((int)(x + x1 + 10), (int)(getHeight() - (y + y1) - 6), 8, 8);
				g1.fillOval((int)(x + x1 - 4), (int)(getHeight() - (y + y1) - 6), 8, 8);
				g1.setColor(Color.ORANGE);
				g1.fillOval((int)(x + x1 + 15), (int)(getHeight() - (y + y1) + 15), 18, 10);	
			} else if(currentShape == 7) {
				drawBasicFlowers(g1,(int)(x + x1 - 7), (int)(getHeight() - (y+y1) - 7));
			}
			
		}	
	}


	@Override
	public void stateChanged(ChangeEvent arg0) { //slider 
		angle = Math.toRadians(sliderAngle.getValue());
		labelAngle.setText("Angle " + (int)(Math.toDegrees(angle)));
		speed = sliderSpeed.getValue();
		time = sliderTime.getValue();
		labelSpeed.setText("Speed " + (int)speed);
		labelTime.setText("Time " + (int)time);
	}

	@Override
	public void itemStateChanged(ItemEvent e) { //comboBox
		if (comboBoxColor.getSelectedItem().equals("Black")) {
			currentColor = Color.BLACK;
		} else if(comboBoxColor.getSelectedItem().equals("Red")) {
			currentColor = Color.RED;
		} else if(comboBoxColor.getSelectedItem().equals("Magenta")) {
			currentColor = Color.MAGENTA;
		} else if(comboBoxColor.getSelectedItem().equals("Yellow")) {
			currentColor = Color.YELLOW;
		} else if(comboBoxColor.getSelectedItem().equals("Green")) {
			currentColor = Color.GREEN;
		} else if(comboBoxColor.getSelectedItem().equals("Blue")) {
			currentColor = Color.BLUE;
		}

		if (comboBoxShape.getSelectedItem().equals("Snow flake")) {
			currentShape = 1;
		} else if(comboBoxShape.getSelectedItem().equals("Blooming snow flake")) {
			currentShape = 2;
		} else if (comboBoxShape.getSelectedItem().equals("Surprising Triangles")) {
			currentShape = 3;
		} else if (comboBoxShape.getSelectedItem().equals("Bland Rectangles")) {
			currentShape = 4;
		} else if (comboBoxShape.getSelectedItem().equals("Bland Circles")) {
			currentShape = 5;
		} else if (comboBoxShape.getSelectedItem().equals("Cute Little Chicken")) {
			currentShape = 6;
		} else if (comboBoxShape.getSelectedItem().equals("Basic Flowers")) {
			currentShape = 7;
		}

	}
}
