package uitls.input;

public abstract class TextInputHelper {

	public static enum FilterStyle {
		ALPHANUMERIC, NORMAL
	}

	protected TextInputTarget	target;

	public void setTarget(TextInputTarget target) {
		this.target = target;
	}

	public TextInputTarget getTarget() {
		return target;
	}

	public abstract void setPointer(int position);

	public abstract void requestFocus(FilterStyle style);

	public void onShowKeyBoard() {
		if (target != null) {
			target.onShowKeyBoard();
			System.out.println("Target is not null");
		}else{
			System.out.println("Target is null");
		}
	}

	public void onHideKeyBoard() {
		if (target != null) {
			target.onHideKeyBoard();
			System.out.println("Target is not null");
		}else{
			System.out.println("Target is null");
		}
		
	}
}
