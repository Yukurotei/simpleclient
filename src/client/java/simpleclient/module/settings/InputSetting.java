package simpleclient.module.settings;

public class InputSetting extends Setting {
	
	private String content;
	private String placeHolder;
	private boolean enabled;
	private boolean doneTyping;
	
	public InputSetting(String name, String placeHolder) {
		super(name);
		this.placeHolder = placeHolder;
		this.content = "";
		this.doneTyping = false;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getPlaceHolder() {
		return placeHolder;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}
	
	public void addLetterToContent(char character) {
		this.content = this.content + character;
	}
	
	public boolean isDoneTyping() {
		return doneTyping;
	}
	
	public void setDoneTyping(boolean state) {
		this.doneTyping = state;
	}
	
	public void toggle() {
		this.enabled = !this.enabled;
	}

}
