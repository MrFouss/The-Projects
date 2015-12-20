package the_projects.model.card;

import java.util.*;

/**
 * Deck of cards that can be used as a real deck or as a discard pile.
 */
public class CardDeck <T extends Card>{

    /**
     * List containing all cards of the deck.
     */
    private LinkedList cardList;

    /**
     * Random object used to shuffle the deck.
     */
    // TODO add to the class diagram
    private Random rnd;

    /**
     * Default constructor that initializes all attributes.
     */
    public CardDeck() {
        this.cardList = new LinkedList<T>();
        this.rnd = new Random();
    }

    /**
     * Shuffles the whole deck.
     */
    public void shuffle() {
        LinkedList<T> newList = new LinkedList<>();

        while(this.cardList.size() > 0) {
            T tmpCard = (T) this.cardList.remove(this.rnd.nextInt(this.cardList.size()));
            newList.add(tmpCard);
        }

        this.cardList = newList;
    }

    /**
     * Draws the first card of the deck.
     *
     * @return the first card of the deck.
     */
    public T drawFirst() throws NoSuchElementException {
        try {
            return (T) this.cardList.removeFirst();
        } catch(NoSuchElementException e) {
            throw new NoSuchElementException();
        }
    }

    /**
     * Draws the last card of the deck.
     *
     * @return the last card of the deck.
     */
    public T drawLast() throws NoSuchElementException {
        try {
            return (T) this.cardList.removeLast();
        } catch(NoSuchElementException e) {
            throw new NoSuchElementException();
        }
    }

    /**
     * Draws the card at the specified index in the deck.
     *
     * @param index integer used to find the card.
     * @return the card at the specified index.
     */
    public T draw(int index) {
        return (T) this.cardList.remove(index);
    }

    /**
     * Adds a card at the top of the deck.
     *
     * @param card the card to add on the deck.
     */
    public void addCard(T card) {
        this.cardList.addFirst(card);
    }

    /**
     * Adds all cards of another flushed deck on top of this deck.
     *
     * @param deck the deck to flush and empty.
     */
    public void addCardsOnTop(CardDeck<T> deck) {
        deck.shuffle();
        this.cardList.addAll(0, deck.getCardList());
    }

    /**
     * Gets the LinkedList object containing all the cards of the deck.
     *
     * @return the LinkedList cardList.
     */
    public LinkedList<T> getCardList() {
        return this.cardList;
    }

}