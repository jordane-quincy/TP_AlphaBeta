package application;

import gui.ChoixCouleursControleur;

import java.net.URL;

import modele.GestionJeuYote;
import modele.Situation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

public class AppliYote extends Application implements  EventHandler<MouseEvent> {

	/**taille d'une case en pixels*/
	int tailleCase;
	/**Matrice des dessins des pions*/
	DessinPion[][] pions;
	/**
	 * @return the pions
	 */
	public DessinPion[][] getPions() {
		return pions;
	}

	/**Le pion en cours de sélection*/
	DessinPion pionSelectionne;
	/**vrai si le joueur doit capturer un pion de l'adversaire*/
	boolean capture;
	/**scene de jeu*/
	Scene scene;
	/**gestion du jeu*/
	GestionJeuYote gestionJeu;
	/**texte de dialogue sur l'application*/
	Text texte;
	/**texte pour sélection*/
	public String texteSelection = ", à vous de jouer, sélectionner un pion";
	/**texte pour pose*/
	public String textePose= ", poser le pion";
	/**texte pour prise*/
	public String textePrise = ", prenez un pion à l'adersaire";
	/**textes pour description joueur*/
	public String[] txtJoueur = {"Joueur 1", "Joueur 2"};

	/**lancement automatique de l'application graphique*/
	public void start(Stage primaryStage) {
		tailleCase = 100;
		pions = new DessinPion[2][12];
		gestionJeu = new GestionJeuYote(this) ;
		construirePlateauJeu(primaryStage);
	}

	/**construction du thêatre et de la scène */
	void construirePlateauJeu(Stage primaryStage) 
	{
		//definir la scene principale
		Group troupe = new Group();
		scene = new Scene(troupe, tailleCase + 6*tailleCase + 6*tailleCase + tailleCase ,tailleCase +5*tailleCase, Color.ANTIQUEWHITE);
		//definir les acteurs et les habiller
		dessinEnvironnement( troupe);
		
		MenuBar menuBar = new MenuBar();
		 
        // --- Menu Couleurs
        Menu menuCouleurs = new Menu("Couleurs");
        MenuItem menuChoixCouleurs = new MenuItem("Changer les couleurs");
        //si action sur l'item alors lancer la fonction choixCouleurs
        menuChoixCouleurs.setOnAction((ActionEvent t)->{choixCouleurs(primaryStage);} ); 
        menuCouleurs.getItems().add(menuChoixCouleurs);
 
        // --- Menu Aide
        Menu menuHelp = new Menu("Aide"); 
        menuBar.getMenus().addAll(menuCouleurs, menuHelp);
        troupe.getChildren().addAll(menuBar);

		primaryStage.setTitle("Yote...");
		primaryStage.setScene(scene); 
		//afficher le theatre
		primaryStage.show();
	}

	/**fonction qui lance une fenetre modale définie en fxml par JavaFX Scene Builder
	 * @param primaryStage theatre de l'application*/
	void choixCouleurs(Stage primaryStage)
	{
			String sceneFile = "/gui/ChoixCouleursGui.fxml";
			URL    url  = null;
			AnchorPane page = null;
			FXMLLoader fxmlLoader =null;
			try 
			{
				url  = getClass().getResource( sceneFile );
				fxmlLoader = new FXMLLoader(url);
				page = (AnchorPane) fxmlLoader.load();
				System.out.println( "  fxmlResource = " + sceneFile );
			}
			catch ( Exception ex )
			{
				System.out.println( "Exception on FXMLLoader.load()" );
				System.out.println( "  * url: " + url );
				System.out.println( "  * " + ex );
				System.out.println( "    ----------------------------------------\n" );
			}  

			//si le chargement a reussi
			if (primaryStage!=null)
			{
				//creation d'un petit theatre pour la fenetre de choix de couleurs
				Stage dialogStage = new Stage();

				dialogStage.setTitle("Jeu du yoté...Couleurs...");
				//fenetre modale, obligation de quitter pour revenir a la fenetre principale
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(primaryStage);
				Scene miniScene = new Scene(page);
				dialogStage.setScene(miniScene);
				//recuperation du controleur associe a la fenetre
				ChoixCouleursControleur controller = fxmlLoader.getController();
				controller.setDialogStage(dialogStage);
				controller.setAppliYoteLink(this);
				//affichage de la fenetre
				dialogStage.showAndWait();				
			}

		}    
	
