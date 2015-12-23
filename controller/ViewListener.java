package the_projects.controller;

import the_projects.model.PhDStudent;

public interface ViewListener {
	public void placeClicked(String name);
	public void pawnCLicked(PhDStudent player);
	
	public void settingValidationButtonCLicked();
	public void removeProjectButtonClicked();
	public void shareKnowledgeButtonCLicked();
	public void useCardButtonClicked();
	public void useCardsButtonCliked();
	public void buildTPButtonClicked();
	public void hackButtonCliked();
}
