package the_projects.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Platform;
import the_projects.model.*;
import the_projects.model.card.*;
import the_projects.view.View;
import the_projects.view.cards.Owner;

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
	LinkedList<PhDStudent> selectedPlayers;

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
							for (int j = 0; j < room.getProject(course).getProjectAmount(); j++) {
								view.displayAddProjectToRoom(room.getName(),i);
							}
						}
					}
					for (PhDStudent phds : model.getPlayers()) {
						Owner owner = playerToOwner(phds);
						ArrayList<String> rooms = new ArrayList<String>(roomCardsToStrings(getRoomCards(phds.getCards().getCardList())));
						ArrayList<Event> events = new ArrayList<Event>(eventCardsToEvents(getEventCards(phds.getCards().getCardList())));
						view.displayDrawCards(Owner.PLAYER_DECK, owner, false, rooms, events, 0);
						Thread.sleep(2000);
						view.displayDiscardCards();
						view.clean();
						Thread.sleep(1500);
					}
					
					// game loop
					
					while (status == GameStatus.VALID) {
						view.displayInfoMessage("NOUVEAU TOUR\n" + model.getCurrentPlayer().getName());
						
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

	private PhDStudent playerRoleToPlayer(Role role) {
		for (PhDStudent p : model.getPlayers()) {
			if (p.getRole() == role) {
				return p;
			}
		}
		return null;
	}
	
	private Room roomNameToRoom(String name) {
		for (Room r : model.getRooms()) {
			if (r.getName().equals(name)) {
				return r;
			}
		}
		return null;
	}
	
	private Owner playerToOwner(PhDStudent player) {
		int i = 1;
		for (PhDStudent p : model.getPlayers()) {
			if (p == player) {
				break;
			}
			i++;
		}
		
		switch (i) {
		case 1:
			return Owner.PLAYER1;
		case 2:
			return Owner.PLAYER2;
		case 3:
			return Owner.PLAYER3;
		case 4:
			return Owner.PLAYER4;
		default:
			return null;
		}
	}
	
	private LinkedList<EventCard> getEventCards(LinkedList<PlayerCard> cards) {
		LinkedList<EventCard> events = new LinkedList<>();
		
		for (PlayerCard c : cards) {
			if (c.getClass() == EventCard.class) {
				events.add((EventCard)c);
			}
		}
		
		return events;
	}
	
	private LinkedList<RoomCard> getRoomCards(LinkedList<PlayerCard> cards) {
		LinkedList<RoomCard> rooms = new LinkedList<>();
		
		for (PlayerCard c : cards) {
			if (c.getClass() == RoomCard.class) {
				rooms.add((RoomCard)c);
			}
		}
		
		return rooms;
	}

	private LinkedList<RoomCard> getRoomCards(LinkedList<PlayerCard> cards, Course course) {
		LinkedList<RoomCard> rooms = new LinkedList<>();
		
		for (PlayerCard c : cards) {
			if (c.getClass() == RoomCard.class && ((RoomCard)c).getRoom().getCourse() == course) {
				rooms.add((RoomCard)c);
			}
		}
		
		return rooms;
	}
	
	private LinkedList<String> roomCardsToStrings(LinkedList<RoomCard> cards) {
		LinkedList<String> strs = new LinkedList<String>();
		
		cards.stream().forEach((x) -> strs.add(x.getRoom().getName()));
		
		return strs;
	}
	
	private LinkedList<Event> eventCardsToEvents(LinkedList<EventCard> cards) {
		LinkedList<Event> events = new LinkedList<>();
		
		cards.stream().forEach((x) -> events.add(x.getEvent()));
		
		return events;
	}
	

	private void resolveEventCard(EventCard extraEventCard) {
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
					view.displayInfoMessage("Les noms d'UV doivent être uniques.");
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
				view.displayInfoMessage("Le nom d'un joueur ne peu pas être vide.");
				return;
			} else {
				if (testedStrings.contains(p)) {
					validSetting = false;
					view.displayInfoMessage("Les noms de joueur doivent être uniques.");
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
				view.displayInfoMessage("Les rôles doivent être tous différents (à l'exception de l'aléatoire).");
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
		action = ActionType.GIVE_UP;
		view.displayValidationMessage("Voulez-vous vraiment abandonner la partie ?");
	}

	@Override
	public void moveButtonClicked() {
		action = ActionType.NONE;
		
		if (actionPoints == 0) {
			view.displayMessage("Vous n'avez pas assez de points d'action.");
		} else {
			action = ActionType.RUN;
			
			if (model.getCurrentPlayer().getRole() == Role.GROUP_LEADER) {
				//TODO activate player clickable
			} else {
				pawnClicked(model.getCurrentPlayer().getRole());
			}
		}
	}
	
	@Override
	synchronized public void pawnClicked(Role player) {
		selectedPlayer = playerRoleToPlayer(player);
		selectedReachableRooms = model.reachableRooms(player, actionPoints);
		view.displayReachableRooms(selectedReachableRooms);
	}

	@Override
	synchronized public void placeClicked(String name) {
		actionPoints -= selectedReachableRooms.get(name);
		
		LinkedList<String> shortP = model.shortestPath(selectedPlayer.getPosition().getName(), name);
		
		view.displayMovePawn(selectedPlayer.getRole(), shortP.toArray(new String[shortP.size()]));
		view.clean();
		selectedPlayer.setPosition(roomNameToRoom(name));
		if (actionPoints > 0) {
			pawnClicked(selectedPlayer.getRole());
		}
	}

	@Override
	public void removeProjectButtonClicked() {
		action = ActionType.NONE;
		
		if (actionPoints == 0) {
			view.displayMessage("Vous n'avez pas assez de points d'action.");
		} else {
			//TODO unrandomize project removal
			
			//randomization
			
			//random order in which the projects' existance will be checked
			LinkedList<Integer> randomIndex = new LinkedList<Integer>();
			
			for (int i = 0; i < 4; i++) {
				while (randomIndex.size() == i) {
					int rand = ThreadLocalRandom.current().nextInt(0, 4);
					if (!randomIndex.contains(rand)) {
						randomIndex.add(rand);
					}
				}
			}
			
			//checking
			Room room = model.getCurrentPlayer().getPosition();
			int i = 0;
			
			for (i = 0; i < 4; i++) {
				Project proj = room.getProject(model.getCourses()[i]);
				if (proj.getProjectAmount() > 0) {
					actionPoints--;
					proj.setProjectAmount(proj.getProjectAmount()-1);
					break;
				}
			}

			if (i == 5) {
				view.displayMessage("Il n'y a aucun projet dans cette salle.");
			} else {
				view.displayRemoveProjectFromRoom(room.getName(), i);
			}	
		}
	}

	//TODO
	@Override
	public void shareKnowledgeButtonClicked() {
		if (actionPoints == 0) {
			view.displayMessage("Vous n'avez pas assez de points d'action.");
		} else {
			//init clickable players
			selectedPlayers = new LinkedList<PhDStudent>();
			for (PhDStudent student : model.getPlayers()) {
				if (model.getCurrentPlayer().getPosition() == student.getPosition() && student != model.getCurrentPlayer()) {
					selectedPlayers.add(student);
				}
			}
			
			if (selectedPlayers.isEmpty()) {
				view.displayMessage("Il n'y a personne avec vous dans la salle.");
			} else {
				//TODO set selected players clickable
			}
		}
	}
	
	//TODO
	@Override
	public void useCardButtonClicked() {
		action = ActionType.NONE;
		
		if (actionPoints > 0) {
			if (model.getCurrentPlayer().getCards().getSize() > 0) {
				LinkedList<String> rooms = roomCardsToStrings(getRoomCards(model.getCurrentPlayer().getCards().getCardList()));
				LinkedList<Event> events = eventCardsToEvents(getEventCards(model.getCurrentPlayer().getCards().getCardList()));
				Owner owner = playerToOwner(model.getCurrentPlayer());

				action = ActionType.USE_CARD;
				view.displayDrawCards(owner, owner, true, new ArrayList<String>(rooms), new ArrayList<Event>(events), 0);
			} else {
				view.displayMessage("Vous n'avez aucune carte dans votre main.");
			}
		} else {
			view.displayMessage("Vous n'avez pas assez de points d'action.");
		}
	}

	@Override
	public void masterButtonClicked() {
		action = ActionType.NONE;
		
		if (actionPoints == 0) {
			view.displayMessage("Vous n'avez pas assez de points d'action.");
		} else {
			if (!model.getCurrentPlayer().getPosition().isLabRoom()) {
				view.displayMessage("Vous n'êtes pas sur une salle de TP.");
			} else {
				int course = 0;
				LinkedList<String> usableCards = null;
				
				for (course = 0; course < 4; course++) {
					LinkedList<RoomCard> cards = getRoomCards(model.getCurrentPlayer().getCards().getCardList(), model.getCourses()[course]);
					if (cards.size() >= 5) {
						usableCards = roomCardsToStrings(cards);
						break;
					}
				}
				
				//TODO save the cards to remove somewhere
				
				if (course == 5) {
					view.displayMessage("Vous n'avez les cartes nécessaires.");
				} else {
					Owner owner = playerToOwner(model.getCurrentPlayer());
					//TODO let the user select the cards
										
					view.displayDrawCards(owner, owner, false, new ArrayList<String>(usableCards.subList(0, 5)), new ArrayList<Event>(), 0);
					action = ActionType.BUILD_TP;
					view.displayValidationMessage("Voulez-vous vraiment utiliser ces cartes ?");
				}
			}
		}
	}

	@Override
	public void buildTPButtonClicked() {
		action = ActionType.NONE;
		
		if (actionPoints == 0) {
			view.displayMessage("Vous n'avez pas assez de points d'action.");
		} else {
			RoomCard room = null;
			
			for (RoomCard r : getRoomCards(model.getCurrentPlayer().getCards().getCardList())) {				
				if (r.getRoom() == model.getCurrentPlayer().getPosition()) {
					room = r;
					break;
				}
			}
			
			if (room == null) {
				view.displayMessage("Vous n'avez pas la carte nécessaire.");
			} else {
				if (model.getLabRoomAmount() == 0) {
					view.displayMessage("Toutes les salles de TP ont déjà été posées.");
				} else {
					actionPoints--;
					model.addLabRoom(room.getRoom().getName());
					view.displaySetRoomToLab(room.getRoom().getName());
				}
			}
		}
	}

	@Override
	public void hackButtonClicked() {
		action = ActionType.NONE;
		
		if (actionPoints == 0) {
			view.displayMessage("Vous n'avez pas assez de points d'action.");
		} else if (model.getCurrentPlayer().getRole() != Role.HACKER) {
			view.displayMessage("Vous n'êtes pas hacker.");
		} else {
			if (model.getCurrentPlayer().getExtraEventCard() == null) {
				LinkedList<Event> discardedEvents = eventCardsToEvents(getEventCards(model.getPlayerDiscard().getCardList()));
				
				if (discardedEvents.isEmpty()) {
					view.displayMessage("Il n'y a pas de carte événement dans la défausse joueur");
				} else {
					action = ActionType.HACK;
					view.displayDrawCards(Owner.PLAYER_DISCARD, Owner.PLAYER_DISCARD, true, new ArrayList<String>(), new ArrayList<Event>(discardedEvents), 0);
				}
			} else {
				action = ActionType.HACK;
				view.displayValidationMessage("Voulez-vous vraiment utiliser votre carte événement ?\n(Elle sera définitivement retirée du jeu.)");
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

	@Override
	public void animationFinished() {

	}

	@Override
	public void cleaned() {

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
		System.out.println("event");
	}

	@Override
	public void roomCardClicked(String room) {
		System.out.println("room");	
	}
}