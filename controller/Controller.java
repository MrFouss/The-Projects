package the_projects.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.swing.internal.plaf.synth.resources.synth;

import the_projects.model.Course;
import the_projects.model.Model;
import the_projects.model.PhDStudent;
import the_projects.model.Project;
import the_projects.model.Role;
import the_projects.model.Room;
import the_projects.model.card.Card;
import the_projects.model.card.Event;
import the_projects.model.card.EventCard;
import the_projects.model.card.PartyCard;
import the_projects.model.card.PlayerCard;
import the_projects.model.card.ProjectCard;
import the_projects.model.card.RoomCard;
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

	public void run() {
		try {
			synchronized (this) {
				// init

				phase = GamePhase.SETTING;
				status = GameStatus.VALID;
				model = null;
				view = new View();
				view.addListener(this);
				actionPoints = 0;
				action = ActionType.NONE;
				selectedPlayer = null;
				reachablePlaces = null;

				// setting phase

				view.displaySetting();
				while (phase == GamePhase.SETTING) {
					//update doable actions
					this.wait(); // waiting for the user to enter and validate
									// his settings
				}

				// game loop

				view.displayGameBoard(model);

				while (status == GameStatus.VALID) {

					// action phase

					actionPoints = 4;
					while (phase == GamePhase.ACTION && status == GameStatus.VALID) {
						this.wait(); // wait for an action to be chosen and executed
					}
					actionPoints = 0;

					if (status == GameStatus.VALID) {

						// card phase

						// display card phase

						if (actionPoints == 0) { // nb cartes deck joueur < 2
							status = GameStatus.CARD_LACK;
						} else {
							PlayerCard card1, card2;
							card1 = (PlayerCard) model.getPlayerDeck()
									.drawFirst();
							card2 = (PlayerCard) model.getPlayerDeck()
									.drawFirst();
							// view.displayCards(card1, card2)
							resolvePlayerCard(card1);
							if (status == GameStatus.VALID) {
								resolvePlayerCard(card2);
							}

							// infection phase

							if (status == GameStatus.VALID) {
								for (int i = 0; i < model.getEmergencyValue()
										&& status == GameStatus.VALID; i++) {
									ProjectCard card = (ProjectCard) model
											.getProjectDeck().drawFirst();

									if (card.getRoom()
											.getProject(
													card.getRoom().getCourse())
											.getProjectAmount() < 3) {
										card.getRoom()
												.getProject(
														card.getRoom()
																.getCourse())
												.setProjectAmount(3);
									} else {
										burnOut(new LinkedList<Room>(),
												card.getRoom(), card.getRoom()
														.getCourse());
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

	public LinkedList<ActionType> possibleActions() {
		LinkedList<ActionType> actions = new LinkedList<ActionType>();
		Boolean containsEvent = false, ContainsRoom = false;
		HashMap<Course, Integer> roomCards = new HashMap<>();
		for (int i = 0; i < 4; i++) {
			roomCards.put(model.getCourses()[i], 0);	
		}
		
		for (PlayerCard card : (LinkedList<PlayerCard>) model.getCurrentPlayer().getCards().getCardList()) {
			if (card.getClass() == EventCard.class) {
				containsEvent = true;
			} else {
				ContainsRoom = true;
				RoomCard rCard = (RoomCard) card;
				roomCards.put(rCard.getRoom().getCourse(), roomCards.get(rCard.getRoom().getCourse()) +1);
			}
		}
		
		Boolean canBuildTP = false;
		
		for (Course c : actions) {
			
		}
		
		if (containsEvent) {
			actions.add(ActionType.USE_CARD);
		}
		
		if (model.getCurrentPlayer().getCards().getCardList().con) {
			
		}
		if (actionPoints > 0) {
			
		} else {
			
		}
		
		return actions;
	}
	
	public void resolveEventCard(EventCard event) {
		switch (event.getEvent()) {
		case ENERGY_DRINK:

			break;
		case GRANT_IN_AID:

			break;
		case HOLIDAYS:

			break;
		case ON_DEMAND_TELEPORTATION:

			break;
		case TIMEOUT:

			break;
		default:
			break;
		}
	}
	
	private void resolvePlayerCard(PlayerCard card) {
		if (PartyCard.class == card.getClass()) {
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
		} else {
			model.getCurrentPlayer().getCards().addCard(card);
		}

		if (model.getCurrentPlayer().getCards().getCardList().size() > 7) {
			action = ActionType.DISCARD;
			// view.displayMessage("Please discard room cards or use event cards. You should not have more than 7 cards");
			// view.displayCards(model.getCurrentPlayer().getCards().getCardList())
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
			selectedPlayer.setPosition(model.getRooms().get(name));
			view.displayMovePawn(selectedPlayer.getRole(), model.shortestPath(selectedPlayer.getPosition().getName(), name));
			view.clean();
		}
	}

	@Override
	synchronized public void pawnClicked(Role player) {
		selectedReachableRooms = model.reachableRooms(player, actionPoints);
		view.displayReachableRooms(selectedReachableRooms);
	}

	@Override
	synchronized public void settingValidationButtonClicked() {
		if (phase == GamePhase.SETTING) {
			// check values and if they are OK : playerNames, UVsNames,
			// difficulty, playerRoles
			// model = new Model(//récupérer les données)
			// view.getPlayerNames()
			// view.getUVNames()
			// view.getDifficulty()
			// view.getRoles()
			phase = GamePhase.ACTION;
			this.notify();
			// otherwise
			// view.displayInvalidSetting();
		}
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
	public void shareKnowledgeButtonCLicked() {
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
				action = ActionType.CARD_USE;
			} else {
				view.displayMessage("No cards in your hands")
			}
		} else {
			view.displayMessage("No cards in your hands");
		}
	}
	
	@Override
	public void buildTPButtonClicked() {
		if (action != ActionType.NONE) {
			// view.clean();
			action = ActionType.NONE;
		}
	}

	@Override
	public void hackButtonCliked() {
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
						action = ActionType.HACKING;
						// view.displayCards(discardEvents);
					} else {
						// view.displayErrorMessage("There is no event card in the player discard pile.");
					}
				} else {
					action = ActionType.HACKING;

					// view.displayConfirmationMessage("Do you really want to use your extra event card ?");
				}
			}
		}
	}

	@Override
	public void ConfirmationButtonClicked() {
		if (phase == GamePhase.ACTION) {
			if (action == ActionType.HACKING) {
				resolveEventCard(model.getCurrentPlayer().getExtraEventCard());
				model.getCurrentPlayer().setExtraEventCard(null);
				// view.extraEventCardChanged(null);
				action = ActionType.NONE;
				// view.clean();
			}
		}
	}

	@Override
	public void cardClicked(Card card) {
		if (phase == GamePhase.ACTION) {
			if (action == ActionType.HACKING) {
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
			} else if (action == ActionType.CARD_USE) {
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
					reachablePlaces = reach;
					//view.displayReachablePlaces(reach);
				}
			}
		}
	}

	@Override
	public void YesButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void NoButtonCLicked() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void eventCardClicked(Event card) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roomCardClicked(String room) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void NoButtonClicked() {
		// TODO Auto-generated method stub
		
	}

}