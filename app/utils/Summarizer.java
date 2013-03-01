package utils;

import net.sf.classifier4J.summariser.SimpleSummariser;

public class Summarizer
{
	public static final SimpleSummariser summarizer = new SimpleSummariser ();

	public static String summarize (String text)
	{
		return summarizer.summarise (text, 6);
	}
}
