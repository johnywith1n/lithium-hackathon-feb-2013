package controllers;

import models.Company;
import models.RssFeed;
import models.forms.RssFeedForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.addRssFeed;

public class RssFeedController extends Controller
{
	public static Result addRssFeed ()
	{
		return ok (addRssFeed.render (Form.form (RssFeedForm.class)));
	}

	public static Result processRssFeed ()
	{
		Form<RssFeedForm> form = Form.form (RssFeedForm.class)
				.bindFromRequest ();
		if (form.hasErrors ())
		{
			return badRequest (addRssFeed.render (form));
		}
		else
		{
			RssFeedForm feedForm = form.get ();
			(new RssFeed(Company.getCompany (feedForm.companyName), feedForm.url)).save ();
			flash ("success", "Rss Feed Added.");
			return ok (addRssFeed.render (Form
					.form (RssFeedForm.class)));
		}
	}
}

