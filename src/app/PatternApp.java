package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import processing.core.PApplet;
import text.Analyzer;
import text.ValuableWord;


public class PatternApp extends PApplet
{
	private Analyzer			analyzer;

	HashMap<String, String>		theTexts	= new HashMap<String, String>();
	LinkedList<ValuableWord>	theWords	= new LinkedList<ValuableWord>();


	public void setup()
	{
		loadTexts();
		analyzer = new Analyzer();

		for (Entry<String, String> entry : theTexts.entrySet()) {
			theWords = analyzer.analyzedSentence(entry.getValue());
		}

		for (int i = 0; i < theWords.size(); i++) {
			ValuableWord vw = theWords.get(i);

			println(vw.text);
		}

	}


	public void draw()
	{

	}


	void loadTexts()
	{
		File folder = new File("data/tests/");
		println(folder.getAbsolutePath());

		File[] abstracts = folder.listFiles();
		// println(folder.listFiles());

		for (File f : abstracts) {
			try {
				if (!f.isHidden()) {
					ArrayList<String> lines = new ArrayList<String>();

					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f.getPath()),
							"UTF8"));
					String line = reader.readLine();
					while (line != null) {
						lines.add(line);
						line = reader.readLine();
					}
					String theAbstract = "";
					for (String s : lines) {
						theAbstract += s + " ";
					}
					theTexts.put(f.getName(), theAbstract);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public static void main(String[] args)
	{
		PApplet.main(new String[] { "app.PatternApp" });
	}
}
