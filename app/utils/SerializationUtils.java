package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class SerializationUtils
{
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
			e.printStackTrace ();
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
				e.printStackTrace ();
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
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace ();
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
				// TODO Auto-generated catch block
				e.printStackTrace ();
			}
		}
		return null;
	}
}
