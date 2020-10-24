package org.gabrieljcampbell.infinitereactivestreams.model;

/**
 * Author @Gabriel J. Campbell
 *
 */
public class StockStats {

	private long id;
	private String content;

	public StockStats(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
