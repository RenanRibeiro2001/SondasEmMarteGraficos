package model;

public class Plato {
	
	private int tamanhoX;
	private int tamanhoY;
        
	public Plato(int tamanhoX, int tamanhoY) {
		this.tamanhoX = tamanhoX;
		this.tamanhoY = tamanhoY;
	}
	
	public void coordenadasPlato() {
		int [][] coordenadas = new int[tamanhoX][tamanhoY];
	}

	public int getTamanhoX() {
		return tamanhoX;
	}

	public void setTamanhoX(int tamanhoX) {
		this.tamanhoX = tamanhoX;
	}

	public int getTamanhoY() {
		return tamanhoY;
	}

	public void setTamanhoY(int tamanhoY) {
		this.tamanhoY = tamanhoY;
	}
	
	
}
