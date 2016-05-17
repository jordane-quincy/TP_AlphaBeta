package gui;

import application.AppliYote;
import application.DessinPion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChoixCouleursControleur {
	/**couleur pour le joueur 1*/
	Color colJ1;
	/**couleur pour le joueur 2*/
	Color colJ2;
	/**couleur pour le fond de l'application*/
	Color colBackground;
	/**selecteur de couleur pour J1 d�crit dans le fichier fxml*/
	@FXML private ColorPicker colPick1;
	/**selecteur de couleur pour J2 d�crit dans le fichier fxml*/
	@FXML private ColorPicker colPick2;
	/**selecteur de couleur pour le fond d�crit dans le fichier fxml*/
	@FXML private ColorPicker colPickBackground;
	
	/**petit theatre associe */
	Stage dialogStage;

	/**application associe*/
	AppliYote application;
	
	
	@FXML void choixCouleurJ1(ActionEvent event) { colJ1 = colPick1.getValue(); }

	@FXML void choixCouleurJ2(ActionEvent event) { colJ2 = colPick2.getValue(); }

	@FXML void choixCouleurBackground(ActionEvent event) { colBackground = colPickBackground.getValue(); }

	/**acceptation du choix des couleurs, utilisateur clique sur OK*/
    @FXML protected void gestionBoutonGo(ActionEvent event) {
    	DessinPion.couleurJ1 = this.colJ1;
    	DessinPion.couleurJ2 = this.colJ2;
    	//la couleur d'un jeton selectionne est plus ou moins claire que la couleur initiale selon que celle-ci est claire ou non
    	DessinPion.couleurJ1Selected = (colJ1.getBrightness()<0.5?colJ1.brighter().brighter():colJ1.darker().darker());
    	DessinPion.couleurJ2Selected = (colJ2.getBrightness()<0.5?colJ2.brighter().brighter():colJ2.darker().darker());
    	// modification du fond d'ecran de l'application
    	this.application.setBackground(this.colBackground);
    	//demande de recoloration de tous les jetons
    	this.application.recolorer();
    	//fermeture de la fenetre
      	dialogStage.close();
      }

    /**annulation : fermeture de la fenetre sans prendre en compte les couleurs*/
    @FXML protected void gestionBoutonCancel(ActionEvent event) {
      	dialogStage.close();
      }

	/**
	 * @param dialogStage lien vers le petit theatre associe
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	/**lien vers l'application
	 * recuperation des couleurs actuellement utilisee
	 * @param _ay une reference a l'application*/
	public void setAppliYoteLink(AppliYote _ay)
	{
		application = _ay;
		this.colJ1 = DessinPion.couleurJ1;
		this.colJ2 = DessinPion.couleurJ2;
		this.colBackground = application.getBackground();
 
		colPick1.setValue(DessinPion.couleurJ1); 
		colPick2.setValue(DessinPion.couleurJ2);
		this.colPickBackground.setValue(this.colBackground);
	}
	


}
