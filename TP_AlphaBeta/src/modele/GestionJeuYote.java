package modele;

import java.awt.Point;
import java.util.ArrayList;

import application.AppliYote;
import application.DessinPion;

/**classe representant le jeu et sa gestion*/
public class GestionJeuYote {
	/**erreur si pose dans une case occupee*/
	static int JETONPRESENT = -1;
	/**erreur si deplacement trop loin (>1 case ou en diagonal)*/
	static int TROPLOIN = - 2;
	/**erreur si saut au dessus de son propre pion*/
	static int AUTOPRISE = -3;
	/**erreur action inverse de la dernière action pour le joueur*/
	static int RETOUR = -4;
	/**coordonnnees incorrectes*/
	static int IMPOSSIBLE = 0;
	/**mouvement, pose correcte*/
	static int MVTOK = 1;
	/**prise de pion adverse correcte*/
	static int PRISEOK = 2;
	
	static final int HEIGHT = 5;
	static final int WIDTH = 6;

	/**matrice indiquant si une case est libre(0), ou prise par un joueur (1 ou 2)*/
	int[][] matriceJeu;
	
	public int[][] getMatriceJeu() {
		return matriceJeu;
	}

	/**no du joeur devant agir*/
	int noJoueurActif;
	
	AppliYote appli;
	
	public GestionJeuYote(AppliYote _appli) {
		matriceJeu = new int[WIDTH][HEIGHT];
		noJoueurActif = 1;
		appli = _appli;
	}
	
	public boolean isSelectionPossible(int noJoeur)
	{
		return (noJoeur == noJoueurActif);
	}
	
	public boolean isPosePossible(DessinPion pion, int i, int j)
	{
		boolean result = false;
		Point p = pion.getPosition();
		if (p!=null) {
			System.out.println("posution : " + p.getX() + "/" + p.getY());
		}
		else {
			System.out.println("pas de position");
		}
		
		if(i<WIDTH && j<HEIGHT) result = (matriceJeu[i][j] == 0);
		if(result && p!=null) // s'il s'agit d'un déplacement 
		{
			Point dest = new Point(i,j);
			Point anciennePosition = pion.getAnciennePosition();
			//System.out.println("Ancienne position : " + anciennePosition.getX() + " / " + anciennePosition.getY());
//				System.err.println("dest = " + dest+", anciennePosition = " + anciennePosition + ", egalité = " + dest.equals(anciennePosition));
			if(dest.equals(anciennePosition)) {
				result = false; 		
			}
			if(result)
			{
				double d = dest.distance(p);
				if((d>1 && d<2) || d>2) result = false;
				if(d==2)
				{
					int difX = (dest.x - p.x)/2;
					int difY = (dest.y - p.y)/2;
					Point intermediaire = new Point(p.x + difX, p.y + difY);
					int contenuCaseIntermediaire = matriceJeu[intermediaire.x][intermediaire.y] ;
					if(contenuCaseIntermediaire== pion.getJoueur() || contenuCaseIntermediaire == 0) result = false;
					if (result) 
					{
						System.out.println("on retire un pion blanc : " + intermediaire.x + " / " + intermediaire.y);
						
						appli.removeTokenOnPoint(intermediaire.x,intermediaire.y);
						matriceJeu[intermediaire.x][intermediaire.y] = 0;
						appli.setCapture(true);
						appli.setText(appli.txtJoueur[noJoueurActif-1] + appli.textePrise);
					}
				}
			}
		}
		return result;
	}
	
