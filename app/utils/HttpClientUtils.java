package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class HttpClientUtils
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger (HttpClientUtils.class);

	public static InputStream getResponse (HttpClient client, String url)
	{
		HttpParams params = client.getParams ();
		HttpConnectionParams.setConnectionTimeout (params, 0);
		HttpConnectionParams.setSoTimeout (params, 0);
		HttpGet request = new HttpGet (url);

		HttpResponse response;
		try
		{
			response = client.execute (request);
			return response.getEntity ().getContent ();
		}
		catch (IOException e)
		{
			LOGGER.error ("Unable to execute http request", e);
		}
		return null;
	}

	public static InputSource getResponseAsInputSource (HttpClient client,
			String url)
	{
		return new InputSource (getResponse (client, url));
	}

	public static String getResponseAsString (HttpClient client, String url)
	{
		StringBuilder builder = new StringBuilder ();
		BufferedReader rd;

		try
		{
			rd = new BufferedReader (new InputStreamReader (getResponse (
					client, url)));
			String line = "";
			while ((line = rd.readLine ()) != null)
			{
				builder.append (line);
			}
		}
		catch (IllegalStateException e)
		{

		}
		catch (IOException e)
		{
			LOGGER.error ("", e);
		}

		return builder.toString ();
	}
	
	public static String extractArticle (HttpClient client, String url)
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
