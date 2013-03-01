package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializationUtils
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger (SerializationUtils.class);
	
	public static byte[] serialize (Object o)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream ();
		ObjectOutput out = null;
		try
		{
			out = new ObjectOutputStream (bos);
			out.writeObject (o);
			return bos.toByteArray ();
		}
		catch (IOException e)
		{
			LOGGER.error ("Unable to serialize object.", e);
		}
		finally
		{
			try
			{
				out.close ();
				bos.close ();
			}
			catch (IOException e)
			{
				LOGGER.error ("Unable to close output streams.", e);
			}
		}
		return null;
	}

	public static Object deserialize (byte[] bytes)
	{
		ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
		ObjectInput in = null;
		try
		{
			in = new ObjectInputStream (bis);
			return in.readObject ();
		}
		catch (IOException e)
		{
			LOGGER.error ("Unable to deserialize object.", e);
		}
		catch (ClassNotFoundException e)
		{
			LOGGER.error ("Unable to find class for object.", e);
		}
		finally
		{
			try
			{
				bis.close ();
				in.close ();

			}
			catch (IOException e)
			{
				LOGGER.error ("Unable to close input streams.", e);
			}
		}
		return null;
	}
}
