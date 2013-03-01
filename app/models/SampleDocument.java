package models;

import gov.sandia.cognition.math.matrix.Vector;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.springframework.util.SerializationUtils;

import play.db.ebean.Model;

@Entity
public class SampleDocument extends Model
{
	private static final long serialVersionUID = 3799034836873937381L;

	@Id
	@GeneratedValue
	@Column(nullable = false)
	private Long uid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_uid", nullable = false)
	private Company company;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String url;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String body;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] vector;

	public SampleDocument(Company company, String url, String body,
			byte[] vector)
	{
		this.company = company;
		this.url = url;
		this.body = body;
		this.vector = vector;
	}

	public Long getUid ()
	{
		return uid;
	}

	public Company getCompany ()
	{
		return company;
	}

	public String getUrl ()
	{
		return url;
	}

	public String getBody ()
	{
		return body;
	}

	public byte[] getVector ()
	{
		return vector;
	}

	public Vector getActualVector ()
	{
		return (Vector) SerializationUtils.deserialize (getVector ());
	}

}
