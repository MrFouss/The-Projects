package the_projects.controller;

import the_projects.model.Role;
import the_projects.model.card.Card;
import the_projects.model.card.Event;

public interface ViewListener {
	void placeClicked(String name);
	void pawnClicked(Role player);
	void eventCardClicked(Event card);
	void roomCardClicked(String room);
	
	void settingValidationButtonClicked();

	void removeProjectButtonClicked();
	void moveButtonClicked();
	void shareKnowledgeButtonClicked();
	void useCardButtonClicked();
	void masterButtonClicked();
	void buildTPButtonClicked();
	void hackButtonClicked();
	void giveUpButtonClicked();
	void YesButtonClicked();
	void NoButtonClicked();

	void cardToCenterFinished();
	void cardToDeckFinished();
	void cleared();

	void endOfStageButtonClicked();


}
