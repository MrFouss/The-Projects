package the_projects.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import com.sun.javafx.tk.Toolkit.Task;

import javafx.application.Platform;
import the_projects.model.*;
import the_projects.model.card.*;
import the_projects.view.View;
import the_projects.view.Cards.Owner;

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
				while (status == GameStatus.VALID) {
					
				
					phase = GamePhase.SETTING;
					// setting phase
	
					view.displaySetting();
					this.wait(); // waiting for the user to enter and validate his settings
	
					view.displayGameBoard(model);
					
					//display initial stuff
					
					for (Room room : model.getRooms()) {
						if (room.isLabRoom()) {
							view.displaySetRoomToLab(room.getName());
						}
						for (int i = 0; i < 4; i++) {
							Course course = model.getCourses()[i];
							if (room.getProject(course).getProjectAmount() > 0) {
								for (int j = 0; j < i; j++) {
									final int k = i;
									System.out.println(room);
									view.displayAddProjectToRoom(room.getName(), k);
								}
							}
						}
					}

					// game loop
					
					while (status == GameStatus.VALID) {
						
						
						phase = GamePhase.ACTION;
						// action phase
	
						actionPoints = 4;
						this.wait(); // wait for actions to be chosen and executed
						actionPoints = 0;		
						
						if (status == GameStatus.VALID) {
	
							phase = GamePhase.CARD_DRAWING;
							// card phase
	
							if (model.getPlayerDeck().getSize() < 2) {
								status = GameStatus.CARD_LACK;
							} else {							
								for (int i = 0; i < 2; i++) {	
									PlayerCard card = model.getPlayerDeck().drawFirst();
									
									if (card.getClass() == RoomCard.class) {
										//view.displayDrawPlayerCards(((RoomCard)card).getRoom().getName()));
										model.getCurrentPlayer().getCards().addCard(card);
									} else if (card.getClass() == EventCard.class) {
										//view.displayDrawPlayerCards(((EventCard)card).getEvent()));
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
										
										System.out.println("solve epidemy");
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
									//view.displayCardsOfPlayer(model.getCurrentPlayer().getRole());
								}
	
								phase = GamePhase.PROPAGATION;
								// infection phase
	
								if (status == GameStatus.VALID) {
									for (int i = 0; i < model.getEmergencyValue() && status == GameStatus.VALID; i++) {
										ProjectCard card = (ProjectCard) model.getProjectDeck().drawFirst();
										//view.displayDrawProjectCards(card.getRoom().getName());
	
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
					case GIVE_UP:
						view.displayValidationMessage("YOU LOST\nYou gave up.\nDo you want to start a new party ?");
						this.wait();
						break;
					default:
						break;
					}
				}
			}	
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		Platform.runLater(() -> view.close());
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
		LinkedList<String> testedStrings = new LinkedList<String>();
		
		Boolean validSetting = true;

		//testing uniqueness and non empty course names
		for (String uv : uvs) {
			if (uv.equals("")) {
				validSetting = false;
				view.displayInfoMessage("Le nom d'une UV ne peu pas être vide.");
				return;
			} else {
				if (testedStrings.contains(uv)) {
					validSetting = false;
					view.displayInfoMessage("Course names should be unique.");
					return;
				} else {
					testedStrings.add(uv);
				}
			}
		}
		
		//testing uniqueness and non empty player names
		for (String p : players) {
			if (p.equals("")) {
				validSetting = false;
				view.displayInfoMessage("Player names cannot be empty.");
				return;
			} else {
				if (testedStrings.contains(p)) {
					validSetting = false;
					view.displayInfoMessage("Player names should be unique.");
					return;
				} else {
					testedStrings.add(p);
				}
			}
		}
		
		//testing uniqueness roles
		for (Role role : roles) {
			if (testedRoles.contains(role) && role != Role.RANDOM) {
				validSetting = false;
				view.displayInfoMessage("Roles should be unique (excepte for RANDOM).");
				return;
			} else {
				testedRoles.add(role);
			}
		}

        // turning players and roles into a HashMap
        HashMap<String, Role> mapPlayers = new HashMap<>();

        for (int i = 0; i < players.size() ; i++) {
			mapPlayers.put(players.get(i), roles.get(i));
		}

		model = new Model(mapPlayers, uvs, diff);
		this.notify();
	}
	
	
	
	@Override
	public void giveUpButtonClicked() {
		view.clean();
		action = ActionType.GIVE_UP;
		view.displayValidationMessage("Do you realy want to give up ?");
	}
	

	@Override
	public void moveButtonClicked() {
		pawnClicked(model.getCurrentPlayer().getRole());
	}
	
	
	@Override
	synchronized public void placeClicked(String name) {
		actionPoints -= selectedReachableRooms.get(name);
		Room room = null;
		for (Room r : model.getRooms()) {
			if (r.getName().equals(name)) {
				room = r;
				break;
			}
		}
		
		
		LinkedList<String> shortP = model.shortestPath(selectedPlayer.getPosition().getName(), name);
		String[] sp = new String[shortP.size()];
		int i = 0;
		for (String string : shortP) {
			sp[i] = string;
			i++;
		}
		
		
		{view.displayMovePawn(selectedPlayer.getRole(), sp); view.clean();};
		selectedPlayer.setPosition(room);
		action = ActionType.NONE;
	}

	@Override
	synchronized public void pawnClicked(Role player) {
		for (PhDStudent p : model.getPlayers()) {
			if (p.getRole() == player) {
				selectedPlayer = p;
				break;
			}
		}
		
		synchronized (selectedPlayer) {
			selectedReachableRooms = model.reachableRooms(player, actionPoints);
		}
		
		view.displayReachableRooms(selectedReachableRooms);
	}

	@Override
	public void removeProjectButtonClicked() {		
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
		int i = 0;
		for (i = 0; i < index.size(); i++) {
			Project proj = room.getProject(model.getCourses()[i]);
			if (proj.getProjectAmount() > 0) {
				actionPoints--;
				proj.setProjectAmount(proj.getProjectAmount()-1);
				projectRemoved = true;
				break;
			}
		}

		if (!projectRemoved) {
			view.displayMessage("Il n'y a aucun projet dans cette salle.");
		} else {
			final int j = i;
			view.displayRemoveProjectFromRoom(room.getName(), j);
		}	
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
		PhDStudent p = model.getPlayers().get(0);
		ArrayList<String> rooms = new ArrayList<>();
		ArrayList<Event> events = new ArrayList<Event>();
		
		for (PlayerCard pc : (LinkedList<PlayerCard>) p.getCards().getCardList()) {
			if (pc.getClass() == RoomCard.class) {
				rooms.add(((RoomCard)pc).getRoom().getName());
			} else {
				events.add(((EventCard)pc).getEvent());
			}
		}
		
		view.displayDrawCards(Owner.PLAYER1, Owner.PLAYER1, true, rooms, events, 0);
		
		
		
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
					//model.getPlayerDiscard().addCard(card);
					// notify that card was put on the player discard pile
				}
			} else if (action == ActionType.USE_CARD) {
				if (card.getClass() == EventCard.class) {
					resolveEventCard((EventCard) card);
					//model.getPlayerDiscard().addCard(card);
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
	synchronized public void YesButtonClicked() {
		if (action == ActionType.GIVE_UP) {
			status = GameStatus.GIVE_UP;
			this.notify();
		}
		if (phase == GamePhase.CONCLUSION) {
			status = GameStatus.VALID;
			this.notify();
		}
	}

	@Override
	synchronized public void NoButtonClicked() {
		if (phase == GamePhase.CONCLUSION) {
			this.notify();
		}
	}

	@Override
	public void endOfStageButtonClicked() {
		action = ActionType.NONE;
		this.notify();
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