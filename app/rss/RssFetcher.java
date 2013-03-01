package rss;

import gov.sandia.cognition.math.matrix.Vector;
import gov.sandia.cognition.text.term.vector.BagOfWordsTransform;
import gov.sandia.cognition.text.term.vector.CosineSimilarityFunction;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.Company;
import models.SampleDocument;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.HttpClientUtils;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import data.DocumentVectorizer;
import data.Link;

public class RssFetcher
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger (RssFetcher.class);

	private final HttpClient client;
	private final Company company;
	private final Set<SampleDocument> samples;
	private final CosineSimilarityFunction cosineSimilarityFunction;
	
	public RssFetcher(Company company, HttpClient client)
	{
		this.client = client;
		this.company = company;
		this.samples = company.getSampleDocs ();
		this.cosineSimilarityFunction = new CosineSimilarityFunction ();
	}

	@SuppressWarnings("unchecked")
	private List<String> getRssEntryUrls (List<String> urls)
	{
		List<String> entryUrls = new ArrayList<String> ();

		for (String url : urls)
		{
			URL inputUrl;
			try
			{
				inputUrl = new URL (url);
				SyndFeedInput input = new SyndFeedInput ();
				SyndFeed feed = input.build (new XmlReader (inputUrl));
				List<SyndEntry> entries = feed.getEntries ();
				for(SyndEntry entry: entries)
				{
					entryUrls.add (entry.getLink ());
				}
			}
			catch (MalformedURLException e)
			{
				LOGGER.error ("Malformed URL:" + url, e);
			}
			catch (FeedException e)
			{
				LOGGER.error ("Unable to process feed for url: " + url, e);
			}
			catch (IOException e)
			{
				LOGGER.error ("", e);
			}
		}

		return entryUrls;
	}

	private boolean isSimilarToSamples(Vector linkVector)
	{
		for(SampleDocument doc: samples)
		{
			if(cosineSimilarityFunction.evaluate (doc.getActualVector (), linkVector) > 0.5)
				return true;
		}
		return false;
	}
	
	/**
	 * @param urls The rss urls
	 * @return A list of links similar to the sample documents
	 */
	public List<Link> getSimilarLinks (List<String> rssUrls)
	{
		List<String> urls = getRssEntryUrls (rssUrls);
		List<Link> results = new ArrayList<Link>();
		
		for(String url: urls)
		{
			String body = HttpClientUtils.extractArticle (client, url);
			BagOfWordsTransform transform = company.getTransform();
			Vector linkVector = DocumentVectorizer.getTransformTextToVector (body, transform);
			if(isSimilarToSamples (linkVector))
				results.add (new Link(url, body));
		}
		
		return results;
	}
}