	public boolean isPosePossibleForArbre(DessinPion pion, int i, int j) {
		//boolean result = false;
		Point p = pion.getPosition();
		boolean result = false;
		if(i<WIDTH && j<HEIGHT) result = (matriceJeu[i][j] == 0);
		if(result && p!=null) // s'il s'agit d'un déplacement 
		{
			Point dest = new Point(i,j);
			Point anciennePosition = pion.getAnciennePosition();
//				System.err.println("dest = " + dest+", anciennePosition = " + anciennePosition + ", egalité = " + dest.equals(anciennePosition));
			if(dest.equals(anciennePosition)) result = false;
			if(result)
			{
				double d = dest.distance(p);
				if((d>1 && d<2) || d>2) result = false;
				if(d==2)
				{
					int difX = (dest.x - p.x)/2;
					int difY = (dest.y - p.y)/2;
					Point intermediaire = new Point(p.x + difX, p.y + difY);
					int contenuCaseIntermediaire = matriceJeu[intermediaire.x][intermediaire.y] ;
					if(contenuCaseIntermediaire== pion.getJoueur() || contenuCaseIntermediaire == 0) result = false;
				}
			}
		}
		return result;
	}
	
	
	public void poseJeton(DessinPion pion, int i, int j)
	{
		Point p = pion.getPosition();
		if(p!=null)
		{
			matriceJeu[p.x][p.y] = 0;
		}
		
		for (int iBis = 0; iBis < WIDTH; iBis++) {
			for (int jBis = 0; jBis < HEIGHT; jBis++) {
				System.out.print(" " + matriceJeu[iBis][jBis]);
			}
			System.out.println();
		}
		
		matriceJeu[i][j]  = pion.getJoueur();
		p = new Point(i,j);
		System.out.println("on set la position");
		pion.setPosition(p);
		
		if(!appli.isCapture()) switchJoueur();
	}
	

	public void retirePion(int i, int j)
	{
		System.err.println("retire pion en " +i+","+j);
		if((i>=0 && j>=0) && (i<WIDTH && j<HEIGHT)) matriceJeu[i][j]  = 0;
	}
	
	void copiePions(DessinPion[][] from, DessinPion[][] to) {
		for (int i = 0; i < 2; i++) {
			for (int j =0; j < 12; j++) {
				to[i][j] = from[i][j].clone();
			}
		}
	}
	

	/**cree un arbre de situation sur 2 nbNiveaux a partir de la situation du jeu courant
	 * @param s situation a partir de laquelle il faut etendre l'arbre
	 * @param nbNiveaux nb de niveaux de l'arbre restants a creer*/
	public void creerArbreSituation(Situation s, int nbNiveaux)
	{
//		if (s.troisPionsAlignes( ) || s.isFull()) s.setClose(true);
		if(nbNiveaux==0) s.setFeuille(true);		
		if(s.isClose() || s.isFeuille() || nbNiveaux==0)
		{
			s.evaluer();
//			s.afficheMatrice();
		}
		else
		{			
			int[][] matriceS = s.getMatriceJeu();
			DessinPion[][] lesPions = s.getPions();
			DessinPion[] lesPionsDuJoueur = lesPions[noJoueurActif - 1];
			int nbPionsJoueurInactifs = 0;
			for(int k=0; k< lesPionsDuJoueur.length; k++){
				for (int i=0; i<WIDTH; i++)
				{
					for (int j=0; j<HEIGHT; j++)
					{
						DessinPion[][] lesPionsDeduits = new DessinPion[2][12];
						copiePions(lesPions, lesPionsDeduits);
						DessinPion lePionModifie = lesPionsDeduits[noJoueurActif - 1][k];
						if(!lePionModifie.isVisible() || !lePionModifie.getIsOnGame()){
							nbPionsJoueurInactifs++;
						}
						if (jeuPossible(i,j,lePionModifie) && isPosePossibleForArbre(lesPionsDuJoueur[k], i, j) && lePionModifie.isVisible() && lePionModifie.getIsOnGame())
						{
							//if (!s.isCapture)
							Situation sprim = new Situation(0, !s.isMax());
							int[][] matriceJeuDeduite = new int[WIDTH][HEIGHT];
							copieMatrice(matriceS, matriceJeuDeduite);
							TypeJoueur tj = (s.isMax()?TypeJoueur.MACHINE:TypeJoueur.JOUEUR);
							matriceJeuDeduite[i][j] = tj.getType();
							lePionModifie.setPosition(new Point(i,j));
							sprim.setMatriceJeu(matriceJeuDeduite);
							sprim.setColumn(i);
							sprim.setModifiedPion(lePionModifie);
							sprim.setLine(j);
							sprim.setMatriceJeu(matriceJeuDeduite);
							sprim.setPions(lesPionsDeduits);
							sprim.setIndex(k);
							s.addSuccesseur(sprim);
							creerArbreSituation(sprim, nbNiveaux-1);
						}
					}
				}
			}
			
			nbPionsJoueurInactifs = nbPionsJoueurInactifs / ( WIDTH * HEIGHT);
			if(nbPionsJoueurInactifs == lesPionsDuJoueur.length){
				s.getSuccesseurs().removeAll(s.getSuccesseurs());
				System.out.println("plus de pions : no future");
			}
		}
	}
	
