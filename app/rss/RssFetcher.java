package rss;

import gov.sandia.cognition.text.term.vector.BagOfWordsTransform;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import models.Company;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.HttpClientUtils;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import data.Link;

public class RssFetcher
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger (RssFetcher.class);

	private final List<String> urls;
	private final HttpClient client;
	private final Company company;
	
	public RssFetcher(Company company, HttpClient client, List<String> urls)
	{
		this.client = client;
		this.urls = urls;
		this.company = company;
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

	public List<Link> getSimilarLinks (List<String> urls)
	{
		List<Link> results = new ArrayList<Link>();
		
		for(String url: urls)
		{
			String body = HttpClientUtils.extractArticle (client, url);
			BagOfWordsTransform transform = company.getTransform();
		}
		
		return results;
	}
}
