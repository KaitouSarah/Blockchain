package no.hvl.dat159;

public class Output {

    //Simplified compared to Bitcoin - The address should be a script
	private long value;
	private String address;
	
	public Output(long value, String address) {
		this.value = value; //HomeMade
		this.address = HashUtil.base64Encode(HashUtil.sha256Hash(address)); //the recipient address //HomeMade
	}

	@Override
	public String toString() {
		return "Output[" + "value=" + value + ", address='" + address + '\'' + ']'; //HomeMade
	}
	
    //TODO Getters?
	public long getValue() {
		return value;
	} //HomeMade

	public String getAddress() {
		return address;
	} //HomeMade
}
