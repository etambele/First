import akka.actor.ActorSystem;
import controllers.AsyncController;
import controllers.CountController;
import controllers.HomeController;
import org.junit.Test;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;
import twitter4j.TwitterException;
import java.util.concurrent.ExecutionException;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.test.Helpers.contentAsString;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;


import org.junit.Test;

import play.mvc.Result;
import play.twirl.api.Content;


/**
 * Unit testing does not require Play application start up.
 *
 * https://www.playframework.com/documentation/latest/JavaTest
 */
public class UnitTest {

 
    @Test
    public void TestSearch() throws TwitterException , InterruptedException ,ExecutionException{
      Result result = new HomeController().Search();
      assertEquals(OK, result.status());
      assertEquals("text/html", result.contentType().get());
      assertEquals("utf-8", result.charset().get());
      assertThat(contentAsString(result)).contains("Tweet Analytics");
     // Content html = views.html.pro.render();
     // assertThat(html.body()).contains("Your new application is ready.");
    }
    
    @Test
    public void TestSearchResults() throws TwitterException , InterruptedException ,ExecutionException{
      Result result = new HomeController().SearchResults("got");
      assertEquals(OK, result.status());
      assertEquals("text/html", result.contentType().get());
      assertEquals("utf-8", result.charset().get());
      assertThat(contentAsString(result)).contains("got");
     // Content html = views.html.pro.render();
     // assertThat(html.body()).contains("Your new application is ready.");
    }
    @Test
    public void TestProfile() throws TwitterException , InterruptedException ,ExecutionException{
      Result result = new HomeController().profile("@");
      assertEquals(OK, result.status());
      assertEquals("text/html", result.contentType().get());
      assertEquals("utf-8", result.charset().get());
      assertThat(contentAsString(result)).contains("@");
     // Content html = views.html.pro.render();
     // assertThat(html.body()).contains("Your new application is ready.");
    }
       
  

    // Unit test a controller with async return
    @Test
    public void testAsync() {
        final ActorSystem actorSystem = ActorSystem.create("test");
        try {
            final ExecutionContextExecutor ec = actorSystem.dispatcher();
            final AsyncController controller = new AsyncController(actorSystem, ec);
            final CompletionStage<Result> future = controller.message();

            // Block until the result is completed
            await().until(() -> {
                assertThat(future.toCompletableFuture()).isCompletedWithValueMatching(result -> {
                    return contentAsString(result).equals("Hi!");
                });
            });
        } finally {
            actorSystem.terminate();
        }
    }

}
