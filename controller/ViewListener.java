package the_projects.controller;

import the_projects.model.PhDStudent;
import the_projects.model.Role;
import the_projects.model.card.Card;
import the_projects.model.card.Event;

public interface ViewListener {
	public void placeClicked(String name);
	public void pawnClicked(Role player);
	public void eventCardClicked(Event card);
	public void roomCardClicked(String room);
	
	public void settingValidationButtonClicked();
	public void removeProjectButtonClicked();
	public void shareKnowledgeButtonClicked();
	public void useCardButtonClicked();
	public void buildTPButtonClicked();
	public void hackButtonCliked();
	public void giveUpButtonClicked();	
	public void YesButtonClicked();
	public void NoButtonClicked();
	
	void endOfStageButtonClicked();
}
