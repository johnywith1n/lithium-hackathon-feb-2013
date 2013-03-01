package data;

import gov.sandia.cognition.text.term.vector.BagOfWordsTransform;

import java.util.ArrayList;
import java.util.List;

import models.Company;
import models.SampleDocument;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import utils.HttpClientUtils;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class ProcessSampleDocuments
{
	private final HttpClient client;
	private final Company company;
	private static final Logger LOGGER = LoggerFactory
			.getLogger (ProcessSampleDocuments.class);

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
		return new Link (url, extractArticle (url));
	}

	public void processDocuments (List<String> urls)
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
					link.getBody (), SerializationUtils.serialize (vectorizer
							.getTransformTextToVector (link.getBody (),
									transform)));
			doc.save ();
		}
	}

	public String extractArticle (String url)
	{
		ArticleExtractor extractor = new ArticleExtractor ();
		try
		{
			return extractor.getText (HttpClientUtils.getResponseAsInputSource (
					client, url));
		}
		catch (BoilerpipeProcessingException e)
		{
			LOGGER.error ("", e);
		}
		return "";
	}

}
