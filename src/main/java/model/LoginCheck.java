package model;

import util.Web3jConstants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class LoginCheck {

    private String walletID;

    private String password;

    public String getWalletID() {
        return walletID;
    }

    public void setWalletID(String walletID) {
        this.walletID = walletID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String findWalletName(){

        try (BufferedReader br = new BufferedReader(new FileReader(Web3jConstants.WALLET_DATABASE_FILE))) {

            String currentLine;
            int index;

            while ((currentLine = br.readLine()) != null) {
                index = currentLine.indexOf("/");
                String temp = currentLine.substring(0, index);
                if(temp.equals(walletID)){
                    return currentLine.substring(index+1, currentLine.length());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
