package rss;

import java.util.Map;

import models.SampleDocument;
import data.Link;

public class SimilarityResult
{
	private final Map<SampleDocument, Double> similarityScores;
	private final Link link;

	public SimilarityResult(Map<SampleDocument, Double> similarityScores, Link link)
	{
		this.similarityScores = similarityScores;
		this.link = link;
	}

	public Link getLink ()
	{
		return link;
	}

	public Map<SampleDocument, Double> getSimilarityScores ()
	{
		return similarityScores;
	}
	
}
