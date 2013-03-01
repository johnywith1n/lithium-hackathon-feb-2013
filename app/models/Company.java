package models;

import gov.sandia.cognition.text.term.vector.BagOfWordsTransform;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.springframework.util.SerializationUtils;

import play.db.ebean.Model;

@Entity
public class Company extends Model
{
	private static final long serialVersionUID = -5970073174809095241L;

	@Id
	@GeneratedValue
	@Column(nullable = false)
	private Long uid;

	@Column(nullable = false)
	private String name;

	@Lob
	private byte[] bag_of_words_transform;

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
	private Set<SampleDocument> sampleDocs;
	
	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
	private Set<RssFeed> rssFeed;

	private static Model.Finder<String, Company> find = new Model.Finder<String, Company> (
			String.class, Company.class);

	public Company(String name)
	{
		this.name = name;
	}

	public Long getUid ()
	{
		return uid;
	}

	public String getName ()
	{
		return name;
	}

	public byte[] getBag_of_words_transform ()
	{
		return bag_of_words_transform;
	}

	public BagOfWordsTransform getTransform ()
	{
		return (BagOfWordsTransform) SerializationUtils
				.deserialize (getBag_of_words_transform ());
	}

	public void setBag_of_words_transform (byte[] bag_of_words_transform)
	{
		this.bag_of_words_transform = bag_of_words_transform;
	}

	public Set<SampleDocument> getSampleDocs ()
	{
		return sampleDocs;
	}

	public Set<RssFeed> getRssFeed ()
	{
		return rssFeed;
	}

	public static Company getCompany (String name)
	{
		return find.where ().eq ("name", name).findUnique ();
	}

	public static void setTransform (Company company, byte[] transform)
	{
		company.setBag_of_words_transform (transform);
		company.save ();
	}
}
