package org.gabrieljcampbell.broadcastera;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockPriceContentController {

	private static final String TEMPLATEA = ": Company A CurrentPrice: %s";
	private static final String TEMPLATEB = ": Company B CurrentPrice: %s";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/stockpricestats")
	public StockPriceContent priceStats() {		
		long priceA = Math.round((Math.random()*100));
		long priceB = Math.round((Math.random()*100));
		return new StockPriceContent(counter.incrementAndGet(), 
			"Stock prices" + String.format(TEMPLATEA, priceA)
			+ String.format(TEMPLATEB, priceB));
	}
}
