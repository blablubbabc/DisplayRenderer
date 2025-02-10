package de.blablubbabc.displayRenderer.animation;

public interface Animation {

	public void update();

	public boolean isOver();

	public boolean removeIfOver();
}
