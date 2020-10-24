# infinitereactivestreams

Hello everyone, I hope you are all doing well. In this post we will look at how to setup and consume infinite streams of data and then process and redistribute the data. We look at the use case of a broadcaster of stock price data, which could be the main stock market. This is consumed by an investment company that has clients who subscribe to this information know as the subscribers.

So the StockPriceBroadcaster is the provider. I use only one broadcaster here but we could extend the use case to multiple broadcasters. This is then consumed by the investment company’s application called InfiniteReactiveStreams. The consumed stock market data is then processed and distributed to it’s clients, who are the investor subscribers. We call one such subscriber InvestorSubscriber. Once again we could have a number of investor subscribers.

The InfiniteReactiveStreams will “pull” data from a the StockPriceBroadcaster. This data will then be processed and sent to the InvestorSubscriber.

The important question to ask is, why do we use reactive streams? Well the StockPriceBroadcaster sends frequent broad casts of stock market prices. These prices are available for a short period of time and no historical results are stored – maybe due to volume of storage on their system. So the Investment company at a given frequency must pull the stock price data in a timely manner. However the InvestorSubscriber might not be able to keep up with the data at the same pace that it is sent because the InvestorSubscriber might be performing analysis on the incoming data, for example Machine learning such as timeseries analysis to influence buy/sell decisions of the investor subscriber’s portfolio. This analysis of the results takes more time than the consumption of the results. This delay introduces the concept of backpressure and the need for replay of recent values – hence Reactive Streams are a suitable candidate technology for such a use case.

In this tutorial we are going to use the RxJava3 Reactive Streams implementation. Most notably we will use RxJava3 specific features such as replay on Flowables(The RxJava3 terminology for a stream of data). Even though the source of data is a Hot observable we are pulling that data periodically hence we are using a cold observable with backpressure to store the hot observable data.

We present two applications in this tutorial. The main application with the all the functionality is the InfiniteReactiveStreams application and the second is just a small Spring-maven-java application that exposes a REST endpoint to GET stock prices.

The main anatomy of the broadcaster, the StockPriceContentController is as follows:

	@GetMapping("/stockpricestats")
	public StockPriceContent priceStats() {		
		long priceA = Math.round((Math.random()*100));
		long priceB = Math.round((Math.random()*100));
		return new StockPriceContent(counter.incrementAndGet(), 
			"Stock prices" + String.format(TEMPLATEA, priceA)
			+ String.format(TEMPLATEB, priceB));
	}


Next we look at the InfiniteReactiveStreams application. The important parts are how we generate the infinite stream using RxJava3 generate(Note: I wrapped the code so it could be clearer in this post):

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


We use the Flowable.generate(Customer<Emitter> generator) In order to create the stream. The values that comprise the stream are StockPriceContent POJOs that we GET from the stockprice boardcaster application, which is set to run on port 8082 with the URL “http://localhost:8082/stockpricestats;.

Next we call the emitter’s onNext() method in order to pass the stockPriceContent to the the stream.

Next  we create a connectableFlow using the replay functionality, we would like the replay buffer to hold seven recent stock prices and once we have read these we truncate them from the stream hence the “true” argument in the replay method.

Then the investorSubscriber subscribes to the connectable flow. Lastly we connect mean that the flow will start generate items when the subscriber requests them.

The next class that we look at is the ContentProviderSubscription, particularly the request(n) method:

The connectable flow is referenced by publisher here. The ConnectableFlow.blockStream method is used because this enables us to truncate the infinite stream to a finite stream of n items using the limit(n) method in the Java Streams API. The values from the stream are added to a StockPriceContent list. This will then be given back to the Investor via a REST GET /stockpricesummary API.

The blog post for this tutorial can be found at https://gabrieljeremiahcampbell.wordpress.com/2020/10/25/infinite-reactive-streams-with-rxjava3-with-a-tutorial/.

As always Happy Coding!
