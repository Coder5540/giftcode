package uitls.input;

public interface TextInputTarget {

	public void setText(String text);

	public void setCursorPosition(int position);

	public String getCurrentText();
	
	public void onShowKeyBoard();
	
	public void onHideKeyBoard();
}
