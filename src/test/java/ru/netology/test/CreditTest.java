package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.CardInfo;
import ru.netology.data.DBUtils;
import ru.netology.pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.DataHelper.getValidCVC;

public class CreditTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080/");
        DBUtils.clearTables();
    }

    // #20
    @SneakyThrows
    @Test
    void shouldStatusBuyCreditValidActiveCard() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkApprovedForm();
        assertEquals("APPROVED", DBUtils.getCreditStatus());
    }

    //# 21 failed
    @SneakyThrows
    @Test
    void shouldStatusBuyCreditValidDeclinedCard() {
        CardInfo card = new CardInfo(getValidDeclinedCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkDeclinedForm();
        assertEquals("DECLINED", DBUtils.getCreditStatus());
    }

    //# 22
    @SneakyThrows
    @Test
    void shouldBuyCreditInvalidCard() {
        CardInfo card = new CardInfo(getInvalidNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkDeclinedForm();
        assertNull(DBUtils.getCreditStatus());
    }

    //# 23
    @SneakyThrows
    @Test
    void shouldBuyCreditInvalidPatternCard() {
        CardInfo card = new CardInfo(getInvalidPatternNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkCardNumberError();
        assertNull(DBUtils.getCreditStatus());
    }

    // # 24
    @SneakyThrows
    @Test
    void shouldBuyCreditEmptyCard() {
        CardInfo card = new CardInfo(getEmptyNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkCardNumberError();
        assertNull(DBUtils.getCreditStatus());
    }

    // # 25 failed
    @SneakyThrows
    @Test
    void shouldBuyCreditZeroCard() {
        CardInfo card = new CardInfo(getZeroNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkCardNumberError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #26 failed
    @SneakyThrows
    @Test
    void shouldBuyCreditInvalidMonthCardExpiredCardError() {
        CardInfo card = new CardInfo(getValidActiveCard(), getPreviousMonth(), getCurrentYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkExpiredCardError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #27
    @SneakyThrows
    @Test
    void shouldBuyCreditInvalidMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getInvalidMonth(), getCurrentYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkMonthError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #28 failed
    @SneakyThrows
    @Test
    void shouldBuyCreditZeroMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getZeroMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkMonthError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #29
    @SneakyThrows
    @Test
    void shouldBuyCreditEmptyMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getEmptyMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkMonthError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #30
    @SneakyThrows
    @Test
    void shouldBuyCreditInvalidYearCard() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getPreviousYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkYearError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #31
    @SneakyThrows
    @Test
    void shouldBuyCreditEmptyYear() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getEmptyYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkYearError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #32
    @SneakyThrows
    @Test
    void shouldBuyCreditZeroYear() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getZeroYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkYearError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #33 failed
    @SneakyThrows
    @Test
    void shouldBuyCreditRussianOwner() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getInvalidLocaleOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkOwnerError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #34 failed
    @SneakyThrows
    @Test
    void shouldBuyCreditFirstNameOwner() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getInvalidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkOwnerError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #35
    @SneakyThrows
    @Test
    void shouldBuyCreditEmptyOwner() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getEmptyOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkOwnerError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #36
    @SneakyThrows
    @Test
    void shouldBuyCreditInvalidCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getInvalidCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkCVCError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #37
    @SneakyThrows
    @Test
    void shouldBuyCreditEmptyCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getEmptyCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkCVCError();
        assertNull(DBUtils.getCreditStatus());
    }

    // #38 failed
    @SneakyThrows
    @Test
    void shouldBuyCreditZeroCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getZeroCVC());
        val mainPage = new MainPage();
        mainPage.checkCreditButton().
                fillingForm(card).
                checkCVCError();
        assertNull(DBUtils.getCreditStatus());
    }

}
