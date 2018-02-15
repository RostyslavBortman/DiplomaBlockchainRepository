package hello;

import model.Account;
import model.Balance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthMining;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import util.Web3jConstants;
import util.Web3jUtils;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.math.BigInteger;


@Controller
public class TransactionController {

    private Web3j web3j = null;

    @PostConstruct
    public void init(){
          web3j = Web3jUtils.buildHttpClient("localhost", Web3jConstants.CLIENT_PORT);;
    }

    @PostMapping("/send")
    public String send(@Valid Account account, @Valid Balance balance, BindingResult bindingResult, Model model) throws Exception {

        EthMining mining = web3j.ethMining().sendAsync().get();

        Credentials credentials = WalletUtils.loadCredentials(
                Web3jConstants.PASSWORD,
                Web3jConstants.PATH_WALLET + Web3jConstants.FILE_NAME);

        String fromAddress = credentials.getAddress();
        String toAddress = account.getTo();
        String amount = String.valueOf(account.getAmount());
        StringBuilder transactionInfo = new StringBuilder();
        account.setAddress(fromAddress);
        model.addAttribute("account", account);

        BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        BigInteger nonce = getNonce(fromAddress);

        transactionInfo.append("Transfer from ")
        .append(fromAddress).append(" to ").append(toAddress).append("\n")
        .append("Balance of Sender before Tx: ").append(Web3jUtils.getBalanceEther(web3j, fromAddress)).append("\n")
        .append("Balance of Recipient before Tx: ").append(Web3jUtils.getBalanceEther(web3j, toAddress)).append("\n")
        .append("Transfer ").append(Web3jUtils.weiToEther(amountWei)).append(" Ether to account").append("\n");

        RawTransaction txRaw = RawTransaction
                .createEtherTransaction(
                        nonce,
                        Web3jConstants.GAS_PRICE,
                        Web3jConstants.GAS_LIMIT_ETHER_TX,
                        toAddress,
                        amountWei);

        byte[] txSignedBytes = TransactionEncoder.signMessage(txRaw, credentials);
        String txSigned = Numeric.toHexString(txSignedBytes);

        BigInteger txFeeEstimate = Web3jConstants.GAS_LIMIT_ETHER_TX.multiply(Web3jConstants.GAS_PRICE);

        if(getBalanceWei(fromAddress).compareTo(amountWei) != -1)
        {
            ensureFunds(fromAddress, amountWei.add(txFeeEstimate));
        } else {
            account.setTransactionInfo("You don't have enough Eth");
            return "main";
        }


        EthSendTransaction ethSendTx = web3j
                .ethSendRawTransaction(txSigned)
                .sendAsync()
                .get();
        System.out.println(ethSendTx.toString());
        Response.Error error = ethSendTx.getError();
        String txHash = ethSendTx.getTransactionHash();

        TransactionReceipt txReceipt = waitForReceipt(txHash);
        BigInteger txFee = txReceipt.getCumulativeGasUsed().multiply(Web3jConstants.GAS_PRICE);

        balance.setBalance(Web3jUtils.getBalanceEther(web3j, fromAddress));

        transactionInfo.append("Tx cost: ").append(txReceipt.getCumulativeGasUsed()).append(" Gas (").append(Web3jUtils.weiToEther(txFee)).append(" Ether)\n")
        .append("Tx hash: ").append(txHash).append("\n")
        .append("Balance of Sender after Tx: ").append(Web3jUtils.getBalanceEther(web3j, fromAddress)).append("\n")
        .append("Balance of Recipient after Tx: ").append(Web3jUtils.getBalanceEther(web3j, toAddress)).append("\n")
        .append("Block Number: ").append(txReceipt.getBlockNumber()).append("\n")
        .append("Block Hash: ").append(txReceipt.getBlockHash()).append("\n");


        account.setTransactionInfo(transactionInfo.toString());

        return "main";
    }

    private void ensureFunds(String address, BigInteger amountWei) throws Exception {
        BigInteger balance = getBalanceWei(address);

        if(balance.compareTo(amountWei) >= 0) {
            return;
        }

        BigInteger missingAmount = amountWei.subtract(balance);
        Web3jUtils.transferFromCoinbaseAndWait(web3j, address, missingAmount);
    }

    private TransactionReceipt waitForReceipt(String transactionHash) throws Exception {
        return Web3jUtils.waitForReceipt(web3j, transactionHash);
    }

    private BigInteger getBalanceWei(String address) throws Exception {
        return Web3jUtils.getBalanceWei(web3j, address);
    }

    private BigInteger getNonce(String address) throws Exception {
        return Web3jUtils.getNonce(web3j, address);
    }




}
