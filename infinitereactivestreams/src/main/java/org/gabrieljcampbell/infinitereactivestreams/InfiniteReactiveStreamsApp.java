package org.gabrieljcampbell.infinitereactivestreams;

import org.gabrieljcampbell.infinitereactivestreams.model.StockPriceContent;
import org.gabrieljcampbell.infinitereactivestreams.publisher.ContentProviderSubscription;
import org.gabrieljcampbell.infinitereactivestreams.subscriber.InvestorSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.flowables.ConnectableFlowable;

/**
 * Author @Gabriel J. Campbell
 *
 */
@SpringBootApplication
public class InfiniteReactiveStreamsApp {


    private static final Logger logger = LoggerFactory.getLogger(InfiniteReactiveStreamsApp.class.getName());
    
    public static void main(String[] args){        
        new Thread(() -> {
            startStreaming();
            }).start();                   
            new Thread(() -> {
                SpringApplication.run(InfiniteReactiveStreamsApp.class, args);
                }).start();    
                logger.info("End of startup");
    }

    
    public static void startStreaming(){
        InvestorSubscriber investorSubscriber = InvestorSubscriber.getInvestorSubscriber();

        RestTemplate restTemplate = new RestTemplate();
        

        Flowable<StockPriceContent> flow = Flowable.
            generate((stockPriceContentEmitter) -> {
                Thread.sleep(100);
            StockPriceContent stockPriceContent = 
                restTemplate.
                    getForObject(
                        "http://localhost:8082/stockpricestats", StockPriceContent.class);
            
            logger.info("Emitting : "+stockPriceContent.toString());
            stockPriceContentEmitter.onNext(stockPriceContent);});
            

            logger.info("Buffer Size = "+ Flowable.bufferSize());

        ConnectableFlowable<StockPriceContent> connectableFlow = flow.replay(7, true);
        
        connectableFlow.subscribe(investorSubscriber);
        ContentProviderSubscription contentProviderSubscription =
             new ContentProviderSubscription( connectableFlow, investorSubscriber);
             investorSubscriber.setSubscription(contentProviderSubscription);

        connectableFlow.connect();   
    }
}
