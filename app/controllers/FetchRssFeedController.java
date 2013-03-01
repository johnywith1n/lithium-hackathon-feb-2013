package controllers;

import models.Company;
import models.forms.FetchRssFeedForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import rss.FetchCoordinator;
import views.html.fetchRssFeed;

public class FetchRssFeedController extends Controller
{
	public static Result createFetch ()
	{
		return ok (fetchRssFeed.render (Form.form (FetchRssFeedForm.class),
				FetchCoordinator.getRunningProcesses ()));
	}

	public static Result runFetch ()
	{
		Form<FetchRssFeedForm> form = Form.form (FetchRssFeedForm.class)
				.bindFromRequest ();
		FetchRssFeedForm feedForm = form.get ();
		if (form.hasErrors ())
		{
			return badRequest (fetchRssFeed.render (form,
					FetchCoordinator.getRunningProcesses ()));
		}
		else if (FetchCoordinator.hasNamedProcess (feedForm.name))
		{
			flash ("error",
					"Please pick a different name, there is already a fetch in progress with that name.");
			return badRequest (fetchRssFeed.render (form,
					FetchCoordinator.getRunningProcesses ()));
		}
		else
		{
			FetchCoordinator.startRssFetchInCallable (feedForm.name,
					Company.getCompany (feedForm.companyName));
			flash ("success", "Fetch in progress.");
			return ok (fetchRssFeed.render (Form.form (FetchRssFeedForm.class),
					FetchCoordinator.getRunningProcesses ()));
		}
	}
}
