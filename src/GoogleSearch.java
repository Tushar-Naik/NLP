import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import com.google.gson.Gson;

public class GoogleSearch {

	String address,query;
	String charset = "UTF-8";
	String title,link;
	URL url;
	Reader reader;
	GoogleResults results;
	int total;
	public GoogleSearch(String q) throws Exception,URISyntaxException
	{
		address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start=6&q=";
		//address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
		query = q;
		charset = "UTF-8";
		address.replaceAll("<b>", "");
		url = new URL(address + URLEncoder.encode(query, charset));
		reader = new InputStreamReader(url.openStream(), charset);
		results = new Gson().fromJson(reader, GoogleResults.class);
		try
		{
			total = results.getResponseData().getResults().size();
		}
		catch(Exception e)
		{
			System.out.println("NULL POINTER"+e.getMessage());
			throw e;
		}
	}	
}

class GoogleResults{
 
    private ResponseData responseData;
    public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
    public String toString() { return "ResponseData[" + responseData + "]"; }
 
    static class ResponseData {
        private List<Result> results;
        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; }
        public String toString() { return "Results[" + results + "]"; }
    }
 
    static class Result {
        private String url;
        private String title;
        public String getUrl() { return url; }
        public String getTitle() { return title; }
        public void setUrl(String url) { this.url = url; }
        public void setTitle(String title) { this.title = title; }
        public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
    }
}