	/** 
	 *creation des cellules et de leurs habits
	 */
	void dessinEnvironnement(Group troupe)
	{

		int decalage = tailleCase/2;
		// dessin des lignes
		for(int i=0; i<=6; i++)
		{
			Line l = new Line(decalage + tailleCase * i, decalage, decalage+ tailleCase * i, decalage + 5*tailleCase ); 
			l.setStrokeWidth(2);
			l.setStroke(Color.DARKGOLDENROD);
			
			troupe.getChildren().add(l);
			if(i<6)
			{
				l = new Line(decalage, decalage + tailleCase * i, decalage+ 6*tailleCase, decalage + tailleCase * i ); 
				l.setStrokeWidth(2);
				l.setStroke(Color.DARKGOLDENROD);
				troupe.getChildren().add(l);
			}
		}

		//creation des zones cliquables dans les grilles
		for(int i=0; i<6; i++)
			for(int j=0; j<5; j++)
			{
				Rectangle r = new Rectangle(decalage + tailleCase*i, decalage+tailleCase*j, tailleCase, tailleCase);
				r.setFill(Color.TRANSPARENT); 
				r.setStroke(Color.TRANSPARENT);
				r.setOnMouseClicked(this);
				troupe.getChildren().add(r);
			}

		int radius = 2*decalage/3;
		for(int j=0; j<2; j++)
		{
			for(int i=0; i<12; i++)
			{
				int cx = (decalage+7*tailleCase+decalage) + (i%6)*tailleCase;
				int cy = decalage+decalage + (i/6) * tailleCase + (j*3*tailleCase);
				DessinPion c = new DessinPion(cx, cy, radius, j+1);
				 DropShadow dropShadow = new DropShadow();
				 dropShadow.setRadius(5.0);
				 dropShadow.setOffsetX(3.0);
				 dropShadow.setOffsetY(3.0);
				 dropShadow.setColor(Color.color(0.4, 0.5, 0.5)); 
				 c.setEffect(dropShadow);
				troupe.getChildren().add(c);
				c.setSmooth(true);
				c.setOnMouseClicked(this);	
				pions[j][i] = c;
			}			
		}
		
		texte = new Text(8*tailleCase, 3*tailleCase,txtJoueur[this.gestionJeu.getNoJoueurActif()-1] + this.texteSelection);
		Font police = Font.font("Arial", FontWeight.BOLD, 14);
		texte.setFont(police);
		troupe.getChildren().add(texte);
		
	}

	/**suite au changement de couleur, demande la recoloration des pions*/
	public void recolorer()
	{
		for(DessinPion[] tab:this.pions)
			for(DessinPion  pion:tab)
			{
				pion.getColorPion();
				pion.colorPion();
			}
	}
	
	

