package the_projects.controller;

import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
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
								
								final int toDiscard = model.getCurrentPlayer().getCards().getSize() - 7;
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
		if (!burnOutRooms.contains(room) && status == GameStatus.VALID && !isThereCoffee(room)) {
			burnOutRooms.add(room);
			
			model.increaseBurnOutGauge();
			animCon.addGauge(false);
			
			if (model.getBurnOutValue() > 7) {
				status = GameStatus.BURN_OUT;
			} else {
				for (Room r : room.getNeighbours()) {
					if (r.getProject(course).getProjectAmount() == 3) {
						burnOut(burnOutRooms, r, course);
					} else if (!isThereCoffee(r)) {
						r.getProject(course).setProjectAmount(r.getProject(course).getProjectAmount() + 1);
						animCon.addAddProject(r, course);
					}
				}
			}
		}
	}
	
	private boolean isThereCoffee(Room room) {
		for (PhDStudent p : model.getPlayers()) {
			if (p.getRole() == Role.COFFEE_MAKER && (p.getPosition() == room || room.getNeighbours().contains(p.getPosition()))) {
				return true;
			}
		}
		return false;
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
			//chose pawn/room
			action = ActionType.RUN;
			if (model.getCurrentPlayer().getRole() == Role.GROUP_LEADER) {
				selectedPlayers = new LinkedList<PhDStudent>(model.getPlayers());
				selectedPlayers.stream().forEach((x) -> view.makePawnClickable(true, x.getRole()));
			} else {
				selectedPlayers = new LinkedList<PhDStudent>();
				pawnClicked(model.getCurrentPlayer().getRole());
			}			
		}
	}
	
	private PhDStudent roleToPlayer(Role r) {
		for (PhDStudent p : model.getPlayers()) {
			if (p.getRole() == r) {
				return p;
			}
		}
		return null;
	}
	
	@Override
	synchronized public void pawnClicked(Role player) {
		for (PhDStudent p : selectedPlayers) {
			view.makePawnClickable(false, p.getRole());
		}
		if (action == ActionType.SHARE_KNOWLEDGE) {
			actionPoints--;
			
			view.displayActionsPoints(actionPoints);
			
			selectedCards = new LinkedList<Card>();
			selectedCards.add(selectedCard);
			
			animCon.addClean();
			animCon.addCardShow(playerToOwner(model.getCurrentPlayer()), playerToOwner(roleToPlayer(player)), selectedCards);
			
			model.getCurrentPlayer().getCards().getCardList().remove(selectedCard);
			playerRoleToPlayer(player).getCards().addCard((PlayerCard)selectedCard);
		} else if (action == ActionType.RUN) {
			selectedPlayer = playerRoleToPlayer(player);
			selectedReachableRooms = model.reachableRooms(player, actionPoints);
			animCon.addClean();
			animCon.add((x) -> {
				x.displayReachableRooms(selectedReachableRooms);
				animCon.wait = false;
				animCon.waitedEvent = null;
			});
		}
	}

	@Override
	synchronized public void placeClicked(String name) {
		//move
		actionPoints -= selectedReachableRooms.get(name);
		view.displayActionsPoints(actionPoints);
		
		LinkedList<String> shortP = model.shortestPath(selectedPlayer.getPosition().getName(), name);
		
		if (action == ActionType.USE_CARD) {
			//discard room card
			selectedCards = new LinkedList<>();
			selectedCards.add(selectedCard);
			
			animCon.addClean();
			animCon.addCardShow(playerToOwner(model.getCurrentPlayer()), Owner.PLAYER_DISCARD, selectedCards);

			model.getCurrentPlayer().getCards().getCardList().remove(selectedCards.get(0));
			model.getPlayerDiscard().addCard((PlayerCard)selectedCards.get(0));
			
			shortP = new LinkedList<String>();
			shortP.add(name);
			
			action = ActionType.RUN;
		}
		
		animCon.addClean();
		animCon.addMovePawn(selectedPlayer, shortP.toArray(new String[shortP.size()]));
		
		if (model.getCurrentPlayer().getRole() == Role.MENTOR) {
			for (String r : shortP) {
				Room ro = roomNameToRoom(r);
				for (Course c : model.getCourses()) {
					if (c.isCompleted()) {
						int i = ro.getProject(c).getProjectAmount();
						ro.getProject(c).setProjectAmount(0);
						for (int j = 0; j < i; j++) {
							animCon.addRemoveProject(ro, c);
						}
					}
				}
			}
			
			for (Course c : model.getCourses()) {
				if (isEradicated(c)) {
					view.displayCourseEradicated(courseToInteger(c));
				}
			}
		}
		
		selectedPlayer.setPosition(roomNameToRoom(name));
		selectedPlayers = new LinkedList<PhDStudent>();
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
				//removing
				for (int j = 0; j < toRemove; j++) {
					animCon.addRemoveProject(room, model.getCourses()[i]);
				}
				if (isEradicated(model.getCourses()[i])) {
					view.displayCourseEradicated(i);
				}
			}	
		}
	}

	private boolean isEradicated(Course c) {
		for (Room r : model.getRooms()) {
			if (r.getProject(c).getProjectAmount() != 0) {
				return false;
			}
		}
		if (c.isCompleted()) {
			return true;
		}
		return false;
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
				//looking for the room card
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
					action = ActionType.SHARE_KNOWLEDGE;
					if (model.getCurrentPlayer() == selectedPlayer) {
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
		if (actionPoints == 0) {
			selectedCards = new LinkedList<Card>(getEventCards(new LinkedList<Card>(model.getCurrentPlayer().getCards().getCardList())));
			if (model.getCurrentPlayer().getRole() == Role.HACKER && model.getCurrentPlayer().getExtraEventCard() != null) {
				selectedCards.add(model.getCurrentPlayer().getExtraEventCard());
			}
			if (phase == GamePhase.ACTION) {
				if (selectedCards.isEmpty()) {
					view.displayMessage("Vous n'avez pas assez de points d'action et vous ne possédez aucune carte événement");
				} else {
					//chose an event card to use
					action = ActionType.USE_CARD;
					animCon.addCardCenter(playerToOwner(model.getCurrentPlayer()), playerToOwner(model.getCurrentPlayer()), true, selectedCards);
				}	
			} else if (phase == GamePhase.DISCARD) {
				//chose card to discard/use
				action = ActionType.USE_CARD;
				Owner owner = playerToOwner(model.getCurrentPlayer());	
				selectedCards = new LinkedList<Card>(model.getCurrentPlayer().getCards().getCardList());
				animCon.addCardCenter(owner, owner, true, selectedCards);
			}
		} else {
			selectedCards = new LinkedList<Card>(model.getCurrentPlayer().getCards().getCardList());
			if (model.getCurrentPlayer().getRole() == Role.HACKER && model.getCurrentPlayer().getExtraEventCard() != null) {
				selectedCards.add(model.getCurrentPlayer().getExtraEventCard());
			}
			if (selectedCards.isEmpty()) {
				view.displayMessage("Vous n'avez aucune carte dans votre main.");
			} else {			
				//chose card to use
				action = ActionType.USE_CARD;
				Owner owner = playerToOwner(model.getCurrentPlayer());					
				animCon.addCardCenter(owner, owner, true, selectedCards);
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
					if (model.getCurrentPlayer().getRole() == Role.DAOUID) {
						if (cards.size() >= 4) {
							selectedCards = new LinkedList<Card>(cards.subList(0, 4));
							break;
						}
					} else {
						if (cards.size() >= 5) {
							selectedCards = new LinkedList<Card>(cards.subList(0, 5));
							break;
						}	
					}
				}

				if (course == 5) {
					view.displayMessage("Vous n'avez pas les cartes nécessaires.");
				} else {
					Owner owner = playerToOwner(model.getCurrentPlayer());
					Course c = ((RoomCard)selectedCards.get(0)).getRoom().getCourse();
					//TODO let the user select the cards
					animCon.addCardShow(owner, Owner.PLAYER_DISCARD, selectedCards);
					model.getCurrentPlayer().getCards().getCardList().removeAll(selectedCards);
					
					LinkedList<PlayerCard> pctemp = new LinkedList<PlayerCard>();
					selectedCards.stream().forEach((x) -> pctemp.add((PlayerCard)x));
					
					model.getPlayerDiscard().getCardList().addAll(pctemp);
					view.displayCourseMastered(courseToInteger(c));
					c.setCompleted();
					if (isEradicated(c)) {
						c.setEradicated();
						view.displayCourseEradicated(courseToInteger(c));
					}
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
			if (model.getCurrentPlayer().getRole() == Role.INSTALLER) {
				if (model.getLabRoomAmount() == 0) {
					view.displayMessage("Toutes les salles de TP ont déjà été posées.");
				} else {
					//build
					actionPoints--;
					view.displayActionsPoints(actionPoints);
					model.addLabRoom(model.getCurrentPlayer().getPosition().getName());
					view.displaySetRoomToLab(model.getCurrentPlayer().getPosition().getName());
				}
			} else {
				RoomCard room = null;
				//looking for the room card
				for (RoomCard r : getRoomCards(new LinkedList<Card>(model.getCurrentPlayer().getCards().getCardList()))) {				
					if (r.getRoom() == model.getCurrentPlayer().getPosition()) {
						selectedCard = r;
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
						//build
						actionPoints--;
						view.displayActionsPoints(actionPoints);
						model.addLabRoom(room.getRoom().getName());
						view.displaySetRoomToLab(room.getRoom().getName());
						selectedCards = new LinkedList<Card>();
						selectedCards.add(selectedCard);
						animCon.addCardShow(playerToOwner(model.getCurrentPlayer()), Owner.PLAYER_DISCARD, selectedCards);
						model.getCurrentPlayer().getCards().getCardList().remove(selectedCard);
						model.getPlayerDiscard().addCard((PlayerCard)selectedCard);
					}
				}
			}
		}
	}

	//TODO add messages to animation
	
	@Override
	synchronized public void hackButtonClicked() {
		action = ActionType.NONE;
		if (actionPoints == 0) {
			view.displayMessage("Vous n'avez pas assez de points d'action.");
		} else if (model.getCurrentPlayer().getRole() != Role.HACKER) {
			view.displayMessage("Vous n'êtes pas hacker.");
		} else {
			if (model.getCurrentPlayer().getExtraEventCard() != null) {
				view.displayMessage("Vous avez déjà une carte hackée.");
			} else {
				selectedCards = new LinkedList<Card>(getEventCards(new LinkedList<Card>(model.getPlayerDiscard().getCardList())));
				
				if (selectedCards.isEmpty()) {
					view.displayMessage("Il n'y a pas de carte événement dans la défausse joueur");
				} else {
					//chose an event card
					action = ActionType.HACK;
					animCon.addCardCenter(Owner.PLAYER_DISCARD, Owner.PLAYER_DISCARD, true, selectedCards);					
				}
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
		if (action == ActionType.SHARE_KNOWLEDGE) {
			selectedCards = new LinkedList<Card>();
			selectedCards.add(selectedCard);
			
			animCon.addCardShow(playerToOwner(selectedPlayer), playerToOwner(model.getCurrentPlayer()), selectedCards);
			selectedPlayer.getCards().getCardList().remove(selectedCard);
			model.getCurrentPlayer().getCards().addCard((PlayerCard)selectedCard);
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


	private EventCard getEventCard(LinkedList<Card> cards, Event e) {
		for (EventCard card : getEventCards(cards)) {
			if (e == card.getEvent()) {
				return card;
			}
		}
		return null;
	}
	
	@Override
	synchronized public void eventCardClicked(Event event) {
		if (action == ActionType.HACK) {			
			actionPoints --;
			selectedCard = getEventCard(new LinkedList<Card>(model.getPlayerDiscard().getCardList()), event);
			model.getCurrentPlayer().setExtraEventCard((EventCard)selectedCard);
			model.getPlayerDiscard().getCardList().remove(selectedCard);
			
			view.displayActionsPoints(actionPoints);
			view.displayChangeOwnerOfDisplayedCard(playerToOwner(model.getCurrentPlayer()), event);
			animCon.addCardStore();
		}
		if (action == ActionType.USE_CARD) {
			view.displayChangeOwnerOfDisplayedCard(Owner.PLAYER_DISCARD, event);
			animCon.addCardStore();
			
			EventCard ec = getEventCard(selectedCards, event);
			resolveEventCard(ec);
			if (ec != model.getCurrentPlayer().getExtraEventCard()) {
				model.getCurrentPlayer().getCards().getCardList().remove(ec);
				model.getPlayerDiscard().addCard(ec);
			} else {
				model.getCurrentPlayer().setExtraEventCard(null);
			}
			
			if (phase == GamePhase.DISCARD) {
				this.notify();
			}
		}
	}

	private RoomCard getRoomCard(LinkedList<Card> cards, String room) {
		for (RoomCard card : getRoomCards(cards)) {
			if (card.getRoom().getName().equals(room)) {
				return card;
			}
		}
		return null;
	}
	
	//TODO animation fails
	@Override
	synchronized public void roomCardClicked(String room) {
		if (action == ActionType.USE_CARD) {
			selectedCard = getRoomCard(selectedCards, room);
			if (phase == GamePhase.ACTION) {
				//chose the room				
				animCon.addCardStore();
				
				selectedReachableRooms = new HashMap<String, Integer>();
				
				if (model.getCurrentPlayer().getPosition() == ((RoomCard)selectedCard).getRoom()) {
					for (Room r : model.getRooms()) {
						if (((RoomCard)selectedCard).getRoom() != r) {
							selectedReachableRooms.put(r.getName(), 1);
						}
					}
				} else {
					selectedReachableRooms.put(room, 1);
				}
				
				selectedPlayer = model.getCurrentPlayer();
				animCon.addReachable();		
			} else if (phase == GamePhase.DISCARD) {
				//discard card
				view.displayChangeOwnerOfDisplayedCard(Owner.PLAYER_DISCARD, room);
				animCon.addCardStore();
				
				model.getCurrentPlayer().getCards().getCardList().remove(selectedCard);
				model.getPlayerDiscard().addCard((PlayerCard)selectedCard);

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
		
		synchronized public void addReachable() {
			this.add((x) -> {
				x.displayReachableRooms(selectedReachableRooms);
				wait = false;
				waitedEvent = null;
			});
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
		
		synchronized public void addRemoveProject(Room r, Course c) {
			this.add((x) -> {
				view.displayRemoveProjectFromRoom(r.getName(), courseToInteger(c));
				wait = false;
				waitedEvent = null;
			});
		}
		
		synchronized public void addAddProject(Room r, Course c) {
			this.add((x) -> {
				view.displayAddProjectToRoom(r.getName(), courseToInteger(c));
				wait = false;
				waitedEvent = null;
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
			synchronized (this) {
				if (type == waitedEvent && waitedEvent != null) {
					waitedEvent = null;
					wait = true;
					this.notify();
				}
			}
		}
		
		synchronized public void finish() {
			synchronized (this) {
				finish = true;
			}			
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