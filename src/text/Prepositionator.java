package text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Random;


public class Prepositionator
{

	LinkedList<String>	dativ;
	LinkedList<String>	genitiv;
	LinkedList<String>	akkdat;
	LinkedList<String>	akkusativ;


	public Prepositionator()
	{
		genitiv = loadLines("data/prepositionator/GEN.txt");
		akkusativ = loadLines("data/prepositionator/AKK.txt");
		dativ = loadLines("data/prepositionator/DAT.txt");
		akkdat = loadLines("data/prepositionator/AKKDAT.txt");

	}


	private LinkedList<String> loadLines(String _filepath)
	{
		LinkedList<String> lines = null;
		BufferedReader reader = null;
		try {
			lines = new LinkedList<String>();
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(_filepath), "UTF8"));
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}


	public String dativ_only()
	{
		return getRandomWord(dativ);
	}


	public String akkusativ_only()
	{
		return getRandomWord(akkusativ);
	}


	public String genitiv_only()
	{
		return getRandomWord(genitiv);
	}


	public String akkusativ_dativ()
	{
		return getRandomWord(akkdat);
	}


	public String akkusativ_all()
	{
		LinkedList<String> newlist = new LinkedList<String>();
		newlist.addAll(akkusativ);
		newlist.addAll(akkdat);
		return getRandomWord(newlist);
	}


	public String dativ_all()
	{
		LinkedList<String> newlist = new LinkedList<String>();
		newlist.addAll(dativ);
		newlist.addAll(akkdat);
		return getRandomWord(newlist);
	}


	private String getRandomWord(LinkedList<String> words)
	{
		Random generator = new Random();
		Object[] values = words.toArray();
		String randomString = (String) values[generator.nextInt(values.length)];
		return randomString;
	}
}
