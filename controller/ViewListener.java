package the_projects.controller;

import the_projects.model.Role;

public interface ViewListener {
	void placeClicked(String name);
	void pawnCLicked(Role player);
	
	void settingValidationButtonCLicked();
	void moveButtonClicked();
	void removeProjectButtonClicked();
	void shareKnowledgeButtonCLicked();
	void useCardButtonClicked();
	void masterButtonCliked();
	void buildTPButtonClicked();
	void hackButtonCliked();
	void endOfStageButtonClicked();
}
