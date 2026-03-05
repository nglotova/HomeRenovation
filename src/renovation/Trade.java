package renovation;

public enum Trade {
	CARPENTER("Carpenter"),
	PLUMBER("Plumber"),
	ELECTRICIAN("Electrician"),
	PAINTER("Painter"),
	ROOFER("Roofer"),
	TILER("Tiler");

	private final String displayName;

	Trade(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}

