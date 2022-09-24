package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.DbUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;
import ru.netology.data.CardInfo;
import ru.netology.data.DBUtils;
import ru.netology.pages.MainPage;
import ru.netology.pages.PaymentPage;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.netology.data.DataHelper.*;

import static com.codeborne.selenide.Selenide.*;

public class PaymentTest {

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

    // #1
    @SneakyThrows
    @Test
    void shouldStatusBuyPaymentValidActiveCard() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkApprovedForm();
        assertEquals("APPROVED", DBUtils.getPaymentStatus());
    }

    //# 2 - failed
    @SneakyThrows
    @Test
    void shouldStatusBuyPaymentValidDeclinedCard() {
        CardInfo card = new CardInfo(getValidDeclinedCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkDeclinedForm();
        assertEquals("DECLINED", DBUtils.getPaymentStatus());
    }

    //# 3
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidCard() {
        CardInfo card = new CardInfo(getInvalidNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkDeclinedForm();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #4
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidPatternCard() {
        CardInfo card = new CardInfo(getInvalidPatternNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCardNumberError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #5
    @SneakyThrows
    @Test
    void shouldBuyPaymentEmptyCard() {
        CardInfo card = new CardInfo(getEmptyNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCardNumberError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #6 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentZeroCard() {
        CardInfo card = new CardInfo(getZeroNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCardNumberError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #7 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidMonthCardExpiredCardError() {
        CardInfo card = new CardInfo(getValidActiveCard(), getPreviousMonth(), getCurrentYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkExpiredCardError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #8
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getInvalidMonth(), getCurrentYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkMonthError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #9 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentZeroMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getZeroMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkMonthError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #10
    @SneakyThrows
    @Test
    void shouldBuyPaymentEmptyMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getEmptyMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkMonthError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #11
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidYearCard() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getPreviousYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkYearError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #12
    @SneakyThrows
    @Test
    void shouldBuyPaymentEmptyYear() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getEmptyYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkYearError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #13
    @SneakyThrows
    @Test
    void shouldBuyPaymentZeroYear() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getZeroYear(), getValidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkYearError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #14 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentRussianOwner() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getInvalidLocaleOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkOwnerError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #15 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentFirstNameOwner() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getInvalidOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkOwnerError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #16
    @SneakyThrows
    @Test
    void shouldBuyPaymentEmptyOwner() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getEmptyOwner(), getValidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkOwnerError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #17
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getInvalidCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCVCError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #18
    @SneakyThrows
    @Test
    void shouldBuyPaymentEmptyCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getEmptyCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCVCError();
        assertNull(DBUtils.getPaymentStatus());
    }

    // #19 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentZeroCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getZeroCVC());
        val mainPage = new MainPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCVCError();
        assertNull(DBUtils.getPaymentStatus());
    }

}
