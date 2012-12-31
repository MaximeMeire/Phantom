package maximemeire.phantom.concurrent;

/**
 * @author Maxime Meire
 *
 */
public class Address {
	
	private final String universe;
	private final String address;
	
	private Address(String addressBook, String address) {
		this.universe = addressBook;
		this.address = this.universe + ":" + address;
	}
	
	public static Address newAddress(String addressBook, String address) {
		return new Address(addressBook, address);
	}
	
	public final String getAddress() {
		return this.address;
	}
	
	@Override
	public String toString() {
		return this.address;
	}
	
	/**
	 * To use the address string its hash code in a map.
	 */
	@Override
	public int hashCode() {
		return this.address.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof Address) {
				String other = ((Address) o).address;
				return other.equals(this.address);
			}
		}
		return false;
	}

}
