package no.hvl.dat159;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Transaction {

    //Simplified compared to Bitcoin
	private List<Input> inputs = new ArrayList<>();
	private List<Output> outputs = new ArrayList<>();
	
	//If we make the assumption that all the inputs belong to the
	//same key, we can have one signature for the entire transaction, 
	//and not one for each input. This simplifies things a lot 
	//(more than you think)!
	private PublicKey senderPublicKey;
	private byte[] signature;
	
	private String txHash;
	
	public Transaction(PublicKey senderPublicKey) {
		this.senderPublicKey = senderPublicKey; //HomeMade
	}
	
	public void addInput(Input input) {
		inputs.add(input); //HomeMade
	}
	
	public void addOutput(Output output) {
		outputs.add(output); //HomeMade
	}

	public String inputsString() {
		return " ".join(inputs.toString());
	}

	public String outputsString() {
		return " ".join(outputs.toString());
	}

	@Override
	public String toString() {

		return "-Transaction-"
				+ "\ninputs: " + inputsString()
				+ "\noutputs: " + outputsString()
				+ "\nSender public key: " + senderPublicKey
				+ "\nSignature: " + Arrays.toString(signature);
	}

	public void signTxUsing(PrivateKey privateKey) {
		calculateTxHash(); //HomeMade
		DSAUtil.signWithDSA(privateKey, txHash);  //HomeMade
	}

	public void calculateTxHash() {
		txHash = HashUtil.base64Encode(HashUtil.sha256Hash(this.txHash)); //HomeMade
	}
	
	public boolean isValid(Map<Input, Output> map) {
		boolean isValid = true;
		//TODO
		/* Transaction validation checklist (simplified):
		 * - The transaction's syntax and data structure must be correct
		 * - Neither lists of inputs or outputs are empty
		 * - Each output value, as well as the total, must be within the allowed range
		 *   of values (less than 21m coins, more than the dust threshold)
		 * - For each input, if the referenced output exists in any other transaction in the pool,
		 * 	 the transaction must be rejected.
		 * - For each input, the referenced output must exist and cannot already be spent.
		 * - Using the referenced output transactions to get input values, check that each input value, as well
		 *   as the sum, are in the allowed ange of values (less than 21m coins, more than 0).
		 * - Reject if the sum of input values is less than sum of output values
		 * - The unlocking scripts for each input must validate against the corresponding output locking scripts.
		 */

		//Check that no variables are empty
		if (!isNotEmpty() || senderPublicKey == null || signature == null
				|| signature.length == 0 || txHash == null) {
			isValid = false;
		//Check for repeating inputs
		} else if (inputs.stream().noneMatch(input -> inputs.indexOf(input) == 1)) {
			isValid = false;
		//Check that output has valid value
		} else if (outputs.stream().allMatch(output -> output.getValue() < 0 || output.getValue() > 21000000)) {
			isValid = false;
		//Check if transaction is signed by sender
		} else if (!DSAUtil.verifyWithDSA(senderPublicKey,inputsString() + outputsString(), signature)) {
			isValid = false;
		//Verify that Output is unspent
		} else if (!isUnspentOutput(map)) {
			isValid = false;
		//Check if sum of inputs and outputs are equal
		} else if (!inputsAndOutputsSumIsEqual(map)) {
			isValid = false;
		//Check if inputs belong to sender
		} else if (inputs.stream().anyMatch(input -> input.getPrevTxHash().equals(HashUtil.addressFromPublicKey(senderPublicKey)))) {
			isValid = false;
		//Check if transaction hash is correct
		} else if (txHash.equals(HashUtil.base64Encode(HashUtil.sha256Hash(this.toString())))) {
			isValid = false;
		}

		return isValid;
	}

	public boolean isUnspentOutput(Map<Input, Output> map){
		for(Output o : outputs){
			for(Map.Entry<Input, Output> i : map.entrySet()){
				if(i.getKey().getPrevTxHash().equals(o.getAddress())){
					return false;
				}
			}
		}
		return true;
	}

	public boolean inputsAndOutputsSumIsEqual(Map<Input, Output> map){
		String hash = inputs.get(0).getPrevTxHash();
		long sumInputs = 0;
		for(Map.Entry<Input, Output> k : map.entrySet()) {
			if(k.getValue().getAddress().equals(hash)){
				sumInputs += k.getValue().getValue();
			}
		}
		long sumValues = outputs.stream().mapToLong(Output::getValue).sum();
		return sumInputs == sumValues;
	}

	public boolean isNotEmpty() {
		return (!(outputs.isEmpty() || inputs.isEmpty()));
	}


	public List<Input> getInputs() {
		return inputs;
	}

	public List<Output> getOutputs() {
		return outputs;
	}

	public PublicKey getSenderPublicKey() {
		return senderPublicKey;
	}

	public byte[] getSignature() {
		return signature;
	}

	public String getTxHash() {
		return txHash;
	}
}
