package no.hvl.dat159;

public class CoinbaseTx {
	
    //Simplified compared to Bitcoin (nothing significant missing)
	private String coinbase; // "The Times 03/Jan/2009 Chancellor 
	                         //  on brink of second bailout for banks"
	private Output output;
	private String txHash;

	public CoinbaseTx(String coinbase, int value, String address) {
		this.txHash = HashUtil.base64Encode(HashUtil.sha256Hash(coinbase)); //HomeMade
		this.output = new Output(value, address); //The Miner's address //HomeMade
	    //Remember to calculate txHash
	}

	@Override
	public String toString() {
	    return "Coinbase Transaction: [Coinbase: " + coinbase + "Output: "
				+ output.getValue() + ", " + output.getAddress() + " ]";
	}

	// HomeMade
	public String getCoinbase() {
		return coinbase;
	}

	public Output getOutput() {
		return output;
	}

	public String getTxHash() {
		return txHash;
	}


}
