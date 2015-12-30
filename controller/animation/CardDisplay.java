package the_projects.controller.animation;

import java.util.ArrayList;
import java.util.LinkedList;

import the_projects.model.Course;
import the_projects.model.Model;
import the_projects.model.card.Card;
import the_projects.model.card.Event;
import the_projects.model.card.EventCard;
import the_projects.model.card.PartyCard;
import the_projects.model.card.ProjectCard;
import the_projects.model.card.RoomCard;
import the_projects.view.View;
import the_projects.view.cards.Owner;

public class CardDisplay implements Animation {
	private Owner src, dest;
	private LinkedList<Card> cards;

	public CardDisplay(Owner src, Owner dst, LinkedList<Card> cards) {
		this.src = src;
		this.dest = dst;
		this.cards = cards;
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
	
	@Override
	public void run(View view) {
		ArrayList<Event> events = new ArrayList<Event>(eventCardsToEvents(getEventCards(cards)));
		ArrayList<String> projs = new ArrayList<String>(projectCardsToStrings(getProjectCards(cards)));
		ArrayList<String> rooms = new ArrayList<String>(roomCardsToStrings(getRoomCards(cards)));
		int parties = getPartyCards(cards);
		
		//TODO remove
		rooms.addAll(projs);
		
		view.displayDrawCards(src, dest, false, rooms, events, parties);
	}
}