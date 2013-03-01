package data;

public class Link
{
	private final String url;
	private final String body;

	public Link(String url, String body)
	{
		this.url = url;
		this.body = body;
	}

	public String getUrl ()
	{
		return url;
	}

	public String getBody ()
	{
		return body;
	}

}
