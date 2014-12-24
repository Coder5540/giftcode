package utils.factory;

import com.badlogic.gdx.utils.Array;

public class UpdateHandlerList extends Array<IUpdateHandler>{

	public UpdateHandlerList() {

	}

	public UpdateHandlerList(final int pCapacity) {
		super(pCapacity);
	}

	public void onUpdate(final float pSecondsElapsed) {
		final int handlerCount = this.size;
		for(int i = handlerCount - 1; i >= 0; i--) {
			this.get(i).onUpdate(pSecondsElapsed);
		}
	}

	public void reset() {
		final int handlerCount = this.size;
		for(int i = handlerCount - 1; i >= 0; i--) {
			this.get(i).reset();
		}
	}

}
