package hello;

import com.lucadev.coinmarketcap.CoinMarketCap;
import com.lucadev.coinmarketcap.model.CoinMarket;
import model.Account;
import model.Balance;
import model.Contract;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthMining;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import util.Web3jConstants;
import util.Web3jUtils;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Controller
public class BalanceController {

    private Web3j web3j = null;

    @PostConstruct
    public void init(){
        web3j = Web3jUtils.buildHttpClient("localhost", Web3jConstants.CLIENT_PORT);;
    }

    @PostMapping("/balance")
    public String getBalance(@Valid Balance balance, @Valid Account account, BindingResult bindingResult, Model model) throws IOException, CipherException, ExecutionException, InterruptedException {

        model.addAttribute("account", new Account());
        model.addAttribute("contract", new Contract());

        EthMining mining = web3j.ethMining().sendAsync().get();

        System.out.println(mining.isMining());

        Credentials credentials = WalletUtils.loadCredentials(
                Web3jConstants.PASSWORD,
                Web3jConstants.PATH_WALLET + Web3jConstants.FILE_NAME);

        String myAddress = credentials.getAddress();

        account.setAddress(myAddress);

        CoinMarket market = CoinMarketCap.ticker("ethereum").get();

        balance.setBalanceText(balance.setBalance(Web3jUtils.getBalanceEther(web3j, myAddress)) + " - " + String.valueOf(Web3jUtils.getBalanceEther(web3j, myAddress).doubleValue() * market.getPriceUSD()).substring(0, String.valueOf(Web3jUtils.getBalanceEther(web3j, myAddress).doubleValue() * market.getPriceUSD()).length() - 3) + "$");

        return "main";
    }

    @PostMapping("/increaseBalance")
    public String increaseBalance(@Valid Balance balance, @Valid Account account, BindingResult bindingResult, Model model) throws Exception {


        model.addAttribute("account", new Account());
        model.addAttribute("contract", new Contract());

        EthMining mining = web3j.ethMining().sendAsync().get();

        if(balance.getBalanceAdd() == null || balance.getBalanceAdd().equals(0)){
            balance.setBalanceAdd("Transaction failed");
            return "main";
        }

        Credentials credentials = WalletUtils.loadCredentials(
                Web3jConstants.PASSWORD,
                Web3jConstants.PATH_WALLET + Web3jConstants.FILE_NAME);

        String myAddress = credentials.getAddress();
        account.setAddress(myAddress);
        String wei = balance.getBalanceAdd();
        BigInteger amountWei = Convert.toWei(balance.getBalanceAdd(), Convert.Unit.ETHER).toBigInteger();
        TransactionReceipt receipt = Web3jUtils.transferFromCoinbaseAndWait(web3j, myAddress, amountWei);
        BigInteger txFee = receipt.getCumulativeGasUsed().multiply(Web3jConstants.GAS_PRICE);

        balance.setBalanceAdd("Transaction succeed");

        return "main";
    }


}
