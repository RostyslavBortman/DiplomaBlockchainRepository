package hello;

import model.Wallet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.web3j.crypto.CipherException;

import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Controller
public class RegistrationController {

    @GetMapping("/registration")
    public String registration(Model model) throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        model.addAttribute("Wallet", new Wallet());

        return "registration";
    }

    @PostMapping("/registered")
    public String redirect(@Valid Wallet wallet, BindingResult bindingResult, Model model) throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        System.out.println(wallet.getEmail());

        System.out.println(wallet.getPassword());

        wallet.createNewWallet();

        return "registered";
    }

}
