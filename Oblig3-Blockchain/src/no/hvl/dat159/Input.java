package no.hvl.dat159;

public class Input {

    //Simplified compared to Bitcoin
    //The signature is moved to Transaction, see comment there.
	private String prevTxHash;
	private int prevOutputIndex;
	
	public Input(String prevTxHash, int prevOutputIndex) {
		this.prevTxHash = prevTxHash; //HomeMade
		this.prevOutputIndex = prevOutputIndex; //HomeMade
	}

	@Override
	public String toString() {
		return "Input: [ Previous transaction hash: " + prevTxHash + ", previous output index: " + prevOutputIndex + " ]"; //HomeMade
	}

    //TODO Getters?
	public String getPrevTxHash() {
		return prevTxHash;
	} //HomeMade

	public int getPrevOutputIndex() {
		return prevOutputIndex;
	} //HomeMade
}
