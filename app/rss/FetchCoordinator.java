package rss;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import models.Company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class FetchCoordinator
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger (FetchCoordinator.class);

	private static final ListeningExecutorService service = MoreExecutors
			.listeningDecorator (Executors.newFixedThreadPool (1));

	private static final Set<String> runningFetchers = new HashSet<String> ();

	private static final Map<String, Company> doneProcessToCompanyMap = new HashMap<String, Company> ();
	private static final Map<String, List<SimilarityResult>> results = new HashMap<String, List<SimilarityResult>> ();

	public synchronized static void startRssFetchInCallable (String name,
			Company company)
	{
		RssFetcherCallable callable = new RssFetcherCallable (company, name);
		ListenableFuture<RssFetcherCallableResult> future = service
				.submit (callable);
		Futures.addCallback (future,
				new FutureCallback<RssFetcherCallableResult> ()
				{
					public void onSuccess (RssFetcherCallableResult result)
					{
						callback (result.getName (), result.getCompany (),
								result.getResult ());
					}

					public void onFailure (Throwable thrown)
					{
						LOGGER.error ("Error on future callback", thrown);
					}
				});
		runningFetchers.add (name);
	}

	public synchronized static void callback (String name, Company company,
			List<SimilarityResult> result)
	{
		doneProcessToCompanyMap.put (name, company);
		results.put (name, result);
		runningFetchers.remove (name);
	}

	public static synchronized boolean hasNamedProcess (String name)
	{
		return runningFetchers.contains (name);
	}

	public static synchronized Set<String> getRunningProcesses ()
	{
		return new HashSet<String> (runningFetchers);
	}
	
	public static synchronized Set<String> getFinishedProcesses ()
	{
		return doneProcessToCompanyMap.keySet ();
	}
}
