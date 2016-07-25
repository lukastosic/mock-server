package ls.mocks;

public class MappingOption {
	private String option;
	private String path;
	
	public MappingOption(String o, String p) {
		option = o;
		path = p;
	}
	
	public String getOption() {
		return option;
	}
	
	public String getPath() {
		return path;
	}
}
