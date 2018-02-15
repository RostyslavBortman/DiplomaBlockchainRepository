package hello;

import model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import util.Web3jConstants;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;


@Controller
public class LoginController {

    @PostMapping("/login")
    public String login(@Valid LoginCheck loginCheck, @Valid Account account, BindingResult bindingResult, Model model, HttpServletRequest request) throws IOException, CipherException {

        model.addAttribute("Wallet", new Wallet());
        model.addAttribute("LoginCheck", new LoginCheck());
        model.addAttribute("balance", new Balance());
        model.addAttribute("contract", new Contract());

        if(loginCheck.findWalletName() != null && loginCheck.getPassword() != null) {
            Web3jConstants.FILE_NAME = loginCheck.findWalletName();
            Web3jConstants.PASSWORD = loginCheck.getPassword();
            Credentials credentials = WalletUtils.loadCredentials(
                    Web3jConstants.PASSWORD,
                    Web3jConstants.PATH_WALLET + Web3jConstants.FILE_NAME);

            String myAddress = credentials.getAddress();

            account.setAddress(myAddress);

            return "main";
        }

        return "login";
    }

    @GetMapping("/login")
    public String loginGet(@Valid LoginCheck loginCheck, BindingResult bindingResult, Model model){

        model.addAttribute("account", new Account());
        model.addAttribute("Wallet", new Wallet());
        model.addAttribute("LoginCheck", new LoginCheck());
        model.addAttribute("balance", new Balance());

        if(!Web3jConstants.FILE_NAME.isEmpty() && !Web3jConstants.PASSWORD.isEmpty()) {
            return "main";
        }
        return "login";
    }

}
