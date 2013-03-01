package controllers;

import gov.sandia.cognition.text.term.vector.BagOfWordsTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.Company;
import models.SampleDocument;
import models.forms.SampleDocumentForm;

import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.util.SerializationUtils;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.HttpClientUtils;
import views.html.addSampleDocument;
import data.DocumentVectorizer;
import data.Link;

public class SampleDocumentController extends Controller
{
	public static Result addSampleDoc ()
	{
		return ok (addSampleDocument.render (Form
				.form (SampleDocumentForm.class)));
	}

	public static Result processSampleDoc ()
	{
		Form<SampleDocumentForm> form = Form.form (SampleDocumentForm.class)
				.bindFromRequest ();
		SampleDocumentForm docForm = form.get ();
		if (form.hasErrors ())
		{
			return badRequest (addSampleDocument.render (form));
		}
		else
		{
			processSampleDocument (docForm.companyName, docForm.url);
			flash ("success", "Sample Document created");
			return ok (addSampleDocument.render (Form
					.form (SampleDocumentForm.class)));
		}
	}

	private static List<Link> getLinks(Set<SampleDocument> docs, String newUrl, String newBody)
	{
		List<Link> links = new ArrayList<Link>();
		for (SampleDocument doc : docs)
		{
			links.add (new Link(doc.getUrl (), doc.getBody ()));
		}
		
		links.add (new Link(newUrl, newBody));
		
		return links;
	}
	
	private static void processSampleDocument (String companyName,
			final String url)
	{
		Company company = Company.getCompany (companyName);
		Set<SampleDocument> docs = company.getSampleDocs ();

		String newBody = HttpClientUtils.extractArticle (new DefaultHttpClient (), url);
		List<Link> links = getLinks(docs, url, newBody);
		
		BagOfWordsTransform transform = (new DocumentVectorizer (links)).createTransform ();
		Company.setTransform (company, SerializationUtils.serialize (transform));
		revectorizeDocuments(docs, transform);

		SampleDocument newDoc = new SampleDocument (company, url, newBody,
				SerializationUtils.serialize (DocumentVectorizer
						.getTransformTextToVector (newBody, transform)));
		newDoc.save ();
	}
	
	private static void revectorizeDocuments(Set<SampleDocument> docs, BagOfWordsTransform transform)
	{
		for (SampleDocument doc : docs)
		{
			doc.setVector (SerializationUtils.serialize (DocumentVectorizer
					.getTransformTextToVector (doc.getBody (), transform)));
		}
	}
}
