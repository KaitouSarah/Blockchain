package no.hvl.dat159;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class Wallet {

    private String id;
    private KeyPair keyPair;
    
    //A refererence to the "global" complete utxo-set
    private Map<Input, Output> utxoMap;

    public Wallet(String id, UTXO utxo) {
        //HomeMade
        this.id = id;
        this.keyPair = DSAUtil.generateRandomDSAKeyPair();
        this.utxoMap = utxo.getMap();
    }

    public String getAddress() {
        return HashUtil.addressFromPublicKey(getPublicKey()); //HomeMade
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic(); //HomeMade
    }

    public Transaction createTransaction(long value, String address) throws Exception {

        // 1. Collect all UTXO for this wallet and calculate balance
        Map<Input, Output> myUtxo = collectMyUtxo();
        long myBalance = getBalance();
        // 2. Check if there are sufficient funds --- Exception?
        if (myBalance < value) throw new Exception("Oh no, not enough funds!");
        // 4. Calculate change
        long change = myBalance - value;
        // 5. Create an "empty" transaction
        Transaction tx = new Transaction(getPublicKey());
        // 6. Add chosen inputs
        myUtxo.forEach((input, output) -> tx.addInput(new Input(output.getAddress(), input.getPrevOutputIndex())));
        // 3. Choose a number of UTXO to be spent --- Strategy?
        Output valueSpent = new Output(value, address);
        tx.addOutput(valueSpent);
        // 7. Add 1 or 2 outputs, depending on change
        if (change > 0) {
            tx.addOutput(new Output(change, getAddress()));
        }
        // 8. Sign the transaction
        tx.signTxUsing(keyPair.getPrivate()); //HomeMade
        // 9. Calculate the hash for the transaction
        tx.calculateTxHash(); //HomeMade
        // 10. return
        return tx;
        
        // PS! We have not updated the UTXO yet. That is normally done
        // when appending the block to the blockchain, and not here!
        // Do that manually from the Application-main.
    }

    @Override
    public String toString() {
        return " -My Wallet- \nID: " + id
                + "\nAddress: " + getAddress()
                + "\nBalance: " + getBalance();}

    public long getBalance() {
        //HomeMade
        return calculateBalance(collectMyUtxo().values());
    }
    
    private long calculateBalance(Collection<Output> outputs) {
        //HomeMade
        return outputs.stream().mapToLong(Output::getValue).sum();
    }

    private Map<Input, Output> collectMyUtxo() {
        //HomeMade
       return utxoMap.entrySet().stream()
                .filter(map -> map.getValue().getAddress().equals(getAddress()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //TODO Getters?
}
