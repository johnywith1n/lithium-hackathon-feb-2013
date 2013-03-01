package models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
@Entity
public class RssFeed  extends Model
{
	private static final long serialVersionUID = 8928006143874915647L;

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

	public RssFeed(Company company, String url)
	{
		this.company = company;
		this.url = url;
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

	
}
