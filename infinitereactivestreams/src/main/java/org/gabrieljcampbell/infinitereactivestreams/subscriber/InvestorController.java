package org.gabrieljcampbell.infinitereactivestreams.subscriber;

import java.util.ArrayList;
import java.util.List;

import org.gabrieljcampbell.infinitereactivestreams.model.StockStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author @Gabriel J. Campbell
 *
 */
@RestController
public final class InvestorController {
	
	private static final Logger logger = LoggerFactory.getLogger(InvestorController.class.getName());
	
	InvestorSubscriber affliateSubscriber = InvestorSubscriber.getInvestorSubscriber();

	@GetMapping("/stockpricesummary")
	public List<StockStats> stockStats() {
				logger.info(affliateSubscriber.getSubscription().toString());
				affliateSubscriber.getSubscription().request(7);
				ArrayList<StockStats> sportStatsResults = affliateSubscriber.sportStats;
				affliateSubscriber.sportStats = new ArrayList<StockStats>();
				return sportStatsResults;
	}

	

	
}
