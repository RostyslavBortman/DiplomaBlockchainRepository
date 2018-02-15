package model;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import util.EmailSender;
import util.Web3jConstants;

import javax.validation.constraints.NotNull;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Wallet {

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String walletID;

    public String getWalletID() {return walletID;}

    public void setWalletID(String walletID){this.walletID = walletID;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void createNewWallet() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {

        String fileName = WalletUtils.generateNewWalletFile(
                password,
                new File(Web3jConstants.PATH_WALLET), true);

        walletID = generateWalletID(fileName);

        // The name of the file to open.

        try {
            // Assume default encoding.
            FileWriter fileWriter =
                    new FileWriter(Web3jConstants.WALLET_DATABASE_FILE, true);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write(walletID + "/" + fileName);
            bufferedWriter.newLine();

            // Always close files.
            bufferedWriter.close();

        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

        EmailSender.SendEmail(email, walletID);

    }

    private String generateWalletID(String fileName){

        return String.valueOf(fileName.hashCode() + 1000000000);

    }

}
