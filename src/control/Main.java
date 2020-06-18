package control;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import model.Entity;
import model.Sonda;
import model.Spritesheet;
import view.UI;
import view.World;

public class Main extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;

	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	private BufferedImage image;
	private BufferedImage menuBackground;

	public static List<Entity> entities;

	public static Spritesheet spritesheet;
	public static Spritesheet spritesheet2;

	public static World world;

	public static Sonda primeiraSonda;
	public static Sonda segundaSonda;

	public static Random rand;

	public UI ui;

	public static String gameState = "MENU";

	private char charPressed;
	private ArrayList<String> charPressedList;
	private boolean executeList = false;


	public boolean largura_defined = false;
	public boolean altura_defined = false;
	public static String largura = "";
	public static String altura = "";
	private boolean cordX_defined_1 = false;
	private boolean cordY_defined_1 = false;
	private String cordX_1 = "";
	private String cordY_1 = "";
	private boolean cordX_defined_2 = false;
	private boolean cordY_defined_2 = false;
	private String cordX_2 = "";
	private String cordY_2 = "";
	private int maxMovimentosSonda = 0;
	private int timeMovimentoSonda = 0;
	private int numSonda = 0;
	private boolean render = false;
	private boolean tryFind = false;
	private boolean find = false;
	private boolean pressedEnter = false;
	private String findedItem = "";
	private ArrayList<String> itensList;

	public Main() {
		// Sound.musicBackground.loop();
		rand = new Random();
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		// Inicializando objetos.
		charPressedList = new ArrayList<String>();
		ui = new UI();

		spritesheet2 = new Spritesheet("/menu.png");
		menuBackground = spritesheet2.getSprite(0, 0, 400, 268);

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		entities = new ArrayList<Entity>();
		itensList = new ArrayList<String>();
		itensList.add("Pedra");
		itensList.add("Et");
		itensList.add("Pizza");
		itensList.add("Foguete");
		itensList.add("Lixo Espacial");
		itensList.add("Satelite");
		itensList.add("Et Morto");
		itensList.add("Água");
		itensList.add("Estação espacial");
		itensList.add("Nave quebrada");

		spritesheet = new Spritesheet("/spritesheet.png");
		primeiraSonda = new Sonda(2, 2, 16, 16, spritesheet.getSprite(32, 0, 16, 16),0);
		segundaSonda = new Sonda(8, 8, 16, 16, spritesheet.getSprite(32, 0, 16, 16),1);
		entities.add(primeiraSonda);
	}

	public void initFrame() {
		frame = new JFrame("Sondas Em Marte");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		Main game = new Main();
		game.start();
	}

	public void tick() {
		if (gameState == "NORMAL") {
			if (executeList && charPressed != 0) {
				executeList = false;
				charPressed = 0;
			}
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}

		}
		if (gameState == "SONDA_ANDANDO") {
			sondaAndando();
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
		}
	}

	public void sondaAndando() {
		// System.out.println(timeMovimentoSonda);
		if (timeMovimentoSonda < maxMovimentosSonda) {
			// System.out.println(primeiraSonda.isMoved());
			if (numSonda == 0) {
				movePrimeiraSonda();
			} else if (numSonda == 1) {
				moveSegundaSonda();
			}
		} else {
			if (!tryFind && !find) {
				tryFind = true;
				ui.setStatus(true);
			}
			if (find && pressedEnter) {
				tryFind = false;
				find = false;
				pressedEnter = false;
				charPressedList.clear();
				charPressed = 0;
				timeMovimentoSonda = 0;
				ui.setStatus(true);
				numSonda = 1;
				entities.remove(primeiraSonda);
				entities.add(segundaSonda);
				gameState = "NORMAL";
			}
		}

	}

	private void movePrimeiraSonda() {
		if (!primeiraSonda.isMoved()) {
			if (charPressedList.get(timeMovimentoSonda).equals(Character.toString('L'))) {
				if (primeiraSonda.getDir() == primeiraSonda.getTop_dir()) {
					primeiraSonda.setDir(primeiraSonda.getLeft_dir());
				} else if (primeiraSonda.getDir() == primeiraSonda.getLeft_dir()) {
					primeiraSonda.setDir(primeiraSonda.getBottom_dir());
				} else if (primeiraSonda.getDir() == primeiraSonda.getBottom_dir()) {
					primeiraSonda.setDir(primeiraSonda.getRight_dir());
				} else if (primeiraSonda.getDir() == primeiraSonda.getRight_dir()) {
					primeiraSonda.setDir(primeiraSonda.getTop_dir());
				}
			} else if (charPressedList.get(timeMovimentoSonda).equals(Character.toString('M'))) {
				if (primeiraSonda.getDir() == primeiraSonda.getTop_dir()) {
					primeiraSonda.setUp(true);
				} else if (primeiraSonda.getDir() == primeiraSonda.getLeft_dir()) {
					primeiraSonda.setLeft(true);
				} else if (primeiraSonda.getDir() == primeiraSonda.getBottom_dir()) {
					primeiraSonda.setDown(true);
				} else if (primeiraSonda.getDir() == primeiraSonda.getRight_dir()) {
					primeiraSonda.setRight(true);
				}
			} else if (charPressedList.get(timeMovimentoSonda).equals(Character.toString('R'))) {
				if (primeiraSonda.getDir() == primeiraSonda.getTop_dir()) {
					primeiraSonda.setDir(primeiraSonda.getRight_dir());
				} else if (primeiraSonda.getDir() == primeiraSonda.getRight_dir()) {
					primeiraSonda.setDir(primeiraSonda.getBottom_dir());
				} else if (primeiraSonda.getDir() == primeiraSonda.getBottom_dir()) {
					primeiraSonda.setDir(primeiraSonda.getLeft_dir());
				} else if (primeiraSonda.getDir() == primeiraSonda.getLeft_dir()) {
					primeiraSonda.setDir(primeiraSonda.getTop_dir());
				}
			}
			timeMovimentoSonda++;
		}
	}

	private void moveSegundaSonda() {
		if (!segundaSonda.isMoved()) {
			if (charPressedList.get(timeMovimentoSonda).equals(Character.toString('L'))) {
				if (segundaSonda.getDir() == segundaSonda.getTop_dir()) {
					segundaSonda.setDir(segundaSonda.getLeft_dir());
				} else if (segundaSonda.getDir() == segundaSonda.getLeft_dir()) {
					segundaSonda.setDir(segundaSonda.getBottom_dir());
				} else if (segundaSonda.getDir() == segundaSonda.getBottom_dir()) {
					segundaSonda.setDir(segundaSonda.getRight_dir());
				} else if (segundaSonda.getDir() == segundaSonda.getRight_dir()) {
					segundaSonda.setDir(segundaSonda.getTop_dir());
				}
			} else if (charPressedList.get(timeMovimentoSonda).equals(Character.toString('M'))) {
				if (segundaSonda.getDir() == segundaSonda.getTop_dir()) {
					segundaSonda.setUp(true);
				} else if (segundaSonda.getDir() == segundaSonda.getLeft_dir()) {
					segundaSonda.setLeft(true);
				} else if (segundaSonda.getDir() == segundaSonda.getBottom_dir()) {
					segundaSonda.setDown(true);
				} else if (segundaSonda.getDir() == segundaSonda.getRight_dir()) {
					segundaSonda.setRight(true);
				}
			} else if (charPressedList.get(timeMovimentoSonda).equals(Character.toString('R'))) {
				if (segundaSonda.getDir() == segundaSonda.getTop_dir()) {
					segundaSonda.setDir(segundaSonda.getRight_dir());
				} else if (segundaSonda.getDir() == segundaSonda.getRight_dir()) {
					segundaSonda.setDir(segundaSonda.getBottom_dir());
				} else if (segundaSonda.getDir() == segundaSonda.getBottom_dir()) {
					segundaSonda.setDir(segundaSonda.getLeft_dir());
				} else if (segundaSonda.getDir() == segundaSonda.getLeft_dir()) {
					segundaSonda.setDir(segundaSonda.getTop_dir());
				}
			}
			timeMovimentoSonda++;
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		if (render) {
			world.render(g);
			primeiraSonda.render(g);
			segundaSonda.render(g);
		}
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		if (gameState == "MENU") {
			g.drawImage(menuBackground, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
			ui.render(g, largura, altura, largura_defined, altura_defined, cordX_defined_1, cordY_defined_1, cordX_1,
					cordY_1, cordX_defined_2, cordY_defined_2, cordX_2, cordY_2);
		} else {
			ui.render(g, charPressedList, gameState, charPressed, tryFind, find, pressedEnter, findedItem);
		}

		bs.show();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				// System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}

		}

		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameState == "NORMAL" && (e.getKeyChar() == 'M' || e.getKeyChar() == 'R' || e.getKeyChar() == 'L'
				|| e.getKeyChar() == 'm' || e.getKeyChar() == 'r' || e.getKeyChar() == 'l')) {
			charPressed = e.getKeyChar();
		}
		if (gameState == "NORMAL" && charPressed != 0 && e.getKeyCode() == KeyEvent.VK_ENTER) {
			charPressedList.add(Character.toString(charPressed).toUpperCase());
			executeList = true;
		}
		if (gameState == "NORMAL" && e.getKeyCode() == KeyEvent.VK_SPACE && charPressedList.size() > 0) {
			gameState = "SONDA_ANDANDO";
			ui.setStatus(false);
			maxMovimentosSonda = charPressedList.size();
		}
		if (gameState == "SONDA_ANDANDO" && tryFind && e.getKeyCode() == KeyEvent.VK_SPACE) {
			find = true;
			tryFind = false;
			findedItem = itensList.get(rand.nextInt(itensList.size()));
		}
		if (gameState == "SONDA_ANDANDO" && find && e.getKeyCode() == KeyEvent.VK_ENTER) {
			pressedEnter = true;
		}
		if (gameState == "MENU" && (e.getKeyChar() >= '0' && e.getKeyChar() <= '9')) {
			if (!largura_defined) {
				largura += e.getKeyChar();
			} else if (!altura_defined) {
				altura += e.getKeyChar();
			} else if (!cordX_defined_1) {
				cordX_1 += e.getKeyChar();
			} else if (!cordY_defined_1) {
				cordY_1 += e.getKeyChar();
			} else if (!cordX_defined_2) {
				cordX_2 += e.getKeyChar();
			} else if (!cordY_defined_2) {
				cordY_2 += e.getKeyChar();
			}
		}
		if (gameState == "MENU" && e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!largura_defined) {
				largura_defined = true;
				return;
			}
			if (!altura_defined && largura_defined) {
				altura_defined = true;
				return;
			}
			if (!cordX_defined_1) {
				cordX_defined_1 = true;
			} else if (!cordY_defined_1) {
				cordY_defined_1 = true;
			} else if (!cordX_defined_2) {
				cordX_defined_2 = true;
			} else if (!cordY_defined_2) {
				cordY_defined_2 = true;
				gameState = "NORMAL";
				world = new World();
				charPressedList.clear();
				charPressed = 0;
				render = true;
				int x1, x2, y1, y2;
				int larg, alt;
				x1 = Integer.parseInt(cordX_1) * 16;
				y1 = Integer.parseInt(cordY_1) * 16;
				x2 = Integer.parseInt(cordX_2) * 16;
				y2 = Integer.parseInt(cordY_2) * 16;
				larg =  Integer.parseInt(largura) * 16;
				alt = Integer.parseInt(altura) * 16;
	
				if(x1 > larg || x1 <= 0) {
					x1 = (larg - 2)* 16;
				}
				if(x2 > larg || x2 <= 0) {
					x2 = (larg - 2)* 16;
				}
				if(y1 > alt || y1 <= 0) {
					y1 = (alt - 2) * 16;
				}
				if(y2 > alt || y2 <= 0) {
					y2 = (alt - 2) * 16; 	
				}
				primeiraSonda.setX(x1);
				primeiraSonda.setY(y1);
				segundaSonda.setX(x2);
				segundaSonda.setY(y2);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public char getCharPressed() {
		return charPressed;
	}

	public void setCharPressed(char charPressed) {
		this.charPressed = charPressed;
	}

	public ArrayList<String> getCharPressedList() {
		return charPressedList;
	}

	public void setCharPressedList(ArrayList<String> charPressedList) {
		this.charPressedList = charPressedList;
	}

}