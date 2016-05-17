package application;

import java.awt.Point;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DessinPion extends Circle {
	/**vrai si le jeton est actuellement selectionne*/
	boolean selected;
	/**numero du joueur associe*/
	int joueur;
	/**couleur courante du jeton*/
	Color cj;
	/**position du jeton dans la grille (null si non posé)*/
	Point position;
	/**ancienne position du jeton dans la grille (null si non posé)*/
	Point anciennePosition;
	/**couleur pour joueur 1*/
	public static Color couleurJ1 = Color.SNOW;
	/**couleur pour joueur 2*/
	public static Color couleurJ2 = Color.DARKSLATEGRAY;
	/**couleur pour joueur 1 si jeton selectionne*/
	public static Color couleurJ1Selected = Color.GAINSBORO;
	/**couleur pour joueur 2 si jeton selectionne*/
	public static Color couleurJ2Selected = Color.FORESTGREEN;


	/**
	*constructeur
	*@param centerX coordonnee X du centre du disque
	*@param centerY coordonnee Y du centre du disque
	*@param radius taille en pixel du rayon du disque
	*@param _joueur numero du joueur associe
	*/
	public DessinPion(double centerX, double centerY, double radius, int _joueur) {
		super(centerX, centerY, radius);
		selected = false;
		joueur = _joueur;
		getColorPion();
		setFill(cj);
	}

	/**active ou desactive la selection du jeton*/
	public void switchSelected()
	{
		selected = !selected;
		getColorPion();
	}

	/**definit la bonne couleur pour le jeton en fonction du joueur et de son état sélectionné ou non
	*@return la couleur du jeton*/
	public Color getColorPion()
	{
		 cj =  (selected? (joueur==1?couleurJ1Selected:couleurJ2Selected) : (joueur==1?couleurJ1:couleurJ2));
		return cj;
	}

	/**remplit le disque avec la couleur courante*/
	public void colorPion()
	{
		setFill(cj);
	}

	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Point position) {
		this.anciennePosition = this.position;
		this.position = position;
	}

	/**
	 * @return the joueur
	 */
	public int getJoueur() {
		return joueur;
	}

	/**
	 * @return the anciennePosition
	 */
	public Point getAnciennePosition() {
		return anciennePosition;
	}

	/**
	 * @param anciennePosition the anciennePosition to set
	 */
	public void setAnciennePosition(Point anciennePosition) {
		this.anciennePosition = anciennePosition;
	}
	
}
