package modele;

import java.util.ArrayList;

import application.DessinPion;

public class Situation {

	/**nom de la situation*/
	private String nom;

	/**nb d'instances*/
	private static int nbInstances = 0;

	/**no de l'instance*/
	private int noInstances;

	/**no de ligne*/
	private int line;
	public int getLine() {
		return line;
	}

	/**no de colonne*/
	private int column;
	
	public int getColumn() {
		return column;
	}

	/**nb pions restant adversaire*/
	private int nbPionsRestantAdversaire;

	/**nb pions */
	private int nbPionsRestant;

	/**indique si l'etat est un etat en mode Max*/
	private boolean max = true;

	/** liste des etats accessibles a partir de l'etat courant*/
	private ArrayList<Situation> successeurs;

	public ArrayList<Situation> getSuccesseurs() {
		return successeurs;
	}

	/**indique si l'etat/la situation est une feuille de l'arbre*/
	private boolean feuille;

	/**indique si l'etat/la situation est une feuille définitive de l'arbre
	 * (i.e. impossible de créer des successeurs a cette situation)*/
	private boolean close;

	/**h = heuristique, estimation de la valeur de la situation*/
	private double h;

	/**grille de jeu correspondant a la situation*/
	private int[][] matriceJeu;
	
	/**Pion � droite*/
	DessinPion[][] pions;
	
	int index;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	/*Pion modifié*/
	DessinPion modifiedPion;
	
	public DessinPion[][] getPions() {
		return pions;
	}

	/**constructeur par defaut*/
	public Situation()
	{
		noInstances = nbInstances++;
		nom=""+noInstances;
		h = 0;
		matriceJeu = new int[6][5];
		successeurs = new ArrayList<>();
		pions = new DessinPion[2][12];
		modifiedPion = null;
	}

	public DessinPion getModifiedPion() {
		return modifiedPion;
	}

	public void setModifiedPion(DessinPion modifiedPion) {
		this.modifiedPion = modifiedPion;
	}

	public void setPions(DessinPion[][] pions) {
		this.pions = pions;
	}

	/**constructeur initialisant l'estimation h 
	 * @param _h estimation de la situation*/
	public Situation(int _h)
	{
		this();
		h = _h;
	}

	/**constructeur initialisant l'estimation h et le type de noeud
	 * @param _h estimation de la situation
	 * @param _estMax determine si la valeur de la situation doit etre maximisee ou non*/
	public Situation(int _h, boolean _estMax)
	{
		this(_h);
		max = _estMax;
	}

	public Situation(DessinPion[][] pions, int[][] matriceJeu) {
		super();
		this.pions = pions;
		this.matriceJeu = matriceJeu;
		successeurs = new ArrayList<>();
	}

	/**
	 * @return the h
	 */
	public double getH() {
		return h;
	}

	/**
	 * @param h the h to set
	 */
	public void setH(double h) {
		this.h = h;
	}

	/**fonction evaluant la situation courante; calcul le 'h'*/
	void evaluer()
	{
		double eval = 0d;
		double coefSituation = (this.max?-1:1);
		//les valeurs positives sont pour l'IA
		// et sont diminuées si le jeu suivant est pour l'humain
		//elles sont augmentées sinon
		double valeur = dangerPossibles();
		valeur += 0.1*coefSituation*Math.abs(valeur);
		eval+=valeur;
		h = eval;
		//		afficheMatrice();
	}
	
	private double dangerPossibles()
	{
		return 0d;
	}
	
	public void setFeuille(boolean _feuille) {
		this.feuille = _feuille;
	}

	public boolean isClose() {
		return this.close;
	}

	public boolean isFeuille() {
		return this.feuille;
	}

	public int[][] getMatriceJeu() {
		return this.matriceJeu;
	}

	public boolean isMax() {
		return this.max;
	}

	public void setColumn(int c) {
		this.column = c;
	}

	public void setLine(int l) {
		this.line = l;
	}

	public void setMatriceJeu(int[][] matrice) 
	{
		this.matriceJeu = matrice;
	}

	/**ajoute un successeur a la situation courante
	 * @param s successeur a la situation courante*/
	public void addSuccesseur(Situation s)
	{
		successeurs.add(s);
		s.nom = nom+"."+s.nom;
	}
	
	public String toString(){
		String retour="nom = "+ nom +"\nh = " + h + "\ncolumn : " + column + "\nligne : " + line + "\n";
		
		for(int i=0; i < 6; i++){
			for(int j=0; j < 5; j++){
				retour += matriceJeu[i][j] +" ";
			}
			retour += "\n";
		}
		retour += "____________\n";
		for(Situation sprim : successeurs){
			retour += sprim;
		}
		return retour;
	}


}
