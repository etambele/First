package twitter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This is a new class
 * @author Emmanuel
 *
 */
/**
 * @author Emmanuel Ambele
 * 
 * 
 *This class contains the Twitter4j API to handle request-performing the search on the tweets.
 *Accepts a keyword and produces tweet that matches it.
 */
public class Twitter {
	String data;
	
	/**
	* Constructor method- Assigns the keyword passed for search to a string variable.
	* @param SearchString
	*/
	
	public Twitter(String SearchString) {
		data = SearchString;
	}
		
	
/**
 * This get() performs authentication action. 
 * Accepts the data/keyword and search for a tweet that matches that keyword.
 * @return tweets as FutureResult.
 * @throws TwitterException
 */
	@SuppressWarnings("unchecked")
	public Future<QueryResult> getTweets() throws TwitterException  {
		// TODO Auto-generated method stub
		CompletableFuture<QueryResult> futureResult = new CompletableFuture<>();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
						.setOAuthConsumerKey("iuh1dBIa8bXvOjBSRLpIF7e40")
						.setOAuthConsumerSecret("GAupJH5iFkWycp9r72dN44Tvd0pO14Tkoi4WSsoMt8dSN4GB3E")
						.setOAuthAccessToken("972273228046569473-4GTgsikGCKHXse3RxzTwqSUk23cEhe8")
						.setOAuthAccessTokenSecret("xctHGp1WG295EARuD7uWKuWJuAI9hgPxmDI0IxmK0ZtAI");
		
		
		new Thread( () -> {
			try {
				TwitterFactory tf = new TwitterFactory(cb.build());
				twitter4j.Twitter twitter = tf.getInstance();
				Query query = new Query(data);
			    futureResult.complete(twitter.search(query));

			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}).start();

		 Query query = new Query(data);
		 return futureResult;
		    
		    
	}
	
	/**
	 * This getProfile() produces the user's account.
	 * Contains the basic details about that person, such as Followers Count, Friends Count, Location, Description and ScreenName.
	 * @return FutureProfile
	 * @throws TwitterException
	 */
	
	public Future<String>  getProfile() throws TwitterException  {
		// TODO Auto-generated method stub
		
		CompletableFuture<String> futureProfile = new CompletableFuture<>();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
						.setOAuthConsumerKey("iuh1dBIa8bXvOjBSRLpIF7e40")
						.setOAuthConsumerSecret("GAupJH5iFkWycp9r72dN44Tvd0pO14Tkoi4WSsoMt8dSN4GB3E")
						.setOAuthAccessToken("972273228046569473-4GTgsikGCKHXse3RxzTwqSUk23cEhe8")
						.setOAuthAccessTokenSecret("xctHGp1WG295EARuD7uWKuWJuAI9hgPxmDI0IxmK0ZtAI");

		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter4j.Twitter twitter = tf.getInstance();
		
		 
		new Thread( () -> {
			User user;
			try {
				user = twitter.showUser(data);
				futureProfile.complete(user.getFollowersCount() + " "+ user.getFriendsCount() + " "+ user.getLocation() + " "+ user.getDescription() + " "+ user.getScreenName());

			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}).start();
		
		return 	futureProfile;			    
		    
	}
	
	
	/**
	 * This getDetails() fetches the Timeline of the user displaying their recent ten tweets.
	 * @return futureStatus
	 * @throws TwitterException
	 */		
		public CompletableFuture<List<Status>> getDetails() throws TwitterException{
			ConfigurationBuilder cb = new ConfigurationBuilder();
			CompletableFuture<List<Status>> futureStatus = new CompletableFuture<>();
			
			cb.setDebugEnabled(true)
							.setOAuthConsumerKey("iuh1dBIa8bXvOjBSRLpIF7e40")
							.setOAuthConsumerSecret("GAupJH5iFkWycp9r72dN44Tvd0pO14Tkoi4WSsoMt8dSN4GB3E")
							.setOAuthAccessToken("972273228046569473-4GTgsikGCKHXse3RxzTwqSUk23cEhe8")
							.setOAuthAccessTokenSecret("xctHGp1WG295EARuD7uWKuWJuAI9hgPxmDI0IxmK0ZtAI");
			TwitterFactory tf = new TwitterFactory(cb.build());
			
			
			new Thread( () -> {

				try {
					twitter4j.Twitter twitter = tf.getInstance();
					futureStatus.complete(twitter.getUserTimeline(data));
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}).start();
				
			return futureStatus;
			
		}


}
