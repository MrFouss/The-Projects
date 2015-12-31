package the_projects.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Platform;
import jdk.nashorn.internal.objects.annotations.SpecializedFunction.LinkLogic;
import sun.awt.image.ImageWatched.Link;
import the_projects.controller.animation.Animation;
import the_projects.controller.animation.CardDisplay;
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
	LinkedList<Card> selectedCards;
	RoomCard selectedRoomCard;
	HashMap<String, Integer> selectedReachableRooms;
	PhDStudent selectedPlayer;
	LinkedList<PhDStudent> selectedPlayers;
	AnimationController animCon = new AnimationController();

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
					animCon = new AnimationController();
					animCon.start();
				
					phase = GamePhase.SETTING;
					// setting phase
	
					view.displaySetting();
					this.wait(); // waiting for the user to enter and validate his settings
					
					view.displayGameBoard(model);
					
					//display initial stuff
					
					Thread.sleep(1000);
					
					//discard

					animCon.addCardShow(Owner.PROJECT_DECK, Owner.PROJECT_DISCARD, new LinkedList<Card>(model.getProjectDiscard().getCardList()));
					
					//projects and labs
					
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
					
					
					
					//player hands
					
					for (PhDStudent phds : model.getPlayers()) {
						Owner owner = playerToOwner(phds);
						animCon.addCardShow(Owner.PLAYER_DECK, owner, new LinkedList<Card>(phds.getCards().getCardList()));
					}
					
					// game loop
					
					while (status == GameStatus.VALID) {
						view.displayCurrentPlayer(model.getCurrentPlayer().getRole());
						animCon.add((x) -> {
							x.displayInfoMessage("NOUVEAU TOUR\n" + model.getCurrentPlayer().getName());
							animCon.wait = false;
						});
						
						phase = GamePhase.ACTION;
						// action phase
	
						actionPoints = 4;
						view.displayActionsPoints(4);
						this.wait(); // wait for actions to be chosen and executed
						actionPoints = 0;
						view.displayActionsPoints(0);
						
						if (status == GameStatus.VALID) {
	
							phase = GamePhase.CARD_DRAWING;
							// card phase
	
							if (model.getPlayerDeck().getSize() < 2) {
								status = GameStatus.CARD_LACK;
							} else {
								LinkedList<PlayerCard> cards = new LinkedList<>();
								Owner owner = playerToOwner(model.getCurrentPlayer());
					
								cards.add(model.getPlayerDeck().drawFirst());
								cards.add(model.getPlayerDeck().drawFirst());
								
								animCon.addCardShow(Owner.PLAYER_DECK, owner, new LinkedList<Card>(cards));
								
								for (int i = 0; i < 2; i++) {	
									PlayerCard card = cards.get(i);
									
									if (card.getClass() == RoomCard.class) {
										model.getCurrentPlayer().getCards().addCard(card);
									} else if (card.getClass() == EventCard.class) {
										model.getCurrentPlayer().getCards().addCard(card);
									} else {
										// résoudre épidémie
										// exams
	
										model.increaseEmergencyGauge();
										
										animCon.addGauge(true);

										// stress
	
										ProjectCard pCard = model.getProjectDeck().drawLast();
										LinkedList<Card> temp = new LinkedList<>();
										temp.add(pCard);
										
										animCon.addCardShow(Owner.PROJECT_DECK, Owner.PROJECT_DISCARD, temp);

										Course course = pCard.getRoom().getCourse();
										int courseIndex = 0;
										for (Course c : model.getCourses()) {
											if (c == course) {
												break;
											}
											courseIndex++;
										}
										
										if (!course.isCompleted()) {
											int toAdd = 3 - pCard.getRoom().getProject(course).getProjectAmount();
											
											pCard.getRoom().getProject(course).setProjectAmount(3);
											for (int j = 0; j < toAdd; j++) {
												view.displayAddProjectToRoom(pCard.getRoom().getName(), courseIndex);
											}
											
											if (pCard.getRoom().getProject(course).getProjectAmount() > 0) {
												burnOut(new LinkedList<Room>(), pCard.getRoom(), course);
											}
										}
										
										model.getProjectDiscard().addCard(pCard);
	
										// intensification
	
										model.getProjectDiscard().shuffle();
										
										animCon.addCardShow(Owner.PROJECT_DISCARD, Owner.PROJECT_DECK, new LinkedList<Card>(model.getProjectDiscard().getCardList()));
			
										model.getProjectDeck().addCardsOnTop(model.getProjectDiscard());
										model.getProjectDiscard().getCardList().removeAll(model.getProjectDiscard().getCardList());
									}
								}
								
								//discard
								
								final int toDiscard = model.getCurrentPlayer().getCards().getSize() - 1;
								if (toDiscard > 0) {
									phase = GamePhase.DISCARD;
									animCon.add((x) -> {
										x.displayInfoMessage("Vous avez " + toDiscard + " carte(s) en trop. Jouez des événements, ou défaussez des cartes.");
										animCon.wait = false;
									});
									
									for (int j = 0; j < toDiscard; j++) {
										useCardButtonClicked();
										this.wait();
									}
								}
							}
							
							
							phase = GamePhase.PROPAGATION;
							// infection phase
							
							if (status == GameStatus.VALID) {
								for (int i = 0; i < model.getEmergencyValue() && status == GameStatus.VALID; i++) {
									ProjectCard card = (ProjectCard) model.getProjectDeck().drawFirst();
									LinkedList<Card> cs = new LinkedList<Card>();
									cs.add(card);
									animCon.addCardShow(Owner.PROJECT_DECK, Owner.PROJECT_DISCARD, cs);
									
									int projAmount = card.getRoom().getProject(card.getRoom().getCourse()).getProjectAmount();
									if (projAmount < 3) {
										card.getRoom().getProject(card.getRoom().getCourse()).setProjectAmount(projAmount + 1);
										view.displayAddProjectToRoom(card.getRoom().getName(), courseToInteger(card.getRoom().getCourse()));
									} else {
										burnOut(new LinkedList<Room>(),card.getRoom(), card.getRoom().getCourse());
									}

									model.getProjectDiscard().addCard(card);
								}
							}
						}
						model.nextPlayer();
					}
	
					phase = GamePhase.CONCLUSION;
					
					switch (status) {
					case CARD_LACK:
						view.displayValidationMessage("DOMMAGE !\nIl n'y a plus assez de cartes dans le deck joueur.\nVoulez-vous recommencer ?");
						this.wait();
						break;
					case BURN_OUT:
						view.displayValidationMessage("DOMMAGE !\nLa gauge de burn out est au maximum.\nVoulez-vous recommencer ?");
						this.wait();
						break;
					case PROJECT_LACK:
						view.displayValidationMessage("DOMMAGE !\nIl y a déjà trop de projets.\nVoulez-vous recommencer ?");
						this.wait();
						break;
					case VALID:
						break;
					case WIN:
						view.displayValidationMessage("BRAVO !\nVous avez complété toutes les UVs.\nVoulez-vous recommencer ?");
						this.wait();
						break;
					case GIVE_UP:
						view.displayValidationMessage("DOMMAGE !\nVous avez abandonné.\nVoulez-vous recommencer ?");
						this.wait();
						break;
					default:
						break;
					}					
				}
				animCon.finish();
			}	
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		Platform.runLater(() -> view.close());
	}

	private void burnOut(LinkedList<Room> burnOutRooms, Room room, Course course) {
		if (!burnOutRooms.contains(room) && status == GameStatus.VALID) {
			burnOutRooms.add(room);
			
			model.increaseBurnOutGauge();
			animCon.addGauge(false);
			
			
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (model.getBurnOutValue() > 7) {
				status = GameStatus.BURN_OUT;
			} else {
				for (Room r : room.getNeighbours()) {
					if (r.getProject(course).getProjectAmount() == 3) {
						burnOut(burnOutRooms, r, course);
					} else {
						r.getProject(course).setProjectAmount(
								r.getProject(course).getProjectAmount() + 1);
						view.displayAddProjectToRoom(r.getName(), courseToInteger(course));
					}
				}
			}
		}
	}

	synchronized private void displayCards(Owner src, Owner dest, LinkedList<Card> cards) {
		ArrayList<Event> events = new ArrayList<Event>(eventCardsToEvents(getEventCards(cards)));
		ArrayList<String> projs = new ArrayList<String>(projectCardsToStrings(getProjectCards(cards)));
		ArrayList<String> rooms = new ArrayList<String>(roomCardsToStrings(getRoomCards(cards)));
		int parties = getPartyCards(cards);
		
		//TODO remove
		rooms.addAll(projs);
		
		view.displayDrawCards(src, dest, false, rooms, events, parties);	
		try {
			Thread.sleep(2000);
			view.clean();
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private int courseToInteger(Course c) {
		for (int i = 0; i < 4; i++) {
			if (model.getCourses()[i] == c) {
				return i;
			}
		}
		return -1;
	}
	
	private int getPartyCards(LinkedList<Card> cards) {
		int partyCards = 0;
		for (Card pc : cards) {
			if (pc.getClass() == PartyCard.class) {
				partyCards++;
			}
		}
		return partyCards;
	}
	
	private LinkedList<EventCard> getEventCards(LinkedList<Card> cards) {
		LinkedList<EventCard> events = new LinkedList<>();
		
		for (Card c : cards) {
			if (c.getClass() == EventCard.class) {
				events.add((EventCard)c);
			}
		}
		
		return events;
	}
	
	//TODO remove
	private LinkedList<ProjectCard> getProjectCards(LinkedList<Card> cards) {
		LinkedList<ProjectCard> rooms = new LinkedList<>();
		
		for (Card c : cards) {
			if (c.getClass() == ProjectCard.class) {
				rooms.add((ProjectCard)c);
			}
		}
		
		return rooms;
	}

	
	private LinkedList<RoomCard> getRoomCards(LinkedList<Card> cards) {
		LinkedList<RoomCard> rooms = new LinkedList<>();
		
		for (Card c : cards) {
			if (c.getClass() == RoomCard.class) {
				rooms.add((RoomCard)c);
			}
		}
		
		return rooms;
	}

	private LinkedList<RoomCard> getRoomCards(LinkedList<Card> cards, Course course) {
		LinkedList<RoomCard> rooms = new LinkedList<>();
		
		for (Card c : cards) {
			if (c.getClass() == RoomCard.class && ((RoomCard)c).getRoom().getCourse() == course) {
				rooms.add((RoomCard)c);
			}
		}
		
		return rooms;
	}
	
	private LinkedList<String> projectCardsToStrings(LinkedList<ProjectCard> cards) {
		LinkedList<String> strs = new LinkedList<String>();
		
		
		for (ProjectCard c : cards) {
			strs.add(c.getRoom().getName());
		}
		
		return strs;
	}
	
	private LinkedList<String> roomCardsToStrings(LinkedList<RoomCard> cards) {
		LinkedList<String> strs = new LinkedList<String>();
		
		for (RoomCard c : cards) {
			strs.add(c.getRoom().getName());
		}
		
		return strs;
	}
	
	private LinkedList<Event> eventCardsToEvents(LinkedList<EventCard> cards) {
		LinkedList<Event> events = new LinkedList<>();
		
		for (EventCard c : cards) {
			events.add(c.getEvent());
		}
		
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

		//testing uniqueness and non empty course names
		for (String uv : uvs) {
			if (uv.equals("")) {
				view.displayInfoMessage("Le nom d'une UV ne peu pas être vide.");
				return;
			} else {
				if (testedStrings.contains(uv)) {
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
				view.displayInfoMessage("Le nom d'un joueur ne peu pas être vide.");
				return;
			} else {
				if (testedStrings.contains(p)) {
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
				model.getPlayers().stream().forEach((x) -> view.makePawnClickable(true, x.getRole()));
			} else {
				pawnClicked(model.getCurrentPlayer().getRole());
			}			
		}
	}
	
	@Override
	synchronized public void pawnClicked(Role player) {
		selectedPlayer = playerRoleToPlayer(player);
		selectedReachableRooms = model.reachableRooms(player, actionPoints);
		animCon.addClean();
		animCon.add((x) -> {
			x.displayReachableRooms(selectedReachableRooms);
			animCon.wait = false;
			animCon.waitedEvent = null;
		});
	}

	@Override
	synchronized public void placeClicked(String name) {
		actionPoints -= selectedReachableRooms.get(name);
		view.displayActionsPoints(actionPoints);
		
		LinkedList<String> shortP = model.shortestPath(selectedPlayer.getPosition().getName(), name);
		
		if (action == ActionType.USE_CARD) {
			LinkedList<Card> toDisplay = new LinkedList<>();
			toDisplay.add(selectedCard);
			
			animCon.addClean();
			animCon.addCardShow(playerToOwner(model.getCurrentPlayer()), Owner.PLAYER_DISCARD, toDisplay);

			model.getCurrentPlayer().getCards().getCardList().remove(toDisplay.get(0));
			model.getPlayerDiscard().addCard((PlayerCard)toDisplay.get(0));
			
			shortP = new LinkedList<String>();
			shortP.add(name);
			
			action = ActionType.RUN;
		}
		
		animCon.addClean();
		animCon.addMovePawn(selectedPlayer, shortP.toArray(new String[shortP.size()]));
		
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
			int toRemove = 0;
			
			for (i = 0; i < 4; i++) {
				Project proj = room.getProject(model.getCourses()[i]);
				if (proj.getProjectAmount() > 0) {
					actionPoints--;
					view.displayActionsPoints(actionPoints);
					if (model.getCourses()[i].isCompleted() || model.getCurrentPlayer().getRole() == Role.MENTOR) {
						toRemove = proj.getProjectAmount();
						proj.setProjectAmount(0);
					} else {
						toRemove = 1;
						proj.setProjectAmount(proj.getProjectAmount()-1);
					}
					
					break;
				}
			}

			if (i == 5) {
				view.displayMessage("Il n'y a aucun projet dans cette salle.");
			} else {
				for (int j = 0; j < toRemove; j++) {
					view.displayRemoveProjectFromRoom(room.getName(), i);
				}
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
				if (model.getCurrentPlayer().getPosition() == student.getPosition()) {
					selectedPlayers.add(student);
				}
			}
			
			if (selectedPlayers.isEmpty()) {
				view.displayMessage("Il n'y a personne avec vous dans la salle.");
			} else {
				selectedCard = null;
				all:
				for (PhDStudent pl : selectedPlayers) {
					for (RoomCard card : getRoomCards(new LinkedList<Card>(pl.getCards().getCardList()))) {
						if (card.getRoom() == pl.getPosition()) {
							selectedPlayer = pl;
							selectedCard = card;
							break all;
						}
					}
				}
			
				if (selectedCard == null) {
					view.displayMessage("Personne dans la salle ne possède la carte de la salle.");
				} else {
					if (model.getCurrentPlayer() == selectedPlayer) {
						view.displayInfoMessage("Vous possédez la carte de la ville dans laquelle vous vous trouvez. Choisissez une personne à qui la donner.");
						selectedPlayers.remove(model.getCurrentPlayer());
						selectedPlayers.stream().forEach((x) -> view.makePawnClickable(true, x.getRole()));
					} else {
						view.displayValidationMessage(selectedPlayer.getName() + " possède la carte de la ville dans laquelle vous vous trouvez. Voulez vous la lui prendre ?");
					}
				}
			}
		}
	}
	
	//TODO
	@Override
	public void useCardButtonClicked() {
		action = ActionType.NONE;
		
		if (actionPoints > 0) {
			if (model.getCurrentPlayer().getCards().getSize() > 0) {
				Owner owner = playerToOwner(model.getCurrentPlayer());				
				animCon.addCardCenter(owner, owner, true, new LinkedList<Card>(model.getCurrentPlayer().getCards().getCardList()));
				
				action = ActionType.USE_CARD;
			} else {
				view.displayMessage("Vous n'avez aucune carte dans votre main.");
			}
		} else {
			if (phase == GamePhase.ACTION) {
				view.displayMessage("Vous n'avez pas assez de points d'action.");
			} else {
				Owner owner = playerToOwner(model.getCurrentPlayer());				
				animCon.addCardCenter(owner, owner, true, new LinkedList<Card>(model.getCurrentPlayer().getCards().getCardList()));
				
				action = ActionType.USE_CARD;
			}
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
				selectedCards = null;
				
				for (course = 0; course < 4; course++) {
					LinkedList<RoomCard> cards = getRoomCards(new LinkedList<Card>(model.getCurrentPlayer().getCards().getCardList()), model.getCourses()[course]);
					if (cards.size() >= 5) {
						selectedCards = new LinkedList<Card>(cards.subList(0, 5));
						break;
					}
				}

				if (course == 5) {
					view.displayMessage("Vous n'avez les cartes nécessaires.");
				} else {
					Owner owner = playerToOwner(model.getCurrentPlayer());
					//TODO let the user select the cards
							
					animCon.addCardCenter(owner, owner, false, selectedCards);
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
			
			for (RoomCard r : getRoomCards(new LinkedList<Card>(model.getCurrentPlayer().getCards().getCardList()))) {				
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
					view.displayActionsPoints(actionPoints);
					model.addLabRoom(room.getRoom().getName());
					view.displaySetRoomToLab(room.getRoom().getName());
				}
			}
		}
	}

	//TODO
	@Override
	public void hackButtonClicked() {
		action = ActionType.NONE;
		
		if (actionPoints == 0) {
			view.displayMessage("Vous n'avez pas assez de points d'action.");
		} else if (model.getCurrentPlayer().getRole() != Role.HACKER) {
			view.displayMessage("Vous n'êtes pas hacker.");
		} else {
			if (model.getCurrentPlayer().getExtraEventCard() == null) {
				selectedCards = new LinkedList<Card>(getEventCards(new LinkedList<Card>(model.getPlayerDiscard().getCardList())));
				
				if (selectedCards.isEmpty()) {
					view.displayMessage("Il n'y a pas de carte événement dans la défausse joueur");
				} else {
					action = ActionType.HACK;
					animCon.addCardCenter(Owner.PLAYER_DISCARD, Owner.PLAYER_DISCARD, true, selectedCards);					
				}
			} else {
				action = ActionType.HACK;
				selectedCards = new LinkedList<Card>();
				selectedCards.add(model.getCurrentPlayer().getExtraEventCard());
				Owner own = playerToOwner(model.getCurrentPlayer());
				animCon.addCardCenter(own, own, false, selectedCards);
				view.displayValidationMessage("Voulez-vous vraiment utiliser votre carte événement ?\n(Elle sera définitivement retirée du jeu.)");
			}
		}
	}

	@Override
	synchronized public void YesButtonClicked() {
		if (action == ActionType.MASTER_UV) {
			for (Card card : selectedCards) {
				model.getCurrentPlayer().getCards().getCardList().remove(card);
				model.getPlayerDiscard().getCardList().add((PlayerCard)card);
				view.displayChangeOwnerOfDisplayedCard(Owner.PLAYER_DISCARD, ((RoomCard)card).getRoom().getName());
			}
			
			animCon.addCardStore();
		}
		if (action == ActionType.GIVE_UP) {
			status = GameStatus.GIVE_UP;
			this.notify();
		}
		if (phase == GamePhase.CONCLUSION) {
			status = GameStatus.VALID;
			this.notify();
		}
		if (action == ActionType.HACK) {
			view.displayChangeOwnerOfDisplayedCard(Owner.PLAYER_DISCARD, model.getCurrentPlayer().getExtraEventCard().getEvent());
			animCon.addCardStore();
			resolveEventCard(model.getCurrentPlayer().getExtraEventCard());
			model.getCurrentPlayer().setExtraEventCard(null);			
		}
	}

	@Override
	synchronized public void NoButtonClicked() {
		if (action == ActionType.MASTER_UV) {
			animCon.addCardStore();
		}
		if (phase == GamePhase.CONCLUSION) {
			status = GameStatus.GIVE_UP;
			this.notify();
		}
		if (action == ActionType.HACK) {
			animCon.addCardStore();			
		}
	}

	@Override
	synchronized public void endOfStageButtonClicked() {
		action = ActionType.NONE;
		this.notify();
	}


	@Override
	synchronized public void eventCardClicked(Event event) {
		if (action == ActionType.HACK) {
			view.displayChangeOwnerOfDisplayedCard(playerToOwner(model.getCurrentPlayer()), event);
			animCon.addCardStore();
			for (Card card : selectedCards) {
				if (((EventCard)card).getEvent() == event) {
					model.getCurrentPlayer().getCards().addCard((PlayerCard)card);
					model.getPlayerDiscard().getCardList().remove(card);
					break;
				}
			}
		}
		if (action == ActionType.USE_CARD) {
			view.displayChangeOwnerOfDisplayedCard(Owner.PLAYER_DISCARD, event);
			animCon.addCardStore();			
			for (Card card : model.getCurrentPlayer().getCards().getCardList()) {
				if (card.getClass() == EventCard.class) {
					if (((EventCard)card).getEvent() == event) {
						resolveEventCard((EventCard) card);
						model.getCurrentPlayer().getCards().getCardList().remove(card);
						model.getPlayerDiscard().addCard((PlayerCard)card);
						break;
					}
				}
			}
			if (phase == GamePhase.DISCARD) {
				for (PlayerCard card : model.getCurrentPlayer().getCards().getCardList()) {
					if (card.getClass() == EventCard.class) {
						if (((EventCard)card).getEvent() == event) {
							model.getCurrentPlayer().getCards().getCardList().remove(card);
							model.getPlayerDiscard().addCard(card);
							break;
						}
					}
				}
				this.notify();
			}
		}
	}

	//TODO animation fails
	@Override
	synchronized public void roomCardClicked(String room) {
		if (action == ActionType.USE_CARD) {
			if (phase == GamePhase.ACTION) {
				view.displayDiscardCards();
				view.clean();
				
				HashMap<String, Integer> reachable = new HashMap<String, Integer>();
				
				if (model.getCurrentPlayer().getPosition() == roomNameToRoom(room)) {
					for (Room r : model.getRooms()) {
						if (!r.getName().equals(room)) {
							reachable.put(r.getName(), 1);
						}
					}
				} else {
					reachable.put(room, 1);
				}
				
				for (Card c : model.getCurrentPlayer().getCards().getCardList()) {
					if (c.getClass() == RoomCard.class) {
						RoomCard rc = (RoomCard)c;
						if (rc.getRoom().getName().equals(room)) {
							selectedCard = rc;
						}
					}
				}
				
				selectedReachableRooms = reachable;
				selectedPlayer = model.getCurrentPlayer();
				view.displayReachableRooms(reachable);		
			} else {
				view.displayChangeOwnerOfDisplayedCard(Owner.PLAYER_DISCARD, room);
				animCon.addCardStore();
				for (PlayerCard card : model.getCurrentPlayer().getCards().getCardList()) {
					if (card.getClass() == RoomCard.class) {
						if (((RoomCard)card).getRoom().getName().equals(room)) {
							model.getCurrentPlayer().getCards().getCardList().remove(card);
							model.getPlayerDiscard().addCard(card);
							break;
						}
					}
				}
				this.notify();
			}
		}
	}
	
	
	
	private class AnimationController extends Thread {
		LinkedList<Animation> anims = new LinkedList<Animation>();
		boolean finish = false;
		boolean wait = false;
		AnimationEventType waitedEvent = null;
		
		@Override
		public void run() {
			synchronized (this) {
				while (!finish) {	
					if (!anims.isEmpty()) {
						anims.getFirst().run(view);
					} else {
						System.out.println("Stop");
						wait = true;
					}
					anims.poll();
					if (wait && !finish) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		synchronized public void add(Animation a) {
			synchronized (this) {
				anims.add(a);
				if (waitedEvent == null && wait == true) {
					wait = false;
					this.notify();
				}
			}
		}
		
		synchronized public void addCardCenter(Owner a, Owner b, boolean clickable, LinkedList<Card> c) {
			this.add((x) -> {
				ArrayList<Event> events = new ArrayList<Event>(eventCardsToEvents(getEventCards(c)));
				ArrayList<String> projs = new ArrayList<String>(projectCardsToStrings(getProjectCards(c)));
				ArrayList<String> rooms = new ArrayList<String>(roomCardsToStrings(getRoomCards(c)));
				int parties = getPartyCards(c);
				
				//TODO remove
				rooms.addAll(projs);
				
				x.displayDrawCards(a, b, clickable, rooms, events, parties);

				wait = true;
				waitedEvent = AnimationEventType.CENTERED;
			});
		}
		
		synchronized public void addCardStore() {
			this.add((x) -> {
				x.displayDiscardCards();
				wait = true;
				waitedEvent = AnimationEventType.STORED;
			});
			
			this.add((x) -> {
				x.clean();
				wait = true;
				waitedEvent = AnimationEventType.CLEARED;
			});
		}
		
		synchronized public void addClean() {
			this.add((x) -> {
				x.clean();
				wait = true;
				waitedEvent = AnimationEventType.CLEARED;
			});
		}
		
		synchronized public void addWait(long time) {
			this.add((x) -> {try {
				Thread.sleep(time); wait = false;
				waitedEvent = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}});
		}
		
		synchronized public void addGauge(boolean propagation) {
			if (propagation) {
				this.add((x) -> {
					x.displayIncreasePropagationGauge();
					wait = true;
					waitedEvent = AnimationEventType.PROPAGATION;
				});
			} else {
				this.add((x) -> {
					x.displayIncreaseBurnOutGauge();
					wait = true;
					waitedEvent = AnimationEventType.OUT_BREAK;
				});
			}
		}
		
		synchronized public void addMovePawn(PhDStudent player, String[] path) {
			this.add((x) -> {
				x.displayMovePawn(player.getRole(), path);
				wait = true;
				waitedEvent = AnimationEventType.PAWN;
			});
		}
		
		synchronized public void addCardShow(Owner a, Owner b, LinkedList<Card> c) {
			this.add((x) -> {
				ArrayList<Event> events = new ArrayList<Event>(eventCardsToEvents(getEventCards(c)));
				ArrayList<String> projs = new ArrayList<String>(projectCardsToStrings(getProjectCards(c)));
				ArrayList<String> rooms = new ArrayList<String>(roomCardsToStrings(getRoomCards(c)));
				int parties = getPartyCards(c);
				
				//TODO remove
				rooms.addAll(projs);
				
				x.displayDrawCards(a, b, false, rooms, events, parties);

				wait = true;
				waitedEvent = AnimationEventType.CENTERED;
			});
			
			this.add((x) -> {try {
				Thread.sleep(1000); wait = false;
				waitedEvent = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}});
			
			this.add((x) -> {
				x.displayDiscardCards();
				wait = true;
				waitedEvent = AnimationEventType.STORED;
			});
			
			this.add((x) -> {
				x.clean();
				wait = true;
				waitedEvent = AnimationEventType.CLEARED;
			});
		}
		
		synchronized public void animationFinished(AnimationEventType type) {
			if (type == waitedEvent && waitedEvent != null) {
				waitedEvent = null;
				wait = true;
				System.out.println(type);
				this.notify();
			}
		}
		
		synchronized public void finish() {
			finish = true;
		}
	}

	@Override
	public void cardToCenterFinished() {
		animCon.animationFinished(AnimationEventType.CENTERED);
	}

	@Override
	public void cardToDeckFinished() {
		animCon.animationFinished(AnimationEventType.STORED);
	}

	@Override
	public void cleared() {
		animCon.animationFinished(AnimationEventType.CLEARED);
	}

	@Override
	public void movePawnFinished() {
		animCon.animationFinished(AnimationEventType.PAWN);
	}

	@Override
	public void propagationFinished() {
		animCon.animationFinished(AnimationEventType.PROPAGATION);
	}

	@Override
	public void outbreakFinished() {
		animCon.animationFinished(AnimationEventType.OUT_BREAK);
	}

	
}