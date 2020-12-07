package com.example.map_project;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.5.
 */
@SuppressWarnings("rawtypes")
public class KingOfCountry extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060408051602081019182905260009081905261002f91600391610043565b5060006002556064600155426004556100de565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061008457805160ff19168380011785556100b1565b828001600101855582156100b1579182015b828111156100b1578251825591602001919060010190610096565b506100bd9291506100c1565b5090565b6100db91905b808211156100bd57600081556001016100c7565b90565b61048d806100ed6000396000f3fe6080604052600436106100915760003560e01c8063a035b1fe11610059578063a035b1fe1461019f578063c5310868146101b4578063cc1c7607146101c9578063cf120b6a14610253578063da93d0d11461027d57610091565b80631b08af49146100965780635d58dd72146100c25780637141761d1461011d5780638da58897146101475780638da5cb5b1461016e575b600080fd5b3480156100a257600080fd5b506100c0600480360360208110156100b957600080fd5b5035610285565b005b3480156100ce57600080fd5b506100f5600480360360208110156100e557600080fd5b50356001600160a01b0316610290565b604080516001600160a01b039094168452602084019290925282820152519081900360600190f35b34801561012957600080fd5b506100c06004803603602081101561014057600080fd5b50356102bb565b34801561015357600080fd5b5061015c610337565b60408051918252519081900360200190f35b34801561017a57600080fd5b5061018361033d565b604080516001600160a01b039092168252519081900360200190f35b3480156101ab57600080fd5b5061015c61034c565b3480156101c057600080fd5b5061015c610352565b3480156101d557600080fd5b506101de610358565b6040805160208082528351818301528351919283929083019185019080838360005b83811015610218578181015183820152602001610200565b50505050905090810190601f1680156102455780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561025f57600080fd5b506100c06004803603602081101561027657600080fd5b50356103e6565b6100c06103fb565b600180549091019055565b6005602052600090815260409020805460018201546002909201546001600160a01b03909116919083565b60000360648160015401116102d3576102d381610285565b60006001541161033457600080546001600160a01b03191633178082556040516001600160a01b039190911691303180156108fc02929091818181858888f19350505050158015610328573d6000803e3d6000fd5b50600060025560646001555b50565b60045481565b6000546001600160a01b031681565b60025481565b60015481565b6003805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156103de5780601f106103b3576101008083540402835291602001916103de565b820191906000526020600020905b8154815290600101906020018083116103c157829003601f168201915b505050505081565b60648160015401116103345761033481610285565b60028054349081018255604080516060810182523381526020808201938452428284019081523060009081526005909252929020905181546001600160a01b0319166001600160a01b03909116178155915160018301555191015556fea265627a7a723158203cec4aa4de5e1a5787c58701798c140e1b1fe1e4abb467dae5420f9b7c06a98d64736f6c634300050d0032";

    public static final String FUNC_ALLIANCE = "alliance";

    public static final String FUNC_ATTACKHISTORY = "attackhistory";

    public static final String FUNC_BECOMEKING = "becomeking";

    public static final String FUNC_LIFE = "life";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PRICE = "price";

    public static final String FUNC_PROTECT = "protect";

    public static final String FUNC_SETLIFE = "setLife";

    public static final String FUNC_SETPRICE = "setPrice";

    public static final String FUNC_STARTTIME = "starttime";

    @Deprecated
    protected KingOfCountry(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected KingOfCountry(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected KingOfCountry(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected KingOfCountry(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> alliance() {
        final Function function = new Function(FUNC_ALLIANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>> attackhistory(String param0) {
        final Function function = new Function(FUNC_ATTACKHISTORY, 
                Arrays.<Type>asList(new Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>>(function,
                new Callable<Tuple3<String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> becomeking(BigInteger _life) {
        final Function function = new Function(
                FUNC_BECOMEKING, 
                Arrays.<Type>asList(new Uint256(_life)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> life() {
        final Function function = new Function(FUNC_LIFE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> price() {
        final Function function = new Function(FUNC_PRICE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> protect(BigInteger _life) {
        final Function function = new Function(
                FUNC_PROTECT, 
                Arrays.<Type>asList(new Uint256(_life)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setLife(BigInteger _life) {
        final Function function = new Function(
                FUNC_SETLIFE, 
                Arrays.<Type>asList(new Uint256(_life)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setPrice(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_SETPRICE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<BigInteger> starttime() {
        final Function function = new Function(FUNC_STARTTIME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static KingOfCountry load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new KingOfCountry(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static KingOfCountry load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new KingOfCountry(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static KingOfCountry load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new KingOfCountry(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static KingOfCountry load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new KingOfCountry(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<KingOfCountry> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(KingOfCountry.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<KingOfCountry> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(KingOfCountry.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<KingOfCountry> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(KingOfCountry.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<KingOfCountry> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(KingOfCountry.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
