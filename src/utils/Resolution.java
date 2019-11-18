package utils;

public enum Resolution {
	
	res800x600(800,	600, 40, 66, 70, 90, 250),
	res1024x768(1024, 768, 60, 100, 90, 110, 340),
	res1080x720(1080, 720, 60, 100, 90, 120, 360),	// native resolution
	res1280x1024(1280, 1024, 70, 115, 160, 130, 390),
	res1536x864(1536, 864, 80, 133, 160, 130, 500),
	res1600x1024(1600, 1024 , 80, 133, 160, 130, 500);
	
	Resolution(int screenWidth, int screenHeight, int cardWidth, int cardHeight, int topMargin, int leftMargin, int rightMargin) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.cardWidth = cardWidth;
		this.cardHeight = cardHeight;
		this.topMargin = topMargin;
		this.leftMargin = leftMargin;
		this.rightMargin = rightMargin;
		this.padding = 10;
	}

	public final int screenWidth;
	public final int screenHeight;
	public final int cardWidth;
	public final int cardHeight;
	public final int topMargin;
	public final int leftMargin;
	public final int rightMargin;
	public final int padding;
	
}