package models.forms;

import play.data.validation.Constraints;

public class SelectFetchProcessForm
{
	@Constraints.Required
	public String name;
}
