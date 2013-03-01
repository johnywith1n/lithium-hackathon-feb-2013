package rss;

import java.util.List;

import models.Company;

public class RssFetcherCallableResult
{
	private final Company company;
	private final String name;
	private final List<SimilarityResult> result;

	public RssFetcherCallableResult(Company company, String name,
			List<SimilarityResult> result)
	{
		super ();
		this.company = company;
		this.name = name;
		this.result = result;
	}

	public Company getCompany ()
	{
		return company;
	}

	public String getName ()
	{
		return name;
	}

	public List<SimilarityResult> getResult ()
	{
		return result;
	}

}