	/**fonction alphabeta, determine la valeur du noeud/ de la situation s
	 * pour recuperer la situation a prendre, balayer ensuite la liste des successeurs de s 
	 * et prendre la premiere situation ayant la meme estimation
	 * @param s situation, etat 
	 * @param alpha borne minimum
	 * @param beta borne maximum
	 * @return estimation de la situation s en fonction du jeu de l'adversaire*/
	public static double  alphaBeta(Situation s , double alpha, double beta) 
	{
	  double  borne, val, ab;
	  if (s.isFeuille() || s.isClose())
	  {
	    s.evaluer();
	    return s.getH();
	  }
	  if (s.isMax())
	  {
	    ArrayList<Situation> successeurs = s.getSuccesseurs();
	    val = -Double.MAX_VALUE;
	    borne = alpha;
	    for (Situation suc:successeurs)
	    {
	      ab = alphaBeta(suc, borne, beta);
	      suc.setH(ab);
	      val = (val>ab?val:ab);
	      if (val>=beta) return val;
	      if (val>borne) borne = val;
	    }
	  }
	  else
	  {
	    ArrayList<Situation> successeurs = s.getSuccesseurs();
	    val = Double.MAX_VALUE;
	    borne = beta;
	    for (Situation suc:successeurs)
	    {
	      ab = alphaBeta(suc, borne, beta);
	      suc.setH(ab);
	      val = (val<ab?val:ab);
	      if (val<=alpha) return val;
	      if (val<borne) borne = val;
	    }
	  }
	  return borne;                 
	}

	//TODO: completer la fonction
	private boolean jeuPossible(int i, int j, DessinPion pion)
	{
		/*fin du jeu*/
		return true;
	}

	/**fonction de recopie de la matrice de jeu 6x5
	 * @param from matrice a recopier
	 * @param to matrice recopiee*/
	private void copieMatrice(int [][]from, int[][]to)
	{
		for(int i=0; i<WIDTH; i++)
			System.arraycopy(from[i], 0, to[i], 0, HEIGHT);
	}
	public void switchJoueur()
	{
		noJoueurActif = 3 - noJoueurActif;
	}
	
	/**
	 * @return the noJoueurActif
	 */
	public int getNoJoueurActif() {
		return noJoueurActif;
	}
		

	/**
	 * @param noJoueurActif the noJoueurActif to set
	 */
	public void setNoJoueurActif(int noJoueurActif) {
		this.noJoueurActif = noJoueurActif;
	}
	
	public static void main(String[] args) {
		Situation s = new Situation(0, true);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 12; j++) {
				DessinPion c = new DessinPion(10,10,10,i+1);
				s.pions[i][j] = c;
			}
		}
		GestionJeuYote gj = new GestionJeuYote(null);
		gj.creerArbreSituation(s, 1);
		System.out.println(s);		
	}

	public static Situation getSituation(double h, Situation s) {
		//On va récupérer toutes les situations possibles ayant le même h que celui donné en argument
		//Puis on choisira au hasard une des situations
		ArrayList<Integer> indexPossible = new ArrayList<Integer>();
		for (int i = 0; i < s.getSuccesseurs().size(); i++) {
			if (s.getSuccesseurs().get(i).getH() == h) {
				indexPossible.add(i);
			}
		}
		int randomNumber = (int)(Math.random()*indexPossible.size());
		
		return s.getSuccesseurs().get(indexPossible.get(randomNumber));
		
	}

}
