package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.mvc.*;
import services.Counter;
import twitter.Twitter;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import views.html.*;
import views.*;

import twitter4j.Query;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
	
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    /**
     * 
     * @author Shruthi Ramamurthy
     * @param
     * 
     */
    public Result Search() {
    		
        return ok(mainview.render("Tweet Analytics")); 
       
    } 
    
    public Result SearchResults(String keys) throws TwitterException , InterruptedException ,ExecutionException{
    
    	List<String> links_temp = new ArrayList<String>();
    	List<String> result_temp = new ArrayList<String>();
    	
    	List<String> links = new ArrayList<String>();
    	List<String> result = new ArrayList<String>();
    	
    	
    	for (String key : keys.split(" ")){
    		
    		links = get_links(key , links) ;
    		result = get_results(key , result) ;
    		Thread.sleep(1000L);
    	}
    	    	
        return ok(viewtweets.render(result,links)); 
    }
    
  public Result profile(String name) throws TwitterException ,InterruptedException ,ExecutionException{    	
    	
    	String Profile = getProfile(name);
    	List<String> Tweets = getDetails(name);
    	Thread.sleep(1000L);
		return ok(profile.render(Profile, Tweets));
	}

    public String getProfile (String Key)throws TwitterException, InterruptedException  {
    	Twitter tweet = new Twitter(Key);
    	
    	CompletableFuture<String> profileInfo = (CompletableFuture<String>) tweet.getProfile();
    	
    	
		try {
			return profileInfo.get() ;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		return null;
    }
    
    public List<String> getDetails (String Key)throws TwitterException, InterruptedException  {
    	
    	Twitter tweet = new Twitter(Key);
    	CompletableFuture<List<Status>> FutureList = (CompletableFuture<List<Status>>) tweet.getDetails();
    	List<String> res = new ArrayList<>();
    	
    	FutureList.thenAccept(s ->  s.stream()
    			                    .map(f -> f.getText())
    			                    .limit(10)
    			                    .forEach(res::add));
    	return res;
    	
     }
    
    
    public List<String> get_results(String key , List<String> a) throws TwitterException, InterruptedException  {
    	
		Twitter tweet = new Twitter(key);
		
		CompletableFuture<QueryResult> FutureResult = (CompletableFuture<QueryResult>) tweet.getTweets();
		List<String> res = new ArrayList<>();
			
			        FutureResult.thenAccept(r -> r.getTweets()
					.stream()
					.map(d -> "\t" + d.getText())
					.limit(10)
					.forEach(a::add));
		
	     return a;
    }
    
	public List<String> get_links(String key , List<String> a) throws TwitterException, InterruptedException, ExecutionException  {
    	
    	Twitter tweet = new Twitter(key);
		List<String> res =    new ArrayList<>();
		
		CompletableFuture<QueryResult> FutureLinks =  (CompletableFuture<QueryResult>) tweet.getTweets();

				FutureLinks.thenAccept( r -> r.getTweets()
						.stream()
		  				.map(d -> "@" + d.getUser().getScreenName())
		  				.limit(10)
		  				.forEach(a::add));
		
		return a;
		
    }
}
