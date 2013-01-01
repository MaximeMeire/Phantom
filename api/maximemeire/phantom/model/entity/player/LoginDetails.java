package maximemeire.phantom.model.entity.player;

public class LoginDetails {
	
	public final int[] isaacSeeds;
	public final String username;
	public final String password;
	
	public LoginDetails(int[] isaacSeeds, String username, String password) {
		this.isaacSeeds = isaacSeeds;
		this.username = username;
		this.password = password;
	}

}
