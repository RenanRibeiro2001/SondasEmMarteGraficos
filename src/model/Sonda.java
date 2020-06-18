package model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import control.Camera;
import control.Main;
import view.World;

public class Sonda extends Entity {

	private boolean right, up, left, down;
	private final int right_dir = 0, left_dir = 1, top_dir = 2, bottom_dir = 3;
	private boolean movingRight = false, movingLeft = false, movingUp = false, movingDown = false;
	private int dir = right_dir;

	private double speed = 1;

	private int moveFrames = 0, maxMoveFrames = 32;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage rightPlayer;
	private BufferedImage leftPlayer;
	private BufferedImage topPlayer;
	private BufferedImage bottomPlayer;
	private int numSprites;

	public Sonda(int x, int y, int width, int height, BufferedImage sprite, int numSprites) {
		super(x, y, width, height, sprite);
		if (numSprites == 0) {
			rightPlayer = Main.spritesheet.getSprite(32, 0, 16, 16);
			leftPlayer = Main.spritesheet.getSprite(32, 16, 16, 16);
			topPlayer = Main.spritesheet.getSprite(32, 32, 16, 16);
			bottomPlayer = Main.spritesheet.getSprite(32, 48, 16, 16);
		}else if(numSprites == 1) {
			rightPlayer = Main.spritesheet.getSprite(48, 0, 16, 16);
			leftPlayer = Main.spritesheet.getSprite(48, 16, 16, 16);
			topPlayer = Main.spritesheet.getSprite(48, 32, 16, 16);
			bottomPlayer = Main.spritesheet.getSprite(48, 48, 16, 16);
		}
	}

	public void tick() {
		depth = 1;
		// setMoved(false);
		if (right && World.isFree((int) (x + speed), this.getY())) {
			setMoved(true);
			// dir = right_dir;
			right = false;
			// x += speed;
			movingRight = true;
		} else if (left && World.isFree((int) (x - speed), this.getY())) {
			setMoved(true);
			// dir = left_dir;
			left = false;
			// x -= speed;
			movingLeft = true;
		}
		// System.out.println(World.isFree(this.getX(), (int) (y - speed)));
		if (up && World.isFree(this.getX(), (int) (y - speed))) {
			setMoved(true);
			up = false;
			// y -= speed;
			movingUp = true;
		} else if (down && World.isFree(this.getX(), (int) (y + speed))) {
			setMoved(true);
			down = false;
			// y += speed;
			movingDown = true;
		}

		moving();
		updateCamera();

	}

	public void moving() {
		/*
		 * if (moved) { frames++; if (frames == maxFrames) { frames = 0; index++; if
		 * (index > maxIndex) index = 0; } }
		 */
		if (movingRight) {
			moveFrames++;
			x += speed;
			if (moveFrames >= maxMoveFrames) {
				moveFrames = 0;
				movingRight = false;
				setMoved(false);
			}
		} else if (movingLeft) {
			moveFrames++;
			x -= speed;
			if (moveFrames >= maxMoveFrames) {
				moveFrames = 0;
				movingLeft = false;
				setMoved(false);
			}
		} else if (movingUp) {
			moveFrames++;
			y -= speed;
			if (moveFrames >= maxMoveFrames) {
				moveFrames = 0;
				movingUp = false;
				setMoved(false);
			}
		} else if (movingDown) {
			moveFrames++;
			y += speed;
			if (moveFrames >= maxMoveFrames) {
				moveFrames = 0;
				movingDown = false;
				setMoved(false);
			}
		}
	}

	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Main.WIDTH / 2), 0, World.WIDTH * 16 - Main.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Main.HEIGHT / 2), 0, World.HEIGHT * 16 - Main.HEIGHT);
	}

	public void render(Graphics g) {
		if (dir == right_dir) {
			g.drawImage(rightPlayer, this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == left_dir) {
			g.drawImage(leftPlayer, this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == top_dir) {
			g.drawImage(topPlayer, this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == bottom_dir) {
			g.drawImage(bottomPlayer, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}

	public BufferedImage getRightPlayer() {
		return rightPlayer;
	}

	public void setRightPlayer(BufferedImage rightPlayer) {
		this.rightPlayer = rightPlayer;
	}

	public BufferedImage getLeftPlayer() {
		return leftPlayer;
	}

	public void setLeftPlayer(BufferedImage leftPlayer) {
		this.leftPlayer = leftPlayer;
	}

	public BufferedImage getTopPlayer() {
		return topPlayer;
	}

	public void setTopPlayer(BufferedImage topPlayer) {
		this.topPlayer = topPlayer;
	}

	public BufferedImage getBottomPlayer() {
		return bottomPlayer;
	}

	public void setBottomPlayer(BufferedImage bottomPlayer) {
		this.bottomPlayer = bottomPlayer;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public int getRight_dir() {
		return right_dir;
	}

	public int getLeft_dir() {
		return left_dir;
	}

	public int getTop_dir() {
		return top_dir;
	}

	public int getBottom_dir() {
		return bottom_dir;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}
}
