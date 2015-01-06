package updatehandler;

import utils.factory.IUpdateHandler;

import com.badlogic.gdx.Gdx;

public class MemoryManager implements IUpdateHandler {

	float	count		= 0;

	long	last_mem	= Gdx.app.getJavaHeap() / 1024;

	@Override
	public void onUpdate(float delta) {
		count += delta;
		if (count >= 5f) {
			count = 0;
			long current_mem = Gdx.app.getJavaHeap() / 1024;
			if (current_mem - last_mem >= 2048) {
				System.gc();
				last_mem = Gdx.app.getJavaHeap() / 1024;
			}
		}
	}

	@Override
	public void reset() {

	}

}
