package hello;


import demo.contract.Greeter;
import model.Account;
import model.Balance;
import model.Contract;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import util.Web3jConstants;
import util.Web3jUtils;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Controller
public class ContractController {

    private Web3j web3j = null;

    @PostConstruct
    public void init(){
        web3j = Web3jUtils.buildHttpClient("localhost", Web3jConstants.CLIENT_PORT);;
    }

    @PostMapping("/deployContract")
    private String deployContract(@Valid Contract contractForm, Account account, BindingResult bindingResult, Model model) throws Exception {

        model.addAttribute("balance", new Balance());

        StringBuilder transactionInfo = new StringBuilder();


        Credentials credentials = WalletUtils.loadCredentials(
                Web3jConstants.PASSWORD,
                Web3jConstants.PATH_WALLET + Web3jConstants.FILE_NAME);

        if(contractForm.getPrice() == null || contractForm.getPrice().equals(BigInteger.valueOf(0))){
            transactionInfo.append("Price can not be empty, negative or 0");
            account.setAddress(credentials.getAddress());
            account.setTransactionInfo(transactionInfo.toString());
            return  "main";
        }

        transactionInfo.append("Deploying contract \n");

        Greeter contract = Greeter
                .deploy(
                        web3j,
                        credentials,
                        Web3jConstants.GAS_PRICE,
                        Web3jConstants.GAS_LIMIT_GREETER_TX,
                        "Сonditions fulfilled. The car is your's now :)",
                        "Conditions failed. Try again. You should pay full price of the car.",
                        contractForm.getPrice())
                .send();

        // get tx receipt
        TransactionReceipt txReceipt = contract
                .getTransactionReceipt()
                .get();

        // get tx hash and tx fees
        String deployHash = txReceipt.getTransactionHash();
        BigInteger deployFees = txReceipt
                .getCumulativeGasUsed()
                .multiply(Web3jConstants.GAS_PRICE);

        transactionInfo.append("Deploy hash: ").append(deployHash).append("\n");
        transactionInfo.append("Deploy fees: ").append(Web3jUtils.weiToEther(deployFees)).append("\n");

        // get initial contract balance
        BigInteger deposits = contract
                .deposits()
                .send();

        String contractAddress = contract.getContractAddress();
        saveContractAddressToFile(contractAddress);
        transactionInfo.append("Contract address: ").append(contractAddress).append("\n");
        transactionInfo.append("Car price is now: ").append(contractForm.getPrice()).append(" eth").append("\n");
        transactionInfo.append("Contract.owner(): " + contract.owner().send());
        account.setAddress(credentials.getAddress());
        account.setTransactionInfo(transactionInfo.toString());

        printBalance("after deploy", credentials, transactionInfo);

        return "main";
    }

    @PostMapping("/buyCar")
    private String sendFunds(@Valid Contract contract, Account account, Balance balance, BindingResult bindingResult, Model model) throws Exception {
    StringBuilder transactionInfo = new StringBuilder();

        model.addAttribute("balance", new Balance());
        // trasfer ether to contract account
        Credentials credentials = WalletUtils.loadCredentials(
                Web3jConstants.PASSWORD,
                Web3jConstants.PATH_WALLET + Web3jConstants.FILE_NAME);


        if(contract.getContractAddress() == null || contract.getContractAddress().equals("")){
            transactionInfo.append("Incorrect contract address");
            account.setAddress(credentials.getAddress());
            account.setTransactionInfo(transactionInfo.toString());
            return  "main";
        }

        String fromAddress = credentials.getAddress();
        String contractAddress = contract.getContractAddress();
        Greeter greeter = Greeter.load(contractAddress, web3j, credentials, Web3jConstants.GAS_PRICE, Web3jConstants.GAS_LIMIT_ETHER_TX);
        BigInteger nonce = getNonce(fromAddress);
        String amount = String.valueOf(greeter.price().send());

        System.out.println(amount);
        BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

        RawTransaction txRaw = RawTransaction
                .createEtherTransaction(
                        nonce,
                        Web3jConstants.GAS_PRICE,
                        Web3jConstants.GAS_LIMIT_ETHER_TX,
                        contractAddress,
                        amountWei);


        if(getBalanceWei(fromAddress).compareTo(amountWei) < 0)
        {
            account.setTransactionInfo("You don't have enough Eth");
            return "main";
        }

        byte[] txSignedBytes = TransactionEncoder.signMessage(txRaw, credentials);
        String txSigned = Numeric.toHexString(txSignedBytes);

        BigInteger txFeeEstimate = Web3jConstants.GAS_LIMIT_ETHER_TX.multiply(Web3jConstants.GAS_PRICE);


        EthSendTransaction ethSendTx = web3j
                .ethSendRawTransaction(txSigned)
                .sendAsync()
                .get();

        Response.Error error = ethSendTx.getError();
        String txHash = ethSendTx.getTransactionHash();

        TransactionReceipt txReceipt = waitForReceipt(txHash);
        BigInteger txFee = txReceipt.getCumulativeGasUsed().multiply(Web3jConstants.GAS_PRICE);

        balance.setBalance(Web3jUtils.getBalanceEther(web3j, fromAddress));

        transactionInfo.append("Contract address balance (after funding): ").append(Web3jUtils.weiToEther(Web3jUtils.getBalanceWei(web3j, contractAddress))).append("\n");

        greeter.sellCar().send();

        transactionInfo.append("Contract.client(): " + greeter.client().send()).append("\n");
        transactionInfo.append("Contract.owner(): " + greeter.owner().send()).append("\n");
        transactionInfo.append("Contract.result(): " + greeter.result().send()).append("\n");
        if(greeter.result().send().equals("Сonditions fulfilled. The car is your's now :)")) {
            greeter.kill().send();
            transactionInfo.append("Contract killed.");
        }
        account.setTransactionInfo(transactionInfo.toString());

        return "main";
    }


    private void printBalance(String info, Credentials credentials, StringBuilder transactionInfo) throws Exception {
        transactionInfo.append("Your account balance (").append(info).append("): ").append(Web3jUtils.weiToEther(Web3jUtils.getBalanceWei(web3j, credentials.getAddress()))).append("\n");
    }

    public void saveContractAddressToFile(String contractAddress) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {

        File file = new File(Web3jConstants.CONTRACT_DATABASE_FILE);

        // The name of the file to open.

        try {
            // Assume default encoding.
            FileWriter fileWriter =
                    new FileWriter(file, true);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write(contractAddress);
            bufferedWriter.newLine();

            // Always close files.
            bufferedWriter.close();

        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

    }

    private BigInteger getNonce(String address) throws Exception {
        return Web3jUtils.getNonce(web3j, address);
    }

    private BigInteger getBalanceWei(String address) throws Exception {
        return Web3jUtils.getBalanceWei(web3j, address);
    }

    private TransactionReceipt waitForReceipt(String transactionHash) throws Exception {
        return Web3jUtils.waitForReceipt(web3j, transactionHash);
    }

}
