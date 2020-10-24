package org.gabrieljcampbell.infinitereactivestreams.model;

/**
 * Author @Gabriel J. Campbell
 *
 */
public class StockPriceContent {
    
    private long id;
	private String content;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "SportContent: id="+getId()+": content = "+getContent();
	}
}