	/**reponse aux evenements de souris*/
	public void handle(MouseEvent me){
		
		System.out.println("clic sur scene en " + me.getSceneX() + "," + me.getSceneY());
		System.out.println("clic sur objet en " + me.getX() + "," + me.getY());
		Object o = me.getSource();
		if(o instanceof DessinPion)
		{
			DessinPion d = (DessinPion)o;
			if(gestionJeu.isSelectionPossible(d.joueur) == (!capture))
			{
				if(this.capture)
				{
					this.capture = false;
					this.gestionJeu.switchJoueur();
					this.texte.setText( txtJoueur[this.gestionJeu.getNoJoueurActif()-1] + this.texteSelection);
					double x = d.getCenterX();
					double y = d.getCenterY();
					int i = (int)((x - tailleCase) / tailleCase);
					int j = (int)((y - tailleCase) / tailleCase);
					this.gestionJeu.retirePion(i,j);
					eliminerPion(d);
				}
				else
				{
					//si on selectionne un nouveau pion ou deselectionne le pion deja selectionne
					if(pionSelectionne==null || pionSelectionne==d)
					{
						d.switchSelected();
						d.colorPion();
						if(pionSelectionne==null) 
						{
							pionSelectionne=d;
							this.texte.setText(this.txtJoueur[this.gestionJeu.getNoJoueurActif()-1] + this.textePose);
						}
						else 
						{
							pionSelectionne=null;
						}
						//appeler l'iA ici aussi
					}
				}
			}
		}
		else
			if(o instanceof Rectangle && pionSelectionne!=null)
			{
				int demiCase = tailleCase/2;
				Rectangle r = (Rectangle)o;
				System.out.println("centre x,y = " + pionSelectionne.getCenterX() + "," + pionSelectionne.getCenterY());
				double x = r.getX();
				double y = r.getY();
				int i = (int)((x - demiCase) / tailleCase);
				int j = (int)((y - demiCase) / tailleCase);
				System.out.println("jeu i,j = " + i + "," + j);
				if(gestionJeu.isPosePossible(pionSelectionne,i, j))
				{
					gestionJeu.poseJeton(pionSelectionne, i, j);
					retireDernierJeu(pionSelectionne);
					double translateX =  x - pionSelectionne.getCenterX() + demiCase;
					double translateY =  y - pionSelectionne.getCenterY() + demiCase;
					int tps =(int)(100 * ((Math.abs(translateX) + Math.abs(translateY) ) /  tailleCase));
//					if(tps<1000) tps = 1000;
					pionSelectionne.switchSelected();
					Timeline timeline = new Timeline();
					timeline.getKeyFrames().addAll(
							new KeyFrame(new Duration(tps), 
									new KeyValue(pionSelectionne.centerXProperty(), x  + demiCase),
									new KeyValue(pionSelectionne.centerYProperty(), y  + demiCase),
									new KeyValue(pionSelectionne.fillProperty(), pionSelectionne.cj)
									)
							);
					timeline.play();
					pionSelectionne = null;
					if(!this.capture) 
						this.texte.setText(this.txtJoueur[this.gestionJeu.getNoJoueurActif()-1] + this.texteSelection);
					
					//Gestion de l'IA
					//situation avec grille et pion
					Situation s = new Situation(pions, gestionJeu.getMatriceJeu());
					
					//gestionJeu.creerArbreSituation
					gestionJeu.creerArbreSituation(s, 2);					
					System.out.println("test");
					//Lancer alpha beta avec racine de l'arbre renvoie valeur de la situation (h)
					double h = GestionJeuYote.alphaBeta(s, -Double.MAX_VALUE, Double.MAX_VALUE);
					System.out.println(h);
					//Balis l'ensemble des successeurs qui correspond au h qu'on a r�cup�r� du alpha beta (si egal prendre aux hasard)
					//dans cette situation on doit savoir quel pion modifi� (possition)
					//et renvoyer �a � l'interface
					//et faire l'annimation
				}
				
			}
	}
	
	/**retire le derniers jeux pour le joueur du pion pion, sauf pour ce pion 
	 * @param pion pion pour lequel il ne faut pas retirer le jeu
	 * */
	void retireDernierJeu(DessinPion pion)
	{
		int noJoueur = pion.getJoueur()-1;
		for(int j=0; j<12; j++)
		{
			if(pions[noJoueur][j] != pion)
				pions[noJoueur][j].setAnciennePosition(null);
		}
	}

	
	public void removeTokenOnPoint(int i, int j)
	{
		int x = (i+1)*this.tailleCase ;
		int y = (j+1)*this.tailleCase ;
		boolean found=false;
//		System.err.println("cherche pion à supprimer en " + x + "," + y);
		DessinPion pionAEliminer=null;
		for(i=0;i<2 && !found; i++)
			for(j=0; j<12 && !found; j++)
			{
				DessinPion pion = this.pions[i][j];
//				System.err.println("trouvé pion  " + pion.getCenterX() + "," + pion.getCenterY());

				if((pion.getCenterX()  == x) && (pion.getCenterY()  == y))
				{
//					System.err.println("trouvé pion à supprimer " );
					found = true;
					pionAEliminer = pion;
				}
			}
		if(pionAEliminer!=null)
		{
//			System.err.println("trouvé pion à supprimer " + pionAEliminer);
			 eliminerPion(pionAEliminer);
		}
			
	}
	
	/**desactiver le pion */
	void eliminerPion(DessinPion pion)
	{
		pion.setVisible(false);
		pion.setOnMouseClicked(null);
		pion.setCenterX(0);
		pion.setCenterY(0);
		pion.setRadius(0);
	}
	
	/**
	 * @param backgrd couleur de fond de l'application
	 */
	public void setBackground(Color backgrd)
	{
		scene.setFill(backgrd);
	}

	/**
	 * @return la couleur de fond de l'application
	 */
	public Color getBackground()
	{
		return (Color) scene.getFill();
	}

	
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * @return the capture
	 */
	public boolean isCapture() {
		return capture;
	}

	/**
	 * @param capture the capture to set
	 */
	public void setCapture(boolean capture) {
		this.capture = capture;
	}

	public void setText(String strTexte)
	{
		texte.setText(strTexte);
	}


}
