import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.google.gson.Gson;
 
public class TestGoogleSea {
 
	public static void main(String[] args) throws IOException, URISyntaxException {
 
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
		String query = "help to type a letter"; //br.readLine();
		String charset = "UTF-8";
		address.replaceAll("<b>", "");
		String title,link;
		URL url = new URL(address + URLEncoder.encode(query, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);
		GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
 
		int total = results.getResponseData().getResults().size();
		System.out.println("total: "+total);
		// Show title and URL of each results

		for(int i=0; i<=total-1; i++){
			
			title=results.getResponseData().getResults().get(i).getTitle();
			String a[]=title.split("<b>|</b>");
			title=title.replace("<b>","");
			title=title.replace("</b>","");
			link=results.getResponseData().getResults().get(i).getUrl();
			System.out.println("Title: " + title);
			System.out.println("URL: " + link + "\n");
			//Desktop.getDesktop().browse(new java.net.URI("www.google.com"));
		}
		/*
		JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JEditorPane editorPane = new JEditorPane();
	    editorPane.setPage(new URL("http://www.google.com"));
	    frame.add(new JScrollPane(editorPane));

	    frame.setSize(300, 200);
	    frame.setVisible(true);
		 */
	}
}
 