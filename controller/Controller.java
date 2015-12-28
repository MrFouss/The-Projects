package the_projects.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;


import javafx.application.Platform;
import the_projects.model.*;
import the_projects.model.card.*;
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
	Card selectedCard;
	HashMap<String, Integer> selectedReachableRooms;
	PhDStudent selectedPlayer;

	public Controller(View view) {
		this.view = view;

		status = GameStatus.VALID;
		model = null;
		view.addListener(this);
		actionPoints = 0;
		action = ActionType.NONE;
		selectedPlayer = null;
		selectedReachableRooms = null;
	}
	
	public void run() {
		try {
			synchronized (this) {
				phase = GamePhase.SETTING;
				// setting phase

				Platform.runLater(() -> view.displaySetting());
				this.wait(); // waiting for the user to enter and validate his settings

				// game loop

				view.displayGameBoard(model);

				while (status == GameStatus.VALID) {

					phase = GamePhase.ACTION;
					// action phase

					/*
					actionPoints = 4;
						//view.clean();
						//update doable actions
						this.wait(); // wait for an action to be chosen and executed
					}
					actionPoints = 0;
					//clean
					*/
					
					if (status == GameStatus.VALID) {

						phase = GamePhase.CARD_DRAWING;
						// card phase

						if (model.getPlayerDeck().getSize() < 2) {
							status = GameStatus.CARD_LACK;
						} else {							
							for (int i = 0; i < 2; i++) {
								PlayerCard card = (PlayerCard)model.getPlayerDeck().drawFirst();
								
								if (card.getClass() == RoomCard.class) {
									view.displayDrawPlayerCards(((RoomCard)card).getRoom().getName());
									model.getCurrentPlayer().getCards().addCard(card);
								} else if (card.getClass() == EventCard.class) {
									view.displayDrawPlayerCards(((EventCard)card).getEvent());
									model.getCurrentPlayer().getCards().addCard(card);
								} else {
									// résoudre épidémie

									// exams

									model.increaseEmergencyGauge();

									// stress

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

									// intensification

									model.getProjectDiscard().shuffle();
									model.getProjectDeck().addCardsOnTop(model.getProjectDiscard());
								}
							}
							
							//discard
							
							if (model.getCurrentPlayer().getCards().getSize() > 7) {
								phase = GamePhase.DISCARD;
								LinkedList<String> roomCards = new LinkedList<>();
								LinkedList<Event> eventCards = new LinkedList<>();
								/*
								for (PlayerCard card : model.getCurrentPlayer().getCards().getCardList()) {
									if (card.getClass() == RoomCard.class) {
										roomCards.add(card.getRoom().getName());
									} else {
										eventCards.add(card.getEvent());
									}
								}*/
								
								view.displayInfoMessage("Please discard room cards or use event cards. You should not have more than 7 cards");
								/*view.displayEventCards(eventCards);
								view.displayRoomCards(roomCards);*/
								view.displayCardsOfPlayer(model.getCurrentPlayer().getRole());
							}

							phase = GamePhase.PROPAGATION;
							// infection phase

							if (status == GameStatus.VALID) {
								for (int i = 0; i < model.getEmergencyValue() && status == GameStatus.VALID; i++) {
									ProjectCard card = (ProjectCard) model.getProjectDeck().drawFirst();
									view.displayDrawProjectCards(card.getRoom().getName());

									int projAmount = card.getRoom().getProject(card.getRoom().getCourse()).getProjectAmount();
									if (projAmount < 3) {
										card.getRoom().getProject(card.getRoom().getCourse()).setProjectAmount(projAmount + 1);
									} else {
										burnOut(new LinkedList<Room>(),card.getRoom(), card.getRoom().getCourse());
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
			e.printStackTrace();
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
					r.getProject(course).setProjectAmount(
							r.getProject(course).getProjectAmount() + 1);
				}
			}
		}
	}

	
	
	
	
	
	
	
	
	@Override
	synchronized public void settingValidationButtonClicked() {
		LinkedList<String> players = view.getPlayerNames();
		LinkedList<String> uvs = view.getUVNames();
		int diff = view.getDifficulty();
		LinkedList<Role> roles = view.getRoles();
		LinkedList<Role> testedRoles = new LinkedList<Role>();
		
		Boolean validSetting = true;

		//TODO add random Management
		for (Role role : roles) {
			if (testedRoles.contains(role)) {
				validSetting = false;
				break;
			} else {
				testedRoles.add(role);
			}
		}

		if (validSetting == true) {
            // turning players and roles into a HashMap
            HashMap<Role, String> mapPlayers = new HashMap<>();

            Role[] roleTab = new Role[roles.size()];
            String[] playerTab = new String[players.size()];
            int i = 0;
            for(Role role : roles) {
                roleTab[i++] = role;
            }
            i = 0;
            for(String player : players) {
                playerTab[i++] = player;
            }
            for(i = 0 ; i < players.size() ; ++i) {
                mapPlayers.put(roleTab[i], playerTab[i]);
            }

			model = new Model(mapPlayers, uvs, diff);
			this.notify();
		} else {
			view.displayInfoMessage("Invalid settings");
		}
	}
	
	
	
	@Override
	public void giveUpButtonClicked() {
		view.clean();
		view.disableAll();
		status = GameStatus.GIVE_UP;
		this.notify();
	}
	
	
	
	@Override
	synchronized public void placeClicked(String name) {
		if (selectedReachableRooms.containsKey(name)) {
			actionPoints -= selectedReachableRooms.get(name);
		//TODO	selectedPlayer.setPosition(model.getRooms().get(name));
		//TODO	view.displayMovePawn(selectedPlayer.getRole(), model.shortestPath(selectedPlayer.getPosition().getName(), name));
			view.clean();
		}
	}

	@Override
	synchronized public void pawnClicked(Role player) {
		selectedReachableRooms = model.reachableRooms(player, actionPoints);
		view.displayReachableRooms(selectedReachableRooms);
	}

	@Override
	public void removeProjectButtonClicked() {
		if (phase == GamePhase.ACTION && actionPoints > 0) {
			if (action != ActionType.NONE) {
				// view.clean();
				action = ActionType.NONE;
			}
			
			LinkedList<Integer> index = new LinkedList<Integer>();
			for (int i = 0; i < 4; i++) {
				while (index.size() == i) {
					int rand = ThreadLocalRandom.current().nextInt(0, 4);
					if (!index.contains(rand)) {
						index.add(rand);
					}
				}
			}
			
			Room room = model.getCurrentPlayer().getPosition();
			Boolean projectRemoved = false;
			for (Integer i : index) {
				Project proj = room.getProject(model.getCourses()[i]);
				if (proj.getProjectAmount() > 0) {
					proj.setProjectAmount(proj.getProjectAmount()-1);
					projectRemoved = true;
					break;
				}
			}
			
			if (!projectRemoved) {
				actionPoints--;
				//view.displayMessage("There is no projects on this room");
			} else {
				//update view about projects
			}
		}
	}

	@Override
	public void moveButtonClicked() {

	}

	@Override
	public void shareKnowledgeButtonClicked() {
		if (action != ActionType.NONE) {
			// view.clean();
			action = ActionType.NONE;
		}
	}

	@Override
	public void useCardButtonClicked() {
		if (action != ActionType.NONE) {
			view.clean();
			action = ActionType.NONE;
		}
		
		LinkedList<PlayerCard> cards = model.getCurrentPlayer().getCards().getCardList();
		if (actionPoints > 0) {
			if (cards.size() > 0) {
				//view.displayCards(model.getCurrentPlayer().getCards().getCardList());
				action = ActionType.USE_CARD;
			} else {
				view.displayMessage("No cards in your hands");
			}
		} else {
			view.displayMessage("No cards in your hands");
		}
	}

	@Override
	public void masterButtonClicked() {

	}

	@Override
	public void buildTPButtonClicked() {
		if (action != ActionType.NONE) {
			// view.clean();
			action = ActionType.NONE;
		}
	}

	@Override
	public void hackButtonClicked() {
		if (phase == GamePhase.ACTION) {
			if (action != ActionType.NONE) {
				// view.clean();
				action = ActionType.NONE;
			}
			if (model.getCurrentPlayer().getRole() == Role.HACKER) {
				if (model.getCurrentPlayer().getExtraEventCard() == null) {
					LinkedList<PlayerCard> discardEvents = new LinkedList<>();
					for (PlayerCard playerCard : (LinkedList<PlayerCard>) model.getPlayerDiscard().getCardList()) {
						if (playerCard.getClass() == EventCard.class) {
							discardEvents.add(playerCard);
						}
					}
					if (!discardEvents.isEmpty()) {
						action = ActionType.HACK;
						// view.displayCards(discardEvents);
					} else {
						// view.displayErrorMessage("There is no event card in the player discard pile.");
					}
				} else {
					action = ActionType.HACK;

					// view.displayConfirmationMessage("Do you really want to use your extra event card ?");
				}
			}
		}
	}

	@Override
	public void ConfirmationButtonClicked() {
		if (phase == GamePhase.ACTION) {
			if (action == ActionType.HACK) {
				resolveEventCard(model.getCurrentPlayer().getExtraEventCard());
				model.getCurrentPlayer().setExtraEventCard(null);
				// view.extraEventCardChanged(null);
				action = ActionType.NONE;
				// view.clean();
			}
		}
	}

	private void resolveEventCard(EventCard extraEventCard) {
	}

	@Override
	public void cardClicked(Card card) {
		if (phase == GamePhase.ACTION) {
			if (action == ActionType.HACK) {
				model.getCurrentPlayer().setExtraEventCard((EventCard) card);
				model.getPlayerDiscard().getCardList().remove(card);
				// view.extraEventCardChanged((EventCard)card);
				action = ActionType.NONE;
				// view.clean();
			} else if (action == ActionType.DISCARD) {
				model.getCurrentPlayer().getCards().getCardList().remove(card);
				// notify that the card was removed from hand
				if (card.getClass() == EventCard.class) {
					resolveEventCard((EventCard) card);
					model.getPlayerDiscard().addCard(card);
					// notify that card was put on the player discard pile
				}
			} else if (action == ActionType.USE_CARD) {
				if (card.getClass() == EventCard.class) {
					resolveEventCard((EventCard) card);
					model.getPlayerDiscard().addCard(card);
					// notify that card was put on the player discard pile
				} else {
					RoomCard rCard = (RoomCard) card;
					HashMap<String, Integer> reach = new HashMap<String, Integer>();
					if (rCard.getRoom() == model.getCurrentPlayer().getPosition()) {
						for (Room r : model.getRooms()) {
							reach.put(r.getName(), 1);
						}
						reach.remove(model.getCurrentPlayer().getPosition().getName());
					} else {
						reach.put(rCard.getRoom().getName(), 1);
					}
					//TODO reachablePlaces = reach;
					//TODO view.displayReachablePlaces(reach);
				}
			}
		}
	}

	@Override
	public void YesButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void NoButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endOfStageButtonClicked() {

	}


	@Override
	public void eventCardClicked(Event card) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roomCardClicked(String room) {
		// TODO Auto-generated method stub
		
	}

}