package hello;

import com.lucadev.coinmarketcap.CoinMarketCap;
import com.lucadev.coinmarketcap.model.CoinMarket;
import com.lucadev.coinmarketcap.model.CoinMarketList;
import edu.self.kraken.api.KrakenApi;
import model.LoginCheck;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import edu.self.kraken.api.KrakenApi.Method;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthMining;
import util.Web3jConstants;
import util.Web3jUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class HelloController {

    private Web3j web3j = null;

    @PostConstruct
    public void init(){
        web3j = Web3jUtils.buildHttpClient("localhost", Web3jConstants.CLIENT_PORT);;
    }

    @RequestMapping("/")
    public String index(Model model) throws IOException, NoSuchAlgorithmException, InvalidKeyException, ExecutionException, InterruptedException {

        model.addAttribute("LoginCheck", new LoginCheck());

        EthMining mining = web3j.ethMining().sendAsync().get();

        return "login";
    }

    @GetMapping("/home")
    public String home(){
        if(!Web3jConstants.FILE_NAME.isEmpty() && !Web3jConstants.PASSWORD.isEmpty()) {
            return "main";
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(){
        return "login";
    }
}
