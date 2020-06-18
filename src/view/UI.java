package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import control.Main;

public class UI {

	private InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("NoTime.ttf");

	private static Font ConeriaScript30F;
	private boolean status = true;

	public UI() {
		try {
			setConeriaScript30F(Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(25f));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void render(Graphics g, ArrayList<String> charPressedList, String gt, char charPressed, boolean tryFind,
			boolean find, boolean pressedEnter, String itemList) {
		if (status) {
			g.setColor(Color.red);
			g.setFont(ConeriaScript30F);
			if (gt.equals("NORMAL") && !tryFind) {
				g.drawString("Digite uma letra do comando e aperte enter:", 20, 37);
				int multiY = 0;
				int multiX = 0;
				if (charPressedList.size() > 0) {
					for (int i = 0; i < charPressedList.size(); i++) {
						if (i % 23 == 0) {
							multiY++;
							multiX = 0;
						} else {
							multiX++;
						}
						g.drawString(charPressedList.get(i).toUpperCase(), 23 + multiX * 30, 40 + multiY * 40);
					}
				}
				g.drawString(Character.toString(charPressed).toUpperCase(), 583, 38); // 23
				g.drawString("Aperte Espaco para a sonda Andar", 140, 450);
			} else if (gt.equals("SONDA_ANDANDO") && tryFind) {
				int fontHeight = g.getFontMetrics().getHeight();
				drawCentralizedString(g, "Aperte Espaco para ver o que a sonda achou", 450);
				// g.drawString("Aperte Espaco para ver o que a sonda achou", 80, 450);
			} else if (gt.equals("SONDA_ANDANDO") && find) {
				int fontHeight = g.getFontMetrics().getHeight();
				drawCentralizedString(g, "Aperte Enter para continuar", 450);
				drawCentralizedString(g, "A Sonda achou: " + itemList, 300);
			}
		}
	}

	public void render(Graphics g, String largura, String altura, boolean largura_defined, boolean altura_defined,
			boolean cordX_defined_1, boolean cordY_defined_1, String cordX_1, String cordY_1, boolean cordX_defined_2,
			boolean cordY_defined_2, String cordX_2, String cordY_2) {
		if (Main.gameState == "MENU") {
			g.setColor(Color.black);
			g.setFont(ConeriaScript30F);
			int fontHeight = g.getFontMetrics().getHeight();
			if (!largura_defined || !altura_defined) {	
				drawCentralizedString(g, "Largura: " + largura, 200);
				drawCentralizedString(g, "Altura: " + altura, 300);
				drawCentralizedString(g, "Aperte Enter para continuar", 450);
			}else if(!cordX_defined_1 || !cordY_defined_1){
				drawCentralizedString(g, "Primeira Sonda", 150);
				drawCentralizedString(g, "X: " + cordX_1, 200);
				drawCentralizedString(g, "Y: " + cordY_1, 300);
				drawCentralizedString(g, "Aperte Enter para continuar", 450);
			}else if(!cordX_defined_2 || !cordY_defined_2) {
				drawCentralizedString(g, "Segunda Sonda", 150);
				drawCentralizedString(g, "X: " + cordX_2, 200);
				drawCentralizedString(g, "Y: " + cordY_2, 300);
				drawCentralizedString(g, "Aperte Enter para continuar", 450);
			}
		}
	}

	private void drawCentralizedString(Graphics g, String str, int y) {
		g.drawString(str, (Main.WIDTH * Main.SCALE) / 2 - g.getFontMetrics().stringWidth(str) / 2, y);
	}

	public static void setConeriaScript30F(Font coneriaScript30F) {
		ConeriaScript30F = coneriaScript30F;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
