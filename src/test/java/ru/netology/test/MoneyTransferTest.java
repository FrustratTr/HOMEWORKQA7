package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class MoneyTransferTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);

        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromSecondToFirst() {
        var dashboardPage = new DashboardPage();

        var balanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        var balanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();

        var transferPage = dashboardPage.transferToFirstCard();

        var amount = 0;
        var cardInfo = getSecondCard();
        transferPage.moneyTransfer(cardInfo, amount);

        var expectedBalanceOfFirstCard = getBalanceIfIncrease(balanceOfFirstCard, amount);
        var expectedBalanceOfSecondCard = getBalanceIfDecrease(balanceOfSecondCard, amount);

        var actualBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        var actualBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();

        assertEquals(expectedBalanceOfFirstCard, actualBalanceOfFirstCard);
        assertEquals(expectedBalanceOfSecondCard, actualBalanceOfSecondCard);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecond() {
        var dashboardPage = new DashboardPage();

        var balanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        var balanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();

        var transferPage = dashboardPage.transferToSecondCard();

        var amount =1000;
        var cardInfo = getFirstCard();
        transferPage.moneyTransfer(cardInfo, amount);

        var expectedBalanceOfFirstCard = getBalanceIfDecrease(balanceOfFirstCard, amount);
        var expectedBalanceOfSecondCard = getBalanceIfIncrease(balanceOfSecondCard, amount);

        var actualBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        var actualBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();

        assertEquals(expectedBalanceOfFirstCard, actualBalanceOfFirstCard);
        assertEquals(expectedBalanceOfSecondCard, actualBalanceOfSecondCard);
    }

    @Test
    void shouldTransferMoneyWhenTransferAmountIsZero() {
        var dashboardPage = new DashboardPage();

        var balanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        var balanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();

        var transferPage = dashboardPage.transferToSecondCard();

        var amount = 0;
        var cardInfo = getFirstCard();
        transferPage.moneyTransfer(cardInfo, amount);

        var expectedBalanceOfFirstCard = getBalanceIfDecrease(balanceOfFirstCard, amount);
        var expectedBalanceOfSecondCard = getBalanceIfIncrease(balanceOfSecondCard, amount);

        var actualBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        var actualBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();

        assertEquals(expectedBalanceOfFirstCard, actualBalanceOfFirstCard);
        assertEquals(expectedBalanceOfSecondCard, actualBalanceOfSecondCard);
    }

    @Test
    void shouldBeErrorWhenCardNumberIncorrect() {
        var dashboardPage = new DashboardPage();

        var transferPage = dashboardPage.transferToFirstCard();

        var amount = 2500;
        var transferInfo = getIncorrectCardNumber();

        transferPage.moneyTransfer(transferInfo, amount);
        transferPage.invalidMoneyTransfer();
    }

    @Test
    void shouldBeErrorWhenCardFieldEmpty() {
        var dashboardPage = new DashboardPage();

        var transferPage = dashboardPage.transferToFirstCard();
        var amount = 1000;

        var transferInfo = getEmptyCardNumber();

        transferPage.moneyTransfer(transferInfo, amount);
        transferPage.invalidMoneyTransfer();
    }

}
