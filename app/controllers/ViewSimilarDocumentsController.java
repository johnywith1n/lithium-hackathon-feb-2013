package controllers;

import models.forms.SelectFetchProcessForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import rss.FetchCoordinator;
import views.html.selectFetch;
import views.html.showSimilarLinks;

public class ViewSimilarDocumentsController extends Controller
{
	public static Result selectView ()
	{
		return ok (selectFetch
				.render (Form.form (SelectFetchProcessForm.class)));
	}

	public static Result showSimilarLinks ()
	{
		Form<SelectFetchProcessForm> form = Form.form (
				SelectFetchProcessForm.class).bindFromRequest ();
		if (form.hasErrors ())
		{
			return badRequest (selectFetch.render (form));
		}
		else
		{
			String name = form.get ().name;
			flash ("success", "Sample Document created");
			return ok (showSimilarLinks.render (
					FetchCoordinator.getResult (name),
					FetchCoordinator.getCompanyName (name)));
		}
	}

}
