package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import play.data.FormFactory;
import play.mvc.*;
import twitter.twitter1;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
	List<String> result = new ArrayList<>();
	List<String> links = new ArrayList<>();

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
 
    public Result welcome(String name) {
        return ok(welcomet.render(name));
    }
    
    public Result that(String name) {
        return ok(thats.render(name));
    }
    
    public Result first() {
    		return ok(app.render());
    	}
    
    public Result quest(String myName) throws TwitterException  {
    	List<String> results = new ArrayList<>();
    	List<String> linkss = new ArrayList<>();
    	String[] searchKeyword = myName.split(" ");
    	for(String word : searchKeyword) {
    		callMethod(word);
    	}
    	results = result;
    	linkss = links;
    	 result = new ArrayList<>();
    	 links = new ArrayList<>();
    	
    	return ok(req.render(results,linkss));
    	
    	}
    
    
    public Result pro(String name) throws TwitterException {
    	
    	twitter1 tweet = new twitter1(name);
    	String profileInfo = tweet.getProfile();
    	List<Status> timeLine = tweet.getDetails();
    	
    	List<String> tweets = new ArrayList<String>();
    	int count = 0 ;
	    for (Status stat : timeLine) {
	    	count++;
	    	if (count <= 10) {
	    		tweets.add(stat.getText());
	    	}
	    	else
	    		break;
	    }
    	
    	
    
		return ok(profile.render(profileInfo, tweets));
	}
    
    public void callMethod(String myName) throws TwitterException  {
    	twitter1 tweet = new twitter1(myName);
    	QueryResult results = tweet.get();
    	int count = 0 ;
	    for (Status stat : results.getTweets()) {
	    	count++;
	    	if (count <= 10) {
	       // System.out.println( count + " " +"@" + stat.getUser().getScreenName() + ": \n " + stat.getText());
	    		result.add( "\t" + stat.getText() );
	    		links.add("@" + stat.getUser().getScreenName());
	    	
	    	}
	    	else
	    		break;
	    }
    }
 
}
