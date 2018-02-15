pragma solidity ^0.4.6;

contract greeter {

    /* Owner of this contract */
    address public owner;

    address public client;
    
    /* Counter for deposits calls */
    uint public deposits;

    uint public price;

    uint public lastFund;

    string success;

    string fail;

    string public result;

    function greeter(string _success, string _fail, uint _price) public {
        owner = msg.sender;
        success = _success;
        fail = _fail;
        price = _price;
        deposits = 0;
    }

    function() payable{
        client = msg.sender;
        deposits += 1;
        lastFund = msg.value;
    }

    function showPrice() returns (uint){
        return price;
    }

    function sellCar() public returns (string){
        if(client != owner && lastFund >= price){
             result = success;
             lastFund = 0;
        } else {
             result = fail;
             lastFund = 0;
        }
    }

    function kill(){
        selfdestruct(owner);
    }

}