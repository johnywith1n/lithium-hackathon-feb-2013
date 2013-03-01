package models.forms;

import play.data.validation.Constraints;

public class SampleDocumentForm
{
	@Constraints.Required
	public String companyName;
	
	@Constraints.Required
	public String url;
}
