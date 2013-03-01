package controllers;

import models.Company;
import models.forms.CompanyForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.addCompany;

public class CompanyController extends Controller
{
	public static Result addCompany ()
	{
		return ok (addCompany.render (Form.form (CompanyForm.class)));
	}

	public static Result processCompany ()
	{
		Form<CompanyForm> form = Form.form (CompanyForm.class).bindFromRequest ();
		if (form.hasErrors ())
		{
			return badRequest (addCompany.render (form));
		}
		else
		{
			(new Company(form.get ().name)).save ();
			flash ("success", "Company created");
			return ok (addCompany.render (Form.form (CompanyForm.class)));
		}
	}
}
