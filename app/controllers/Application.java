package controllers;

import java.io.File;
import java.io.IOException;

import models.Company;

import org.apache.http.impl.client.DefaultHttpClient;

import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import data.ProcessSampleDocuments;

public class Application extends Controller
{

	public static Result index () throws IOException
	{
		Company company = Company.getCompany ("sephora");
		if (company == null)
			company = new Company ("sephora");
		ProcessSampleDocuments processor = new ProcessSampleDocuments (
				new DefaultHttpClient (), company);
		processor.processDocuments (Files.readLines (new File (
				"resources/sample_articles.txt"), Charsets.UTF_8));
		return ok ("foo");
	}

}
