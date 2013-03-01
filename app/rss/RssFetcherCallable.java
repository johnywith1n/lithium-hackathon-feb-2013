package rss;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import models.Company;
import models.RssFeed;

import org.apache.http.impl.client.DefaultHttpClient;

public class RssFetcherCallable implements Callable<RssFetcherCallableResult>
{
	private final RssFetcher fetcher;
	private final Company company;
	private final String processName;

	public RssFetcherCallable(Company company,String processName)
	{
		this.fetcher = new RssFetcher (company, new DefaultHttpClient ());
		this.company = company;
		this.processName = processName;
	}

	@Override
	public RssFetcherCallableResult call () throws Exception
	{
		List<SimilarityResult> result = fetcher.getSimilarLinks (getUrls (company.getRssFeed ()));
		return new RssFetcherCallableResult(company, processName, result);
	}

	private List<String> getUrls (Set<RssFeed> feeds)
	{
		List<String> result = new ArrayList<String> ();

		for (RssFeed feed : feeds)
		{
			result.add (feed.getUrl ());
		}

		return result;
	}
		
}
