package no.hvl.dat159;

public class Application {
    
    private static UTXO utxo = new UTXO();
	
	public static void main(String[] args) throws Exception {
	    
        /*
         * In this assignment, we are going to look at how to represent and record
         * monetary transactions. We will use Bitcoin as the basis for the assignment,
         * but there will be some simplifications.
         * 
         * We are skipping the whole blockchain this time, and instead focus on the
         * transaction details, the UTXOs and how money movements are represented.
         * 
         * (If you want to, you can of course extend the assignment by collecting the
         * individual transactions into blocks, create a Merkle tree for the block
         * header, validate, mine and add the block to a blockchain.)
         * 
         */

        // 0. To get started, we need a few (single address) Wallets. Create 2 wallets.
        //    Think of one of them as the "miner" (the one collecting "block rewards").
		UTXO utxo = new UTXO();
 		Wallet minerWallet = new Wallet(HashUtil.base64Encode(HashUtil.sha256Hash("minerWallet")), utxo);
 		Wallet randomWallet = new Wallet(HashUtil.base64Encode(HashUtil.sha256Hash("buyerWallet")), utxo);


        // 1. The first "block" (= round of transactions) contains only a coinbase
        //    transaction. Create a coinbase transaction that adds a certain
        //    amount to the "miner"'s address. Update the UTXO-set (add only).
        CoinbaseTx genesisBlock = new CoinbaseTx("Genesis", 50, minerWallet.getAddress());
		utxo.addOutputFrom(genesisBlock);
		System.out.println("Block 1 = " + genesisBlock.toString());
        // 2. The second "block" contains two transactions, the mandatory coinbase
        //    transaction and a regular transaction. The regular transaction shall
        //    send ~20% of the money from the "miner"'s address to the other address.
        CoinbaseTx coinbaseTx = new CoinbaseTx("coinbaseTx	", 50, minerWallet.getAddress());

        //    Validate the regular transaction created by the "miner"'s wallet:
        //      - All the content must be valid (not null++)!!! (sValid)
        //      - All the inputs are unspent and belongs to the sender
        //      - There are no repeating inputs!!! (sValid)
        //      - All the outputs must have a value > 0 (sValid)
        //      - The sum of inputs equals the sum of outputs
        //      - The transaction is correctly signed by the sender (sValid)
        //      - The transaction hash is correct
        
        //    Update the UTXO-set (both add and remove).
		try {
			Transaction tx = minerWallet.createTransaction(10, randomWallet.getAddress());
			if (!tx.isValid(utxo.getMap())) throw new Exception("Not a valid transaction");

			utxo.addAndRemoveOutputsFrom(tx);
			System.out.println("\nBlock 2 = " + coinbaseTx.toString() + "\n" + tx.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

        // 3. Do the same once more. Now, the "miner"'s address should have two or more
        //    unspent outputs (depending on the strategy for choosing inputs) with a
        //    total of 2.6 * block reward, and the other address should have 0.4 ...
        
        //    Validate the regular transaction ...
        
        //    Update the UTXO-set ...
	    
        // 4. Make a nice print-out of all that has happened, as well as the end status.
        //
        //      for each of the "block"s (rounds), print
        //          "block" number
        //          the coinbase transaction
        //              hash, message
        //              output
        //          the regular transaction(s), if any
        //              hash
        //              inputs
        //              outputs
        //      End status: the set of unspent outputs
        //      End status: for each of the wallets, print
        //          wallet id, address, balance
	}
}
