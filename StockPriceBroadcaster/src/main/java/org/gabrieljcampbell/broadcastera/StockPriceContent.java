package org.gabrieljcampbell.broadcastera;

public class StockPriceContent {

	private final long id;
	private final String content;

	public StockPriceContent(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
