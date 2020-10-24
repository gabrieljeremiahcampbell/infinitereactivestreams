package org.gabrieljcampbell.infinitereactivestreams.subscriber;

import java.util.ArrayList;

import org.gabrieljcampbell.infinitereactivestreams.model.StockPriceContent;
import org.gabrieljcampbell.infinitereactivestreams.model.StockStats;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Author @Gabriel J. Campbell
 *
 */
@Service
public final class InvestorSubscriber implements Subscriber<StockPriceContent> {

    private static final Logger logger = LoggerFactory.getLogger(InvestorSubscriber.class.getName());

    public ArrayList<StockStats> sportStats = new ArrayList<StockStats>();
    private Subscription s;

    private static InvestorSubscriber investorSubscriber = new InvestorSubscriber();

    private InvestorSubscriber() {

    }

    @Override
    public void onSubscribe(Subscription s) {
        this.s = s;     
    }

    @Override
    public void onNext(StockPriceContent t) {
        logger.info("AffliateSubscriber onNext called:");
        sportStats.add(new StockStats(t.getId(), t.getContent()));
        try {
            Thread.sleep(300);
        } catch (InterruptedException interruptedException) {
            logger.info(interruptedException.getLocalizedMessage());
        }
      
    }

    @Override
    public void onError(Throwable throwable) {
        logger.info(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        logger.info("We should do something here!!");

    }

    public Subscription getSubscription(){
        return this.s;
    }

    public void setSubscription(Subscription s){
        this.s = s;
    }

    /**
     * InvestorSubscriber is a singleton.
     */
    public static InvestorSubscriber getInvestorSubscriber(){
        if(investorSubscriber!= null){
            return investorSubscriber;
        }else{
            return new InvestorSubscriber();
        }
    }

}
