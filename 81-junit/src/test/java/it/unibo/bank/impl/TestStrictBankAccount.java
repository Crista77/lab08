package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.unibo.bank.impl.SimpleBankAccount.*;
import static it.unibo.bank.impl.StrictBankAccount.TRANSACTION_FEE;
import static org.junit.jupiter.api.Assertions.*;
import static it.unibo.bank.impl.StrictBankAccount.*;

public class TestStrictBankAccount {

    private final static int INITIAL_AMOUNT = 100;

    // 1. Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    @BeforeEach
    public void setUp() {
        this.mRossi = new AccountHolder("Mario", "Rossi", INITIAL_AMOUNT);
        this.bankAccount = new StrictBankAccount(mRossi, 0.0);
    }

    // 2. Test the initial state of the StrictBankAccount
    @Test
    public void testInitialization() {
        Assertions.assertEquals(0.0, this.bankAccount.getBalance());
        Assertions.assertEquals(0,  this.bankAccount.getTransactionsCount());
        Assertions.assertEquals(mRossi, this.bankAccount.getAccountHolder());
    }


    // 3. Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
    @Test
    public void testManagementFees() {
        double expectedValue = INITIAL_AMOUNT - MANAGEMENT_FEE - TRANSACTION_FEE - ATM_TRANSACTION_FEE;
        Assertions.assertFalse(bankAccount.getTransactionsCount() > 0);
        this.bankAccount.depositFromATM(this.mRossi.getUserID(), INITIAL_AMOUNT);
        this.bankAccount.chargeManagementFees(this.mRossi.getUserID());
        Assertions.assertEquals(expectedValue, this.bankAccount.getBalance());
        Assertions.assertTrue(bankAccount.getTransactionsCount() == 0);
    }

    // 4. Test the withdraw of a negative value
    @Test
    public void testNegativeWithdraw() {
        int valueToWihdraw = -77;
        try {
        bankAccount.withdrawFromATM(this.mRossi.getUserID(), valueToWihdraw); 
        Assertions.fail();     
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Cannot withdraw a negative amount", e.getMessage());
        }

    }

    // 5. Test withdrawing more money than it is in the account
    @Test
    public void testWithdrawingTooMuch() {
        int valueToWihdraw = 99;
        try {
        bankAccount.withdrawFromATM(this.mRossi.getUserID(), valueToWihdraw);
        Assertions.fail();   
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Insufficient balance", e.getMessage());
        }
    }
}
