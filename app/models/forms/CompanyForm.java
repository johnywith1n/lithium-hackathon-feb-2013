package models.forms;

import models.Company;
import play.data.validation.Constraints;

public class CompanyForm
{
	@Constraints.Required
	public String name;
	
	public String validate ()
	{
		if (Company.getCompany (name) != null)
			return "A company already exists with this name";
		return null;
	}

}
