package the_projects.controller;

import the_projects.model.Game;
import the_projects.model.GameStatus;
import the_projects.model.card.PartyCard;
import the_projects.model.card.PlayerCard;
import the_projects.view.Card;
import the_projects.view.View;

/**
 *
 */
public class Controller extends Thread implements ViewListener {
	Game model;
	View view;
	GameStatus status;
	Phase phase;
	int actionPoints;
    
    public void run() {
    	try {
    		synchronized (this) {
    			//init
    			
    			phase = Phase.START;
    			status = GameStatus.VALID;
    			model = null;
    			view = new View();
    			actionPoints = 0;
    			
    			//start phase
    			
    			//display start view
    			this.wait(); //wait for the user to click on the start button
    			
    			//setting phase
    			
    	    	//display setting phase
    			while (phase == phase.SETTING) {
    				this.wait(); //waiting for the user to enter and validate his settings
				}
    			
    			//game loop
    			
    			while (status == GameStatus.VALID) {
    				
    				//action phase
    				
    				//display action phase
    				actionPoints = 4;
    				while (phase == Phase.ACTION && status == GameStatus.VALID) {
						this.wait(); //TODO wait for an action to be chosen and executed						
					}
    				actionPoints = 0;
    				
    				if (status == GameStatus.VALID) {
    					
    					//card phase
    					
						//display card phase
    					if (actionPoints == 0) { //nb cartes deck joueur < 2
    						status = GameStatus.CARD_LACK;
    					} else {
    						for (int i = 0; i < 2 && status == GameStatus.VALID; i++) {
								PlayerCard card = new PlayerCard() {};
								//draw a card
								if (PartyCard.class == card.getClass()) {
									//résoudre épidémie
									//défausser carte
								} else {
									//ajouter la carte à la main
								}
							}
    						
    						if (status == GameStatus.VALID) {
								
    							//infection phase
    							/*
    							SI status == ok

    								//phase infection

    								nbcarteatirer = marqueur vitesse
    								POUR i de 1 à nbcartesatirer && status == ok						
    									tirer une carte infection
    									status = *résoudre carte infection(carte)
    									défausser

    								joueur = joueur suivant

    								 */
							}
    					}
					}
    				
    				//joueur suivant
				}
			}
    		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    private void actionLoop() {
    	
    }
    
    private void cardDrawingPhase() {
    	
    }
    
    private void propagationPhase() {
    	
    }
    
    private void conclusionPhase() {
    	
    }

	@Override
	public void startButtonClicked() {
		if (phase == Phase.START) {
			phase = Phase.SETTING;
			this.notify();
		}	
	}

	@Override
	public void placeClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cardCLicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionButtonClicked() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void settingValidationButtonCLicked() {
		if (phase == Phase.SETTING) {
			//check values and if they are OK
				phase = Phase.ACTION;
				this.notify();
			//otherwise
				//view.displayMessageInvalidSetting();
		}
	}
    
    }