package org.gabrieljcampbell.infinitereactivestreams.publisher;

import java.util.ArrayList;
import java.util.List;

import org.gabrieljcampbell.infinitereactivestreams.model.StockPriceContent;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.rxjava3.flowables.ConnectableFlowable;

/**
 * Author @Gabriel J. Campbell
 *
 */
public final class ContentProviderSubscription implements Subscription {

    private static final Logger logger = LoggerFactory.getLogger(ContentProviderSubscription.class.getName());

     public static ConnectableFlowable<StockPriceContent> publisher;
     public static Subscriber<? super StockPriceContent> subscriber;
     List<StockPriceContent> contentList = new ArrayList<StockPriceContent>();

    public ContentProviderSubscription(ConnectableFlowable<StockPriceContent> publisher ,Subscriber<? super StockPriceContent> subscriber) {
        ContentProviderSubscription.setPublisher(publisher);
        ContentProviderSubscription.setSubscriber(subscriber);
    }
  
    @Override
    public void request(long n) {
        try {
            logger.info("Begin Request(n)");            
            
            contentList = new ArrayList<StockPriceContent>();
                if(publisher != null ){
                    publisher.blockingStream(Long.valueOf(n).intValue() + 1).limit(n).
                        forEach((StockPriceContent stockPriceContent) -> contentList.add(stockPriceContent));
                }

            for (StockPriceContent stockPriceContent : contentList) {
                if (subscriber != null) subscriber.onNext(stockPriceContent);
                logger.info("Subscriber is: "+subscriber.toString()+ " Number of items requested = "+ n);                
            }
        } catch (Exception ex) {
            logger.info( ex.getLocalizedMessage());
            subscriber.onError(ex);
        }
    }
    @Override
    public void cancel() {
        logger.info("Do some cancellation stuff here.");
    }

    public static ConnectableFlowable<StockPriceContent> getPublisher(){
        return ContentProviderSubscription.publisher;
    }

    public static void setPublisher(ConnectableFlowable<StockPriceContent> publisher){
        ContentProviderSubscription.publisher = publisher;
    }

    public static Subscriber<? super StockPriceContent> getSubscriber(){
        return ContentProviderSubscription.subscriber;
    }

    public static void setSubscriber(Subscriber<? super StockPriceContent> subscriber){
        ContentProviderSubscription.subscriber = subscriber;
    }
}