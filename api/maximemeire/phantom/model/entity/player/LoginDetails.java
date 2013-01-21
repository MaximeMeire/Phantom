package maximemeire.phantom.model.entity.player;

import maximemeire.phantom.network.ISAACCipher;

public class LoginDetails {
	
	public final int[] isaacSeed;
	public final String username;
	public final String password;
	
	public LoginDetails(int[] isaacSeed, String username, String password) {
		this.isaacSeed = isaacSeed;
		this.username = username;
		this.password = password;
	}
	
	public ISAACCipher getInboundCipher() {
		return new ISAACCipher(isaacSeed);		
	}
	
	public ISAACCipher getOutboundCipher() {
		int[] seed = new int[4];
		for (int i = 0; i < 4; i++) 
			seed[i] = isaacSeed[i] + 50;
		return new ISAACCipher(seed);
	}

}
