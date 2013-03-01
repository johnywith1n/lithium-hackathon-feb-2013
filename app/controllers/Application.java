package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import models.Company;
import models.SampleDocument;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import play.mvc.Controller;
import play.mvc.Result;
import rss.RssFetcher;
import rss.SimilarityResult;
import views.html.index;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import data.ProcessSampleDocuments;

public class Application extends Controller
{

	public static Result index () throws IOException
	{
		return ok (index.render ("Hello"));
	}
	
	private static void testPipeline() throws IOException
	{
		Company company = Company.getCompany ("sephora");
		if (company == null)
			company = new Company ("sephora");
		HttpClient client = new DefaultHttpClient ();
		System.out.println("Processing samples");
		ProcessSampleDocuments processor = new ProcessSampleDocuments (
				client, company);
		processor.processDocuments (Files.readLines (new File (
				"resources/sample_articles.txt"), Charsets.UTF_8));
		System.out.println("Processing Rss feeds");
		RssFetcher fetcher = new RssFetcher (company, client);
		List<SimilarityResult> results = fetcher.getSimilarLinks (Files.readLines (new File("resources/rss_feeds.txt"), Charsets.UTF_8));
		BufferedWriter writer = Files.newWriter (new File("out.txt"), Charsets.UTF_8);
		for(SimilarityResult result: results)
		{
			writer.write (result.getLink ().getUrl ());
			writer.newLine ();
			Map<SampleDocument, Double> map =result.getSimilarityScores ();
			for(SampleDocument doc: map.keySet ())
			{
				writer.write (doc.toString () + " "+ map.get (doc));
				writer.newLine ();
			}
		}
		writer.close ();
	}

}
