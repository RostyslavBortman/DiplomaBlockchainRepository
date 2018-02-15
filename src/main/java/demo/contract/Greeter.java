package demo.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.2.0.
 */
public class Greeter extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6040516105a43803806105a48339810160405280805182019190602001805182019190602001805160008054600160a060020a03191633600160a060020a031617905591506005905083805161006992916020019061008d565b50600682805161007d92916020019061008d565b5060035550506000600255610128565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100ce57805160ff19168380011785556100fb565b828001600101855582156100fb579182015b828111156100fb5782518255916020019190600101906100e0565b5061010792915061010b565b5090565b61012591905b808211156101075760008155600101610111565b90565b61046d806101376000396000f3006060604052600436106100985763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663109e94cf81146100cf578063323a5e0b146100fe57806341c0e1b514610123578063653721471461013857806365ffe43d146101c25780638da5cb5b146101d55780638e0e0abb146101e857806397691f14146101fb578063a035b1fe1461020e575b6001805473ffffffffffffffffffffffffffffffffffffffff191633600160a060020a031617815560028054909101905534600455005b34156100da57600080fd5b6100e2610221565b604051600160a060020a03909116815260200160405180910390f35b341561010957600080fd5b610111610230565b60405190815260200160405180910390f35b341561012e57600080fd5b610136610236565b005b341561014357600080fd5b61014b610244565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561018757808201518382015260200161016f565b50505050905090810190601f1680156101b45780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101cd57600080fd5b6101116102e2565b34156101e057600080fd5b6100e26102e8565b34156101f357600080fd5b6101116102f7565b341561020657600080fd5b61014b6102fe565b341561021957600080fd5b61011161038a565b600154600160a060020a031681565b60025481565b600054600160a060020a0316ff5b60078054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102da5780601f106102af576101008083540402835291602001916102da565b820191906000526020600020905b8154815290600101906020018083116102bd57829003601f168201915b505050505081565b60045481565b600054600160a060020a031681565b6003545b90565b610306610390565b600054600154600160a060020a0390811691161480159061032b575060035460045410155b1561035e576005805461035391600791600260001961010060018416150201909116046103a2565b5060006004556102fb565b6006805461038191600791600260001961010060018416150201909116046103a2565b50600060045590565b60035481565b60206040519081016040526000815290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106103db5780548555610417565b8280016001018555821561041757600052602060002091601f016020900482015b828111156104175782548255916001019190600101906103fc565b50610423929150610427565b5090565b6102fb91905b80821115610423576000815560010161042d5600a165627a7a723058201a38e995040e29a30a9713a0d1a42a3b5172b07ca547bf31a0739e52acceafd90029";

    protected Greeter(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Greeter(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> client() {
        Function function = new Function("client", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> deposits() {
        Function function = new Function("deposits", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> kill() {
        Function function = new Function(
                "kill", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> result() {
        Function function = new Function("result", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> lastFund() {
        Function function = new Function("lastFund", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> owner() {
        Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> showPrice() {
        Function function = new Function(
                "showPrice", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> sellCar() {
        Function function = new Function(
                "sellCar", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> price() {
        Function function = new Function("price", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<Greeter> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _success, String _fail, BigInteger _price) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_success), 
                new org.web3j.abi.datatypes.Utf8String(_fail), 
                new org.web3j.abi.datatypes.generated.Uint256(_price)));
        return deployRemoteCall(Greeter.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Greeter> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _success, String _fail, BigInteger _price) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_success), 
                new org.web3j.abi.datatypes.Utf8String(_fail), 
                new org.web3j.abi.datatypes.generated.Uint256(_price)));
        return deployRemoteCall(Greeter.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static Greeter load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Greeter(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Greeter load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Greeter(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
