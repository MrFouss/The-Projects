package the_projects.controller;

import java.util.LinkedList;
import java.util.Stack;

import the_projects.model.Course;
import the_projects.model.Model;
import the_projects.model.PhDStudent;
import the_projects.model.Room;
import the_projects.model.card.Card;
import the_projects.model.card.PartyCard;
import the_projects.model.card.PlayerCard;
import the_projects.model.card.ProjectCard;
import the_projects.view.View;

/**
 *
 */
public class Controller extends Thread implements ViewListener {
	Model model;
	View view;
	
	GameStatus status;
	GamePhase phase;
	ActionType action;
	
	int actionPoints;
	PhDStudent selectedPlayer;
    
    public void run() {
    	try {
    		synchronized (this) {
    			//init
    			
    			phase = GamePhase.SETTING;
    			status = GameStatus.VALID;
    			model = null;
    			view = new View();
    			//view.addListener(this);
    			actionPoints = 0;
    			action = ActionType.NONE;
    			selectedPlayer = null;
    			
    			//setting phase
    			
    	    	//view.displaySetting()
    			while (phase == GamePhase.SETTING) {
    				this.wait(); //waiting for the user to enter and validate his settings
				}
    			
    			//game loop
    			
    			//view.displayGameBoard()
    			
    			while (status == GameStatus.VALID) {
    				
    				//action phase
    				
    				actionPoints = 4;
    				selectedPlayer = model.getCurrentPlayer();
    				while (phase == GamePhase.ACTION && status == GameStatus.VALID) {
						this.wait(); //wait for an action to be chosen and executed						
					}
    				actionPoints = 0;
    				
    				if (status == GameStatus.VALID) {
    					
    					//card phase
    					
						//display card phase
    					if (actionPoints == 0) { //nb cartes deck joueur < 2
    						status = GameStatus.CARD_LACK;
    					} else {
    						PlayerCard card1, card2;
    						card1 = (PlayerCard) model.getPlayerDeck().drawFirst();
    						card2 = (PlayerCard) model.getPlayerDeck().drawFirst();
    						//view.displayCards(card1, card2)
    						resolvePlayerCard(card1);
    						if (status == GameStatus.VALID) {
    							resolvePlayerCard(card2);
							}
    						
    						//infection phase
    						
    						if (status == GameStatus.VALID) {
    							for (int i = 0; i < model.getEmergencyValue() && status == GameStatus.VALID; i++) {
									ProjectCard card = (ProjectCard) model.getProjectDeck().drawFirst();
									
									if (card.getRoom().getProject(card.getRoom().getCourse()).getProjectAmount() < 3) {
										card.getRoom().getProject(card.getRoom().getCourse()).setProjectAmount(3);
									} else {
										burnOut(new LinkedList<Room>(), card.getRoom(), card.getRoom().getCourse());
									}

									model.getProjectDiscard().addCard(card);
								}
							}
    					}
					}
    				
    				model.nextPlayer();
				}
    			
    			phase = GamePhase.CONCLUSION;
    			
    			switch (status) {
				case CARD_LACK:
					
					break;
				case BURN_OUT:
					
					break;
				case PROJECT_LACK:
					
					break;
				case VALID:
					
					break;
				case WIN:
					
					break;
				default:
					break;
				}
			}
    		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    private void resolvePlayerCard(PlayerCard card) {
    	if (PartyCard.class == card.getClass()) {
			//résoudre épidémie
    		
    		//exams
    		
    		model.increaseEmergencyGauge();
    		
    		//stress
    		
    		ProjectCard pCard = (ProjectCard) model.getProjectDeck().drawLast();
    		Course course = pCard.getRoom().getCourse();
    		if (!course.isCompleted()) {
				if (pCard.getRoom().getProject(course).getProjectAmount() > 0) {
					pCard.getRoom().getProject(course).setProjectAmount(3);
					burnOut(new LinkedList<Room>(), pCard.getRoom(), course);
				} else {
					pCard.getRoom().getProject(course).setProjectAmount(3);
				}
			}
    		model.getProjectDiscard().addCard(pCard);
    		
    		//intensification
    		
    		model.getProjectDiscard().shuffle();
    		model.getProjectDeck().addCardsOnTop(model.getProjectDiscard());    		
		} else {
			model.getCurrentPlayer().getCards().addCard(card);
		}
    	
    	if (model.getCurrentPlayer().getCards() != null) { //if >7
			//demander de défausser ou jouer une carte
		}
    }
    
    private void burnOut(LinkedList<Room> burnOutRooms, Room room, Course course) {
    	if (!burnOutRooms.contains(room)) {
    		model.increaseBurnOutGauge();
    		if (model.getBurnOutValue() > 7) {
				status = GameStatus.BURN_OUT;
			}
			burnOutRooms.add(room);
			for (Room r : room.getNeighbours()) {
				if (status != GameStatus.VALID) {
					break;
				}
				if (r.getProject(course).getProjectAmount() == 3) {
					burnOut(burnOutRooms, r, course);
				} else {
					r.getProject(course).setProjectAmount(r.getProject(course).getProjectAmount()+1);
				}
			}
		}
    }

	@Override
	public void placeClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pawnCLicked(PhDStudent player) {
		if (phase == GamePhase.ACTION) {
			selectedPlayer = player;
			//model.reachableRooms(roomName, remainingMoves)
			//view.displayReachablePlaces(model.reachableRooms(selectedPlayer, actionPoints))
		}
		
	}

	@Override
	public void moveButtonClicked() {
		if (phase == GamePhase.ACTION) {
			//view.displayReachablePlaces(model.getReachablePlaces(selectedPlayer, actionPoints)
		}		
	}
	
	@Override
	synchronized public void settingValidationButtonCLicked() {
		if (phase == GamePhase.SETTING) {
			//check values and if they are OK : playerNames, UVsNames, difficulty, playerRoles
				//model = new Model(//récupérer les données)
				//view.getPlayerNames()
				//view.getUVNames()
				//view.getDifficulty()
				//view.getRoles()
				phase = GamePhase.ACTION;
				this.notify();
			//otherwise
				//view.displayInvalidSetting();
		}
	}

	@Override
	public void removeProjectButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shareKnowledgeButtonCLicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void useCardButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void useCardsButtonCliked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildTPButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hackButtonCliked() {
		// TODO Auto-generated method stub
		
	}
    
    }