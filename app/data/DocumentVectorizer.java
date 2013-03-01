package data;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import gov.sandia.cognition.math.matrix.Vector;
import gov.sandia.cognition.text.term.DefaultTerm;
import gov.sandia.cognition.text.term.DefaultTermCounts;
import gov.sandia.cognition.text.term.DefaultTermIndex;
import gov.sandia.cognition.text.term.DefaultTermOccurrence;
import gov.sandia.cognition.text.term.Term;
import gov.sandia.cognition.text.term.TermIndex;
import gov.sandia.cognition.text.term.TermOccurrence;
import gov.sandia.cognition.text.term.filter.DefaultStopList;
import gov.sandia.cognition.text.term.filter.StopListFilter;
import gov.sandia.cognition.text.term.vector.BagOfWordsTransform;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class DocumentVectorizer
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger (DocumentVectorizer.class);
	private static final String stopwordsListFile = "resources/stopwords.txt";

	private final List<Link> links;
	private final int minUnigramCount;
	private final StopListFilter stopListFilter;

	public DocumentVectorizer(List<Link> links)
	{
		this.links = links;
		this.minUnigramCount = 20;
		this.stopListFilter = getStopListFilter ();
	}

	private StopListFilter getStopListFilter ()
	{
		StopListFilter filter;
		try
		{
			filter = new StopListFilter (
					DefaultStopList.loadFromText (new File (stopwordsListFile)));
		}
		catch (IOException e)
		{
			LOGGER.error ("Unable to load stop words.", e);
			filter = new StopListFilter (new DefaultStopList ());
		}
		return filter;
	}

	private Iterable<TermOccurrence> transformWordsToTermOccurrences (
			Iterable<Word> input)
	{
		return Iterables.transform (input,
				new Function<Word, TermOccurrence> ()
				{
					@Override
					public TermOccurrence apply (Word input)
					{
						TermOccurrence output = new DefaultTermOccurrence (
								new DefaultTerm (input.word ()), 0, 0);
						return output;
					}
				});
	}

	private boolean keepWord (String word)
	{
		return word.length () > 1 && !word.startsWith ("http")
				&& !word.startsWith ("www") && !word.startsWith ("href")
				&& !word.startsWith ("<");
	}

	private List<Word> getTokenization (String text)
	{
		List<Word> result = new ArrayList<Word> ();
		PTBTokenizer<Word> ptbt = new PTBTokenizer<Word> (new StringReader (
				text), new WordTokenFactory (), "untokenizable=noneDelete");
		while (ptbt.hasNext ())
		{
			Word w = ptbt.next ();
			w.setWord (w.word ().toLowerCase ());
			result.add (w);
		}
		return result;
	}

	private Iterable<TermOccurrence> filter (List<Word> words)
	{
		return this.stopListFilter
				.filterTerms (transformWordsToTermOccurrences (words));
	}

	private void addToTermCounts (Iterable<TermOccurrence> terms,
			DefaultTermCounts counts)
	{
		for (TermOccurrence term : terms)
		{
			Term t = term.asTerm ();
			String word = t.getName ();
			if (keepWord (word))
				counts.add (t);
		}
	}

	private void addTextToTermIndex (TermIndex index, DefaultTermCounts counts)
	{
		for (Term term : counts.getTerms ())
		{
			int count = counts.getCount (term);
			if (count > minUnigramCount)
				index.add (term);
		}
	}

	private TermIndex indexTerms ()
	{
		DefaultTermCounts counts = new DefaultTermCounts ();

		for (Link link : links)
		{
			addToTermCounts (filter (getTokenization (link.getBody ())), counts);
		}

		TermIndex index = new DefaultTermIndex ();
		addTextToTermIndex (index, counts);

		return index;
	}

	public BagOfWordsTransform createTransform ()
	{
		return new BagOfWordsTransform (indexTerms ());
	}

	public Vector getTransformTextToVector (String text,
			BagOfWordsTransform transform)
	{
		List<Term> terms = new ArrayList<Term> ();
		for (TermOccurrence t : filter (getTokenization (text)))
		{
			Term term = t.asTerm ();
			if (keepWord (term.getName ()))
				terms.add (term);
		}

		return transform.convertToVector (terms);
	}
}
