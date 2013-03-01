package data;

import gov.sandia.cognition.text.term.vector.BagOfWordsTransform;

import java.util.ArrayList;
import java.util.List;

import models.Company;
import models.SampleDocument;

import org.apache.http.client.HttpClient;
import org.springframework.util.SerializationUtils;

import utils.HttpClientUtils;

public class ProcessSampleDocuments
{
	private final HttpClient client;
	private final Company company;

	public ProcessSampleDocuments(HttpClient client, Company company)
	{
		this.client = client;
		this.company = company;
	}

	public List<Link> retrieveUrls (List<String> urls)
	{
		List<Link> links = new ArrayList<Link> ();
		for (String url : urls)
		{
			links.add (retrieveUrl (url));
		}
		return links;
	}

	public Link retrieveUrl (String url)
	{
		return new Link (url, HttpClientUtils.extractArticle (client, url));
	}

	public void processDocumentsAndSaveDocuments (List<String> urls)
	{
		List<Link> links = retrieveUrls (urls);
		DocumentVectorizer vectorizer = new DocumentVectorizer (links);
		BagOfWordsTransform transform = vectorizer.createTransform ();
		company.setBag_of_words_transform (SerializationUtils
				.serialize (transform));
		company.save ();

		for (Link link : links)
		{
			SampleDocument doc = new SampleDocument (company, link.getUrl (),
					link.getBody (), SerializationUtils.serialize (DocumentVectorizer
							.getTransformTextToVector (link.getBody (),
									transform)));
			doc.save ();
		}
	}
	
	public BagOfWordsTransform processDocuments (List<String> urls)
	{
		List<Link> links = retrieveUrls (urls);
		DocumentVectorizer vectorizer = new DocumentVectorizer (links);
		BagOfWordsTransform transform = vectorizer.createTransform ();
		company.setBag_of_words_transform (SerializationUtils
				.serialize (transform));
		company.save ();

		return transform;
	}
}
