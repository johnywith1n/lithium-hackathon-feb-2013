package models.forms;

import play.data.validation.Constraints;

public class FetchRssFeedForm
{
	@Constraints.Required
	public String companyName;
	
	@Constraints.Required
	public String name;
}
