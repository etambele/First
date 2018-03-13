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
 * 
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 * 
 * @author Emmanuel Ambele, Shruthi Ramamurthy
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     * 
     * @author Emmanuel Ambele, Shruthi Ramamurthy
     */
	
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
  
    /**
	 * This Search() renders the mainview.html
	 * Contains the text Tweet Analytics displayed on the homepage
	 * @return 
	 * @throws
	 * @author Emmanuel Ambele, Shruthi Ramamurthy
	 */
    public Result Search() {
    		
        return ok(mainview.render("Tweet Analytics")); 
       
    } 
    
    /**
	 * This SearchResults() renders the viewtweets.html
	 * contains logic for splitting the search keyword and searching for tweets related to each keyword using the get_links and 
	 * get_results
	 * @param keys
	 * @return 
	 * @throws TwitterException, TnterruptedException, ExecutionException
	 * @author Emmanuel Ambele, Shruthi Ramamurthy
	 */    
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
    
  /**
   * 
   * This profile() renders the viewtweets.html
   * contains logic used to get the users profile details by twitter handle
   * @return 
   * @param name
   * @throws TwitterException, TnterruptedException, ExecutionException
   * @author Emmanuel Ambele, Shruthi Ramamurthy  
   * @throws TwitterException
   * @throws InterruptedException
   * @throws ExecutionException
   */
    
  public Result profile(String name) throws TwitterException ,InterruptedException ,ExecutionException{    	
    	
    	String Profile = getProfile(name);
    	List<String> Tweets = getDetails(name);
    	Thread.sleep(1000L);
		return ok(profile.render(Profile, Tweets));
	}
  
  /**
   * 
   * This getprofile() renders the viewtweets.html
   * contains logic used to get the users profile details by twitter handle
   * @return 
   * @param key
   * @throws TwitterException, TnterruptedException, ExecutionException
   * @author Emmanuel Ambele, Shruthi Ramamurthy  
   * @throws TwitterException
   * @throws InterruptedException
   * @throws ExecutionException
   */
 
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
    /**
     * 
     * This getDetails() renders the viewtweets.html
     * contains logic used to get the users profile details by twitter handle
     * @return 
     * @param name
     * @throws TwitterException, TnterruptedException, ExecutionException
     * @author Emmanuel Ambele, Shruthi Ramamurthy  
     * @throws TwitterException
     * @throws InterruptedException
     * @throws ExecutionException
     */

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
    
    /**
    
     * @param key
     * @param a    
   * This get_results() renders the viewtweets.html
   * contains logic used to get the users profile details by twitter handle
   * @return 
   * @param name
   * @author Emmanuel Ambele, Shruthi Ramamurthy  
   * @throws TwitterException
   * @throws InterruptedException
   * @throws ExecutionException
   */
    
    
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
    
    /**
     * 
     * This get_links() renders the viewtweets.html
     * contains logic used to get the users profile details by twitter handle
     * @return 
     * @param key
     * @param a
     * @author Emmanuel Ambele, Shruthi Ramamurthy  
     * @throws TwitterException
     * @throws InterruptedException
     * @throws ExecutionException
     */
          
    
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
